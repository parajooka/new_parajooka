package com.paraframework.common;

import java.util.Random;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*
 * 인증문자 핸들러 사용법
 * 
 * 1. 문자 인증을 사용할 페이지에 접근시 최초로 인증키를 발급한다 :: RandomAuthNumber()
 * 인증키는 새로고침 이전까지 인증문자를 중복 발송을 방지하기위한 인증키이다.
 * 
 * 2. 사용자가 연락처를 기입후 문자 발송을 요청할 경우 중복 체크후 발송한다 :: SendAuthSMS()
 * 인증키를 이용해서 이미 인증문자가 발송 되었을경우에는 false를 반환한다.
 * 
 * 3. 사용자가 인증번호를 입력후 인증 요청을 할 경우 데이터를 검증한다. :: CheckAuthSMSNumber()
 * 만약 인증번호가 서버에서 발급한 번호와 일치한다면 세션에 AuthSMSResult를 true로 변환한다.
 * 
 * 4. 모든 인증을 완료하고 검증이 다된 경우 기록을 모두 삭제해야한다. :: ClearAuthSMSLog()
 * 기록을  삭제하지 않을시 다른 페이지에서 충돌이 발생 할 수 있다.
 * 
 * 5. 사용자가 현재 인증을 진행중인지를 반환해주는 메서드 :: CheckUserAuthSMS()
 * 현재 진행중일 경우 -> true, 아예 진행하지 않앗을경우 -> false
 */

public class AuthSMSHandler {
	/**
	 * 5분간 유효한 인증문자 인증키를 발급한다.
	 * @param request
	 */
	public void RandomAuthNumber(HttpServletRequest request) {
		final HttpSession session = request.getSession();
		
		//이미 인증을 1회이상 진행했거나 아직 인증이 진행중인데 다시 인증 요청을 한경우
		//기존의 타이머는 삭제하고 다시 인증키 발급을 시도한다.
		if (session.getAttribute("AuthSMSKeyTimer") != null) {
			TimerTask orign_timer = (TimerTask)session.getAttribute("AuthSMSKeyTimer");
			orign_timer.cancel();
			session.removeAttribute("AuthSMSKeyTimer");
			session.removeAttribute("AuthSMSKey");
			session.removeAttribute("AuthSMS");
			session.removeAttribute("AuthSMSNumber");
			session.removeAttribute("AuthSMSResult");
			session.removeAttribute("AuthSMSPhone");
		}
		
		Random rnd =new Random();
		StringBuffer buf =new StringBuffer();
		for(int i = 0; i < 20; i++){
		    if(rnd.nextBoolean()){
		        buf.append((char)((int)(rnd.nextInt(26))+97));
		    }else{
		        buf.append((rnd.nextInt(10)));
		    }
		}
		
		Timer timer = new Timer();
     	TimerTask task = new TimerTask() {
 			
 			@Override
 			public void run() {
 				//정해진 시간뒤에 인증문자 기록 삭제
 				session.removeAttribute("AuthSMSKeyTimer");
 		        session.removeAttribute("AuthSMSKey");
 				session.removeAttribute("AuthSMS");
 				session.removeAttribute("AuthSMSNumber");
 				session.removeAttribute("AuthSMSResult");
 				session.removeAttribute("AuthSMSPhone");
 			}
 		};
 		
 		timer.schedule(task, (5 * 1000 * 60) + 1000);
 		
		session.setAttribute("AuthSMSKey", buf.toString());
		session.setAttribute("AuthSMSKeyTimer", task);
	}

	/**
	 * 문자 발송
	 * @param request
	 * @param auth_name 인증요청 명칭
	 * @param message 메세지
	 * @param phone_number 연락처
	 * @return true = 발송 성공, false = 이미 발송 or 발송 실패
	 */
	public boolean SendAuthSMS(HttpServletRequest request, String phone_number) {
        final HttpSession session = request.getSession();
		if (session.getAttribute("AuthSMSKeyTimer") == null) {
			RandomAuthNumber(request);
		}
		
		double randomValue = Math.random();    
        int intValue = (int)(randomValue * 899999) + 100000;
        
         SendSMS sms = new SendSMS();
         
         
         String auth_sms = (String) session.getAttribute("AuthSMS");
         String auth_sms_key = (String) session.getAttribute("AuthSMSKey");
         
         if (auth_sms == null || !auth_sms.equals(auth_sms_key)) {
	 		//발송
        	session.setAttribute("AuthSMS", auth_sms_key); //인증키 기록
        	session.setAttribute("AuthSMSNumber", intValue); //인증번호 기록
			session.setAttribute("AuthSMSResult", false); //인증 여부(최초 : 실패)
			session.setAttribute("AuthSMSPhone", phone_number); //인증 핸드폰번호 기록
	 		return sms.sendSMS(phone_number, "Para & Jooka 인증번호 ["+ intValue +"]");
         } else {
        	//이미 발송된 상태
        	return false;
         }
	}
	
	
	/**
	 * 인증완료
	 * @param request
	 * @param auth_name 인증 세션 명칭
	 * @param phone_number 연락처
	 * @return -1 = 알수없는 오류, 0 = 인증 만료, 1 = 인증 실패 , 2 = 인증 성공
	 */
	public int CheckAuthSMSNumber(HttpServletRequest request, int auth_number) {
		int checked = -1;
		HttpSession session = request.getSession();
		
		//인증 핸드폰 번호
		String phoneNumber = (String) session.getAttribute("AuthSMSPhone");
		//인증 번호
		int AuthNumber = (int) session.getAttribute("AuthSMSNumber");
		
		//인증키 만료
		if (session.getAttribute("AuthSMSKey") == null) {
			this.RandomAuthNumber(request);
			SendAuthSMS(request, phoneNumber);
			checked = 0;
			return checked;
		//인증 실패 (인증번호 불일치)
		} else if (AuthNumber != auth_number) {
			checked = 1;
			return checked;
		//인증 성공
		} else if (AuthNumber == auth_number) {
			checked = 2;
			
			//타이머 중지 및 삭제
			TimerTask orign_timer = (TimerTask)session.getAttribute("AuthSMSKeyTimer");
			orign_timer.cancel();
			session.removeAttribute("AuthSMSKeyTimer");
			session.setAttribute("AuthSMSResult", true); //인증여부 갱신 :: 성공
		}
		
		return checked;
	}
	
	/**
	 * 인증로그 전체 삭제
	 * @param request
	 */
	public void ClearAuthSMSLog(HttpServletRequest request) {
		final HttpSession session = request.getSession();		
		
		session.removeAttribute("AuthSMSKeyTimer");
		session.removeAttribute("AuthSMSKey");
		session.removeAttribute("AuthSMS");
		session.removeAttribute("AuthSMSNumber");
		session.removeAttribute("AuthSMSResult");
		session.removeAttribute("AuthSMSPhone");
	}
	
	/**
	 * 사용자가 인증이 진행중인지 체크
	 * @param request
	 * @return
	 */
	public boolean CheckUserAuthSMS(HttpServletRequest request) {
		final HttpSession session = request.getSession();		
		
		if (session.getAttribute("AuthSMSResult") == null) {
			return false;
		} else {
			return true;
		}
	}
}
