package org.srv.utils.excel.se;

import org.srv.utils.excel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @ClassName   : SCell.java
 * @Description : SExcel의 Cell 제어 Utility
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
public class SCell extends Cell implements Serializable{
	/** serial version UID **/
	private static final long serialVersionUID = 1L;
	/** cell value string **/
	private String cell;
	/**<pre>
	 * constructor
	 * 빈 Cell을 생성
	 * </pre>
	 * @param row 부모 row
	 * **/
	public SCell(SRow row){
		this.row	= row;
	}
	/**<pre>
	 * constructor
	 * Cell을 생성
	 * </pre>
	 * @param row 부모 row
	 * @param cell Cell String value
	 * **/
	public SCell(SRow row, String cell){
		this.row	= row;
		this.cell	= cell;
	}
	/**<pre>
	* 현재 Cell의 value type을 반환한다.
	* </pre>
	* @return int cell type
	*/	
	public int getCellType(){
		return CELL_TYPE_STRING;
	}
	/**<pre>
	* Cell의 값을 할당한다.
	* </pre>
	* @param value cell value
	* @return SCell
	*/	
	public SCell setValue(Object value){
		this.cell	= String.valueOf(value);
		return this;
	}
	/**<pre>
	* Cell에 double값을 할당한다.
	* </pre>
	* @param value cell value
	* @return SCell
	*/	
	public SCell setValue(double value){
		this.cell	= String.valueOf(value);
		return this;
	}
	/**<pre>
	* Cell에 boolean 값을 할당한다.
	* </pre>
	* @param value cell value
	* @return SCell
	*/	
	public SCell setValue(boolean value){
		this.cell	= String.valueOf(value);
		return this;
	}
	/**<pre>
	* Cell의 값을 할당한다.
	* </pre>
	* @param val cell value
	* @param cellType cell type
	* @return SCell
	*/	
	public SCell setValue(Object val,int cellType){
		this.cell	= String.valueOf(val);
		return this;
	}
	
	/**<pre>
	* Cell에 formula 값을 할당한다.(SCell은 사용할 필요가 없다.)
	* </pre>
	* @param formula cell value
	* @return HCell
	*/	
	public Cell setFormula(String formula){
		this.cell	= formula;
		return this;
	}
	/**<pre>
	* Cell의 formula 값을 반환한다.(SCell은 사용할 필요가 없다.)
	* </pre>
	* @return String
	* @deprecated
	*/	
	public String getFormula(){
		return cell;
	}

	
	/**<pre>
	* Cell의 값을 String 형으로 반환한다.
	* </pre>
	* @return String
	*/	
	public String getString(){
		return cell;
	}
	/**<pre>
	* Cell의 값을 Date 형으로 반환한다.
	* </pre>
	* @param format Cell value의 SimpleDateFormat
	* @return Date
	*/
	public Date getDate(String format) throws ParseException{
		return new SimpleDateFormat(format).parse(cell);
	}
	/**<pre>
	* Cell의 값을 boolean 형으로 반환한다.
	* </pre>
	* @return boolean
	*/
	public boolean getBoolean(){
		return Boolean.parseBoolean(cell);
	}
	/**<pre>
	* Cell의 값을 Object 형으로 반환한다.
	* </pre>
	* @return Object
	*/
	public Object getObject(){
		return cell;
	}
	@Override
	public boolean setCellStyle(CellStyle cs) {
		// TODO Auto-generated method stub
		return false;
	}
}