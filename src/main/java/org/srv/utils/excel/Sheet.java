package org.srv.utils.excel;

import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.srv.utils.excel.he.HSheet;
import org.srv.utils.excel.se.SSheet;
import org.srv.utils.excel.xe.XSheet;
import org.srv.vo.HMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName   : Sheet.java
 * @Description : Excel의 Sheet 제어 Utility
 * @author ADM기술팀
 * @since 2016. 6. 29.
 * @version 1.0
 * @see Excel
 * @see HSheet
 * @see XSheet
 * @see SSheet
 * @Modification Information
 * <pre>
 *     since          author              description
 *  ===========    =============    ===========================
 *  2016. 6. 29.     ADM기술팀     	최초 생성
 * </pre>
 */
public abstract class Sheet implements Serializable{
	private static final long serialVersionUID = 1L;

	/** sheet name **/
	protected String sheetName;
	/** row list **/
	protected List<Row> rowList;
	/** 가장 cell이 많은 row의 cell갯수 **/
	protected int maxCellSize;
	/** 부모 Excel 객체**/
	protected Excel excel;

	/**<pre>
	* 부모 Excel객체를 반환한다.
	* </pre>
	* @return Excel 현 Sheet의 부모 Excel객체
	*/	
	public Excel getExcel(){
		return excel;
	}
	/**<pre>
	* Sheet 명을 지정한다.
	* </pre>
	* @param sheetName sheet name
	*/	
	public void setSheetName(String sheetName){
		this.sheetName	= sheetName;
	}
	/**<pre>
	* Sheet 명을 반환한다.
	* </pre>
	* @return String 현 Sheet 명
	*/	
	public String getSheetName(){
		return sheetName;
	}
	/**<pre>
	* Sheet의 내용으로 Row목록을 생성한다.
	* </pre>
	*/	
	public abstract void loadRow();
	/**<pre>
	* row 갯수를 반환한다.
	* </pre>
	* @return int row갯수 
	*/	
	public int getRowCount(){
		return rowList.size();
	}
	/**<pre>
	* Row를 반환한다.
	* </pre>
	* @param rowIndex row번호
	* @return Row
	*/	
	public Row getRow(int rowIndex){
		return rowList.get(rowIndex);
	}
	/**<pre>
	* Cell을 반환한다.
	* </pre>
	* @param rowIndex row번호
	* @param cellIndex Cell번호
	* @return Cell
	*/	
	public Cell getCell(int rowIndex,int cellIndex){
		return rowList.get(rowIndex).getCell(cellIndex);
	}
	/**<pre>
	* 빈 Row를 추가한다.
	* </pre>
	* @return Row 추가한 Row
	*/	
	public Row addRow(){
		return insertRow(rowList.size());
	}
	/**<pre>
	* 빈 Row를 삽입한다.
	* </pre>
	* @param rowIndex row번호
	* @return Row 삽입한 Row
	*/	
	public abstract Row insertRow(int rowIndex);
	/**<pre>
	* Row를 추가한다.
	* </pre>
	* @param arr Row value
	* @return Row 추가한 Row
	*/	
	public Row addRow(Object[] arr){
		return insertRow(rowList.size(),arr);
	}
	/**<pre>
	* Row를 삽입한다.
	* </pre>
	* @param rowIndex row번호
	* @param arr Row value
	* @return Row 삽입한 Row
	*/	
	public abstract Row insertRow(int rowIndex,Object[] arr);
	/**<pre>
	* Row를 추가한다.
	* </pre>
	* @param list Row value
	* @return Row 추가한 Row
	*/	
	public Row addRow(List<?> list){
		return insertRow(rowList.size(),list);
	}
	/**<pre>
	* Row를 삽입한다.
	* </pre>
	* @param rowIndex row번호
	* @param list Row value
	* @return Row 삽입한 Row
	*/	
	public Row insertRow(int rowIndex,List<?> list){
		return insertRow(rowIndex,list.toArray());
	}
	/**<pre>
	* Row의 값을 설정한다.
	* </pre>
	* @param rowIndex row번호
	* @param list Row value
	* @return Row Row
	*/	
	public Row setRow(int rowIndex,List<Object> list){
		return setRow(rowIndex,list.toArray());
	}
	/**<pre>
	* Row의 값을 설정한다.
	* </pre>
	* @param rowIndex row번호
	* @param arr Row value
	* @return Row Row
	*/	
	public Row setRow(int rowIndex,Object[] arr){
		Row row	= null;
		if(rowIndex>=rowList.size())
			row	= addRow();
		else
			row	= rowList.get(rowIndex);

		for(int i=0;i<arr.length;i++){
			row.setCell(i, arr[i]);
		}
		if(maxCellSize<arr.length){
			maxCellSize	= arr.length;
		}
		return row;
	}
	/**<pre>
	* Row를 삭제한다
	* </pre>
	* @param rowIndex 삭제할 row 번호
	* @return Sheet
	*/	
	public abstract Sheet removeRow(int rowIndex);
	/**<pre>
	* Sheet의 값을 List<List<Object>>로 반환한다.
	* </pre>
	* @return List<List<Object>> Sheet value
	*/	
	public List<List<Object>> toList(){
		List<List<Object>> list	= new ArrayList<List<Object>>(rowList.size());
		for(int i=0;i<rowList.size();i++){
			list.add(rowList.get(i).toList());
		}
		return list;
	}
	//20160219 sunny 추가
	public List<HMap> toHMapList(){
		List<HMap> list	= new ArrayList<HMap>(rowList.size());
		String[] keys = null;
		for(int i=0;i<rowList.size();i++){			
			if(i==0){
				Row row = rowList.get(i);
				keys = row.toStringArray();
			}else{
				list.add(rowList.get(i).toHMap(keys));
			}		
		}
		return list;
	}

	/**<pre>
	* Sheet의 값을 Object[][]로 반환한다.
	* </pre>
	* @return Object[][] Sheet value
	*/	
	public Object[][] toArray(){
		Object[][] sheetArray	= new Object[rowList.size()][maxCellSize];
		Object [] rowArray		= null;
		for(int i=0;i<rowList.size();i++){
			rowArray	= rowList.get(i).toArray();
			System.arraycopy(rowArray, 0, sheetArray[i], 0, sheetArray[i].length);
		}
		return sheetArray;
	}
	/**<pre>
	* Sheet의 내용을 separated value형식의 문자열로 반환한다.
	* </pre>
	* @return String separated value형식의 문자열
	*/	
	public String toString(){
		StringBuffer sb	= new StringBuffer();
		for(int i=0;i<rowList.size();i++){
			sb.append(i==0?"":Excel._LINE).append(rowList.get(i).toString());
		}
		return sb.toString();
	}
	
	//20150107 기능 추가 by sunny
	abstract public Header getHeader();
	abstract public Footer getFooter();
	abstract public boolean defaultHF(String title);
}