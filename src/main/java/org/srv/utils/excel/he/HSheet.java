package org.srv.utils.excel.he;

import org.srv.utils.excel.Excel;
import org.srv.utils.excel.Row;
import org.srv.utils.excel.Sheet;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.srv.utils.excel.se.SSheet;
import org.srv.utils.excel.xe.XSheet;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @ClassName   : HSheet.java
 * @Description : HExcel의 Sheet 제어 Utility
 * @author ADM기술팀
 * @since 2016. 6. 29.
 * @version 1.0
 * @see Excel
 * @see Sheet
 * @see XSheet
 * @see SSheet
 * @Modification Information
 * <pre>
 *     since          author              description
 *  ===========    =============    ===========================
 *  2016. 6. 29.     ADM기술팀     	최초 생성
 * </pre>
 */
public class HSheet extends Sheet implements Serializable {
	/** serial version UID **/
	private static final long serialVersionUID = 1L;
	/** HSS Format sheet object **/
	private HSSFSheet sheet;

	/**<pre>
	 * constructor
	 * 빈 Sheet 생성
	 * </pre>
	 * @param excel parent Excel object
	 * */
	public HSheet(Excel excel){
		this.excel	= excel;
		rowList		= new ArrayList<Row>();
	}
	/**<pre>
	 * constructor
	 * Sheet 내용 loading
	 * </pre>
	 * @param excel parent Excel object
	 * @param sheetName sheet name
	 * @param sheet HSSFormat Sheet
	 * */
	public HSheet(Excel excel, String sheetName, HSSFSheet sheet){
		this.excel	= excel;
		this.sheetName	= sheetName;
		this.sheet	= sheet;
		rowList		= new ArrayList<Row>(sheet.getLastRowNum()<0?0:sheet.getLastRowNum()+1);
		loadRow();
	}
	/**<pre>
	* Sheet의 내용으로 Row목록을 생성한다.
	* </pre>
	*/	
	public void loadRow(){
		int cellSize	= 0;
		for(int i=0;i<=sheet.getLastRowNum();i++){
			if(sheet.getRow(i)==null)
				sheet.createRow(i);
			rowList.add(new HRow(this,sheet.getRow(i)));
			cellSize	= sheet.getRow(i).getLastCellNum();
			if(maxCellSize<cellSize) 
				maxCellSize	= cellSize;
		}
	}
	/**<pre>
	* 빈 Row를 삽입한다.
	* </pre>
	* @param rowIndex row번호
	* @return HRow 삽입한 Row
	*/	
	public HRow insertRow(int rowIndex){
		HRow row	= new HRow(this, sheet.createRow(rowIndex ));
		rowList.add(rowIndex,row);
		return row;
	}
	/**<pre>
	* Row를 삽입한다.
	* </pre>
	* @param rowIndex row번호
	* @param arr Row value
	* @return HRow 삽입한 Row
	*/	
	public HRow insertRow(int rowIndex,Object[] arr){
		HRow row	= new HRow(this, sheet.createRow(rowIndex ));
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
	* @return HSheet
	*/	
	public HSheet removeRow(int rowIndex){
		sheet.removeRow(sheet.getRow(rowIndex));
		rowList.remove(rowIndex);
		return this;
	}
	
	//20160107 기능추가 by sunny
	
	@Override
	public Header getHeader() {
		// TODO Auto-generated method stub
		return sheet.getHeader();
	}
	@Override
	public Footer getFooter() {
		// TODO Auto-generated method stub
		return sheet.getFooter();
	}
	@Override
	public boolean defaultHF(String title) {
		Header header = getHeader();
		header.setCenter(HSSFHeader.font("Arial", "Bold") + HSSFHeader.fontSize((short) 14) + title);		
		   
		//Set Footer Information with Page Numbers
	    Footer footer = getFooter();
	    footer.setRight( "Page " + HeaderFooter.page() + " of " + HeaderFooter.numPages() );
		return true;
	}
}
