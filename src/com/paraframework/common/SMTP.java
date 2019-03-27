package com.paraframework.common;

import javax.mail.*;
import javax.mail.internet.*;

public class SMTP {
	private static Session session = null;
	private static String host = "smtp.naver.com"; 
	private static String user = "para_jooka@naver.com";  // 네이버일 경우 네이버 계정, gmail경우 gmail 계정 
	private static String password = "parajooka1@";      // 패스워드
	
	public void SendMail(String receiverMail, String title, String content) {
		try { 
			MimeMessage message = new MimeMessage(session); 
			message.setFrom(new InternetAddress(user)); 
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverMail)); 
			
			// 메일 제목 
			message.setSubject(title); 
			
			// 메일 내용 
			message.setContent(content, "text/html; charset=utf-8");
			
			// send the message 
			Transport.send(message); 
		} catch (MessagingException e) { 
			e.printStackTrace(); 
		}
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		SMTP.host = host;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		SMTP.user = user;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		SMTP.password = password;
	}

	public static Session getSession() {
		return session;
	}

	public static void setSession(Session session) {
		SMTP.session = session;
	}
}