package org.srv.file;

import java.util.Date;

/**
 * @ClassName   : FileVO.java
 * @Description : 파일 업로드/다운로드용 VO class
 * @author ADM기술팀
 * @since 2016. 6. 29.
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 *     since          author              description
 *  ===========    =============    ===========================
 *  2016. 6. 29.     ADM기술팀     	최초 생성
 * </pre>
 */
public class FileVO {
	
	/**
	 * 파일 아이디
	 */
	private String fileId = null;
	
	/**
	 * 파일 이름 (업로드/다운로드시)
	 */
	private String originalFileName = null;
	
	/**
	 * 저장된 파일 이름
	 */
	private String storedFileName = null;
	
	/**
	 * 파일 크기
	 */
	private long fileSize = -1L;
	
	/**
	 * 저장 폴더
	 */
	private String folderPath = null;
	
	/**
	 * 등록 날짜
	 */
	private Date regDate = null;
	
	
	/**
	 * upload param
	 */
	private String paramName = null;
	

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getStoredFileName() {
		return storedFileName;
	}

	public void setStoredFileName(String storedFileName) {
		this.storedFileName = storedFileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

}
