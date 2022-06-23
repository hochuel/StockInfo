package org.srv.utils.excel;

import org.apache.poi.ss.usermodel.CellStyle;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;


public abstract class Cell implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 숫자 형 cell type **/
    public static final int CELL_TYPE_NUMERIC 	= org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
	/** 문자열 형 cell type **/
    public static final int CELL_TYPE_STRING 	= org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;
	/** fomula(수식) 형 cell type **/
    public static final int CELL_TYPE_FORMULA 	= org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA;
	/** blank 형 cell type **/
    public static final int CELL_TYPE_BLANK 	= org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK;
	/** boolean 형 cell type **/
    public static final int CELL_TYPE_BOOLEAN 	= org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN;
	/** error 형 cell type **/
    public static final int CELL_TYPE_ERROR 	= org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR;
    /** parent row **/
    protected Row row;

	/**<pre>
	* 현재 Cell의 value type을 반환한다.
	* </pre>
	* @return int cell type
	*/	
	public abstract int getCellType();
	/**<pre>
	* 부모가 되는 Row를 반환한다.
	* </pre>
	* @return Row
	*/	
	public Row getRow(){
		return row;
	}
	/**<pre>
	* Cell의 값을 할당한다.
	* </pre>
	* @param value cell value
	* @return Cell
	*/	
	public abstract Cell setValue(Object value);
	/**<pre>
	* Cell에 double값을 할당한다.
	* </pre>
	* @param value cell value
	* @return Cell
	*/	
	public abstract Cell setValue(double value);
	/**<pre>
	* Cell에 boolean 값을 할당한다.
	* </pre>
	* @param value cell value
	* @return Cell
	*/	
	public abstract Cell setValue(boolean value);
	/**<pre>
	* Cell의 값을 할당한다.
	* </pre>
	* @param val cell value
	* @param cellType cell type
	* @return Cell
	*/	
	public abstract Cell setValue(Object val,int cellType);
	/**<pre>
	* Cell에 formula 값을 할당한다.
	* </pre>
	* @param formula cell value
	* @return Cell
	*/	
	public abstract Cell setFormula(String formula);
	/**<pre>
	* Cell의 formula 값을 반환한다.
	* </pre>
	* @return String formula
	*/	
	public abstract String getFormula() throws ParseException;

	/**<pre>
	* Cell의 값을 String 형으로 반환한다.
	* </pre>
	* @return String
	*/	
	public abstract String getString();
	/**<pre>
	* Cell의 값을 short 형으로 반환한다.
	* </pre>
	* @return short
	*/	
	public short getShort() throws NumberFormatException{
		return Short.parseShort(getString());
	}
	/**<pre>
	* Cell의 값을 int 형으로 반환한다.
	* </pre>
	* @return int
	*/	
	public int getInt() throws NumberFormatException{
		return Integer.parseInt(getString());
	}
	/**<pre>
	* Cell의 값을 long 형으로 반환한다.
	* </pre>
	* @return long
	*/	
	public long getLong() throws NumberFormatException{
		return Long.parseLong(getString());
	}
	/**<pre>
	* Cell의 값을 float 형으로 반환한다.
	* </pre>
	* @return float
	*/	
	public float getFloat() throws NumberFormatException{
		return Float.parseFloat(getString());
	}
	/**<pre>
	* Cell의 값을 double 형으로 반환한다.
	* </pre>
	* @return double
	*/	
	public double getDouble() throws NumberFormatException{
		return Double.parseDouble(getString());
	}
	/**<pre>
	* Cell의 값을 Date 형으로 반환한다.
	* </pre>
	* @return Date
	*/
	public Date getDate() throws ParseException{
		return getDate(null);
	}
	/**<pre>
	* Cell의 값을 Date 형으로 반환한다.
	* </pre>
	* @param format Cell value의 SimpleDateFormat
	* @return Date
	*/
	public abstract Date getDate(String format) throws ParseException;
	/**<pre>
	* Cell의 값을 boolean 형으로 반환한다.
	* </pre>
	* @return boolean
	*/
	public abstract boolean getBoolean();
	/**<pre>
	* Cell의 값을 Object 형으로 반환한다.
	* </pre>
	* @return Object
	*/
	public abstract Object getObject();
	
	//20150107 기능 추가 by sunny
	public abstract boolean setCellStyle(CellStyle cs);
}