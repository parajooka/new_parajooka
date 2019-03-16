package com.paraframework.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
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
				if (!BaseController.isMobile(request)) {
					request.getSession().setAttribute("admin", true);
				}
			}
		}
		
		
		 //홈페이지 메뉴 로딩
		if (!BaseController.MenuUpload) {
			BaseController.MenuUpload = true;
			request.getServletContext().setAttribute("menu_list", service.getViewMenu());
		}
		
		//홈페이지 정보 호출
		if (!BaseController.HomePageUpload) {
			Homepage homepage = homepage_service.getHomepage();
			
			if (homepage != null) {
				BaseController.HomePageUpload = true;
				request.getServletContext().setAttribute("homepage", homepage);
			}
		}
		
		//방문 로그 기록
		WriteLogged(request);
		
		return true;
	}
	
	public void WriteLogged(HttpServletRequest request) throws IOException {
		File temp = new File("C:\\res\\logged\\para-jooka");

		// 생성한 File객체의 서브 디렉토리와 파일 목록을 가져와서
		// File형 배열로 선언된 a에 담는다.
		File[] a = temp.listFiles();
		
		//로그파일이 없다면 신규 로그파일 생성후 기록입력
		if (a.length == 0) {
			File new_logged_file = new File("C:\\res\\logged\\para-jooka\\visited_log.txt");
			bufferedWriter = new BufferedWriter(new FileWriter(new_logged_file, true));
			bufferedWriter.write("방문자 아이피 : "+ BaseController.getIpAddress(request) +"|| 시간 : "+ formatTime2.format(new Date()));
			bufferedWriter.close();
			return;
		}

		//마지막에있는 로그파일 호출
		File logged_file = a[a.length - 1];
	    @SuppressWarnings("resource")
	    BufferedReader in = new BufferedReader(new FileReader(logged_file));

         if(logged_file.isFile() && logged_file.canWrite()){
        	 int lineCnt = 0;
        	 while(in.readLine() != null) lineCnt++;
        	 
        	 //한파일당 로그가 100줄 이상일경우 새로운 로그파일을 만들어서 입력한다.
        	 if (lineCnt >= 100) {
        		 File new_logged_file = new File("C:\\res\\logged\\para-jooka\\visited_log" + (a.length + 1) + ".txt");
        		 bufferedWriter = new BufferedWriter(new FileWriter(new_logged_file, true));
        	 } else {
        		bufferedWriter = new BufferedWriter(new FileWriter(logged_file, true));
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
        	 
             bufferedWriter.write("방문자 아이피 : "+ BaseController.getIpAddress(request) +" || 시간 : "+ formatTime2.format(new Date()) + " || 방문 페이지: "+ request.getRequestURL() +""+ strParam);
             bufferedWriter.close();
         }
	}
}
