package org.srv.utils.excel.xe;

import org.srv.utils.excel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.srv.utils.excel.Excel;
import org.srv.utils.excel.Row;
import org.srv.utils.excel.Sheet;
import org.srv.utils.excel.he.HCell;
import org.srv.utils.excel.se.SCell;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 	<pre>
 * XExcel의 Cell 제어 Utility
 * 
 *   수정일         수정자                   수정내용
 *  -------        --------    ---------------------------
 *  2010. 05. 18    IT아키텍처팀        최초작성
 * </pre>
 * @author ABLE Project IT아키텍처팀
 * @version 1.0
 * @see Excel
 * @see Sheet
 * @see Row
 * @see HCell
 * @see XCell
 * @see SCell
 * @since jdk 1.5
 */	
public class XCell extends Cell implements Serializable{
	/** serial version UID **/
	private static final long serialVersionUID = 1L;
	/** XSSFormat cell **/
	private XSSFCell cell;
	/**<pre>
	 * constructor
	 * 빈 Cell을 생성
	 * </pre>
	 * @param row 부모 row
	 * **/
	public XCell(XRow row){
		this.row	= row;
	}
	/**<pre>
	 * constructor
	 * Cell을 생성
	 * </pre>
	 * @param row 부모 row
	 * @param cell XSSFormat Cell object
	 * **/
	public XCell(XRow row, XSSFCell cell){
		this.row	= row;
		this.cell	= cell;
	}
	/**<pre>
	* 현재 Cell의 value type을 반환한다.
	* </pre>
	* @return int cell type
	*/	
	public int getCellType(){
		if(cell==null)
			return CELL_TYPE_STRING;
		return cell.getCellType();
	}
	/**<pre>
	* Cell의 값을 할당한다.
	* </pre>
	* @param value cell value
	* @return XCell
	*/	
	public XCell setValue(Object value){
		Class<?> valClass	= value.getClass();
		if(valClass==short.class || valClass==int.class || valClass==long.class 
				|| valClass==float.class || valClass==double.class
				|| valClass==Short.class || valClass==Integer.class || valClass==Long.class 
				|| valClass==Float.class || valClass==Double.class
		){
			cell.setCellValue(Double.parseDouble(String.valueOf(value)));
		}else if(valClass==boolean.class || valClass==Boolean.class ){
			cell.setCellValue(Boolean.parseBoolean(String.valueOf(value)));
		}else if(valClass==Date.class ){
			cell.setCellValue((Date)(value));
		}else if(valClass==Calendar.class ){
			cell.setCellValue((Calendar)(value));
		}else{
			cell.setCellValue(String.valueOf(value));
		}
		return this;
	}
	/**<pre>
	* Cell에 double값을 할당한다.
	* </pre>
	* @param value cell value
	* @return XCell
	*/	
	public XCell setValue(double value){
		cell.setCellValue(value);
		return this;
	}
	/**<pre>
	* Cell에 boolean 값을 할당한다.
	* </pre>
	* @param value cell value
	* @return XCell
	*/
	public XCell setValue(boolean value){
		cell.setCellValue(value);
		return this;
	}
	/**<pre>
	* Cell의 값을 할당한다.
	* </pre>
	* @param val cell value
	* @param cellType cell type
	* @return XCell
	*/	
	public XCell setValue(Object val,int cellType){
		String value	= String.valueOf(val);
		if(cellType==CELL_TYPE_STRING ){
			cell.setCellValue(value);
		}else if(cellType==CELL_TYPE_NUMERIC){
			BigDecimal bd	= new BigDecimal(value);
			if(bd.longValue()==bd.doubleValue()){
				cell.setCellValue(Long.parseLong(value));
			}else{
				cell.setCellValue(Double.parseDouble(value));
			}
		}else if(cellType==CELL_TYPE_BOOLEAN ){//Boolean XCell type 
			cell.setCellValue(Boolean.parseBoolean(value));
		}else if(cellType==CELL_TYPE_ERROR  ){// Error XCell type
			cell.setCellValue(value);
		}else if(cellType==CELL_TYPE_BLANK){
			cell.setCellValue(value);
		}else if(cellType==CELL_TYPE_FORMULA){
			cell.setCellFormula(value);
		}else{
			cell.setCellValue(value);
		}
		cell.setCellType(cellType);
		return this;
	}
	/**<pre>
	* Cell에 formula 값을 할당한다.
	* </pre>
	* @param formula cell value
	* @return HCell
	*/	
	public Cell setFormula(String formula){
		cell.setCellFormula(formula);
		return this;
	}

	/**<pre>
	* Cell의 값을 String 형으로 반환한다.
	* </pre>
	* @return String
	*/	
	public String getString(){
		String cellValue	= "";
		if(cell==null)
			return cellValue;

		if(getCellType()==CELL_TYPE_STRING ){
			cellValue	= cell.getStringCellValue();
		}else if(getCellType()==CELL_TYPE_NUMERIC){
			BigDecimal bd	= new BigDecimal(String.valueOf( cell.getNumericCellValue()));
			if(bd.longValue()==bd.doubleValue()){
				cellValue	= String.valueOf(bd.longValue());
			}else{
				cellValue	= String.valueOf(bd.doubleValue());
			}
		}else if(getCellType()==CELL_TYPE_BOOLEAN ){//Boolean XCell type 
			cellValue	= String.valueOf(cell.getBooleanCellValue());
		}else if(getCellType()==CELL_TYPE_ERROR  ){// Error XCell type
			cellValue	= String.valueOf(cell.getErrorCellValue());
		}else if(getCellType()==CELL_TYPE_BLANK){
			cellValue	= "";
		}else if(getCellType()==CELL_TYPE_FORMULA){
			CellValue value	= ((XExcel)(getRow().getSheet().getExcel())).evaluator.evaluate(cell);
			int cellType	= value.getCellType();
			if(cellType==CELL_TYPE_STRING ){
				cellValue	= value.getStringValue();
			}else if(cellType==CELL_TYPE_NUMERIC){
				cellValue	= String.valueOf(value.getNumberValue());
			}else if(cellType==CELL_TYPE_BOOLEAN ){
				cellValue	= String.valueOf(value.getBooleanValue());
			}else if(cellType==CELL_TYPE_ERROR  ){
				cellValue	= String.valueOf(value.getErrorValue());
			}else if(cellType==CELL_TYPE_BLANK){
				cellValue	= "";
			}else{
				cellValue	= String.valueOf(value.formatAsString());
			}
		}else{
			try{cellValue	= cell.getStringCellValue();}catch(Exception e){}
		}
		return cellValue;
	}
	/**<pre>
	* Cell의 값을 formula 형으로 반환한다.
	* </pre>
	* @return String  수식
	*/
	public String getFormula() throws ParseException{
		return cell.getCellFormula();
	}
	
	/**<pre>
	* Cell의 값을 Date 형으로 반환한다.
	* </pre>
	* @param format Cell value의 SimpleDateFormat
	* @return Date
	*/
	public Date getDate(String format) throws ParseException{
		if(format!=null)
			return new java.text.SimpleDateFormat(format).parse(getString());
		else
			return cell.getDateCellValue();
	}
	/**<pre>
	* Cell의 값을 boolean 형으로 반환한다.
	* </pre>
	* @return boolean
	*/
	public boolean getBoolean(){
		return cell.getBooleanCellValue();
	}
	/**<pre>
	* Cell의 값을 Object 형으로 반환한다.
	* </pre>
	* @return Object
	*/
	public Object getObject(){
		Object obj	= null;
		if(getCellType()==CELL_TYPE_STRING ){
			obj	= cell.getStringCellValue();
		}else if(getCellType()==CELL_TYPE_NUMERIC){
			BigDecimal bd	= new BigDecimal(String.valueOf( cell.getNumericCellValue()));
			if(bd.longValue()==bd.doubleValue()){
				obj	= Long.parseLong(String.valueOf(bd.longValue()));
			}else{
				obj	= Double.parseDouble( String.valueOf(bd.doubleValue()));
			}
		}else if(getCellType()==CELL_TYPE_BOOLEAN ){//Boolean XCell type 
			obj	= Boolean.parseBoolean( String.valueOf(cell.getBooleanCellValue()));
		}else if(getCellType()==CELL_TYPE_ERROR  ){// Error XCell type
			obj	= String.valueOf(cell.getErrorCellValue());
		}else if(getCellType()==CELL_TYPE_BLANK){
			obj	= "";
		}else if(getCellType()==CELL_TYPE_FORMULA){
			CellValue value	= ((XExcel)(getRow().getSheet().getExcel())).evaluator.evaluate(cell);
			int cellType	= value.getCellType();
			if(cellType==CELL_TYPE_STRING ){
				obj	= value.getStringValue();
			}else if(cellType==CELL_TYPE_NUMERIC){
				obj	= String.valueOf(value.getNumberValue());
			}else if(cellType==CELL_TYPE_BOOLEAN ){
				obj	= String.valueOf(value.getBooleanValue());
			}else if(cellType==CELL_TYPE_ERROR  ){
				obj	= String.valueOf(value.getErrorValue());
			}else if(cellType==CELL_TYPE_BLANK){
				obj	= "";
			}else{
				obj	= String.valueOf(value.formatAsString());
			}
		}else{
			try{obj	= cell.getStringCellValue();}catch(Exception e){}
		}
		return obj;
	}
	@Override
	public boolean setCellStyle(CellStyle cs) {
			cell.setCellStyle(cs);
		return true;
	}
}