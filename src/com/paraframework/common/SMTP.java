package com.paraframework.common;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class SMTP {
	private final static String host = "smtp.naver.com"; 
	private final static String user = "para_jooka@naver.com";  // 네이버일 경우 네이버 계정, gmail경우 gmail 계정 
	private final static String password = "parajooka1$";      // 패스워드
	
	public void SendMail(String receiverMail, String title, String content) {
		// SMTP 서버 정보를 설정한다. 
		Properties props = new Properties(); 
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host); 
		props.put("mail.smtp.port", 587); 
		
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() { 
			protected PasswordAuthentication getPasswordAuthentication() { 
				return new PasswordAuthentication(user, password); 
			} 
		}); 
				
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
}