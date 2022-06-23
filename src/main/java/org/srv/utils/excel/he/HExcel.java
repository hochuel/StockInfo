package org.srv.utils.excel.he;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.srv.utils.excel.Excel;
import org.srv.utils.excel.Sheet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  <pre>
 * HSSF형식의 Excel 제어 Utility(ms excel 2007 미만)
 *  [사용예]
 *      Excel excel = Excel.make(new File("D:/score.xls"));
 *      excel.getSheet(1).addRow(new Object[]{11,"김덕순","1950.03.21",false,3.14});
 *      excel.addRow(1, new Object[]{12,"김말순","1953.07.24",true,4.14});
 *      excel.setValue(1, 0, 1, "김순자");
 *      excel.removeSheet(2);
 *
 *      Excel exce6 = Excel.make(new File("D:/score.data"),"|");
 *      Excel excel7    = exce6.convertToOtherFormat(Excel.EXCEL_TYPE_HSSF);
 *      excel7.write(new java.io.File("D:/score2"+Excel.HSSF_EXTENSION));
 * </pre>
 *
 * @ClassName   : HExcel.java
 * @Description : HSSF형식의 Excel 제어 Utility(ms excel 2007 미만)
 * @author ADM기술팀
 * @since 2016. 6. 29.
 * @version 1.0
 * @see Excel
 * @see XExcel
 * @see SExcel
 * @Modification Information
 * <pre>
 *     since          author              description
 *  ===========    =============    ===========================
 *  2016. 6. 29.     ADM기술팀     	최초 생성
 * </pre>
 */
public class HExcel extends Excel implements Serializable{
	/** serial version UID **/
	private static final long serialVersionUID = 1L;
	/** HSSF 형식의 workbook **/
	private HSSFWorkbook hWBook;
	public HSSFFormulaEvaluator evaluator;

	/**<pre>
	 * default constructor
	 * 엑셀타입을 HSSF로 지정
	 * HSSFWorkbook 생성
	 * 빈 Sheet 목록 생성
	 * </pre>
	 * **/
	public HExcel(){
		type		= EXCEL_TYPE_HSSF;
		hWBook 		= new HSSFWorkbook();
		evaluator	= new HSSFFormulaEvaluator(hWBook);
		sheetList	= new ArrayList<Sheet>();
	}
	/**<pre>
	 * constructor
	 * 엑셀타입을 HSSF로 지정
	 * HSSFWorkbook 생성
	 * Sheet 목록 생성
	 * </pre>
	 * @param excelFile HSSF형식의 excel 파일
	 * @throws IOException
	 * **/
	public HExcel(File excelFile) throws IOException{
		type		= EXCEL_TYPE_HSSF;
		sheetList	= new ArrayList<Sheet>();
		read(excelFile);
	}
	/**<pre>
	 * constructor
	 * 엑셀타입을 HSSF로 지정
	 * HSSFWorkbook 생성
	 * Sheet 목록 생성
	 * </pre>
	 * @param is HSSF형식의 excel InputStream
	 * @throws IOException
	 * **/
	public HExcel(InputStream is) throws IOException{
		type		= EXCEL_TYPE_HSSF;
		sheetList	= new ArrayList<Sheet>();
		read(is);
	}
	/**<pre>
	 * HSSF형식의 excel InputStream으로부터 HExcel객체를 생성한다.
	 * </pre>
	 * @param is HSSF형식의 excel InputStream
	 * @throws FileNotFoundException, IOException 
	 * @return HExcel
	 * **/
	public HExcel read(InputStream is) throws FileNotFoundException, IOException{
		POIFSFileSystem pfs			= null;
		try{
			pfs			= new POIFSFileSystem(is);
			this.hWBook	= new HSSFWorkbook(pfs);
			evaluator	= new HSSFFormulaEvaluator(hWBook);
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
		for(int i=0;i<hWBook.getNumberOfSheets();i++){
			sheetList.add(new HSheet(this,hWBook.getSheetName(i), hWBook.getSheetAt(i)));
		}
	}
	/**<pre>
	 * HSSFWorkbook을 저장하고 내용을 loading한다.
	 * </pre>
	 * @param hWBook HSSF형식의 workbook 객체
	 * @return HExcel
	 * **/
	public HExcel setWBook(HSSFWorkbook hWBook){
		this.hWBook	= hWBook;
		evaluator	= new HSSFFormulaEvaluator(hWBook);
		loadSheet();
		return this;
	}
	/**<pre>
	 * HSSFWorkbook 반환한다.
	 * </pre>
	 * @return HSSFWorkbook
	 * **/
	public HSSFWorkbook getWBook( ){
		return hWBook;
	}
	/**<pre>
	 * sheet명에 해당하는 Sheet를 반환한다.
	 * </pre>
	 * @param sheetName sheet 명
	 * @return Sheet
	 * **/
	public Sheet getSheet(String sheetName){
		return sheetList.get(hWBook.getSheetIndex(sheetName));
	}
	/**<pre>
	 * Sheet를 삽입한다.
	 * </pre>
	 * @param sheetIndex sheet번호
	 * @param sheetName sheet name
     * @param arr sheet value
	 * @return HSheet 생성된 sheet
	 * **/
	public HSheet insertSheet(int sheetIndex,String sheetName, Object[][] arr){
		HSheet sheet	= new HSheet(this, sheetName, hWBook.createSheet(sheetName));
		for(int i=0;i<arr.length;i++){
			sheet.addRow(arr[i]);
		}
		hWBook.setSheetOrder(sheetName, sheetIndex);
		sheetList.add(sheetIndex,sheet);
		return sheet;
	}
	/**<pre>
	 * Sheet를 삽입한다.
	 * </pre>
	 * @param sheetIndex sheet번호
	 * @param sheetName sheet name
     * @param list sheet value
	 * @return HSheet 생성된 sheet
	 * **/
	public HSheet insertSheet(int sheetIndex,String sheetName, List<List<Object>> list){
		HSheet sheet	= new HSheet(this, sheetName, hWBook.createSheet(sheetName));
		for(int i=0;i<list.size();i++){
			sheet.addRow(list.get(i));
		}
		hWBook.setSheetOrder(sheetName, sheetIndex);
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
		hWBook.removeSheetAt(sheetIndex);
		sheetList.remove(sheetIndex);
		return this;
	}
	/**<pre>
	 * HSSF형삭의 EXCEL을 OutputStream으로 전송한다.
	 * </pre>
	 * @param os 전송 대상 OutputStream
	 * @throws IOException
	 * **/
	public void write(OutputStream os) throws IOException{
		try{
			hWBook.write(os);
		}catch(IOException ie){
			throw ie;
		}finally{
			try{os.close();}catch(Exception e){}
		}
	}
	@Override
	public Workbook getWorkbook() {		
		return hWBook;
	}
	@Override
	public boolean setWorkbook(Workbook wb) {
		
		hWBook = (HSSFWorkbook) wb;
		
		return true;
	}
}
