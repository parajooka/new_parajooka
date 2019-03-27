package com.paraframework.listener;


import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.paraframework.common.SMTP;

/**
 * 관리자페이지에서 직접 작성한 html을 jsp로 변환하여 파일로 저장하고
 * 사용자가 페이지를 요청시 임시로 저장된 jsp로 보여주기위한 리스너
 * @author DaeYeop
 *
 */
public class SMTPListener implements ServletContextListener {
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent request) {
		// SMTP 서버 정보를 설정한다. 
		Properties props = new Properties(); 
		props.put("mail.smtp.host", SMTP.getHost()); 
		props.put("mail.smtp.port", 587); 
		props.put("mail.smtp.auth", "true");
		
		SMTP.setSession(Session.getDefaultInstance(props, new javax.mail.Authenticator() { 
			protected PasswordAuthentication getPasswordAuthentication() { 
				return new PasswordAuthentication(SMTP.getUser(), SMTP.getPassword()); 
			} 
		})); 
	}
}
