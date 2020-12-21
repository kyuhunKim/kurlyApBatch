package com.lgcns.wcs.kurly.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtil {

	private HttpUtil() {
		
	}
	public static String getUrlToJson(String inputUrl, String jsonValue, String method) {
		
		String inputLine = "";
		StringBuffer outResult = new StringBuffer();
		
//		log.info("=inputUrl="+inputUrl);
		
		try
		{
			if(inputUrl == null || "".equals(inputUrl)) {
				return "inputUrl error";
			}
			if(method == null || "".equals(method)) {
				method = "GET";
			}
			URL url = new URL(inputUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(method);
			conn.setRequestProperty("Content-Type","application/json");
			conn.setRequestProperty("Accept-Charset","UTF-8");
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			
			if(!"".equals(jsonValue)) {
				OutputStream os = conn.getOutputStream();
				os.write(jsonValue.getBytes("UTF-8"));
				os.flush();
			}
			
			//리턴 결과 얻기
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			while ((inputLine = in.readLine()) != null) {
				outResult.append(inputLine);
			}
			conn.disconnect();
			
			
		} catch(Exception e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
		return outResult.toString();
	}

	public static String getLocalIp() {
		InetAddress local;
		String localIp = "127.0.0.1";
		try {
			local = InetAddress.getLocalHost();
			localIp = local.getHostAddress();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		return localIp;
	}

	public static String getHostName() {
		InetAddress local;
		String hostName = "localhost";
		try {
			local = InetAddress.getLocalHost();
			hostName = local.getHostName();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		return hostName;
	}

}