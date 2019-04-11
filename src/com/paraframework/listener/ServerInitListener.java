package com.paraframework.listener;


import java.io.File;
import java.net.MalformedURLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.paraframework.common.ControllerCommonMethod;
import com.paraframework.common.handler.HomepageInfoHandler;
import com.paraframework.object.Homepage;
import com.paraframework.service.HomepageService;

public class ServerInitListener implements ServletContextListener {
	@Autowired
	private HomepageService service;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent request) {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		HomepageInfoHandler infoHandler = new HomepageInfoHandler();
		
		//홈페이지 기본정보 업로드
		infoHandler.UploadHomepageObject(request.getServletContext());
		//사용자 메뉴 업로드
		infoHandler.UploadCustomerMenu(request.getServletContext());
		//관리자 메뉴 업로드
		infoHandler.UploadAdminMenu(request.getServletContext());
		
		//Root path 컨텍스트에 업로드
		request.getServletContext().setAttribute("admin_root_path", ControllerCommonMethod.admin_page_path);
		request.getServletContext().setAttribute("customer_root_path", ControllerCommonMethod.customer_page_path);
	}
}
