package org.srv.utils.excel;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.srv.file.FileTool;
import org.srv.utils.excel.he.HExcel;
import org.srv.utils.excel.se.SExcel;
import org.srv.utils.excel.xe.XExcel;
import org.srv.vo.HMap;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * <pre>
 * Excel 제어 Utility
 *  개발자는 excel file의 type에 상관없이 동일한 로직으로 제어가 가능하다.
 *  또한 상이한 format으로의 자유로운 변환을 지원한다.
 *  
 *  [ 지원 format ]
 *      HSSF(ms excel 2007 미만 버젼-.xls)
 *      XSSF(ms excel 2007 이후 버젼-.xlsx)
 *      CVS(comma separated value)
 *      TVS(tab separated value)
 *      기타 separated value
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
 * @ClassName   : Excel.java
 * @Description : 클래스 설명을 기술합니다.
 * @author ADM기술팀
 * @since 2016. 6. 29.
 * @version 1.0
 * @Modification Information
 * <pre>
 *     since          author              description
 *  ===========    =============    ===========================
 *  2016. 6. 29.     ADM기술팀     	최초 생성
 * </pre>
 */
public abstract class Excel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** ms excel type hssf **/
	public static int EXCEL_TYPE_HSSF	= 1;
	/** ms excel type xssf **/
	public static int EXCEL_TYPE_XSSF	= 2;
	/** excel type csv **/
	public static int EXCEL_TYPE_CSV	= 3;
	/** excel type tsv **/
	public static int EXCEL_TYPE_TSV	= 4;
	/** excel type other **/
	public static int EXCEL_TYPE_OTHER	= 5;

	/** hssf format excel file extension **/
	public static final String HSSF_EXTENSION	= ".xls";
	/** xssf format excel file extension **/
	public static final String XSSF_EXTENSION	= ".xlsx";
	/** cvs file extension **/
	public static final String CSV_EXTENSION	= ".csv";
	/** tvs file extension **/
	public static final String TSV_EXTENSION	= ".tsv";
	/** tvs file extension **/
	public static final String OTHER_EXTENSION	= ".txt";
	
	/** line separator **/
	public static final String _LINE	= System.getProperty("line.separator");
	/** current field separator **/
	public String separator	= "\t";
	
	/** current excel format **/
	public int type	= EXCEL_TYPE_HSSF;
	/** type extension **/
	public String extension	= ".xls";
	
	/** list od excel sheet **/
	protected List<Sheet> sheetList;

	/**<pre>
	* 인자로 받은 type에 맞는 Excel 객체를 생성한다.
	* </pre>
	* @param type excel format type
	* @return Excel 생성된 Excel 객체
	*/
	public static Excel make(int type){
		if(type==EXCEL_TYPE_XSSF){
			return new XExcel();
		}else if(type==EXCEL_TYPE_HSSF){
			return new HExcel();
		}else if(type==EXCEL_TYPE_TSV){
			return new SExcel(EXCEL_TYPE_TSV);
		}else if(type==EXCEL_TYPE_CSV){
			return new SExcel(EXCEL_TYPE_CSV);
		}else{
			return new SExcel(EXCEL_TYPE_TSV);
		}
	}
	
	/**<pre>
	* 인자로 받은 separator로 separated value 형식의 Excel을 생성한다.
	* </pre>
	* @param spChar field separator
	* @return Excel 생성된 Excel 객체
	*/
	public static Excel make(String spChar){
		return new SExcel(spChar);
	}
	/**<pre>
	* Excel file의 내용으로 Excel객체 생성
	* </pre>
	* @param file excel 파일
	* @return Excel 생성된 Excel 객체
	* @throws IOException
	*/
	public static Excel make(File file) throws IOException{
		if(file.getName().endsWith(HSSF_EXTENSION)){
			return new HExcel(file);
		}else if(file.getName().endsWith(XSSF_EXTENSION)){
			return new XExcel(file);
		}else if(file.getName().endsWith(CSV_EXTENSION)){
			return new SExcel(file,",");
		}else if(file.getName().endsWith(TSV_EXTENSION)){
			return new SExcel(file,"\t");
		}else{
			return null;
		}
	}
	
	public static Excel makeSec(File file, String orgFileName) throws IOException{
		if(orgFileName.endsWith(HSSF_EXTENSION)){
			return new HExcel(file);
		}else if(orgFileName.endsWith(XSSF_EXTENSION)){
			return new XExcel(file);
		}else if(orgFileName.endsWith(CSV_EXTENSION)){
			return new SExcel(file,",");
		}else if(orgFileName.endsWith(TSV_EXTENSION)){
			return new SExcel(file,"\t");
		}else{
			return null;
		}
	}
	/**<pre>
	* Excel file의 내용으로 Excel객체 생성
	* </pre>
	* @param file excel 파일
	* @param spChar 필드 구분자
	* @return Excel 생성된 Excel 객체
	* @throws IOException
	*/
	public static Excel make(File file,String spChar) throws IOException{
		return new SExcel(file,spChar);
	}
	/**<pre>
	* 엑셀파일의 내용을 읽어 Excel객체를 생성한다.
	* </pre>
	* @param excelFile excel 파일
	* @return Excel 생성된 Excel 객체
	* @throws FileNotFoundException,IOException
	*/
	public Excel read(File excelFile) throws FileNotFoundException, IOException{
		return read(new FileInputStream(excelFile));
	}
	
	/**<pre>
	* InputStream의 내용을 읽어 Excel객체를 생성한다.
	* </pre>
	* @param is excel data Inputstream
	* @return Excel 생성된 Excel 객체
	* @throws FileNotFoundException,IOException
	*/
	public Excel read(InputStream is, int type) throws FileNotFoundException, IOException{
		if(type==EXCEL_TYPE_HSSF){
			return new HExcel().read(is);
		}else if(type==EXCEL_TYPE_XSSF){
			return new XExcel().read(is);
		}else if(type==EXCEL_TYPE_CSV){
			return new SExcel(",").read(is);
		}else if(type==EXCEL_TYPE_CSV){
			return new SExcel("\t").read(is);
		}else{
			return null;
		}
	}
	
	/**<pre>
	* InputStream의 내용을 읽어 Excel객체를 생성한다.
	* </pre>
	* @param is excel data Inputstream
	* @return Excel 생성된 Excel 객체
	* @throws FileNotFoundException,IOException
	*/
	public abstract Excel read(InputStream is) throws FileNotFoundException, IOException;
	/**<pre>
	* Excel 객체의 TYPE을 추출한다.
	* </pre>
	* @return int type
	*/
	public int getType(){
		return type;
	}
	/**<pre>
	* Excel 객체의 파일 확장자를 추출한다.
	* </pre>
	* @return String extension
	*/
	public String getExtension(){
		if(type==EXCEL_TYPE_HSSF){
			return HSSF_EXTENSION;
		}else if(type==EXCEL_TYPE_XSSF){
			return XSSF_EXTENSION;
		}else if(type==EXCEL_TYPE_CSV){
			return CSV_EXTENSION;
		}else if(type==EXCEL_TYPE_TSV){
			return TSV_EXTENSION;
		}else{
			return OTHER_EXTENSION;
		}
	}
	
	/**<pre>
	* Excel 객체의 모든 내용을 삭제한다.
	* </pre>
	*/
	public Excel clearAll(){
		for(int i=sheetList.size()-1;i>=0;i--){
			removeSheet(i);
		}
		return this;
	}
	/**<pre>
	* 배열 데이터의 내용을 Excel에 셋팅한다.
	* </pre>
	* @param datas
	*/
	public Excel setArray(Object[][][] datas){
		clearAll();
		for(int i=0;i<datas.length;i++){
			addSheet(datas[i]);
		}
		return this;
	}
	/**<pre>
	* 배열 데이터의 내용을 Excel에 셋팅한다.
	* </pre>
	* @param datas
	*/
	public Excel setArray(List<Object[][]> datas){
		clearAll();
		for(int i=0;i<datas.size();i++){
			addSheet(datas.get(i));
		}
		return this;
	}
	/**<pre>
	* List 데이터의 내용을 Excel에 셋팅한다.
	* </pre>
	* @param datas
	*/
	public Excel setList(List<List<List<Object>>>  datas){
		clearAll();
		for(int i=0;i<datas.size();i++){
			addSheet(datas.get(i));
		}
		return this;
	}
	
	/**<pre>
	* Excel 객체의 row를 추출한다.
	* </pre>
	* @param sheetIndex sheet번호 (separated value형식일 경우 0번만 존재)
	* @param rowIndex 행번호
	* @return Row 추출한 Row객체
	*/
	public Row getRow(int sheetIndex, int rowIndex){
		return getSheet(sheetIndex).getRow(rowIndex);
	}
	/**<pre>
	* Excel 객체의 cell을 추출한다.
	* </pre>
	* @param sheetIndex sheet번호 (separated value형식일 경우 0번만 존재)
	* @param rowIndex 행번호
	* @param cellIndex cell(필드) 번호
	* @return Cell 추출한 Cell 객체
	*/
	public Cell getCell(int sheetIndex, int rowIndex, int cellIndex){
		return getSheet(sheetIndex).getRow(rowIndex).getCell(cellIndex);
	}
	/**<pre>
	* Cell data type을 추출한다.
	* </pre>
	* @param sheetIndex sheet번호 (separated value형식일 경우 0번만 존재)
	* @param rowIndex 행번호
	* @param cellIndex cell(필드) 번호
	* @return int 추출한 Cell data type
	*/
	public int getCellType(int sheetIndex, int rowIndex, int cellIndex){
		return getCell(sheetIndex,rowIndex,cellIndex).getCellType();
	}
	/**<pre>
	* Cell value를 String형으로 반환한다.
	* </pre>
	* @param sheetIndex sheet번호 (separated value형식일 경우 0번만 존재)
	* @param rowIndex 행번호
	* @param cellIndex cell(필드) 번호
	* @return String 추출한 Cell value
	*/
	public String getString(int sheetIndex, int rowIndex, int cellIndex){
		return getCell(sheetIndex,rowIndex,cellIndex).getString();
	}
	/**<pre>
	* Cell value를 short 형으로 반환한다.
	* </pre>
	* @param sheetIndex sheet번호 (separated value형식일 경우 0번만 존재)
	* @param rowIndex 행번호
	* @param cellIndex cell(필드) 번호
	* @return short 추출한 Cell value
	*/
	public short getShort(int sheetIndex, int rowIndex, int cellIndex){
		return getCell(sheetIndex,rowIndex,cellIndex).getShort();
	}
	/**<pre>
	* Cell value를 int 형으로 반환한다.
	* </pre>
	* @param sheetIndex sheet번호 (separated value형식일 경우 0번만 존재)
	* @param rowIndex 행번호
	* @param cellIndex cell(필드) 번호
	* @return int 추출한 Cell value
	*/
	public int getInt(int sheetIndex, int rowIndex, int cellIndex){
		return getCell(sheetIndex,rowIndex,cellIndex).getInt();
	}
	/**<pre>
	* Cell value를 long 형으로 반환한다.
	* </pre>
	* @param sheetIndex sheet번호 (separated value형식일 경우 0번만 존재)
	* @param rowIndex 행번호
	* @param cellIndex cell(필드) 번호
	* @return long 추출한 Cell value
	*/
	public long getLong(int sheetIndex, int rowIndex, int cellIndex){
		return getCell(sheetIndex,rowIndex,cellIndex).getLong();
	}
	/**<pre>
	* Cell value를 float 형으로 반환한다.
	* </pre>
	* @param sheetIndex sheet번호 (separated value형식일 경우 0번만 존재)
	* @param rowIndex 행번호
	* @param cellIndex cell(필드) 번호
	* @return float 추출한 Cell value
	*/	
	public float getFloat(int sheetIndex, int rowIndex, int cellIndex){
		return getCell(sheetIndex,rowIndex,cellIndex).getFloat();
	}
	/**<pre>
	* Cell value를 double 형으로 반환한다.
	* </pre>
	* @param sheetIndex sheet번호 (separated value형식일 경우 0번만 존재)
	* @param rowIndex 행번호
	* @param cellIndex cell(필드) 번호
	* @return double 추출한 Cell value
	*/	
	public double getDouble(int sheetIndex, int rowIndex, int cellIndex){
		return getCell(sheetIndex,rowIndex,cellIndex).getDouble();
	}
	/**<pre>
	* Cell value를 boolean 형으로 반환한다.
	* </pre>
	* @param sheetIndex sheet번호 (separated value형식일 경우 0번만 존재)
	* @param rowIndex 행번호
	* @param cellIndex cell(필드) 번호
	* @return boolean 추출한 Cell value
	*/	
	public boolean getBoolean(int sheetIndex, int rowIndex, int cellIndex){
		return getCell(sheetIndex,rowIndex,cellIndex).getBoolean();
	}
	/**<pre>
	* Cell value를 java.util.Date 형으로 반환한다.
	* </pre>
	* @param sheetIndex sheet번호 (separated value형식일 경우 0번만 존재)
	* @param rowIndex 행번호
	* @param cellIndex cell(필드) 번호
	* @return java.util.Date 추출한 Cell value
	* @throws ParseException
	*/	
	public Date getDate(int sheetIndex, int rowIndex, int cellIndex) throws ParseException{
		return getDate(sheetIndex, rowIndex, cellIndex,null);
	}
	/**<pre>
	* Cell value를 java.util.Date 형으로 반환한다.
	* </pre>
	* @param sheetIndex sheet번호 (separated value형식일 경우 0번만 존재)
	* @param rowIndex 행번호
	* @param cellIndex cell(필드) 번호
	* @param format date를 나타내는 문자열의 SimpleDateFormat
	* @return java.util.Date 추출한 Cell value
	* @throws ParseException
	*/	
	public Date getDate(int sheetIndex, int rowIndex, int cellIndex,String format) throws ParseException{
		return getCell(sheetIndex,rowIndex,cellIndex).getDate(format);
	}
	/**<pre>
	* Sheet 객체를 반환한다.
	* </pre>
	* @param sheetIndex sheet번호 (separated value형식일 경우 0번만 존재)
	* @return Sheet 추출한 Sheet
	*/	
	public Sheet getSheet(int sheetIndex){
		return sheetList.get(sheetIndex);
	}
	/**<pre>
	* Sheet 객체를 반환한다.
	* </pre>
	* @param sheetName sheet명 (separated value형식일 경우 Sheet0만 존재)
	* @return Sheet 추출한 Sheet
	*/	
	public abstract Sheet getSheet(String sheetName);
	/**<pre>
	* Sheet 갯수를 반환한다.
	* separated value형식일 경우 1
	* </pre>
	* @return int Sheet 갯수
	*/	
	public int getSheetCount(){
		return sheetList.size();
	}
	/**<pre>
	* Sheet에 포함된 row 갯수를 반환한다.
	* </pre>
	* @param sheetIndex sheet번호 (separated value형식일 경우 0번만 존재)
	* @return int row 갯수
	*/	
	public int getRowCount(int sheetIndex){
		return sheetList.get(sheetIndex).getRowCount();
	}
	/**<pre>
	* Row에 포함된 cell 갯수를 반환한다.
	* </pre>
	* @param sheetIndex sheet번호 (separated value형식일 경우 0번만 존재)
	* @param rowIndex row 번호
	* @return int cell 갯수
	*/	
	public int getCellCount(int sheetIndex,int rowIndex){
		return sheetList.get(sheetIndex).getRow(rowIndex).getCellCount();
	}
	/**<pre>
	* 빈 Sheet 를 추가한다 .
	* </pre>
	* @return Sheet 생성된 Sheet
	*/	
	public Sheet addSheet(){
		return insertSheet(sheetList.size(),"Sheet"+(sheetList.size()),new Object[0][0][0]);
	}
	/**<pre>
	* 빈 Sheet 를 삽입한다 .
	* </pre>
	* @param sheetIndex 새로운 Sheet를 삽입할 인덱스
	* @return Sheet 생성된 Sheet
	*/	
	public Sheet insertSheet(int sheetIndex){
		return insertSheet(sheetIndex,"Sheet"+(sheetList.size()),new Object[0][0][0]);
	}
	/**<pre>
	* Sheet 를 추가한다 .
	* Sheet명은 Sheet+순번으로 셋팅된다.
	* </pre>
	* @param arr 생성될 Sheet의 value
	* @return Sheet 생성된 Sheet
	*/	
	public Sheet addSheet(Object[][] arr){
		return insertSheet(sheetList.size(),"Sheet"+(sheetList.size()),arr);
	}
	/**<pre>
	* Sheet 를 추가한다 .
	* Sheet명은 Sheet+순번으로 셋팅된다.
	* </pre>
	* @param list 생성될 Sheet의 value
	* @return Sheet 생성된 Sheet
	*/	
	public Sheet addSheet(List<List<Object>> list){
		return insertSheet(sheetList.size(),"Sheet"+(sheetList.size()),list);
	}
	/**<pre>
	* Sheet 를 삽입한다 .
	* </pre>
	* @param sheetIndex 삽입 위치
	* @param sheetName sheet name
	* @param arr 생성될 Sheet의 value
	* @return Sheet 생성된 Sheet
	*/	
	public abstract Sheet insertSheet(int sheetIndex,String sheetName, Object[][] arr);
	/**<pre>
	* Sheet 를 삽입한다 .
	* </pre>
	* @param sheetIndex 삽입 위치
	* @param sheetName sheet name
	* @param list 생성될 Sheet의 value
	* @return Sheet 생성된 Sheet
	*/	
	public abstract Sheet insertSheet(int sheetIndex,String sheetName, List<List<Object>> list);
	/**<pre>
	* Sheet의 각 cell에 값을 할당한다.
	* </pre>
	* @param sheetIndex 값을 할당할 sheet번호
	* @param list Cell에 할당될 value
	* @return Sheet 생성된 Sheet
	*/	
	public Sheet setSheet(int sheetIndex,List<List<Object>> list){
		Sheet sheet	= sheetList.get(sheetIndex);
		for(int i=0;i<list.size();i++){
			sheet.setRow(i, list.get(i));
		}
		return sheet;
	}
	/**<pre>
	* Sheet의 각 cell에 값을 할당한다.
	* </pre>
	* @param  sheetIndex 값을 할당할 sheet번호
	* @param arr Cell에 할당될 value
	* @return Sheet 생성된 Sheet
	*/	
	public Sheet setSheet(int sheetIndex,Object[][] arr){
		Sheet sheet	= sheetList.get(sheetIndex);
		for(int i=0;i<arr.length;i++){
			sheet.setRow(i, arr[i]);
		}
		return sheet;
	}
	/**<pre>
	* Sheet에 빈 row를 추가한다.
	* </pre>
	* @param sheetIndex row를 추가할 sheet번호
	* @return Row 생성된 Row
	*/	
	public Row addRow(int sheetIndex){
		return getSheet(sheetIndex).addRow();
	}
	/**<pre>
	* Sheet에 빈 row를 삽입한다.
	* </pre>
	* @param sheetIndex row를 추가할 sheet번호
	* @param rowIndex 삽입할 row번호
	* @return Row 생성된 Row
	*/	
	public Row insertRow(int sheetIndex, int rowIndex){
		return getSheet(sheetIndex).insertRow(rowIndex);
	}
	/**<pre>
	* Sheet에 row를 추가한다.
	* </pre>
	* @param sheetIndex row를 추가할 sheet번호
	* @param arr 추가할 row value
	* @return Row 생성된 Row
	*/	
	public Row addRow(int sheetIndex,Object[] arr){
		return getSheet(sheetIndex).addRow(arr);
	}
	/**<pre>
	* Sheet에 row를 삽입한다.
	* </pre>
	* @param sheetIndex row를 추가할 sheet번호
	* @param rowIndex 삽입할 row번호
	* @param arr 추가할 row value
	* @return Row 생성된 Row
	*/	
	public Row insertRow(int sheetIndex, int rowIndex, Object[] arr){
		return getSheet(sheetIndex).insertRow(rowIndex,arr);
	}
	/**<pre>
	* Sheet에 row를 추가한다.
	* </pre>
	* @param sheetIndex row를 추가할 sheet번호
	* @param list 추가할 row value
	* @return Row 생성된 Row
	*/	
	public Row addRow(int sheetIndex,List<Object> list){
		return getSheet(sheetIndex).addRow(list);
	}
	/**<pre>
	* Sheet에 row를 삽입한다.
	* </pre>
	* @param sheetIndex row를 추가할 sheet번호
	* @param rowIndex 삽입할 row번호
	* @param list 추가할 row value
	* @return Row 생성된 Row
	*/	
	public Row insertRow(int sheetIndex, int rowIndex, List<Object> list){
		return getSheet(sheetIndex).insertRow(rowIndex,list);
	}
	/**<pre>
	* Row에 빈 cell을 추가한다.
	* </pre>
	* @param sheetIndex sheet번호
	* @param rowIndex row번호
	* @return Cell 생성된 Cell
	*/	
	public Cell addCell(int sheetIndex,int rowIndex){
		return getSheet(sheetIndex).getRow(rowIndex).addCell();
	}
	/**<pre>
	* Row에 빈 cell을 삽입한다.
	* </pre>
	* @param sheetIndex sheet번호
	* @param rowIndex row번호
	* @param cellIndex cell번호
	* @return Cell 생성된 Cell
	*/	
	public Cell insertCell(int sheetIndex, int rowIndex,int cellIndex){
		return getSheet(sheetIndex).getRow(rowIndex).insertCell(cellIndex);
	}
	/**<pre>
	* Row에 cell을 추가한다.
	* </pre>
	* @param sheetIndex sheet번호
	* @param rowIndex row번호
	* @param value cell value
	* @return Cell 생성된 Cell
	*/	
	public Cell addCell(int sheetIndex,int rowIndex, Object value){
		return getSheet(sheetIndex).getRow(rowIndex).addCell(value);
	}
	/**<pre>
	* Row에 cell을 삽입한다.
	* </pre>
	* @param sheetIndex sheet번호
	* @param rowIndex row번호
	* @param cellIndex cell번호
	* @param value cell value
	* @return Cell 생성된 Cell
	*/	
	public Cell insertRow(int sheetIndex, int rowIndex,int cellIndex, Object value){
		return getSheet(sheetIndex).getRow(rowIndex).insertCell(cellIndex,value);
	}
	/**<pre>
	* Sheet를 삭제한다.
	* </pre>
	* @param sheetIndex 삭제할 sheet번호
	* @return Excel
	*/	
	public abstract Excel removeSheet(int sheetIndex);
	/**<pre>
	* row를 삭제한다.
	* </pre>
	* @param sheetIndex sheet번호
	* @param rowIndex 삭제할 row번호
	* @return Excel
	*/	
	public Excel removeRow(int sheetIndex,int rowIndex){
		getSheet(sheetIndex).removeRow(rowIndex);
		return this;
	}
	/**<pre>
	* Cell을 삭제한다.
	* </pre>
	* @param sheetIndex sheet번호
	* @param rowIndex row번호
	* @param cellIndex 삭제할 cell번호
	* @return Excel
	*/	
	public Excel removeCell(int sheetIndex,int rowIndex,int cellIndex){
		getSheet(sheetIndex).getRow(rowIndex).removeCell(cellIndex);
		return this;
	}
	/**<pre>
	* Cell에 값을 할당한다.
	* </pre>
	* @param sheetIndex sheet번호
	* @param rowIndex row번호
	* @param cellIndex 삭제할 cell번호
	* @param value cell value
	* @return Excel
	*/	
	public Excel setValue(int sheetIndex,int rowIndex, int cellIndex, Object value){
		getSheet(sheetIndex).getRow(rowIndex).getCell(cellIndex).setValue(value);
		return this;
	}
	/**<pre>
	* Excel의 내용을 List<List<List<Object>>> 형으로 반환한다.
	* </pre>
	* @return List<List<List<Object>>>
	*/	
	public List<List<List<Object>>> toList(){
		List<List<List<Object>>> list	= new ArrayList<List<List<Object>>>(sheetList.size());
		for(int i=0;i<sheetList.size();i++){
			list.add(sheetList.get(i).toList());
		}
		return list;
	}
	/**<pre>
	* Excel의 내용을 List<Object[][]> 형으로 반환한다.
	* </pre>
	* @return List<Object[][]>
	*/	
	public List<Object[][]> toArray(){
		List <Object[][]> excelArray	= new ArrayList<Object[][]>(sheetList.size());
		for(int i=0;i<sheetList.size();i++){
			excelArray.add(sheetList.get(i).toArray());
		}
		return excelArray;
	}
	//2016.02.19 sunny 추가
	//excel 데이타를 HMap 으로 변환하여 db에 넣기 위해..
	public List<List<HMap>> toHMapList(){
		List<List<HMap>> excelArray	= new ArrayList<List<HMap>>(sheetList.size());
		for(int i=0;i<sheetList.size();i++){
			excelArray.add(sheetList.get(i).toHMapList()); //List<HMap>
		}
		return excelArray;
	}
	/**<pre>
	* Sheet 의 내용을 Object[][] 형으로 반환한다.
	* </pre>
	* @param sheetIndex sheet번호
	* @return Object[][]
	*/	
	public Object[][] toArray(int sheetIndex){
		return sheetList.get(sheetIndex).toArray();
	}
	/**<pre>
	* Row 의 내용을 Object[]형으로 반환한다.
	* </pre>
	* @param sheetIndex sheet번호
	* @param rowIndex row 번호
	* @return Object[]
	*/	
	public Object[] toArray(int sheetIndex,int rowIndex){
		return sheetList.get(sheetIndex).getRow(rowIndex).toArray();
	}
	/**<pre>
	* Excel을 File로 생성한다.
	* </pre>
	* @param file Excel의 내용을 기술할 File객체
	* @throws IOException
	*/	
	public void write(File file) throws IOException{
		//if(!file.exists())
		//	FileTool.createNewDirectory(file.getPath());	//file.mkdir();
		write(new FileOutputStream(file));
	}
	
	//2016.01.19 sunny 함수추가
	public void write(String filePath, String fileName) throws IOException{
		//new java.io.File(fileLocation+"/"+filename+excel.getExtension())
		File file = new File(filePath);
		if(!file.exists())
			FileTool.createNewDirectory(filePath);	//file.mkdir();
		
		File realFile = new File(filePath+"/"+fileName);
		write(realFile);
	}
	//2016.01.19 sunny 함수추가
	public File writeForHMap(Excel excel, String filePath, String fileName, List<HMap> mapList) throws IOException{		
		
		fileName = fileName+excel.getExtension();
		
		HMap tmap = mapList.get(0);
		Set key = tmap.keySet();
		String[] keys = new String[key.size()];  
		
		
		//첫번째 셀에는 컬럼 명을 작성한다.
		excel.addSheet();
		excel.addRow(0);
		
		int c=0;
		for (Iterator iterator = key.iterator(); iterator.hasNext();) {
			String keyname = (String) iterator.next();
			excel.addCell(0, 0, keyname);
			keys[c] = keyname;
			c++;
		}
		
		//데이터를 셀에 넣는다.
		for(int i=0;i<mapList.size();i++){
			HMap map = (HMap) mapList.get(i);
			excel.addRow(0);
			for(int j=0;j<keys.length;j++){
			    excel.addCell(0, i+1, map.get(keys[j]));
			}
			/*excel.addCell(0, i+1, map.get(keys[0]));
			excel.addCell(0, i+1, map.get(keys[1]));
			excel.addCell(0, i+1, map.get(keys[2]));
			excel.addCell(0, i+1, map.get(keys[3]));
			excel.addCell(0, i+1, map.get(keys[4]));	*/			
		}			
		
		File file = new File(filePath);
		if(!file.exists())
			FileTool.createNewDirectory(filePath);	//file.mkdir();
		
		File realFile = new File(filePath+"/"+fileName);
		//write(realFile);
		return realFile;
	}
	
	public File writeForVO(Excel excel, String filePath, String fileName, List<Object> voList) throws Exception{
		boolean result = false;
		File realFile = null;
		
		fileName = fileName+excel.getExtension();
		try{
			
			if(null!=voList && voList.size()>0){
				//컬럼 셀 작성
				Object firstObject = voList.get(0);
				Field[] fields = firstObject.getClass().getDeclaredFields();
				
				excel.addSheet();
				excel.addRow(0);
				for(int j=0; j<=fields.length-1;j++){
					String fieldName = fields[j].getName();
					excel.addCell(0, 0, fieldName);
				}
			}else{
				return null;
			}
			
			for(int i=0;i<voList.size();i++){
				Object obj = voList.get(i);			
				Field[] fields = obj.getClass().getDeclaredFields();
				
				excel.addRow(0);
				for(int j=0; j<=fields.length-1;j++){
					fields[j].setAccessible(true);
					Object resultField = fields[j].get(obj);
					excel.addCell(0, i+1, (null==resultField)? "" : resultField);						
				}
			}
			
			File file = new File(filePath);
			if(!file.exists())
				FileTool.createNewDirectory(filePath);	//file.mkdir();
			
			realFile = new File(filePath+"/"+fileName);
			//write(realFile);
			
			result = true;
		}catch(ReflectiveOperationException e){
			result = false;
		}catch(IllegalArgumentException e){
			result = false;
		}
		
		return realFile;
		
	}
	
	/**<pre>
	* Excel을 OutputStream으로 전송한다.
	* </pre>
	* @param os Excel의 내용을 전송할 OutputStream
	* @throws IOException
	*/	
	public abstract void write(OutputStream os) throws IOException;
	/**<pre>
	* Excel을 separated value format으로 복사한다.
	* </pre>
	* @param spChar separated value의 separator
	* @return Excel format변경한 Excel객체
	* @throws Exception
	*/	
	public Excel convertToOtherFormat(String spChar) throws Exception{
		return convertToOtherFormat(EXCEL_TYPE_OTHER,spChar);
	}
	/**<pre>
	* Excel을 다른 format으로 복사한다.
	* </pre>
	* @param type 새로 복사할 Excel type
	* @return Excel format변경한 Excel객체
	* @throws Exception
	*/	
	public Excel convertToOtherFormat(int type) throws Exception{
		if(type==EXCEL_TYPE_XSSF){
			return convertToOtherFormat(type,null);
		}else if(type==EXCEL_TYPE_HSSF){
			return convertToOtherFormat(type,null);
		}else if(type==EXCEL_TYPE_TSV){
			return convertToOtherFormat(type,"\t");
		}else if(type==EXCEL_TYPE_CSV){
			return convertToOtherFormat(type,",");
		}else{
			return convertToOtherFormat(type,"\t");
		}
	}
	/**<pre>
	* Excel을 다른 format으로 복사한다.
	* </pre>
	* @param type 새로 복사할 Excel type
	* @param spChar separated value format의 separator(separated value가 아닐경우 null)
	* @return Excel format변경한 Excel객체
	* @throws Exception
	*/	
	public Excel convertToOtherFormat(int type,String spChar) throws Exception{
		if(this.type==type && this.separator==spChar)
			return this;

		Excel excel	= null;

		//Convert With MS Excel Format
		if( ( this.type==EXCEL_TYPE_XSSF || this.type==EXCEL_TYPE_HSSF ) && (type==EXCEL_TYPE_XSSF || type==EXCEL_TYPE_HSSF ) ){
			//HSSF<->XSSF 
			if(type==EXCEL_TYPE_XSSF){
				
				excel	= new XExcel();
				Sheet 	st	= null;
				Sheet	st2	= null;
				Row		rw	= null;
				Row		rw2	= null;
				Cell	cl	= null;
				
				for(int i=0;i<sheetList.size();i++){
					st	= sheetList.get(i);
					st2	= excel.addSheet();
					for(int j=0;j<st.getRowCount();j++){
						rw	= st.getRow(j);
						rw2	= st2.addRow();
						
						for(int k=0;k<rw.getCellCount();k++){
							cl	= rw.getCell(k);
							if(cl.getCellType()==Cell.CELL_TYPE_FORMULA){
								rw2.addCell().setFormula(cl.getFormula());
							}else{
								rw2.addCell().setValue(cl.getString(), cl.getCellType());
							}
						}
					}
				}
			}
			return excel;
		}
		
		if(type==EXCEL_TYPE_XSSF){
			excel	= new XExcel();
		}else if(type==EXCEL_TYPE_HSSF){
			excel	= new HExcel();
		}else if(type==EXCEL_TYPE_TSV){
			excel	= new SExcel(EXCEL_TYPE_TSV);
		}else if(type==EXCEL_TYPE_CSV){
			excel	= new SExcel(EXCEL_TYPE_CSV);
		}else{
			excel	= new SExcel(spChar);
		}
		for(int i=0;i<sheetList.size();i++){
			excel.addSheet(sheetList.get(i).toArray());
		}
		return excel;
	}

    /**
     * 엑셀 내용을 String[]집합인 ArrayList로 변환
     * @return ArrayList
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
	public ArrayList getExcelDatas(File excel, int colCnt) throws Exception{
        ArrayList list  = new ArrayList();
        String[] rowArr = null;
        POIFSFileSystem fs  = null;
        FileInputStream fi  = null;
        
        try{
            fi              = new FileInputStream(excel);
            
            //POSFS을 이용하여 엑셀 워크북을 생성합니다
            fs              = new POIFSFileSystem(fi); 
            HSSFWorkbook workbook = new HSSFWorkbook(fs);
            
            //생성된 워크북을 이용하여  엑셀 첫번째시트를 생성합니다.
            HSSFSheet sheet = workbook.getSheetAt(0);
            
            //생성된 시트를 이용하여 그 행의 수를 추출
            int rows        = sheet.getPhysicalNumberOfRows();
        
			@SuppressWarnings("unused")
			int cells       = 0;
            HSSFRow row     = null;
            HSSFCell cell   = null;

            for (int r = 0; r < rows; r++) {
                
                // 시트에 대한 행의 정보를 가지고 온다.
                row     = sheet.getRow(r);
                
                rowArr  = new String[colCnt];

                if (row == null) {
                    break;
                }
                
                //생성된 행을 이용하여 그 셀의 수 추출
                cells   = row.getPhysicalNumberOfCells();

                //지정된 셀의 갯수만큼 읽어 들인다. 
                for (short c = 0; c < colCnt; c++) {
                    
                    //행에대한 셀을 하나씩 추출하여 셀 타입에 따라 처리
                    cell  = row.getCell(c);
                    
                    //지정된 셀의 수가 엑셀의 셀의 수보다 큰경우 null값으로 지정
                    //if(colCnt > cells){
                        //rowArr[c] = null;
                        
                    //}else{
                    
                    //셀의 정보가 null인경우
                    if(cell ==null) {
                        rowArr[c]   = null;
                        
                    }else{
                        if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING ){//문자열타입
                            rowArr[c]   = cell.getStringCellValue();
                            
                        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){//숫자타입
                            rowArr[c]   = getRealNumber(String.valueOf(cell.getNumericCellValue()));
                            
                        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_BLANK ){//Blank Cell type 
                        
                        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN ){//Boolean Cell type 
                            rowArr[c]   = String.valueOf(cell.getBooleanCellValue());
                            
                        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_ERROR  ){// Error Cell type
                            rowArr[c]   = String.valueOf(cell.getErrorCellValue());
                            
                        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA  ){//Formula Cell type

                        }
                    }
                }//end of for
                list.add(rowArr);
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception(e);
        }finally{
            try{if(fi!=null){fi.close();}}catch(Exception e){}
        }
        return list;
    }
    
    /**
     * 엑셀의 지수 표현 문자
     * @param String        지수값
     * @return String
     */
    private final String _EXP_SIGN   = "E";
    public String getRealNumber( String exponent ) throws Exception{
        
        if(exponent.indexOf(_EXP_SIGN)<0){
            return exponent;
        }
        
        String[] arr    = exponent.split(_EXP_SIGN);
        
        if(arr[1].startsWith("+")){
            arr[1]  = arr[1].substring(1);
        }
        
        BigDecimal v1   = new BigDecimal(arr[0]);
        BigDecimal v2   = new BigDecimal(Math.pow(10, Double.parseDouble(arr[1])));
        
        return Long.toString(v1.multiply(v2).longValue());
    }
    
    //20160106 기능 추가
    //by sunny
    /*public Workbook getWorkbook(){
		return getWB();
	}*/
    public abstract Workbook getWorkbook();
    
    public abstract boolean setWorkbook(Workbook wb);
}