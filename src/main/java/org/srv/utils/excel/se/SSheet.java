package org.srv.utils.excel.se;


import org.srv.utils.StringUtil;
import org.srv.utils.excel.Row;

import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.srv.utils.excel.Excel;
import org.srv.utils.excel.Sheet;
import org.srv.utils.excel.he.HSheet;
import org.srv.utils.excel.xe.XSheet;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @ClassName   : SSheet.java
 * @Description : SExcel의 Sheet 제어 Utility
 * @author ADM기술팀
 * @since 2016. 6. 29.
 * @version 1.0
 * @see Excel
 * @see HSheet
 * @see XSheet
 * @Modification Information
 * <pre>
 *     since          author              description
 *  ===========    =============    ===========================
 *  2016. 6. 29.     ADM기술팀     	최초 생성
 * </pre>
 */
public class SSheet extends Sheet implements Serializable {
	/** serial version UID **/
	private static final long serialVersionUID = 1L;
	/** sheet string **/
	private String sheet;
	
	/**<pre>
	 * constructor
	 * 빈 Sheet 생성
	 * </pre>
	 * @param excel parent Excel object
	 * */
	public SSheet(Excel excel){
		this.excel	= excel;
		rowList		= new ArrayList<Row>();
	}
	/**<pre>
	 * constructor
	 * Sheet 내용 loading
	 * </pre>
	 * @param excel parent Excel object
	 * @param sheetName sheet name
	 * @param sheet Separated value String
	 * */
	public SSheet(Excel excel, String sheetName, String sheet){
		this.excel	= excel;
		this.sheetName	= sheetName;
		this.sheet	= sheet;
		rowList		= new ArrayList<Row>();
		loadRow();
	}
	/**<pre>
	* Sheet의 내용으로 Row목록을 생성한다.
	* </pre>
	*/	
	public void loadRow(){
		int cellSize	= 0;
		if(sheet==null || "".equals(sheet))
			return;

//		sheet	= org.apache.maven.doxia.util.StringUtil.replaceAll(sheet, "\r\n", "\n");
//		sheet	= org.apache.maven.doxia.util.StringUtil.replaceAll(sheet, "\n", Excel._LINE);
		
		//org.apache.commons.lang.StringUtils 여기서 사용하던 split 함수를 able의 StringUtil 클래스의 함수로 호출
	    //20151229 수정 sunny		
		sheet	= StringUtil.replace(sheet, "\r\n", "\n");
		sheet	= StringUtil.replace(sheet, "\n", Excel._LINE);
		
		String[] arr	= sheet.split(Excel._LINE);
		SRow row		= null;
		for(int i=0;i<arr.length;i++){
			rowList.add(row = new SRow(this,arr[i]));
			cellSize	= row.getCellCount();
			if(maxCellSize<cellSize) 
				maxCellSize	= cellSize;
		}
	}
	/**<pre>
	* 빈 Row를 삽입한다.
	* </pre>
	* @param rowIndex row번호
	* @return SRow 삽입한 Row
	*/	
	public SRow insertRow(int rowIndex){
		SRow row	= new SRow(this, "" );
		rowList.add(rowIndex,row);
		return row;
	}
	/**<pre>
	* Row를 삽입한다.
	* </pre>
	* @param rowIndex row번호
	* @param arr Row value
	* @return SRow 삽입한 Row
	*/	
	public SRow insertRow(int rowIndex,Object[] arr){
		SRow row	= new SRow(this, "");
		for(int i=0;i<arr.length;i++){
			row.addCell(arr[i]);
		}
		if(maxCellSize<arr.length){
			maxCellSize	= arr.length;
		}
		rowList.add(rowIndex,row);
		return row;
	}
	/**<pre>
	* Row를 삭제한다
	* </pre>
	* @param rowIndex 삭제할 row 번호
	* @return SSheet
	*/	
	public SSheet removeRow(int rowIndex){
		rowList.remove(rowIndex);
		return this;
	}
	@Override
	public Header getHeader() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Footer getFooter() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean defaultHF(String title) {
		// TODO Auto-generated method stub
		return false;
	}
}
