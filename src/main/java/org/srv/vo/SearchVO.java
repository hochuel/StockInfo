package org.srv.vo;

import java.io.Serializable;

/**
 *
 * @ClassName   : SearchVO.java
 * @Description : 조건 검색용 VO 공통 상속 class
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
public class SearchVO implements Serializable{
	private static final long serialVersionUID = 1L;

	/** search from request **/
	public static final String SEARCH_TYPE_REQUEST	= "1";
	/** search from session **/
	public static final String SEARCH_TYPE_SESSION	= "2";
	
    /** 현재페이지 */
    private int pageIndex = 1;
    
    /** 페이지갯수 */
    private int pageUnit = 10;
    
    /** 페이지사이즈 */
    private int pageSize = 10;

    /** firstIndex */
    private int firstIndex = 1;

    /** lastIndex */
    private int lastIndex = 1;

    /** recordCountPerPage */
    private int recordCountPerPage = 10;

    /** search type */
    private String searchType;
    /**
     * 페이징 시작 번호를 반환한다.
     * @return int 시작 페이지
     * **/
	public int getFirstIndex() {
		return firstIndex;
	}
    /**
     * 페이징 시작 번호를 설정한다.
     * @param firstIndex 시작 페이지 번호
     * **/
	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}
    /**
     * 페이징 끝 번호를 반환한다.
     * @return int 끝 페이지
     * **/
	public int getLastIndex() {
		return lastIndex;
	}
    /**
     * 페이징 끝 번호를 설정한다.
     * @param lastIndex 끝 페이지 번호
     * **/
	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}
    /**
     * 페이지당 보여줄 row 수를 반환한다.
     * @return int 페이지당 보여줄 row 수
     * **/
	public int getRecordCountPerPage() {
		return recordCountPerPage;
	}
    /**
     * 페이지당 보여줄 row 수를 지정한다.
     * @param recordCountPerPage 페이지당 보여줄 row 수
     * **/
	public void setRecordCountPerPage(int recordCountPerPage) {
		this.recordCountPerPage = recordCountPerPage;
	}
    /**
     * 현재 페이지 번호를 반환한다.
     * @return int 현재 페이지 번호
     * **/
    public int getPageIndex() {
        return pageIndex;
    }
    /**
     * 현재 페이지 번호를 지정한다.
     * @param pageIndex 현재 페이지 번호
     * **/
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
    /**
     * 조건 검색 방식을 반환한다.(기본설정은 SEARCH_TYPE_REQUEST)
     * @return String 조건 검색 방식
     * **/
    public String getSearchType() {
        return searchType;
    }
    /**
     * 조건 검색 방식을 설정한다.(기본설정은 SEARCH_TYPE_REQUEST)
     * @param searchType 조건 검색 방식
     * **/
    public void setSearchType(String searchType) {
    	this.searchType = (searchType==null||"".equals(searchType))?SEARCH_TYPE_REQUEST:searchType;
    }
    /**
     * 페이지당 row 갯수를 반환한다.
     * @return int page당 row갯수
     * **/
    public int getPageUnit() {
        return pageUnit;
    }
    /**
     * 페이지당 row 갯수를 지정한다.
     * @param pageUnit page당 row갯수
     * **/
    public void setPageUnit(int pageUnit) {
        this.pageUnit = pageUnit;
    }
    /**
     * 전체 페이지 갯수를 반환한다.
     * @return int 총페이지 수
     * **/
    public int getPageSize() {
        return pageSize;
    }
    /**
     * 전체 페이지 갯수를 지정한다.
     * @param pageSize 총페이지 수
     * **/
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    /**
     * 검색조건 객체 정보를 문자열로 반환한다.
     * @return String 검색조건 객체 정보
     * **/
    //public String toString() {
    //    return ToStringBuilder.reflectionToString(this);
    //}
    
}
