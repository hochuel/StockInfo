package org.srv.vo;

import org.apache.commons.collections4.map.ListOrderedMap;

/**
 *
 * @ClassName   : HMap.java
 * @Description : 데이터의 저장 및 전송에 사용되는 Map객체
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
public class HMap extends ListOrderedMap {
	/** serial version UID **/
	private static final long serialVersionUID = 1L;

	// String Casting 을 하지 않기 위해 함수 추가
	// null 일 경우 "" 으로 리턴됨
	public String getString(String key){
		String result = "";
		
		if(null!=get(key))
			result = (String) get(key);
		
		return result;
	}
	
	
	// _를 없애고 다음글자를 대문자로 만드는데 소문자로 시작함
    public Object put(Object key, Object value){
        return super.put( convert2CamelCase((String)key), value );
    }
    
    // _를 없애고 다음글자를 대문자로 만듬
    public Object put2(Object key, Object value){
        return super.put( convert3CamelCase((String)key), value );
    }
    
    // _를 없애지 않고 그대로 변수로 사용함
    public Object put3(Object key, Object value){
        return super.put( key, value );
    }

	/**
	 * underscore ('_') 가 포함되어 있는 문자열을 Camel Case ( 낙타등
	 * 표기법 - 단어의 변경시에 대문자로 시작하는 형태. 시작은 소문자) 로 변환해주는
	 * utility 메서드 ('_' 가 나타나지 않고 첫문자가 대문자인 경우도 변환 처리
	 * 함.)
	 * @param underScore
	 *        - '_' 가 포함된 변수명
	 * @return Camel 표기법 변수명
	 */
    public static String convert2CamelCase(String underScore){
        if(underScore.indexOf('_') < 0 && Character.isLowerCase(underScore.charAt(0)))
            return underScore;
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        int len = underScore.length();
        for(int i = 0; i < len; i++){
            char currentChar = underScore.charAt(i);
            if(currentChar == '_'){
                nextUpper = true;
                continue;
            }
            if(nextUpper){
                result.append(Character.toUpperCase(currentChar));
                nextUpper = false;
            } else{
                result.append(Character.toLowerCase(currentChar));
            }
        }

        return result.toString();
    }
    
    /**
     * 첫글자가 소문자든 대문자든 상관없음. 대문자일 경우 그대로 대문자로 시작함
     * @param underScore
     * @return
     */
    public static String convert3CamelCase(String underScore){
        if(underScore.indexOf('_') < 0 )
            return underScore;
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        int len = underScore.length();
        for(int i = 0; i < len; i++){
            char currentChar = underScore.charAt(i);
            if(currentChar == '_'){
                nextUpper = true;
                continue;
            }
            if(nextUpper){
                result.append(Character.toUpperCase(currentChar));
                nextUpper = false;
            } else{
                result.append(currentChar);
            }
        }
        return result.toString();
    }

}
