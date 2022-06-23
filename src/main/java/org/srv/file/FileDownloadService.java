package org.srv.file;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * <pre>
 * Statements
 * </pre>
 *
 * @ClassName   : FileDownloadService.java
 * @Description : 파일의 download service를 담당한다.
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
public class FileDownloadService {
	/** logger **/
	private static Log log	= LogFactory.getLog(FileDownloadService.class); 

    /**
	 * 파일다운로드를 담당한다.
	 * 한글 파일명 처리를 해준다.
	 */
    public static void fileDown( String filePath , String downloadFileName, HttpServletRequest request, HttpServletResponse response ) throws IOException{
        String encodedFilename = "";
        String header = request.getHeader("User-Agent");
        
        // IE 검사 - 한글파일 다운로드
        if (checkMSIE(request)) {
            encodedFilename = URLEncoder.encode(downloadFileName, "UTF-8").replaceAll("\\+", "%20");
        } else {
            if(header.indexOf("Chrome") > -1) {
                StringBuffer sb = new StringBuffer();
                for(int i = 0 ; i < downloadFileName.length() ; i++) {
                    char c = downloadFileName.charAt(i);
                    if(c > '~') {
                        sb.append(URLEncoder.encode("" + c, "UTF-8"));
                    } else{
                        sb.append(c);
                    }
                }
                encodedFilename = sb.toString();
            } else {
                encodedFilename = "\"" + new String(downloadFileName.getBytes("UTF-8"), "8859_1") + "\"";
            }
        }
        
        response.setStatus(HttpServletResponse.SC_OK);
    	response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename="+encodedFilename);
        response.setHeader("Content-Transfer-Encoding", "binary");
    	
		File fFile	= new File(filePath);
		if(!fFile.exists() || fFile.isDirectory()){
			throw new FileNotFoundException("File Not Found : " + filePath);
		}
		write(new FileInputStream(fFile),response.getOutputStream());
	}
    
    /**
     * 파일다운로드를 담당한다.
     * 한글 파일명 처리를 해준다.
     * FileVO 객체를 이용하여 다운로드 한다.
     * @param fileVO
     * @param request
     * @param response
     * @throws IOException
     */
    public static void fileDown( FileVO fileVO, HttpServletRequest request, HttpServletResponse response) throws IOException{
    	String filePath = fileVO.getFolderPath() + File.separator + fileVO.getStoredFileName();
    	String downloadFileName = fileVO.getOriginalFileName();
    	fileDown(filePath, downloadFileName, request, response);
    }
    
    /**
	 * OutputStream 으로 write.
	 * 
	 * @param is 입력 stream
	 * @param os 출력 stream
	 * @exception IOException
	 */
    private static void write( InputStream is , OutputStream os ) throws IOException{
    	if(is==null || os==null){
    		return;
    	}
    	byte[] buf	= new byte[1024];
    	int iReadSize			= 0;
    	try{
    		while( (iReadSize=is.read(buf))!=-1 ){
    			os.write(buf,0,iReadSize);
    		}
    		os.flush();
    	}catch(IOException ie){
    		log.error(ie);
    		throw ie;
    	}finally{
    		try{if(is!=null){is.close();}}catch(IOException ie){}
    		try{if(os!=null){os.close();}}catch(IOException ie){}
    	}
    }    
    
    /**
     * Internet Explorer 확인 함수
     * @param request
     * @return
     */
    private static boolean checkMSIE(HttpServletRequest request) {
    	String header = request.getHeader("User-Agent");
    	// MSIE IE<=10, Trident = IE11
    	if (header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1 ) {
    		return true;
        } else {
        	return false;
        }
    }
}
