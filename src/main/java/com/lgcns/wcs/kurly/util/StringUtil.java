package com.lgcns.wcs.kurly.util;


public final class StringUtil {

	private StringUtil() {
		throw new AssertionError();
	}

	/**
	 * 문자열의 Empty or Null 체크
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.trim().equals(""));
	}

	/**
	 * str이 null이면 replacer로 리턴하고, null이 아니면 그대로 str을 리턴함
	 * 
	 * @param str
	 * @param replacer
	 * @return
	 */
	public static String nvl(String str, String replacer) {
		if (str == null) {
			return replacer;
		} else {
			return str;
		}
	}

	/**
	 * 실행 시간 계산
	 * @param apiRunTimeStart
	 * @param apiRunTimeEnd
	 * @return diffTime
	 */
	public static String formatInterval(long apiRunTimeStart, long apiRunTimeEnd) {
		
		long diffTime = ( apiRunTimeEnd - apiRunTimeStart ); 
		
		return diffTime+"";
	}

	/**
	 * 문자열을 cutByte크기로 잘라낸다.
	 * 
	 * @param strSourceStr
	 * @param cutByte
	 * @param strPostfixStr
	 * @return
	 */
	public static String cutString(String strSourceStr, int cutByte, String strPostfixStr) {

		boolean bTrim = false;
		String strSource = strSourceStr;
		String strPostfix = strPostfixStr;

		if (strSource == null) {
			return "";
		}

		strPostfix = (strPostfix == null) ? "" : strPostfix;
		int postfixSize = 0;
		for (int i = 0; i < strPostfix.length(); i++) {
			if (strPostfix.charAt(i) < 256) {
				postfixSize += 1;
			} else {
				postfixSize += 2;
			}
		}

		if (postfixSize > cutByte) {
			return strSource;
		}

		if (bTrim) {
			strSource = strSource.trim();
		}
		char[] charArray = strSource.toCharArray();

		int strIndex = 0;
		int byteLength = 0;
		for (; strIndex < strSource.length(); strIndex++) {

			int byteSize = 0;
			if (charArray[strIndex] < 256) {
				// 1byte character 이면
				byteSize = 1;
			} else {
				// 2byte character 이면
				byteSize = 2;
			}

			if ((byteLength + byteSize) > cutByte - postfixSize) {
				break;
			}
			
			byteLength += byteSize;
			//byteLength = byteLength += byteSize;
		}

		if (strIndex == strSource.length()) {
			strPostfix = "";
		} else {
			if ((byteLength + postfixSize) < cutByte) {
				strPostfix = " " + strPostfix;
			}
		}

		return strSource.substring(0, strIndex) + strPostfix;
	}

	/**
	 * lpad 함수 : str의 왼쪽에 주어진 길이만큼 addStr로 채운다
	 * 
	 * @param str 대상문자열
	 * @param len 길이
	 * @param addStr 대체문자
	 * @return 문자열
	 */

	public static String lpad(String str, int len, String addStr) {
		String result = str;
		int templen = len - result.length();

		for (int i = 0; i < templen; i++) {
			result = addStr + result;
		}

		return result;
	}

}