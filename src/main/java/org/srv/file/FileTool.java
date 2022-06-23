/**
 *  Class Name : EgovFileTool.java
 *  Description : 시스템 디렉토리 정보를 확인하여 제공하는  Business class
 *  Modification Information
 *
 *     수정일         수정자                   수정내용
 *   -------    --------    ---------------------------
 *   2009.01.13    조재영          최초 생성
 *
 *  @author 공통 서비스 개발팀 조재영,박지욱
 *  @since 2009. 01. 13
 *  @version 1.0
 *  @see
 *
 *  Copyright (C) 2009 by MOPAS  All right reserved.
 */
package org.srv.file;

import org.srv.utils.CommonSecurityUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @ClassName   : FileTool.java
 * @Description : File Handling을 위한 유틸리티
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
public class FileTool {

	// 파일사이즈 1K
	static final long BUFFER_SIZE = 1024L;
	// 파일구분자
	static final char FILE_SEPARATOR = File.separatorChar;
	// 윈도우시스템 파일 접근권한
	static final char ACCESS_READ = 'R'; // 읽기전용
	static final char ACCESS_SYS = 'S'; // 시스템
	static final char ACCESS_HIDE = 'H'; // 숨김
	// 최대 문자길이
	static final int MAX_STR_LEN = 1024;

	/**
	 * <pre>
	 * Comment : 디렉토리 존재여부를 확인한다. (단일디렉토리 확인용)
	 * </pre>
	 * @return boolean result
	 */
	public static boolean checkExistDirectory(String targetDirPath) throws Exception {

		// 인자값 유효하지 않은 경우 공백 리턴
		if (targetDirPath == null || targetDirPath.equals("")) {
			return false;
		}

		boolean result = false;
		File f = new File(CommonSecurityUtil.filePathBlackList(targetDirPath));
		if (f.exists() && f.isDirectory()) {
			result = true;
		}

		return result;
	}

	/**
	 * <pre>
	 * Comment : 디렉토리 존재여부를 확인한다. (생성일자를 조건으로 조건구간내 포함되는지 확인)
	 * </pre>
	 * @return boolean result 
	 */
	public static boolean checkExistDirectory(String targetDirPath, String fromDate, String toDate) throws Exception {

		// 인자값 유효하지 않은 경우 공백 리턴
		if (targetDirPath == null || targetDirPath.equals("") || fromDate == null || fromDate.equals("") || toDate == null || toDate.equals("")) {
			return false;
		}

		boolean result = false;
		String lastModifyedDate = "";
		File f = null;

		f = new File(CommonSecurityUtil.filePathBlackList(targetDirPath));
		lastModifyedDate = getUpdtDate(f.getPath());
		//log.debug("getLastModifiedDateFromFile(f):"+lastModifyedDate);
		if (Integer.parseInt(lastModifyedDate) >= Integer.parseInt(fromDate) && Integer.parseInt(lastModifyedDate) <= Integer.parseInt(toDate)) {
			result = true;
		}

		return result;
	}

	/**
	 * <pre>
	 * Comment : 조건구간내에 생성된 디렉토리 목록을 조회한다.
	 * </pre>
	 *String updtFrom, String updtTo
	 */
	public static List<String> getDirectoryListByUpdtPd(String baseDirPath, String updtFrom, String updtTo) {

		// 인자값 유효하지 않은 경우 빈 ArrayList 리턴
		if (baseDirPath == null || baseDirPath.equals("") || updtFrom == null || updtFrom.equals("") || updtTo == null || updtTo.equals("")) {
			return new ArrayList<String>();
		}

		File f = null;
		File childFile = null;
		String[] subDirList;
		String subDirPath = "";
		List<String> childResult = null;
		List<String> result = new ArrayList<String>();

		f = new File(CommonSecurityUtil.filePathBlackList(baseDirPath));
		subDirList = f.list();
		for (int i = 0; i < subDirList.length; i++) {

			subDirPath = baseDirPath + FILE_SEPARATOR + subDirList[i];
			childFile = new File(CommonSecurityUtil.filePathBlackList(subDirPath));
			if (childFile.isDirectory()) {
				//childResult = getLastDirectoryForModifiedDate(subDirPath , fromDate, toDate);
				String lastModifyedDate = getUpdtDate(childFile.getPath());
				if (Integer.parseInt(lastModifyedDate) >= Integer.parseInt(updtFrom) && Integer.parseInt(lastModifyedDate) <= Integer.parseInt(updtTo)) {
					result.add(baseDirPath + FILE_SEPARATOR + subDirList[i]);
				}
				childResult = getDirectoryListByUpdtPd(baseDirPath + FILE_SEPARATOR + subDirList[i], updtFrom, updtTo);
				// 하위디렉토리의 결과를 추가한다.
				for (int j = 0; j < childResult.size(); j++) {
					result.add((String) childResult.get(j));
				}
			}
		}

		return result;
	}

	/**
	 * <pre>
	 * Comment : 디렉토리(파일)의 읽기권한을 확인한다.
	 * </pre>
	 */
	public static boolean canRead(String filePath) {

		// 인자값 유효하지 않은 경우 빈 false 리턴
		if (filePath == null || filePath.equals("")) {
			return false;
		}

		File f = null;
		boolean result = false;
		f = new File(CommonSecurityUtil.filePathBlackList(filePath));
		if (f.exists()) {
			result = f.canRead();
		}

		return result;
	}

	/**
	 * <pre>
	 * Comment : 디렉토리(파일)의 쓰기권한을 확인한다.(대상경로가 파일인 경우만 정보가 유효함)
	 * </pre>
	 *
	 */
	public static boolean canWrite(String filePath) {

		// 인자값 유효하지 않은 경우 빈 false 리턴
		if (filePath == null || filePath.equals("")) {
			return false;
		}

		File f = null;
		boolean result = false;
		f = new File(CommonSecurityUtil.filePathBlackList(filePath));
		if (f.exists()) {
			result = f.canWrite();
		}

		return result;
	}

	/**
	 * <pre>
	 * Comment : 디렉토리(파일)의 이름을  확인한다.
	 * </pre>
	 *
	 */
	public static String getName(String filePath) {

		// 인자값 유효하지 않은 경우 빈 false 리턴
		if (filePath == null || filePath.equals("")) {
			return "";
		}

		File f = null;
		String result = "";

		f = new File(CommonSecurityUtil.filePathBlackList(filePath));
		if (f.exists()) {
			result = f.getName();
		}

		return result;
	}

	/**
	 * <pre>
	 * Comment : 디렉토리(파일)를 삭제한다. (파일,디렉토리 구분없이 존재하는 경우 무조건 삭제한다)
	 * </pre>
	 *
	 * @param filePath 삭제하고자 하는 파일의 절대경로
	 * @return String 성공하면 삭제된 절대경로, 아니면 블랭크
	 */

	public static String deletePath(String filePath) {
		File file = new File(CommonSecurityUtil.filePathBlackList(filePath));
		String result = "";

		if (file.exists()) {
			result = file.getAbsolutePath();
			if (!file.delete()) {
				result = "";
			}
		}

		return result;
	}

	/**
	 * 디렉토리에 파일이 존재하는지 체크하는 기능
	 * @return boolean result 존재여부 True / False
	 * @exception Exception
	 */
	public static boolean checkFileExstByName(String dir, String file) throws Exception {

		// 파일 존재 여부
		boolean result = false;

		// 디렉토리 오픈
		String drctry = dir.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File srcDrctry = new File(CommonSecurityUtil.filePathBlackList(drctry));

		// 디렉토리이면서, 존재하면
		if (srcDrctry.exists() && srcDrctry.isDirectory()) {

			// 디렉토리 안 목록을 조회한다. (파일명)
			File[] fileArray = srcDrctry.listFiles();
			List<String> list = getSubFilesByName(fileArray, file);
			if (list != null && list.size() > 0) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * 확장자별로 디렉토리에 파일이 존재하는지 체크하는 기능
	 * @return boolean result 존재여부 True / False
	 * @exception Exception
	 */
	public static boolean checkFileExstByExtnt(String dir, String extnt) throws Exception {

		// 파일 존재 여부
		boolean result = false;

		// 디렉토리 오픈
		String drctry = dir.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File srcDrctry = new File(CommonSecurityUtil.filePathBlackList(drctry));

		// 디렉토리이면서, 존재하면
		if (srcDrctry.exists() && srcDrctry.isDirectory()) {

			// 디렉토리 안 목록을 조회한다. (확장자별)
			File[] fileArray = srcDrctry.listFiles();
			List<String> list = getSubFilesByExtnt(fileArray, extnt);
			if (list != null && list.size() > 0) {
				result = true;
			}
		}

		return result;
	}


	/**
	 * 수정기간별로 디렉토리에 파일이 존재하는지 체크하는 기능
	 * @return boolean result 존재여부 True / False
	 * @exception Exception
	 */
	public static boolean checkFileExstByUpdtPd(String dir, String updtFrom, String updtTo) throws Exception {

		// 파일 존재 여부
		boolean result = false;

		// 디렉토리 오픈
		String drctry = dir.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File srcDrctry = new File(CommonSecurityUtil.filePathBlackList(drctry));

		// 디렉토리이면서, 존재하면
		if (srcDrctry.exists() && srcDrctry.isDirectory()) {

			// 디렉토리 안 목록을 조회한다. (수정기간별)
			File[] fileArray = srcDrctry.listFiles();
			List<String> list = getSubFilesByUpdtPd(fileArray, updtFrom, updtTo);
			if (list != null && list.size() > 0) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * 사이즈별로 디렉토리에 파일이 존재하는지 체크하는 기능
	 * @return boolean result 존재여부 True / False
	 * @exception Exception
	 */
	public static boolean checkFileExstBySize(String dir, long sizeFrom, long sizeTo) throws Exception {

		// 파일 존재 여부
		boolean result = false;

		// 디렉토리 오픈
		String drctry = dir.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File srcDrctry = new File(CommonSecurityUtil.filePathBlackList(drctry));

		// 디렉토리이면서, 존재하면
		if (srcDrctry.exists() && srcDrctry.isDirectory()) {

			// 디렉토리 안 목록을 조회한다. (사이즈별)
			File[] fileArray = srcDrctry.listFiles();
			List<String> list = getSubFilesBySize(fileArray, sizeFrom, sizeTo);
			if (list != null && list.size() > 0) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * 디렉토리 내부 하위목록들 중에서 파일을 찾는 기능(모든 목록 조회)
	 * @return ArrayList list 파일목록(절대경로)
	 * @exception Exception
	 */
	public static List<String> getSubFilesByAll(File[] fileArray) throws Exception {

		ArrayList<String> list = new ArrayList<String>();

		for (int i = 0; i < fileArray.length; i++) {
			// 디렉토리 안에 디렉토리면 그 안의 파일목록에서 찾도록 재귀호출한다.
			if (fileArray[i].isDirectory()) {
				File[] tmpArray = fileArray[i].listFiles();
				list.addAll(getSubFilesByAll(tmpArray));
				// 파일이면 담는다.
			} else {
				list.add(fileArray[i].getAbsolutePath());
			}
		}

		return list;
	}

	/**
	 * 디렉토리 내부 하위목록들 중에서 파일을 찾는 기능(파일명)
	 * @return ArrayList list 파일목록(절대경로)
	 * @exception Exception
	 */
	public static List<String> getSubFilesByName(File[] fileArray, String file) throws Exception {

		List<String> list = new ArrayList<String>();

		for (int i = 0; i < fileArray.length; i++) {
			// 디렉토리 안에 디렉토리면 그 안의 파일목록에서 찾도록 재귀호출한다.
			if (fileArray[i].isDirectory()) {
				File[] tmpArray = fileArray[i].listFiles();
				list.addAll(getSubFilesByName(tmpArray, file));
				// 파일이면 파일명이 같은지 비교한다.
			} else {
				if (fileArray[i].getName().equals(file)) {
					list.add(fileArray[i].getAbsolutePath());
				}
			}
		}

		return list;
	}

	/**
	 * 디렉토리 내부 하위목록들 중에서 파일을 찾는 기능(확장자별)
	 * @return ArrayList list 파일목록(절대경로)
	 * @exception Exception
	 */
	public static List<String> getSubFilesByExtnt(File[] fileArray, String extnt) throws Exception {

		List<String> list = new ArrayList<String>();

		for (int i = 0; i < fileArray.length; i++) {
			// 디렉토리 안에 디렉토리면 그 안의 파일목록에서 찾도록 재귀호출한다.
			if (fileArray[i].isDirectory()) {
				File[] tmpArray = fileArray[i].listFiles();
				list.addAll(getSubFilesByExtnt(tmpArray, extnt));
				// 파일이면 확장자명이 들어있는지 비교한다.
			} else {
				if (fileArray[i].getName().indexOf(extnt) != -1) {
					list.add(fileArray[i].getAbsolutePath());
				}
			}
		}

		return list;
	}

	/**
	 * 디렉토리 내부 하위목록들 중에서 파일을 찾는 기능(최종수정기간별)
	 * @return ArrayList list 파일목록(절대경로)
	 * @exception Exception
	 */
	public static List<String> getSubFilesByUpdtPd(File[] fileArray, String updtFrom, String updtTo) throws Exception {

		List<String> list = new ArrayList<String>();

		for (int i = 0; i < fileArray.length; i++) {
			// 디렉토리 안에 디렉토리면 그 안의 파일목록에서 찾도록 재귀호출한다.
			if (fileArray[i].isDirectory()) {
				File[] tmpArray = fileArray[i].listFiles();
				list.addAll(getSubFilesByUpdtPd(tmpArray, updtFrom, updtTo));
				// 파일이면 수정기간내에 존재하는지 비교한다.
			} else {
				// 파일의 최종수정일자 조회
				long date = fileArray[i].lastModified();
				java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.KOREA);
				String lastUpdtDate = dateFormat.format(new java.util.Date(date));
				// 수정기간 내에 존재하는지 확인
				if (Integer.parseInt(lastUpdtDate) >= Integer.parseInt(updtFrom) && Integer.parseInt(lastUpdtDate) <= Integer.parseInt(updtTo)) {
					list.add(fileArray[i].getAbsolutePath());
				}
			}
		}

		return list;
	}

	/**
	 * 디렉토리 내부 하위목록들 중에서 파일을 찾는 기능(사이즈별)
	 * @return ArrayList list 파일목록(절대경로)
	 * @exception Exception
	 */
	public static List<String> getSubFilesBySize(File[] fileArray, long sizeFrom, long sizeTo) throws Exception {

		List<String> list = new ArrayList<String>();

		for (int i = 0; i < fileArray.length; i++) {
			// 디렉토리 안에 디렉토리면 그 안의 파일목록에서 찾도록 재귀호출한다.
			if (fileArray[i].isDirectory()) {
				File[] tmpArray = fileArray[i].listFiles();
				list.addAll(getSubFilesBySize(tmpArray, sizeFrom, sizeTo));
				// 파일이면, 사이즈내에 존재하는지 비교한다.
			} else {
				// 파일의 사이즈 조회
				long size = fileArray[i].length();
				// 사이즈 내에 존재하는지 확인
				if (size >= (sizeFrom * BUFFER_SIZE) && size <= (sizeTo * BUFFER_SIZE)) {
					list.add(fileArray[i].getAbsolutePath());
				}
			}
		}

		return list;
	}

	/**
	 * <pre>
	 * Comment : 디렉토리를 생성한다.
	 * </pre>
	 *
	 * @param dirPath 생성하고자 하는 절대경로
	 * @return 성공하면 새성된 절대경로, 아니면 블랭크
	 */

	public static String createNewDirectory(String dirPath) {

		// 인자값 유효하지 않은 경우 블랭크 리턴
		if (dirPath == null || dirPath.equals("")) {
			return "";
		}

		File file = new File(CommonSecurityUtil.filePathBlackList(dirPath));
		String result = "";
		// 없으면 생성
		if (file.exists()) {
			// 혹시 존재해도 파일이면 생성 - 생성되지 않는다.(아래는 실질적으로는 진행되지 않음)
			if (file.isFile()) {
				//new File(file.getParent()).mkdirs();
				if (file.mkdirs()) {
					result = file.getAbsolutePath();
				}
			} else {
				result = file.getAbsolutePath();
			}
		} else {
			// 존해하지 않으면 생성
			if (file.mkdirs()) {
				result = file.getAbsolutePath();
			}
		}

		return result;
	}

	/**
	 * <pre>
	 * Comment : 파일을 생성한다.
	 * </pre>
	 *
	 */
	public static String createNewFile(String filePath) {

		// 인자값 유효하지 않은 경우 블랭크 리턴
		if (filePath == null || filePath.equals("")) {
			return "";
		}

		File file = new File(CommonSecurityUtil.filePathBlackList(filePath));
		String result = "";
		try {
			if (file.exists()) {
				result = filePath;
			} else {
				// 존재하지 않으면 생성함
				new File(file.getParent()).mkdirs();
				if (file.createNewFile()) {
					result = file.getAbsolutePath();
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	/**
	 * <pre>
	 * Comment : 디렉토리를 삭제한다.
	 * </pre>
	 *
	 * @param dirDeletePath 삭제하고자 하는디렉토리의 절대경로(파일의 경로가 들어오는 경우 삭제하지 않음)
	 * @return 성공하면 삭제된 절대경로, 아니면블랭크
	 */

	public static String deleteDirectory(String dirDeletePath) {

		// 인자값 유효하지 않은 경우 블랭크 리턴
		if (dirDeletePath == null || dirDeletePath.equals("")) {
			return "";
		}
		String result = "";
		File file = new File(CommonSecurityUtil.filePathBlackList(dirDeletePath));
		if (file.isDirectory()) {
			String[] fileList = file.list();
			//소속된 파일을 모두 삭제
			for (int i = 0; i < fileList.length; i++) {

				//log.debug("fileList["+i+"] : "+ dirDeletePath +"/"+fileList[i]);
				File f = new File(CommonSecurityUtil.filePathBlackList(dirDeletePath) + "/" + fileList[i]);
				if (f.isFile()) {
					//디렉토리에 속한 파일들을 모두 삭제한다.
					f.delete();
				} else {
					//디렉토리에 속한 하위 디렉토리들에 대한 삭제 명령을 재귀적으로 호출시킨다.
					deleteDirectory(dirDeletePath + "/" + fileList[i]);
				}
			}
			// 디렉토리에 속한 파일들과 하위 디렉토리가 삭제되었으면 디렉토리 자신을 삭제한다.
			result = deletePath(dirDeletePath);
		} else {
			result = "";
		}

		return result;
	}

	/**
	 * <pre>
	 * Comment : 파일을 삭제한다.
	 * </pre>
	 *
	 * @param fileDeletePath 삭제하고자 하는파일의 절대경로
	 * @return 성공하면 삭제된 파일의 절대경로, 아니면블랭크
	 */

	public static String deleteFile(String fileDeletePath) {

		// 인자값 유효하지 않은 경우 블랭크 리턴
		if (fileDeletePath == null || fileDeletePath.equals("")) {
			return "";
		}
		String result = "";
		File file = new File(CommonSecurityUtil.filePathBlackList(fileDeletePath));
		if (file.isFile()) {
			result = deletePath(fileDeletePath);
		} else {
			result = "";
		}

		return result;
	}


	/**
	 * 파일의 최종수정일자별 파일목록 조회하는 기능
	 * @return ArrayList list 파일목록
	 * @exception Exception
	 */
	public static List<String> getFileListByDate(String drctry, String updtDate) throws Exception {

		// 결과 목록
		List<String> list = null;

		// 디렉토리 오픈
		String drctry1 = drctry.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File file = new File(CommonSecurityUtil.filePathBlackList(drctry1));

		// 디렉토리이며, 존재하면 최종수정일자가 같은 파일목록 조회 시작
		if (file.exists() && file.isDirectory()) {
			File[] fileArray = file.listFiles();
			list = getSubFilesByDate(fileArray, updtDate);
		}

		return list;
	}

	/**
	 * 파일의 최종수정기간내 파일목록 조회하는 기능
	 * @return ArrayList list 파일목록
	 * @exception Exception
	 */
	public static List<String> getFileListByUpdtPd(String drctry, String updtFrom, String updtTo) throws Exception {

		// 결과 목록
		List<String> list = null;

		// 디렉토리 오픈
		String drctry1 = drctry.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File file = new File(CommonSecurityUtil.filePathBlackList(drctry1));

		// 디렉토리이며, 최종수정기간내  존재하는 파일목록 조회 시작
		if (file.exists() && file.isDirectory()) {
			File[] fileArray = file.listFiles();
			list = getSubFilesByUpdtPd(fileArray, updtFrom, updtTo);
		}

		return list;
	}

	/**
	 * 하위디렉토리 포함 최종수정일자가 같은 파일목록을 찾는 기능
	 * @return ArrayList list 파일목록
	 * @exception Exception
	 */
	public static List<String> getSubFilesByDate(File[] fileArray, String updtDate) throws Exception {

		List<String> list = new ArrayList<String>();

		for (int i = 0; i < fileArray.length; i++) {
			// 디렉토리 안에 디렉토리면 그 안의 파일목록에서 찾도록 재귀호출한다.
			if (fileArray[i].isDirectory()) {
				File[] tmpArray = fileArray[i].listFiles();
				list.addAll(getSubFilesByDate(tmpArray, updtDate));
				// 파일이면 파일명이 같은지 비교한다.
			} else {
				// 파일의 최종수정일자 조회
				long date = fileArray[i].lastModified();
				java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.KOREA);
				String lastUpdtDate = dateFormat.format(new java.util.Date(date));
				if (Integer.parseInt(lastUpdtDate) == Integer.parseInt(updtDate)) {
					list.add(fileArray[i].getAbsolutePath());
				}
			}
		}

		return list;
	}



	/**
	 * 두 파일의 사이즈를 비교하는 기능 (KB 단위 비교)
	 * @return boolean result 동일여부 True / False
	 * @exception Exception
	 */
	public static boolean cmprFilesBySize(String cmprFile1, String cmprFile2) throws Exception {

		// 파일 동일 여부
		boolean result = false;

		// 파일 오픈
		String cmprFile11 = cmprFile1.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		String cmprFile22 = cmprFile2.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File file1 = new File(CommonSecurityUtil.filePathBlackList(cmprFile11));
		File file2 = new File(CommonSecurityUtil.filePathBlackList(cmprFile22));

		// 파일이며, 존재하면 파일 사이즈 비교
		if (file1.exists() && file2.exists() && file1.isFile() && file2.isFile()) {

			// 파일1 사이즈
			long size1 = file1.length();

			// 파일2 사이즈
			long size2 = file2.length();

			// 사이즈 비교
			if (size1 == size2) {
				result = true;
			}

		}

		return result;
	}

	/**
	 * 두 파일의 수정일자를 비교하는 기능
	 * @return boolean result 동일여부 True / False
	 * @exception Exception
	 */
	public static boolean cmprFilesByUpdtPd(String cmprFile1, String cmprFile2) throws Exception {

		// 파일 동일 여부
		boolean result = false;

		// 파일 오픈
		String cmprFile11 = cmprFile1.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		String cmprFile22 = cmprFile2.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File file1 = new File(CommonSecurityUtil.filePathBlackList(cmprFile11));
		File file2 = new File(CommonSecurityUtil.filePathBlackList(cmprFile22));

		// 파일이며, 존재하면 파일 수정일자 비교
		if (file1.exists() && file2.exists() && file1.isFile() && file2.isFile()) {

			// 파일1 수정일자
			long date1 = file1.lastModified();
			java.text.SimpleDateFormat dateFormat1 = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.KOREA);
			String lastUpdtDate1 = dateFormat1.format(new java.util.Date(date1));

			// 파일2 수정일자
			long date2 = file2.lastModified();
			java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.KOREA);
			String lastUpdtDate2 = dateFormat2.format(new java.util.Date(date2));

			// 수정일자 비교
			if (lastUpdtDate1.equals(lastUpdtDate2)) {
				result = true;
			}
		}

		return result;
	}


	/**
	 * 파일 복사(Copy)한다.
	 * @return boolean result 복사여부 True / False
	 * @exception Exception
	 */
	public static boolean copyFile(String source, String target) throws Exception {

		// 복사여부
		boolean result = false;

		// 원본 파일
		String src = source.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File srcFile = new File(CommonSecurityUtil.filePathBlackList(src));

		// 타켓 파일
		String tar = target.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);

		// 원본 파일이 존재하는지 확인한다.
		if (srcFile.exists()) {

			// 복사될 target 파일 생성
			tar = createNewFile(tar);
			//log.debug("tar:"+tar);
			File tarFile = new File(CommonSecurityUtil.filePathBlackList(tar));
			//log.debug("tarFile:"+tarFile.getAbsolutePath());
			// 복사
			result = copyFile(srcFile, tarFile);
		}

		return result;
	}

	/**
	 * 여러 파일을 다른 디렉토리에 복사(Copy)한다.
	 * @return boolean result 복사여부 True / False
	 * @exception Exception
	 */
	public static boolean copyFiles(String[] source, String target) throws Exception {

		// 복사여부
		boolean result = true;

		// 복사 이전에 복사할 파일들의 경로가 올바른지 확인한다.
		for (int i = 0; i < source.length; i++) {
			String src = source[i].replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
			File chkFile = new File(CommonSecurityUtil.filePathBlackList(src));
			if (!chkFile.exists()) {
				//log.debug("+++ 원본 파일이 존재하지 않습니다.");
				return result;
			}
		}

		String tar = target.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);

		// 복사를 시작한다.
		for (int j = 0; j < source.length; j++) {

			if (result) { //result != false

				// 타겟파일이름 명명
				File chkFile = new File(CommonSecurityUtil.filePathBlackList(source[j]));
				String tarTemp = tar + FILE_SEPARATOR + chkFile.getName();

				// 복사될 target 파일 생성
				tarTemp = createNewFile(tarTemp);
				File tarFile = new File(CommonSecurityUtil.filePathBlackList(tarTemp));

				// 복사
				result = copyFile(chkFile, tarFile);
			}
		} // end for

		return result;
	}

	/**
	 * 확장자별 파일들을 다른 디렉토리에 복사(Copy)한다.
	 * @return boolean result 복사여부 True / False
	 * @exception Exception
	 */
	public static boolean copyFilesByExtnt(String source, String extnt, String target) throws Exception {

		// 복사여부
		boolean result = true;

		// 원본 파일
		String src = source.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File srcFile = new File(CommonSecurityUtil.filePathBlackList(src));

		// 원본 디렉토리가 존재하는지 확인한다.
		if (srcFile.exists() && srcFile.isDirectory()) {

			String tar = target.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);

			// 원본 디렉토리 안에서 확장자가 일치하는 파일목록을 가져온다.
			File[] fileArray = srcFile.listFiles();
			List<String> list = getSubFilesByExtnt(fileArray, extnt);

			// 복사를 시작한다.
			for (int i = 0; i < list.size(); i++) {
				if (result) { //f(result != false){
					// 원본파일 절대경로
					String abspath = (String) list.get(i);

					// 타겟파일이름 명명
					File chkFile = new File(CommonSecurityUtil.filePathBlackList(abspath));
					String tarTemp = tar + FILE_SEPARATOR + chkFile.getName();

					// 복사될 target 파일 생성
					tarTemp = createNewFile(tarTemp);
					File tarFile = new File(CommonSecurityUtil.filePathBlackList(tarTemp));

					// 복사
					result = copyFile(chkFile, tarFile);
				}
			} // end for
		}

		return result;
	}

	/**
	 * 수정기간내 파일들을 다른 디렉토리에 복사(Copy)한다.
	 * @return boolean result 복사여부 True / False
	 * @exception Exception
	 */
	public static boolean copyFilesByUpdtPd(String source, String updtFrom, String updtTo, String target) throws Exception {

		// 복사여부
		boolean result = true;

		// 원본 파일
		String src = source.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File srcFile = new File(CommonSecurityUtil.filePathBlackList(src));

		// 원본 디렉토리가 존재하는지 확인한다.
		if (srcFile.exists() && srcFile.isDirectory()) {

			String tar = target.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);

			// 원본 디렉토리 안에서 수정기간내 존재하는 파일목록을 가져온다.
			File[] fileArray = srcFile.listFiles();
			List<String> list = getSubFilesByUpdtPd(fileArray, updtFrom, updtTo);

			// 복사를 시작한다.
			for (int i = 0; i < list.size(); i++) {

				if (result) { //f(result != false){

					// 원본파일 절대경로
					String abspath = (String) list.get(i);

					// 타겟파일이름 명명
					File chkFile = new File(CommonSecurityUtil.filePathBlackList(abspath));
					String tarTemp = tar + FILE_SEPARATOR + chkFile.getName();

					// 복사될 target 파일 생성
					tarTemp = createNewFile(tarTemp);
					File tarFile = new File(tarTemp);

					// 복사
					result = copyFile(chkFile, tarFile);
				}
			} // end for
		}

		return result;
	}

	/**
	 * 사이즈내 파일들을 다른 디렉토리에 복사(Copy)한다.
	 * @return boolean result 복사여부 True / False
	 * @exception Exception
	 */
	public static boolean copyFilesBySize(String source, long sizeFrom, long sizeTo, String target) throws Exception {

		// 복사여부
		boolean result = true;

		// 원본 파일
		String src = source.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File srcFile = new File(CommonSecurityUtil.filePathBlackList(src));

		// 원본 디렉토리가 존재하는지 확인한다.
		if (srcFile.exists() && srcFile.isDirectory()) {

			String tar = target.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);

			// 원본 디렉토리 안에서 사이즈내 존재하는 파일목록을 가져온다.
			File[] fileArray = srcFile.listFiles();
			List<String> list = getSubFilesBySize(fileArray, sizeFrom, sizeTo);

			// 복사를 시작한다.
			for (int i = 0; i < list.size(); i++) {

				if (result) { //result != false
					// 원본파일 절대경로
					String abspath = (String) list.get(i);

					// 타겟파일이름 명명
					File chkFile = new File(CommonSecurityUtil.filePathBlackList(abspath));
					String tarTemp = tar + FILE_SEPARATOR + chkFile.getName();

					// 복사될 target 파일 생성
					tarTemp = createNewFile(tarTemp);
					File tarFile = new File(CommonSecurityUtil.filePathBlackList(tarTemp));

					// 복사
					result = copyFile(chkFile, tarFile);
					if (result) {
						break;
					}
				}
			} // end for

		}

		return result;
	}


	/**
	 * 복사를 수행하는 기능
	 * @return boolean result 복사여부 True / False
	 * @exception Exception
	 */
	public static boolean copyFile(File srcFile, File tarFile) throws Exception {

		// 결과
		boolean result = false;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			// 복사
			fis = new FileInputStream(srcFile);

			//예외상황에 따른 처리 추가함. -> 만약 tarFile 이 디렉토리명인 경우 디렉토리 밑으로 새로 파일을 생성해서 복사한다.. like DOS
			File tarFile1 = tarFile;
			if (tarFile1.isDirectory()) {
				tarFile1 = new File(CommonSecurityUtil.filePathBlackList(tarFile1.getAbsolutePath()) + "/" + srcFile.getName());
			}
			fos = new FileOutputStream(tarFile1);
			byte[] buffer = new byte[(int) BUFFER_SIZE];
			int i = 0;
			if (fis != null && fos != null) {
				while ((i = fis.read(buffer)) != -1) {
					fos.write(buffer, 0, i);
				}
			}

			result = true;
		} finally {
			resourceClose(fis, fos);
		}

		return result;
	}


	/**
	 * <pre>
	 * Comment : 디렉토리를 삭제한다. (생성일자 조건으로 삭제)
	 * </pre>
	 *
	 * @param dirDeletePath 삭제하고자 하는디렉토리의 절대경로(파일의 경로가 들어오는 경우 삭제하지 않음)
	 * @param fromDate 디렉토리의 삭제조건 시작일자
	 * @param toDate 디렉토리의 삭제조건 종료일자
	 * @return 성공하면 삭제된 절대경로, 아니면블랭크
	 */
	public static String deleteDirectory(String dirDeletePath, String fromDate, String toDate) {

		// 인자값 유효하지 않은 경우 블랭크 리턴
		if (dirDeletePath == null || dirDeletePath.equals("") || fromDate == null || fromDate.equals("") || toDate == null || toDate.equals("")) {
			return "";
		}

		// 찾은 결과를 전달할 ArrayList
		String result = "";
		File file = new File(CommonSecurityUtil.filePathBlackList(dirDeletePath));

		// 추가된 삭제조건 옵션에 합당한지 확인
		boolean isInCondition = false;
		String lastModifyedDate = getUpdtDate(file.getPath());
		//log.debug("lastModifyedDate:"+lastModifyedDate);
		
		if (Integer.parseInt(lastModifyedDate) >= Integer.parseInt(fromDate) && Integer.parseInt(lastModifyedDate) <= Integer.parseInt(toDate)) {
			isInCondition = true;
		}

		// 삭제조건에 부합되면 디렉토리 삭제조치함
		if (file.isDirectory() && isInCondition) {
			result = deleteDirectory(dirDeletePath);
		}

		return result;
	}


	/**
	 * 파일(디렉토리)가 존재하는 디렉토리(Parent)를 조회하는 기능
	 * @return String drctryName 디렉토리
	 * @exception Exception
	 */
	public static String getParentDrctryName(String filePath) throws Exception {

		String drctryName = "";
		String src = filePath.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);

		File srcFile = new File(CommonSecurityUtil.filePathBlackList(src));
		if (srcFile.exists()) {
			drctryName = srcFile.getParent();
		}

		return drctryName;
	}


	/**
	 * 파일(디렉토리)의 최종수정일자를 조회하는 기능
	 * @return String updtDate 최종수정일자(YYYYMMDD 형태)
	 * @exception Exception
	 */
	public static String getUpdtDate(String filePath) {

		String updtDate = "";
		String src = filePath.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);

		File srcFile = new File(CommonSecurityUtil.filePathBlackList(src));
		if (srcFile.exists()) {
			long date = srcFile.lastModified();
			java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.KOREA);
			updtDate = dateFormat.format(new java.util.Date(date));
		}

		return updtDate;
	}

	/**
	 * 파일의 사이즈를 조회하는 기능
	 * @return Long size 사이즈(Byte)
	 * @exception Exception
	 */
	public static long getSize(String filePath) throws Exception {

		long size = 0L;
		String src = filePath.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);

		File srcFile = new File(CommonSecurityUtil.filePathBlackList(src));
		if (srcFile.exists()) {
			size = srcFile.length();
		}

		return size;
	}

	/**
	 * <pre>
	 * Comment : 디렉토리를 복사한다.
	 * </pre>
	 * @return boolean result 복사가 성공하면 true, 실패하면 false를 리턴한다.
	 */
	public static boolean copyDirectory(String originalDirPath, String targetDirPath) throws Exception {

		// 인자값 유효하지 않은 경우 공백 리턴
		if (originalDirPath == null || originalDirPath.equals("") || targetDirPath == null || targetDirPath.equals("")) {
			return false;
		}
		boolean result = false;
		File f = null;

		f = new File(CommonSecurityUtil.filePathBlackList(originalDirPath));
		// 원본이 유효해야 진행한다.
		if (f.exists() && f.isDirectory()) {

			//타겟으로 설정한 경로가 유효한지 확인(중간경로에 파일명 이 포함되어있으면 유효하지 못하므로 진행안함.
			String targetDirPath1 = createNewDirectory(targetDirPath);
			if (targetDirPath1.equals("")) {
				result = false;
			} else {
				File targetDir = new File(CommonSecurityUtil.filePathBlackList(targetDirPath1));
				targetDir.mkdirs();
				// 디렉토리에 속한 파일들을 복사한다.
				String[] originalFileList = f.list();
				if (originalFileList.length > 0) {
					for (int i = 0; i < originalFileList.length; i++) {
						File subF = new File(CommonSecurityUtil.filePathBlackList(originalDirPath) + FILE_SEPARATOR + originalFileList[i]);
						if (subF.isFile()) {
							//하위목록이 파일이면 파일복사실행 -> 실패 발생하는 경우 복사를 중단한다.
							result = copyFile(originalDirPath + FILE_SEPARATOR + originalFileList[i], targetDir.getAbsolutePath() + FILE_SEPARATOR + originalFileList[i]);
						} else {
							//하위목록이 디렉토리이면 복사를 재귀적으로 호출한다.
							result = copyDirectory(originalDirPath + "/" + originalFileList[i], targetDirPath1 + "/" + originalFileList[i]);
						}
					}
				} else {
					result = true;
				}
			}
		} else {
			// 원본자체가 유효하지 않은 경우는 false 리턴하고 종료
			result = false;
		}

		return result;
	}

	/**
	 * <pre>
	 * Comment : 디렉토리를 복사한다. (생성일자 조건으로  복사)
	 * </pre>
	 * @param fromDate 디렉토리의 복사조건 시작일자
	 * @param toDate 디렉토리의 복사조건 종료일자
	 * @return boolean result 복사가 성공함변 true, 실패하면 false를 리턴한다.
	 */
	public static boolean copyDirectory(String originalDirPath, String targetDirPath, String fromDate, String toDate) throws Exception {

		// 인자값 유효하지 않은 경우 공백 리턴
		if (originalDirPath == null || originalDirPath.equals("") || targetDirPath == null || targetDirPath.equals("") || fromDate == null || fromDate.equals("") || toDate == null
				|| toDate.equals("")) {
			return false;
		}
		boolean result = false;
		File f = null;

		f = new File(CommonSecurityUtil.filePathBlackList(originalDirPath));
		boolean isInCondition = false;
		String lastModifyedDate = getUpdtDate(f.getPath());
		if (Integer.parseInt(lastModifyedDate) >= Integer.parseInt(fromDate) && Integer.parseInt(lastModifyedDate) <= Integer.parseInt(toDate)) {
			isInCondition = true;
		}

		// 원본이 유효하고 조건에 부합되야 진행한다.
		if (f.exists() && f.isDirectory() && isInCondition) {

			//타겟으로 설정한 경로가 유효한지 확인(중간경로에 파일명 이 포함되어있으면 유효하지 못하므로 진행안함.
			String targetDirPath1 = createNewDirectory(targetDirPath);
			if (targetDirPath1.equals("")) {
				result = false;
			} else {
				File targetDir = new File(CommonSecurityUtil.filePathBlackList(targetDirPath1));
				targetDir.mkdirs();
				// 디렉토리에 속한 파일들을 복사한다.
				String[] originalFileList = f.list();
				if (originalFileList.length > 0) {
					for (int i = 0; i < originalFileList.length; i++) {
						File subF = new File(CommonSecurityUtil.filePathBlackList(originalDirPath) + FILE_SEPARATOR + originalFileList[i]);
						if (subF.isFile()) {
							//하위목록이 파일이면 파일복사실행 -> 실패 발생하는 경우 복사를 중단한다.
							result = copyFile(originalDirPath + FILE_SEPARATOR + originalFileList[i], targetDir.getAbsolutePath() + FILE_SEPARATOR + originalFileList[i]);
						} else {
							//하위목록이 디렉토리이면 복사를 재귀적으로 호출한다.
							//하위목록에 해당하는 폴더에 대해서는 생성일자 검사를 하지 않는다.(현재 폴더가 복사대상이면 현재폴더의 하위는 제외없이 복사함)
							result = copyDirectory(originalDirPath + "/" + originalFileList[i], targetDirPath1 + "/" + originalFileList[i]);
						}
					}
				} else {
					result = true;
				}
			}

		} else {
			// 원본자체가 유효하지 않은 경우는 false 리턴하고 종료
			result = false;
		}

		return result;
	}


	/**
	 * 디렉토리의 사이즈를 조회한다.
	 * @return long size 디렉토리사이즈
	 * @exception Exception
	 */
	public static long getDirectorySize(String targetDirPath) throws Exception {

		File f = new File(CommonSecurityUtil.filePathBlackList(targetDirPath));
		if (!f.exists()) {
			return 0;
		}
		if (f.isFile()) {
			return f.length();
		}

		File[] list = f.listFiles();
		long size = 0;
		long fileSize = 0;

		for (int i = 0; i < list.length; i++) {

			if (list[i].isDirectory()) {
				// 디렉토리 안에 디렉토리면 그 안의 파일목록에서 찾도록 재귀호출한다.
				fileSize = getDirectorySize(list[i].getAbsolutePath());
			} else {
				// 파일의 사이즈 조회
				fileSize = list[i].length();
			}
			size = size + fileSize;
		}
		return size;
	}



	public static void resourceClose(Closeable  ... resources) {
		for (Closeable resource : resources) {
			if (resource != null) {
				try {
					resource.close();
				} catch (Exception ignore) {
					ignore.printStackTrace();
				}
			}
		}
	}

}
