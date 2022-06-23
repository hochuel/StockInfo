package org.srv.utils.excel.xe;

import org.srv.utils.excel.Excel;
import org.srv.utils.excel.Sheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * <pre>
 * XSSF형식의 Excel 제어 Utility(ms excel 2007 이상)
 *  [사용예]
 *      Excel excel = Excel.make(new File("D:/score.xlsx"));
 *      excel.getSheet(1).addRow(new Object[]{11,"김덕순","1950.03.21",false,3.14});
 *      excel.addRow(1, new Object[]{12,"김말순","1953.07.24",true,4.14});
 *      excel.setValue(1, 0, 1, "김순자");
 *      excel.removeSheet(2);
 *
 *      Excel exce6 = Excel.make(new File("D:/score.data"),"|");
 *      Excel excel7    = exce6.convertToOtherFormat(Excel.EXCEL_TYPE_XSSF);
 *      excel7.write(new java.io.File("D:/score2"+Excel.XSSF_EXTENSION));
 * </pre>
 *
 * @ClassName   : XExcel.java
 * @Description : XSSF형식의 Excel 제어 Utility(ms excel 2007 이상)
 * @author ADM기술팀
 * @since 2016. 6. 29.
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 *     since          author              description
 *  ===========    =============    ===========================
 *  2016. 6. 29.     ADM기술팀     	최초 생성
 * </pre>
 */
public class XExcel extends Excel implements Serializable{
	/** serial version UID **/
	private static final long serialVersionUID = 1L;
	/** XSSF 형식의 workbook **/
	private XSSFWorkbook xWBook;
	protected XSSFFormulaEvaluator evaluator;
	
	/**<pre>
	 * default constructor
	 * 엑셀타입을 XSSF로 지정
	 * XSSFWorkbook 생성
	 * 빈 Sheet 목록 생성
	 * </pre>
	 * **/
	public XExcel(){
		type		= EXCEL_TYPE_XSSF;
		xWBook 		= new XSSFWorkbook();
		evaluator	= new XSSFFormulaEvaluator(xWBook);
		sheetList	= new ArrayList<Sheet>();
	}
	/**<pre>
	 * constructor
	 * 엑셀타입을 XSSF로 지정
	 * XSSFWorkbook 생성
	 * Sheet 목록 생성
	 * </pre>
	 * @param excelFile XSSF형식의 excel 파일
	 * @throws IOException
	 * **/
	public XExcel(File excelFile) throws IOException{
		type		= EXCEL_TYPE_XSSF;
		sheetList	= new ArrayList<Sheet>();
		read(excelFile);
	}
	/**<pre>
	 * constructor
	 * 엑셀타입을 XSSF로 지정
	 * XSSFWorkbook 생성
	 * Sheet 목록 생성
	 * </pre>
	 * @param is XSSF형식의 excel InputStream
	 * @throws IOException
	 * **/
	public XExcel(InputStream is) throws IOException{
		type		= EXCEL_TYPE_XSSF;
		sheetList	= new ArrayList<Sheet>();
		read(is);
	}
	/**<pre>
	 * XSSF형식의 excel InputStream으로부터 XExcel객체를 생성한다.
	 * </pre>
	 * @param is XSSF형식의 excel InputStream
	 * @throws FileNotFoundException, IOException 
	 * @return XExcel
	 * **/
	public XExcel read(InputStream is) throws FileNotFoundException, IOException{
		XSSFExcelExtractor extractor		= null;
		try{
			this.xWBook	= new XSSFWorkbook(is);
			evaluator	= new XSSFFormulaEvaluator(xWBook);
			extractor	= new XSSFExcelExtractor(xWBook);
			extractor.setFormulasNotResults(true);
			extractor.setIncludeSheetNames(true);
			
			loadSheet();
		}catch(IOException ex){
			throw ex;
		}finally{
			try{if(is!=null){is.close();}}catch(Exception e){}
		}
		return this;
	}
	/**<pre>
	 * Excel의 Sheet목록을 loading한다.
	 * </pre>
	 * **/
	private void loadSheet(){
		for(int i=0;i<xWBook.getNumberOfSheets();i++){
			sheetList.add(new XSheet(this,xWBook.getSheetName(i), xWBook.getSheetAt(i)));
		}
	}
	/**<pre>
	 * XSSFWorkbook을 저장하고 내용을 loading한다.
	 * </pre>
	 * @param xWBook XSSF형식의 workbook 객체
	 * @return XExcel
	 * **/
	public XExcel setWBook(XSSFWorkbook xWBook){
		this.xWBook	= xWBook;
		evaluator	= new XSSFFormulaEvaluator(xWBook);
		return this;
	}
	/**<pre>
	 * XSSFWorkbook 반환한다.
	 * </pre>
	 * @return XSSFWorkbook
	 * **/
	public XSSFWorkbook getWBook( ){
		return xWBook;
	}
	/**<pre>
	 * sheet명에 해당하는 Sheet를 반환한다.
	 * </pre>
	 * @param sheetName sheet 명
	 * @return Sheet
	 * **/
	public Sheet getSheet(String sheetName){
		return sheetList.get(xWBook.getSheetIndex(sheetName));
	}
	/**<pre>
	 * Sheet를 삽입한다.
	 * </pre>
	 * @param sheetIndex sheet번호
	 * @param sheetName sheet name
     * @param arr sheet value
	 * @return XSheet 생성된 sheet
	 * **/
	public XSheet insertSheet(int sheetIndex,String sheetName, Object[][] arr){
		XSheet sheet	= new XSheet(this, sheetName, xWBook.createSheet(sheetName));
		for(int i=0;i<arr.length;i++){
			sheet.addRow(arr[i]);
		}
		xWBook.setSheetOrder(sheetName, sheetIndex);
		sheetList.add(sheetIndex,sheet);
		return sheet;
	}
	/**<pre>
	 * Sheet를 삽입한다.
	 * </pre>
	 * @param sheetIndex sheet번호
	 * @param sheetName sheet name
     * @param list sheet value
	 * @return XSheet 생성된 sheet
	 * **/
	public XSheet insertSheet(int sheetIndex,String sheetName, List<List<Object>> list){
		XSheet sheet	= new XSheet(this, sheetName, xWBook.createSheet(sheetName));
		for(int i=0;i<list.size();i++){
			sheet.addRow(list.get(i));
		}
		xWBook.setSheetOrder(sheetName, sheetIndex);
		sheetList.add(sheetIndex,sheet);
		return sheet;
	}
	/**<pre>
	 * Sheet를 제거한다.
	 * </pre>
	 * @param sheetIndex 제거할 sheet번호
	 * @return Excel
	 * **/
	public Excel removeSheet(int sheetIndex){
		xWBook.removeSheetAt(sheetIndex);
		sheetList.remove(sheetIndex);
		return this;
	}
	/**<pre>
	 * XSSF형삭의 EXCEL을 OutputStream으로 전송한다.
	 * </pre>
	 * @param os 전송 대상 OutputStream
	 * @throws IOException
	 * **/
	public void write(OutputStream os) throws IOException{
		try{
			xWBook.write(os);
		}catch(IOException ie){
			throw ie;
		}finally{
			try{os.close();}catch(Exception e){}
		}
	}
	
	@Override
	public Workbook getWorkbook() {
		return xWBook;
	}
	@Override
	public boolean setWorkbook(Workbook wb) {
		xWBook = (XSSFWorkbook) wb;
		return true;
	}
	

}
