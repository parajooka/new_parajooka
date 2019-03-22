package com.paraframework.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.paraframework.object.AccessIp;
import com.paraframework.object.Homepage;
import com.paraframework.service.AccessIpService;
import com.paraframework.service.HomepageService;
import com.paraframework.service.MenuService;

public class BasicInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private MenuService service;
	@Autowired
	private AccessIpService acc_service;
	@Autowired
	private HomepageService homepage_service;
	private static SimpleDateFormat formatTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREAN);
	private static SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
	private static BufferedWriter bufferedWriter= null;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String url = request.getRequestURL().toString();
		url.substring(0, url.indexOf("/"));
		
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
		
		
		 //홈페이지 메뉴 로딩
		if (!ControllerCommonMethod.MenuUpload) {
			ControllerCommonMethod.MenuUpload = true;
			request.getServletContext().setAttribute("menu_list", service.getViewMenu());
		}
		
		//홈페이지 정보 호출
		if (!ControllerCommonMethod.HomePageUpload) {
			Homepage homepage = homepage_service.getHomepage();
			
			if (homepage != null) {
				ControllerCommonMethod.HomePageUpload = true;
				request.getServletContext().setAttribute("homepage", homepage);
			}
		}
		
		//방문 로그 기록
		WriteLogged(request);
		
		return true;
	}
	
	public void WriteLogged(HttpServletRequest request) throws IOException {
		File temp = new File("C:\\res\\logged\\para-jooka\\logged_"+ formatTime.format(new Date()) + ".txt");
		bufferedWriter = new BufferedWriter(new FileWriter(temp, true));
		
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
		 
	    bufferedWriter.write("방문자 아이피 : "+ ControllerCommonMethod.getIpAddress(request) +" || 시간 : "+ formatTime2.format(new Date()) + " || 방문 페이지: "+ request.getRequestURL() +""+ strParam);
		bufferedWriter.close();
	}
}
