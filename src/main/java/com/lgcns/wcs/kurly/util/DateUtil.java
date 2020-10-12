package com.lgcns.wcs.kurly.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {

	private DateUtil() {
		throw new AssertionError();
	}

	/**
	 * 현재 날짜를 주어진 포멧의 문자로 리턴
	 * 
	 * @param formatStr null이나 빈문자인 경우 기본 포멧(yyyyMMdd)
	 * @return
	 */
	public static String getToday(String formatStr) {

		String format = formatStr;
		if (format == null || format.equals("")) {
			format = "yyyyMMdd";
		}

		Date date = new Date();
		SimpleDateFormat sdate = new SimpleDateFormat(format);

		return sdate.format(date);
	}

}