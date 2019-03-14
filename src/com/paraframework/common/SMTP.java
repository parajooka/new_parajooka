package com.paraframework.common;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;

public class SMTP {

	public void SendMail(String receiverMail, String title, String content) {
		String host = "smtp.naver.com"; 
		String user = "para_jooka@naver.com";  // 네이버일 경우 네이버 계정, gmail경우 gmail 계정 
		String password = "parajooka1@";      // 패스워드
		
		// SMTP 서버 정보를 설정한다. 
		Properties props = new Properties(); 
		props.put("mail.smtp.host", host); 
		props.put("mail.smtp.port", 587); 
		props.put("mail.smtp.auth", "true");
		
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