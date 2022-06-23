package org.srv.utils;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @ClassName   : DateFormatUtil.java
 * @Description : 날짜 및 시간에 대한 변환을 수행하는 클래스
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
public class DateFormatUtil {

	/**
	 * 기본 Locale에 해당하는 형식으로 날짜를 변환한다.
	 * 
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String formatDate(Date date) {
		return DateFormat.getDateInstance().format(date);
	}
	
	/**
	 * Locale에 해당하는 형식으로 날짜를 변환한다.
	 * 
	 * @param locale 로케일
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String formatDate(Locale locale, Date date) {
		return DateFormat.getDateInstance(DateFormat.DEFAULT, locale).format(date);
	}
	
	/**
	 * 주어진 스타일에 따라, 기본 Locale에 해당하는 형식으로 날짜를 변환한다.
	 * 
	 * @param style 날짜 스타일 (사용 가능한 값 : {@link DateFormatUtil#FULL}, {@link DateFormatUtil#LONG}, {@link DateFormatUtil#MEDIUM}, {@link DateFormatUtil#SHORT}, {@link DateFormatUtil#DEFAULT})
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String formatDate(int style, Date date) {
		return DateFormat.getDateInstance(style).format(date);
	}
	
	/**
	 * 주어진 스타일에 따라, Locale에 해당하는 형식으로 날짜를 변환한다.
	 * 
	 * @param style 날짜 스타일 (사용 가능한 값 : {@link DateFormatUtil#FULL}, {@link DateFormatUtil#LONG}, {@link DateFormatUtil#MEDIUM}, {@link DateFormatUtil#SHORT}, {@link DateFormatUtil#DEFAULT})
	 * @param locale 로케일
	 * @param date 날짜
	 * @return 날짜 문자열
	 */
	public static String formatDate(int style, Locale locale, Date date) {
		return DateFormat.getDateInstance(style, locale).format(date);
	}
	
	/**
	 * 기본 Locale에 해당하는 형식으로 날짜 및 시간을 변환한다.
	 * 
	 * @param date 날짜 및 시간
	 * @return 날짜 및 시간 문자열
	 */
	public static String formatDateTime(Date date) {
		return DateFormat.getDateTimeInstance().format(date);
	}
	
	/**
	 * Locale에 해당하는 형식으로 날짜 및 시간을 변환한다.
	 * 
	 * @param locale 로케일
	 * @param date 날짜 및 시간
	 * @return 날짜 및 시간 문자열
	 */
	public static String formatDateTime(Locale locale, Date date) {
		return DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale).format(date);
	}
	
	/**
	 * 주어진 스타일에 따라, 기본 Locale에 해당하는 형식으로 날짜 및 시간을 변환한다.
	 * 
	 * @param dateStyle 날짜 스타일 (사용 가능한 값 : {@link DateFormatUtil#FULL}, {@link DateFormatUtil#LONG}, {@link DateFormatUtil#MEDIUM}, {@link DateFormatUtil#SHORT}, {@link DateFormatUtil#DEFAULT})
	 * @param timeStyle 시간 스타일 (사용 가능한 값 : {@link DateFormatUtil#FULL}, {@link DateFormatUtil#LONG}, {@link DateFormatUtil#MEDIUM}, {@link DateFormatUtil#SHORT}, {@link DateFormatUtil#DEFAULT})
	 * @param date 날짜 및 시간
	 * @return 날짜 및 시간 문자열
	 */
	public static String formatDateTime(int dateStyle, int timeStyle, Date date) {
		return DateFormat.getDateTimeInstance(dateStyle, timeStyle).format(date);
	}
	
	/**
	 * 주어진 스타일에 따라, Locale에 해당하는 형식으로 날짜 및 시간을 변환한다.
	 * 
	 * @param dateStyle 날짜 스타일 (사용 가능한 값 : {@link DateFormatUtil#FULL}, {@link DateFormatUtil#LONG}, {@link DateFormatUtil#MEDIUM}, {@link DateFormatUtil#SHORT}, {@link DateFormatUtil#DEFAULT})
	 * @param timeStyle 시간 스타일 (사용 가능한 값 : {@link DateFormatUtil#FULL}, {@link DateFormatUtil#LONG}, {@link DateFormatUtil#MEDIUM}, {@link DateFormatUtil#SHORT}, {@link DateFormatUtil#DEFAULT})
	 * @param locale 로케일
	 * @param date 날짜 및 시간
	 * @return 날짜 및 시간 문자열
	 */
	public static String formatDateTime(int dateStyle, int timeStyle, Locale locale, Date date) {
		return DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale).format(date);
	}

	/**
	 * 기본 Locale에 해당하는 형식으로 시간을 변환한다.
	 * 
	 * @param date 시간
	 * @return 시간 문자열
	 */
	public static String formatTime(Date date) {
		return DateFormat.getTimeInstance().format(date);
	}
	
	/**
	 * Locale에 해당하는 형식으로 시간을 변환한다.
	 * 
	 * @param locale 로케일
	 * @param date 시간
	 * @return 시간 문자열
	 */
	public static String formatTime(Locale locale, Date date) {
		return DateFormat.getTimeInstance(DateFormat.DEFAULT, locale).format(date);
	}

	/**
	 * 주어진 스타일에 따라, 기본 Locale에 해당하는 형식으로 시간을 변환한다.
	 * 
	 * @param style 시간 스타일 (사용 가능한 값 : {@link DateFormatUtil#FULL}, {@link DateFormatUtil#LONG}, {@link DateFormatUtil#MEDIUM}, {@link DateFormatUtil#SHORT}, {@link DateFormatUtil#DEFAULT})
	 * @param date 시간
	 * @return 시간 문자열
	 */
	public static String formatTime(int style, Date date) {
		return DateFormat.getTimeInstance(style).format(date);
	}

	/**
	 * 주어진 스타일에 따라, Locale에 해당하는 형식으로 시간을 변환한다.
	 * 
	 * @param style 시간 스타일 (사용 가능한 값 : {@link DateFormatUtil#FULL}, {@link DateFormatUtil#LONG}, {@link DateFormatUtil#MEDIUM}, {@link DateFormatUtil#SHORT}, {@link DateFormatUtil#DEFAULT})
	 * @param locale 로케일
	 * @param date 시간
	 * @return 시간 문자열
	 */
	public static String formatTime(int style, Locale locale, Date date) {
		return DateFormat.getTimeInstance(style, locale).format(date);
	}
	
}