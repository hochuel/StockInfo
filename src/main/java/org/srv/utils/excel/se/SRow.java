package org.srv.utils.excel.se;

import org.srv.utils.StringUtil;
import org.srv.utils.excel.Cell;
import org.srv.utils.excel.Row;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @ClassName   : SRow.java
 * @Description : SExcel의 Row 제어 Utility
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
public class SRow extends Row implements Serializable{
	/** serial version UID **/
	private static final long serialVersionUID = 1L;
	/** Separated value Row String **/
	private String row;
	/**
	 * 빈 row를 생성
	 * @param sheet parent sheet
	 * **/
	public SRow(SSheet sheet){
		this.sheet	= sheet;
		this.cellList	= new ArrayList<Cell>();
	}
	/**
	 * row를 생성
	 * @param sheet parent sheet
	 * @param row Separated value row String
	 * **/
	public SRow(SSheet sheet,String row){
		this.sheet	= sheet;
		this.row	= row;
		this.cellList	= new ArrayList<Cell>();
		loadCell();
	}
	/**<pre>
	* Separated value row String을 반환한다.
	* </pre>
	* @return String
	*/	
	public String getRow(){
		StringBuffer sb	= new StringBuffer();
		for(int i=0;i<cellList.size();i++){
			sb.append(i==0?"":getSheet().getExcel().separator).append(cellList.get(i).getString());
		}
		return sb.toString();
	}
	/**<pre>
	* Row에 속한 cell목록을 loading한다.
	* </pre>
	*/	
	public void loadCell(){
		if(row==null || "".equals(row))
			return;
		//org.apache.commons.lang.StringUtils 여기서 사용하던 split 함수를 able의 StringUtil 클래스의 함수로 호출
		//20151229 수정 sunny
		String[] arr	=  StringUtil.split(row, getSheet().getExcel().separator.charAt(0));
		//String[] arr	=  org.apache.maven.doxia.util.StringUtilStringUtils.split(row, getSheet().getExcel().separator.charAt(0));
		
		for(int i=0;i<arr.length;i++){
			cellList.add(new SCell(this,arr[i]));
		}
		if(row.endsWith(getSheet().getExcel().separator)){
			cellList.add(new SCell(this,""));
		}
	}
	/**<pre>
	* Row에 빈 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @return SCell 추가된 cell
	*/	
	public SCell insertCell(int cellIndex){
		SCell cell	= new SCell( this, "" );
		cellList.add(cellIndex,cell);
		setMaxCellSize(cellList.size());
		return cell;
	}
	/**<pre>
	* Row에 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @param value cell value
	* @return SCell 추가된 cell
	*/	
	public SCell insertCell(int cellIndex, Object value){
		SCell cell	= new SCell( this, String.valueOf(value) );
		cellList.add(cellIndex,cell);
		setMaxCellSize(cellList.size());
		return cell;
	}
	/**<pre>
	* Row에 short값을 가진 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @param value cell value
	* @return SCell 추가된 cell
	*/	
	public SCell insertCell(int cellIndex, short value){
		SCell cell	= new SCell( this, String.valueOf(value) );
		cellList.add(cellIndex,cell);
		setMaxCellSize(cellList.size());
		return cell;
	}
	/**<pre>
	* Row에 double 값을 가진 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @param value cell value
	* @return SCell 추가된 cell
	*/	
	public SCell insertCell(int cellIndex, double value){
		SCell cell	= new SCell( this, String.valueOf(value) );
		cellList.add(cellIndex,cell);
		setMaxCellSize(cellList.size());
		return cell;
	}
	/**<pre>
	* Row에 boolean 값을 가진 Cell을 삽입한다.
	* </pre>
	* @param cellIndex cell 번호
	* @param value cell value
	* @return SCell 추가된 cell
	*/	
	public SCell insertCell(int cellIndex, boolean value){
		SCell cell	= new SCell( this, String.valueOf(value) );
		cellList.add(cellIndex,cell);
		setMaxCellSize(cellList.size());
		return cell;
	}
	/**<pre>
	* Cell 을 삭제한다.
	* </pre>
	* @param cellIndex 삭제할 cell 번호
	* @return SRow 
	*/	
	public SRow removeCell(int cellIndex) {
		cellList.remove(cellIndex);
		return this;
	}
}
