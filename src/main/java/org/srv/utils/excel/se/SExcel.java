package org.srv.utils.excel.se;

import org.apache.poi.ss.usermodel.Workbook;
import org.srv.utils.excel.Excel;
import org.srv.utils.excel.Sheet;
import org.srv.utils.excel.he.HExcel;
import org.srv.utils.excel.xe.XExcel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * Separated value 형식의 Excel 제어 Utility
 *  [사용예]
 *      Excel excel = Excel.make(new File("D:/score.csv"));
 *      excel.getSheet(1).addRow(new Object[]{11,"김덕순","1950.03.21",false,3.14});
 *      excel.addRow(1, new Object[]{12,"김말순","1953.07.24",true,4.14});
 *      excel.setValue(1, 0, 1, "김순자");
 *      excel.removeSheet(2);
 *
 *      Excel exce6 = Excel.make(new File("D:/score.data"),"|");
 *      Excel excel7    = exce6.convertToOtherFormat(Excel.EXCEL_TYPE_TSV);
 *      excel7.write(new java.io.File("D:/score2"+Excel.TSV_EXTENSION));
 * </pre>
 *
 * @ClassName   : SExcel.java
 * @Description : 클래스 설명을 기술합니다.
 * @author ADM기술팀
 * @since 2016. 6. 29.
 * @version 1.0
 * @see Excel
 * @see HExcel
 * @see XExcel
 * @Modification Information
 * <pre>
 *     since          author              description
 *  ===========    =============    ===========================
 *  2016. 6. 29.     ADM기술팀     	최초 생성
 * </pre>
 */
public class SExcel extends Excel implements Serializable{
	/** serial version UID **/
	private static final long serialVersionUID = 1L;
	/** separated value 형식의 최초 문자열 **/
	private String sWBook;
	/**<pre>
	 * constructor
	 * separator 저장
	 * 빈 Sheet 목록 생성
	 * </pre>
	 * @param spChar field separator
	 * **/
	public SExcel(String spChar){
		setSeparator(spChar);
		sheetList	= new ArrayList<Sheet>();
	}
	/**<pre>
	 * constructor
	 * separator 저장
	 * 빈 Sheet 목록 생성
	 * </pre>
	 * @param type format type
	 * **/
	public SExcel(int type){
		setSeparator(type);
		sheetList	= new ArrayList<Sheet>();
	}
	/**<pre>
	 * constructor
	 * 엑셀타입을 Separated value 로 지정
	 * 필드 구분자 지정
	 * Sheet 목록 생성
	 * </pre>
	 * @param excelFile Separated value 형식의 excel File
	 * @param spChar 엑셀 필드 구분자
	 * @throws IOException
	 * **/
	public SExcel(File excelFile, String spChar) throws IOException{
		setSeparator(spChar);
		sheetList	= new ArrayList<Sheet>();
		read(excelFile);
	}
	/**<pre>
	 * constructor
	 * 엑셀타입을 Separated value 로 지정
	 * 필드 구분자 지정
	 * Sheet 목록 생성
	 * </pre>
	 * @param excelFile Separated value 형식의 excel File
	 * @param type 엑셀 타입
	 * @throws IOException
	 * **/
	public SExcel(File excelFile, int type) throws IOException{
		setSeparator(type);
		sheetList	= new ArrayList<Sheet>();
		read(excelFile);
	}
	/**<pre>
	 * constructor
	 * 엑셀타입을 Separated value 로 지정
	 * 필드 구분자 지정
	 * Sheet 목록 생성
	 * </pre>
	 * @param is Separated value 형식의 excel InputStream
	 * @param spChar 엑셀 필드 구분자
	 * @throws IOException
	 * **/
	public SExcel(InputStream is, String spChar) throws IOException{
		setSeparator(spChar);
		sheetList	= new ArrayList<Sheet>();
		read(is);
	}
	/**<pre>
	 * constructor
	 * 엑셀타입을 Separated value 로 지정
	 * 필드 구분자 지정
	 * Sheet 목록 생성
	 * </pre>
	 * @param is Separated value 형식의 excel InputStream
	 * @param type 엑셀 타입
	 * @throws IOException
	 * **/
	public SExcel(InputStream is, int type) throws IOException{
		setSeparator(type);
		sheetList	= new ArrayList<Sheet>();
		read(is);
	}
	/**
	 * 엑셀형식에 따른 field separator을 설정한다.
	 * @param spChar 필드 구분자
	 * **/
	private void setSeparator(String spChar){
		if(",".equals(spChar)){
			type		= EXCEL_TYPE_CSV;
		}else if("\t".equals(spChar)){
			type		= EXCEL_TYPE_TSV;
		}else{
			type		= EXCEL_TYPE_OTHER;
		}
		this.separator	= spChar;
	}
	/**
	 * 엑셀형식에 따른 field separator을 설정한다.
	 * @param type 엑셀형식
	 * **/
	private void setSeparator(int type){
		this.type	= type;
		if(type==EXCEL_TYPE_CSV)
			this.separator	= ",";
		else if(type==EXCEL_TYPE_TSV)
			this.separator	= "\t";
		else
			this.separator	= "\t";
	}
	/**<pre>
	 * Separated value 형식의 excel InputStream으로부터 SExcel객체를 생성한다.
	 * </pre>
	 * @param is Separated value 형식의 excel InputStream
	 * @throws FileNotFoundException, IOException 
	 * @return SExcel
	 * **/
	public SExcel read(InputStream is) throws FileNotFoundException, IOException{
		StringBuffer sb		= new StringBuffer();
		BufferedReader br	= null;
		try{
			String line	= "";
			br	= new BufferedReader(new InputStreamReader(is));
			while( (line=br.readLine())!=null ){
				sb.append(line).append(_LINE);
			}
			sWBook	= sb.toString();
			loadSheet();
		}catch(IOException ex){
			throw ex;
		}finally{
			try{if(br!=null){br.close();}}catch(Exception e){}
			try{if(is!=null){is.close();}}catch(Exception e){}
		}
		return this;
	}
	/**<pre>
	 * Excel의 Sheet목록을 loading한다.
	 * </pre>
	 * **/
	private void loadSheet(){
		sheetList.add(new SSheet(this,"Sheet0", sWBook));
	}
	/**<pre>
	 * Separated value형식의 문자열을 저장하고 내용을 loading한다.
	 * </pre>
	 * @param sWBook Separated value 형식의 문자열
	 * @return SExcel
	 * **/
	public SExcel setWBook(String sWBook){
		this.sWBook	= sWBook;
		return this;
	}
	/**<pre>
	 * 엑셀 생성 초기 문자열을 반환한다.
	 * </pre>
	 * @return String
	 * **/
	public String getWBook( ){
		return sWBook;
	}
	/**<pre>
	 * sheet명에 해당하는 Sheet를 반환한다.
	 * </pre>
	 * @param sheetName sheet 명
	 * @return Sheet
	 * **/
	public Sheet getSheet(String sheetName){
		for(int i=0;i<sheetList.size();i++){
			if(sheetList.get(i).getSheetName().equals(sheetName))
				return sheetList.get(i);
		}
		return null;
	}
	/**<pre>
	 * Sheet를 삽입한다.
	 * </pre>
	 * @param sheetIndex sheet번호
	 * @param sheetName sheet name
     * @param arr sheet value
	 * @return SSheet 생성된 sheet
	 * **/
	public SSheet insertSheet(int sheetIndex,String sheetName, Object[][] arr){
		SSheet sheet	= new SSheet(this, sheetName, "");
		for(int i=0;i<arr.length;i++){
			sheet.addRow(arr[i]);
		}
		sheetList.add(sheetIndex,sheet);
		return sheet;
	}
	/**<pre>
	 * Sheet를 삽입한다.
	 * </pre>
	 * @param sheetIndex sheet번호
	 * @param sheetName sheet name
     * @param list sheet value
	 * @return SSheet 생성된 sheet
	 * **/
	public SSheet insertSheet(int sheetIndex,String sheetName, List<List<Object>> list){
		SSheet sheet	= new SSheet(this, sheetName,"");
		for(int i=0;i<list.size();i++){
			sheet.addRow(list.get(i));
		}
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
		sheetList.remove(sheetIndex);
		return this;
	}
	/**<pre>
	 * separated value 형삭의 EXCEL을 OutputStream으로 전송한다.
	 * </pre>
	 * @param os 전송 대상 OutputStream
	 * @throws IOException
	 * **/
	public void write(OutputStream os) throws IOException{
		try{
			byte[] buf	= null;
			for(int i=0;i<sheetList.size();i++){
				buf	= sheetList.get(i).toString().getBytes();
				os.write(buf);
			}
		}catch(IOException ie){
			throw ie;
		}finally{
			try{os.close();}catch(Exception e){}
		}
	}
	@Override
	public Workbook getWorkbook() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean setWorkbook(Workbook wb) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
