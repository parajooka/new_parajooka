package com.paraframework.common.interceptor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.gson.Gson;
import com.paraframework.common.ControllerCommonMethod;
import com.paraframework.object.AccessIp;
import com.paraframework.service.AccessIpService;

public class BasicInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private AccessIpService acc_service;
	@Autowired
	private static SimpleDateFormat formatTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREAN);
	private static SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
	private static BufferedWriter bufferedWriter= null;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String url = request.getRequestURL().toString();
		url.substring(0, url.indexOf("/"));
		
		
		/*
		 * if (url.matches(
		 * ".*([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}.*"
		 * )) { return ControllerCommonMethod.alertMessage("차단된 url로 접근 하였습니다.",
		 * request, response); }
		 */
		 
			
		/* 로직처리 */ 
		String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        
		List<AccessIp> access = acc_service.getAccessIp();
		
		if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
			request.getSession().setAttribute("admin", true);
		}
		
		for (AccessIp a : access) {
			if (ip.equals(a.getAccess_ip()) || ip.indexOf(a.getAccess_ip()) >= 0) {
				if (!ControllerCommonMethod.isMobile(request)) {
					request.getSession().setAttribute("admin", true);
				}
			}
		}
		
		
		//방문 로그 기록
		WriteLogged(request, ip);
		
		return true;
	}
	
	public void WriteLogged(HttpServletRequest request, String ip) throws IOException {
		//외부 사이트에 아이피 위치추적 요청
		Gson gson = new Gson();
        HashMap<String, Object> resultMap = null;
        
        System.out.println(ip);
        
        String country = null;
        String city = null;
        String region = null;
        
        try {
            //URL url = new URL("http://ip-api.com/json/naver.com");
            URL url = new URL("http://ip-api.com/json/"+ ip);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            int responseCode = con.getResponseCode();
            String inputLine;
            StringBuffer responseBuffer = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                responseBuffer.append(inputLine);
            }
            in.close();
            System.out.println(responseCode +" code");
            if(200==responseCode) {
                resultMap = gson.fromJson(responseBuffer.toString(), HashMap.class);
                System.out.println(resultMap.get("status") +" status");
                if("success".equals(resultMap.get("status"))){
                	country = (String) resultMap.get("country");
                	city = (String) resultMap.get("city");
                	region = (String) resultMap.get("regionName");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }   
        
		File dir = new File("C:\\res\\logged\\"+ ControllerCommonMethod.project_name);
		
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		File temp = new File("C:\\res\\logged\\"+ ControllerCommonMethod.project_name +"\\logged_"+ formatTime.format(new Date()) + ".txt");
		bufferedWriter = new BufferedWriter(new FileWriterWithEncoding(temp, "UTF-8", true));
		
		//이미 오늘의 로그파일이 만들어진경우 한줄바꿈
		if (temp != null && temp.isFile() && temp.canWrite()) {
       	 	bufferedWriter.newLine();
		}
		
	   	Enumeration<String> param = request.getParameterNames();
	    String strParam = ""; 
	    while(param.hasMoreElements()) { 
	        String name = (String)param.nextElement(); 
	        String value = request.getParameter(name); 
	        strParam += name + "=" + value + "&"; 
	    }
	     
	    if (strParam.length() > 0) {
	    	strParam = "?"+ strParam.substring(0, strParam.length() - 1);
	    }
		 
	    bufferedWriter.write("방문자 아이피 : "+ ControllerCommonMethod.getIpAddress(request) +" || 국가 : "+ country +"|| 도시 : "+ city +", "+ region +" || 시간 : "+ formatTime2.format(new Date()) + " || 방문 페이지: "+ request.getRequestURL() +""+ strParam);
		bufferedWriter.close();
	}
}
