package org.srv.utils.excel;

import org.srv.utils.excel.he.HRow;
import org.srv.utils.excel.se.SRow;
import org.srv.utils.excel.xe.XRow;
import org.srv.vo.HMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName   : Row.java
 * @Description : Excel의 Row 제어 Utility
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
public abstract class Row implements Serializable{
	private static final long serialVersionUID = 1L;

	/** cell list **/
	protected List<Cell> cellList;
	/** parent sheet **/
	protected Sheet sheet;

	/**<pre>
	*parent sheet를 반환한다.
	* </pre>
	* @return Sheet parent Sheet object
	*/	
	public Sheet getSheet(){
		return sheet;
	}
	/**<pre>
	* Row에 속한 cell목록을 loading한다.
	* </pre>
	*/	
	public abstract void loadCell();
	/**<pre>
	* Row에 Cell을 추가한다.
	* </pre>
	* @param value cell value
	* @return Cell 추가된 cell
	*/	
	public Cell addCell(Object value){
		return insertCell(cellList.size(),value);
	}
	/**<pre>
	* Row에 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @param value cell value
	* @return Cell 추가된 cell
	*/	
	public abstract Cell insertCell(int cellIndex, Object value);
	/**<pre>
	* Row에 빈 Cell을 추가한다.
	* </pre>
	* @return Cell 추가된 cell
	*/	
	public Cell addCell(){
		return insertCell(cellList.size());
	}
	/**<pre>
	* Row에 빈 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @return Cell 추가된 cell
	*/	
	public abstract Cell insertCell(int cellIndex);
	/**<pre>
	* Row에 short값을 가진 Cell을 추가한다.
	* </pre>
	* @param value cell value
	* @return Cell 추가된 cell
	*/	
	public Cell addCell(short value){
		return insertCell(cellList.size(),value);
	}
	/**<pre>
	* Row에 short값을 가진 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @param value cell value
	* @return Cell 추가된 cell
	*/	
	public abstract Cell insertCell(int cellIndex, short value);
	/**<pre>
	* Row에 double값을 가진 Cell을 추가한다.
	* </pre>
	* @param value cell value
	* @return Cell 추가된 cell
	*/	
	public Cell addCell(double value){
		return insertCell(cellList.size(),value);
	}
	/**<pre>
	* Row에 double 값을 가진 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @param value cell value
	* @return Cell 추가된 cell
	*/	
	public abstract Cell insertCell(int cellIndex, double value);
	/**<pre>
	* Row에 boolean 값을 가진 Cell을 추가한다.
	* </pre>
	* @param value cell value
	* @return Cell 추가된 cell
	*/	
	public Cell addCell(boolean value){
		return insertCell(cellList.size(),value);
	}
	/**<pre>
	* Row에 boolean 값을 가진 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @param value cell value
	* @return Cell 추가된 cell
	*/	
	public abstract Cell insertCell(int cellIndex, boolean value);
	/**<pre>
	* Cell 값을 할당한다.
	* </pre>
	* @param cellIndex cell 번호
	* @param value cell value
	* @return Cell 
	*/	
	public Cell setCell(int cellIndex,Object value){
		Cell cell	= null;
		if(cellIndex>=cellList.size())
			cell	= addCell();
		else
			cell	= cellList.get(cellIndex);
		return cell.setValue(value);
	}
	/**<pre>
	* Cell 을 삭제한다.
	* </pre>
	* @param cellIndex 삭제할 cell 번호
	* @return Row 
	*/	
	public abstract Row removeCell(int cellIndex);
	/**<pre>
	* 현재 row의 Cell 갯수를 반환한다.
	* </pre>
	* @return int Cell 갯수 
	*/	
	public int getCellCount(){
		return cellList.size();
	}
	/**<pre>
	* Cell을 반환한다.
	* </pre>
	* @param cellIndex cell 번호
	* @return int Cell 갯수 
	*/	
	public Cell getCell(int cellIndex){
		return cellList.get(cellIndex);
	}
	/**<pre>
	* Row의 내용을 List로 반환한다.
	* </pre>
	* @return List<Object>  
	*/	
	public List<Object> toList(){
		List<Object> list	= new ArrayList<Object>(cellList.size());
		for(int i=0;i<cellList.size();i++){
			list.add(getCell(i).getObject());
		}
		return list;
	}
	//20160219 sunny 추가
	public HMap toHMap(String[] keys){
		HMap hmap	= new HMap();
		for(int i=0;i<cellList.size();i++){
			hmap.put(keys[i], cellList.get(i).getString());
		}
		return hmap;
	}
	
	/**<pre>
	* Row의 내용을 Object[]로 반환한다.
	* </pre>
	* @return Object[]
	*/	
	public Object[] toArray(){
		Object[] rowArray	= new Object[cellList.size()];
		for(int i=0;i<cellList.size();i++){
			rowArray[i]	= getCell(i).getObject();
		}
		return rowArray;
	}
	//20160219 sunny 추가
	public String[] toStringArray(){
		String[] rowArray	= new String[cellList.size()];
		for(int i=0;i<cellList.size();i++){
			rowArray[i]	= getCell(i).getString();
		}
		return rowArray;
	}
	/**<pre>
	* Sheet에서 가장 많은 Cell을 가진 row의 Cell갯수 저장한다.
	* </pre>
	* @param cellSize 현재 row의 Cell갯수
	*/	
	public void setMaxCellSize(int cellSize){
		if(getSheet().maxCellSize<cellSize)
			getSheet().maxCellSize	= cellSize;
	}
	/**<pre>
	* row 의 내용을 separated value형식의 문자열로 반환한다. 
	* </pre>
	* @return String separated value형식의 row 의 내용
	*/	
	public String toString(){
		StringBuffer sb	= new StringBuffer();
		for(int i=0;i<cellList.size();i++){
			sb.append(i==0?"":getSheet().getExcel().separator).append(cellList.get(i).getString());
		}
		return sb.toString();
	}
}