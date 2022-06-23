package org.srv.utils.excel.xe;

import org.srv.utils.excel.Excel;
import org.srv.utils.excel.Row;
import org.srv.utils.excel.Sheet;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <pre>
 * Statements
 * </pre>
 *
 * @ClassName   : XSheet.java
 * @Description : XExcel의 Sheet 제어 Utility
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
public class XSheet extends Sheet implements Serializable {
	/** serial version UID **/
	private static final long serialVersionUID = 1L;
	/** XSS Format sheet object **/
	private XSSFSheet sheet;
	
	/**<pre>
	 * constructor
	 * 빈 Sheet 생성
	 * </pre>
	 * @param excel parent Excel object
	 * */
	public XSheet(Excel excel){
		this.excel	= excel;
		rowList		= new ArrayList<Row>();
	}
	/**<pre>
	 * constructor
	 * Sheet 내용 loading
	 * </pre>
	 * @param excel parent Excel object
	 * @param sheetName sheet name
	 * @param sheet XSSFormat Sheet
	 * */
	public XSheet(Excel excel, String sheetName, XSSFSheet sheet){
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
			rowList.add(new XRow(this,sheet.getRow(i)));
			cellSize	= sheet.getRow(i).getLastCellNum();
			if(maxCellSize<cellSize)
				maxCellSize	= cellSize;
		}
	}
	/**<pre>
	* 빈 Row를 삽입한다.
	* </pre>
	* @param rowIndex row번호
	* @return Row 삽입한 Row
	*/	
	public XRow insertRow(int rowIndex){
		XRow row	= new XRow(this, sheet.createRow(rowIndex ));
		rowList.add(rowIndex,row);
		return row;
	}
	/**<pre>
	* Row를 삽입한다.
	* </pre>
	* @param rowIndex row번호
	* @param arr Row value
	* @return XRow 삽입한 Row
	*/	
	public XRow insertRow(int rowIndex,Object[] arr){
		XRow row	= new XRow(this, sheet.createRow(rowIndex ));
		for(int i=0;i<arr.length;i++){
			row.addCell(arr[i]);
		}
		rowList.add(rowIndex,row);
		return row;
	}
	/**<pre>
	* Row를 삭제한다
	* </pre>
	* @param rowIndex 삭제할 row 번호
	* @return XSheet
	*/	
	public XSheet removeRow(int rowIndex){
		sheet.removeRow(sheet.getRow(rowIndex));
		rowList.remove(rowIndex);
		return this;
	}
	
	//20160107 기능 추가 by sunny
	@Override
	public Header getHeader() {
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
