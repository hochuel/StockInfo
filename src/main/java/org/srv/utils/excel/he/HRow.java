package org.srv.utils.excel.he;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.srv.utils.excel.Cell;
import org.srv.utils.excel.Excel;
import org.srv.utils.excel.Row;
import org.srv.utils.excel.se.SRow;
import org.srv.utils.excel.xe.XRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @ClassName   : HRow.java
 * @Description : HExcel의 Row 제어 Utility
 * @author ADM기술팀
 * @since 2016. 6. 29.
 * @version 1.0
 * @see Excel
 * @see Row
 * @see XRow
 * @see SRow
 * @Modification Information
 * <pre>
 *     since          author              description
 *  ===========    =============    ===========================
 *  2016. 6. 29.     ADM기술팀     	최초 생성
 * </pre>
 */
public class HRow extends Row implements Serializable{
	/** serial version UID **/
	private static final long serialVersionUID = 1L;
	/** HSSFormat Row **/
	private HSSFRow row;
	/**
	 * 빈 row를 생성
	 * @param sheet parent sheet
	 * **/
	public HRow(HSheet sheet){
		this.sheet	= sheet;
		this.cellList	= new ArrayList<Cell>();
	}
	/**
	 * row를 생성
	 * @param sheet parent sheet
	 * @param row HSSFormat row object
	 * **/
	public HRow(HSheet sheet, HSSFRow row){
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
	* HSSFormat row를 반환한다.
	* </pre>
	* @return HSSFRow
	*/	
	public HSSFRow getRow(){
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
			cellList.add(new HCell(this,row.getCell(i)));
		}
	}
	/**<pre>
	* Row에 빈 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @return HCell 추가된 cell
	*/	
	public HCell insertCell(int cellIndex){
		HCell cell	= new HCell( this, row.createCell( cellIndex ) );
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
	* @return HCell 추가된 cell
	*/	
	public HCell insertCell(int cellIndex, Object value){
		Class<?> valClass	= value.getClass();
		if(valClass==short.class || valClass==int.class || valClass==long.class 
				|| valClass==float.class || valClass==double.class
				|| valClass==Short.class || valClass==Integer.class || valClass==Long.class 
				|| valClass==Float.class || valClass==Double.class
		){
			HCell cell	= new HCell( this, row.createCell( cellIndex ) );
			cell.setValue(value,Cell.CELL_TYPE_NUMERIC);
			cellList.add(cellIndex,cell);
			setMaxCellSize(row.getLastCellNum());
			return cell;
		}else if(valClass==boolean.class || valClass==Boolean.class ){
			HCell cell	= new HCell( this, row.createCell( cellIndex ) );
			cell.setValue(value,Cell.CELL_TYPE_BOOLEAN);
			cellList.add(cellIndex,cell);
			setMaxCellSize(row.getLastCellNum());
			return cell;
		}else if(valClass==Date.class ){
			HCell cell	= new HCell( this, row.createCell( cellIndex ) );
			cell.setValue((Date)value);
			cellList.add(cellIndex,cell);
			setMaxCellSize(row.getLastCellNum());
			return cell;
		}else if(valClass==Calendar.class ){
			HCell cell	= new HCell( this, row.createCell( cellIndex ) );
			cell.setValue((Calendar)value);
			cellList.add(cellIndex,cell);
			setMaxCellSize(row.getLastCellNum());
			return cell;
		}else{
			HCell cell	= new HCell( this, row.createCell( cellIndex ) );
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
	* @return HCell 추가된 cell
	*/	
	public HCell insertCell(int cellIndex, short value){
		HCell cell	= new HCell( this, row.createCell( cellIndex ) );
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
	* @return HCell 추가된 cell
	*/	
	public HCell insertCell(int cellIndex, double value){
		HCell cell	= new HCell( this, row.createCell( cellIndex ) );
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
	* @return HCell 추가된 cell
	*/	
	public HCell insertCell(int cellIndex, boolean value){
		HCell cell	= new HCell( this, row.createCell( cellIndex ) );
		cell.setValue(value);
		cellList.add(cellIndex,cell);
		setMaxCellSize(row.getLastCellNum());
		return cell;
	}
	/**<pre>
	* Cell 을 삭제한다.
	* </pre>
	* @param cellIndex 삭제할 cell 번호
	* @return HRow 
	*/	
	public HRow removeCell(int cellIndex) {
		row.removeCell(row.getCell(cellIndex));
		cellList.remove(cellIndex);
		return this;
	}
}