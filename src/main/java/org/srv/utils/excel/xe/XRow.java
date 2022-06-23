package org.srv.utils.excel.xe;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.srv.utils.excel.Cell;
import org.srv.utils.excel.Row;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @ClassName   : XRow.java
 * @Description : XExcel의 Row 제어 Utility
 * @author ADM기술팀
 * @since 2016. 6. 29.
 * @version 1.0
 * @see Excel
 * @see Sheet
 * @see HRow
 * @see XRow
 * @see SRow
 * @Modification Information
 * <pre>
 *     since          author              description
 *  ===========    =============    ===========================
 *  2016. 6. 29.     ADM기술팀     	최초 생성
 * </pre>
 */
public class XRow extends Row implements Serializable{
	/** serial version UID **/
	private static final long serialVersionUID = 1L;
	/** XSSFormat Row **/
	private XSSFRow row;
	/**
	 * 빈 row를 생성
	 * @param sheet parent sheet
	 * **/
	public XRow(XSheet sheet){
		this.sheet	= sheet;
		this.cellList	= new ArrayList<Cell>();
	}
	/**
	 * row를 생성
	 * @param sheet parent sheet
	 * @param row XSSFormat row object
	 * **/
	public XRow(XSheet sheet, XSSFRow row){
		this.sheet	= sheet;
		this.row	= row;
		if(row==null){
			this.cellList	= new ArrayList<Cell>();
		}else{
			this.cellList	= new ArrayList<Cell>(row.getLastCellNum()<0?0:row.getLastCellNum());
			loadCell();
		}
	}
	/**<pre>
	* XSSFormat row를 반환한다.
	* </pre>
	* @return XSSFRow
	*/	
	public XSSFRow getRow(){
		return row;
	}
	/**<pre>
	* Row에 속한 cell목록을 loading한다.
	* </pre>
	*/	
	public void loadCell(){
		for(int i=0;i<row.getLastCellNum();i++){
			if(row.getCell(i)==null)
				row.createCell(i);
			cellList.add(new XCell(this,row.getCell(i)));
		}
	}
	/**<pre>
	* Row에 빈 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @return XCell 추가된 cell
	*/	
	public XCell insertCell(int cellIndex){
		XCell cell	= new XCell( this, row.createCell( cellIndex ) );
		cell.setValue("");
		cellList.add(cellIndex,cell);
		setMaxCellSize(row.getLastCellNum());
		return cell;
	}
	/**<pre>
	* Row에 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @param value cell value
	* @return XCell 추가된 cell
	*/	
	public XCell insertCell(int cellIndex, Object value){
		Class<?> valClass	= value.getClass();
		if(valClass==short.class || valClass==int.class || valClass==long.class 
				|| valClass==float.class || valClass==double.class
				|| valClass==Short.class || valClass==Integer.class || valClass==Long.class 
				|| valClass==Float.class || valClass==Double.class
		){
			XCell cell	= new XCell( this, row.createCell( cellIndex ) );
			cell.setValue(value,Cell.CELL_TYPE_NUMERIC);
			cellList.add(cellIndex,cell);
			setMaxCellSize(row.getLastCellNum());
			return cell;
		}else if(valClass==boolean.class || valClass==Boolean.class ){
			XCell cell	= new XCell( this, row.createCell( cellIndex ) );
			cell.setValue(value,Cell.CELL_TYPE_BOOLEAN);
			cellList.add(cellIndex,cell);
			setMaxCellSize(row.getLastCellNum());
			return cell;
		}else if(valClass==Date.class ){
			XCell cell	= new XCell( this, row.createCell( cellIndex ) );
			cell.setValue((Date)value);
			cellList.add(cellIndex,cell);
			setMaxCellSize(row.getLastCellNum());
			return cell;
		}else if(valClass==Calendar.class ){
			XCell cell	= new XCell( this, row.createCell( cellIndex ) );
			cell.setValue((Calendar)value);
			cellList.add(cellIndex,cell);
			setMaxCellSize(row.getLastCellNum());
			return cell;
		}else{
			XCell cell	= new XCell( this, row.createCell( cellIndex ) );
			cell.setValue(String.valueOf(value));
			cellList.add(cellIndex,cell);
			setMaxCellSize(row.getLastCellNum());
			return cell;
		}
	}
	/**<pre>
	* Row에 short값을 가진 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @param value cell value
	* @return XCell 추가된 cell
	*/	
	public XCell insertCell(int cellIndex, short value){
		XCell cell	= new XCell( this, row.createCell( cellIndex ) );
		cell.setValue(value);
		cellList.add(cellIndex,cell);
		setMaxCellSize(row.getLastCellNum());
		return cell;
	}
	/**<pre>
	* Row에 double 값을 가진 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @param value cell value
	* @return XCell 추가된 cell
	*/	
	public XCell insertCell(int cellIndex, double value){
		XCell cell	= new XCell( this, row.createCell( cellIndex ) );
		cell.setValue(value);
		cellList.add(cellIndex,cell);
		setMaxCellSize(row.getLastCellNum());
		return cell;
	}
	/**<pre>
	* Row에 boolean 값을 가진 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @param value cell value
	* @return XCell 추가된 cell
	*/	
	public XCell insertCell(int cellIndex, boolean value){
		XCell cell	= new XCell( this, row.createCell( cellIndex ) );
		cell.setValue(value);
		cellList.add(cellIndex,cell);
		setMaxCellSize(row.getLastCellNum());
		return cell;
	}
	/**<pre>
	* Cell 을 삭제한다.
	* </pre>
	* @param cellIndex 삭제할 cell 번호
	* @return XRow 
	*/	
	public XRow removeCell(int cellIndex) {
		row.removeCell(row.getCell(cellIndex));
		cellList.remove(cellIndex);
		return this;
	}
}

