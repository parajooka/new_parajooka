package com.para.common;


import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.para.service.hire.HireService;
import com.paraframework.common.BaseController;

public class HireIpInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private HireService service;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String uri = request.getRequestURI();
		if (
			uri.indexOf("/resources") < 0 && 
			uri.indexOf("/res") < 0 &&
			uri.indexOf("/getTime") < 0 &&
			uri.indexOf("/jooka/admin") < 0
			) {
			Cookie[] cookies = request.getCookies();
			
			if(cookies != null){
				boolean getcookie = false;
		        for(int i=0; i < cookies.length; i++) {
		            Cookie c = cookies[i] ;
		            // 저장된 쿠키 이름을 가져온다
		            String cName = c.getName();
		            
		            if (cName.equals("hire_hide")) {
		            	getcookie = true;
		            	break;
		            }
		        }

		        if (!getcookie) {
		        	int count = service.CountHireByuser(new Date());
		    		if (count > 0) {
		    			request.setAttribute("now_hire_count", count);
		    			request.setAttribute("browser", BaseController.isMobile(request));
		    		}
		        }
		    } else {
		    	int count = service.CountHireByuser(new Date());
	    		if (count > 0) {
	    			request.setAttribute("now_hire_count", count);
	    			request.setAttribute("browser", BaseController.isMobile(request));
	    		}
		    }
		}
		
		return true;
	}
}
