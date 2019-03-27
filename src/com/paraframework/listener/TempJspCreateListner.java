package com.paraframework.listener;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.paraframework.common.handler.TempJspHandler;

/**
 * 관리자페이지에서 직접 작성한 html을 jsp로 변환하여 파일로 저장하고
 * 사용자가 페이지를 요청시 임시로 저장된 jsp로 보여주기위한 리스너
 * @author DaeYeop
 *
 */
public class TempJspCreateListner implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent request) {
		//JSP를 만들 핸들러 호출
		TempJspHandler jspHandler = new TempJspHandler();
		
		//jsp작성 시작
		jspHandler.ReCreateAllTempJsp(request.getServletContext());
	}
}
