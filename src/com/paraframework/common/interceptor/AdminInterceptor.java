 package com.paraframework.common.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.paraframework.common.ControllerCommonMethod;
import com.paraframework.object.AccessIp;
import com.paraframework.service.AccessIpService;

public class AdminInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private AccessIpService service;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		/* 로직처리 */ 
		String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        } 
        
		List<AccessIp> access = service.getAccessIp();
		
		if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
			return true;
		}
		
		for (AccessIp a : access) {
			if (ip.equals(a.getAccess_ip()) || ip.indexOf(a.getAccess_ip()) >= 0) {
				if (!ControllerCommonMethod.isMobile(request)) {
					return true;
				} else {
					return ControllerCommonMethod.alertMessage("모바일에서는 관리자 페이지 접근이 불가능합니다.", request, response);
				}
			}
		}
		
		return ControllerCommonMethod.alertMessage("허용되지않은 접근입니다.", request, response);
	}
}