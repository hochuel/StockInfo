/*
 * Copyright 2008-2014 MOPAS(Ministry of Public Administration and Security).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.srv.utils.excel.view;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @ClassName   : ExcelUtil.java
 * @Description : 엑셀 서비스를 제공하기 위해 유용한 유틸을 포함하는 클래스이다.
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
public final class ExcelUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

	private ExcelUtil() {
		// no-op
	}
	
    /**
     * 엑셀의 셀값을 String 타입으로 변환하여 리턴한다.
     * 
     * @param cell <code>Cell</code>
     * @return 결과 값
     */
    public static String getValue(Cell cell) {

        String result = "";

        if (null == cell || cell.equals(null)) {
            return "";
        }

        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            LOGGER.debug("### Cell.CELL_TYPE_BOOLEAN : {}", Cell.CELL_TYPE_BOOLEAN);
            result = String.valueOf(cell.getBooleanCellValue());

        } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
            LOGGER.debug("### Cell.CELL_TYPE_ERROR : {}", Cell.CELL_TYPE_ERROR);
            // byte errorValue =
            // cell.getErrorCellValue();

        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            LOGGER.debug("### Cell.CELL_TYPE_FORMULA : {}", Cell.CELL_TYPE_FORMULA);
            
			String stringValue = null;
			String longValue = null;

			try {
				stringValue = cell.getRichStringCellValue().getString();
				longValue = doubleToString(cell.getNumericCellValue());
			} catch (Exception e) {
				LOGGER.debug("{}", e);
			}

			if (stringValue != null) {
				result = stringValue;
			} else if (longValue != null) {
				result = longValue;
			} else {
				result = cell.getCellFormula();
			}
            
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            LOGGER.debug("### Cell.CELL_TYPE_NUMERIC : {}", Cell.CELL_TYPE_NUMERIC);

            result = DateUtil.isCellDateFormatted(cell)? org.srv.utils.DateUtil.toString(cell.getDateCellValue(), "yyyy/MM/dd", null) : doubleToString(cell.getNumericCellValue());

        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            LOGGER.debug("### Cell.CELL_TYPE_STRING : {}", Cell.CELL_TYPE_STRING);
            result = cell.getRichStringCellValue().getString();

        } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            LOGGER.debug("### Cell.CELL_TYPE_BLANK : {}", Cell.CELL_TYPE_BLANK);
        }

        return result;
    }

    /**
     * double 형의 셀 데이터를 String 형으로 변환하여 리턴한다.
     *
     * @param d <code>double</code>
     * @return 결과 값
     */
    public static String doubleToString(double d) {
        long lValue = (long) d;
        return (lValue == d) ? Long.toString(lValue) : Double.toString(d);
    }

}
