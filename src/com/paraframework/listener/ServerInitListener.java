package com.paraframework.listener;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.paraframework.common.ControllerCommonMethod;

public class ServerInitListener implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent request) {
		request.getServletContext().setAttribute("admin_root_path", ControllerCommonMethod.admin_page_path);
		request.getServletContext().setAttribute("customer_root_path", ControllerCommonMethod.customer_page_path);
	}
}
