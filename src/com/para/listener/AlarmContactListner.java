package com.para.listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.para.object.contact.Contact;
import com.para.service.contact.ContactService;
import com.paraframework.common.SMTP;

/**
 * Contact 당일 예약이 존재 할 경우 담당자에게 메일 발송
 * @author DaeYeop
 *
 */
@WebListener
public class AlarmContactListner implements ServletContextListener {
	@Autowired
	private ContactService service;

	private SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
	private SimpleDateFormat formatTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//ServletContextListner에서는 Autowired가 적용되지않는다.
		//이유는 Listner가 스프링 관련 내용보다 먼저 읽히기떄문
		//이럴떄는 강제로 Autowired를 호출하여 매핑시켜준다.
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		try {
			work();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public long SleepTime() {
		Date now = null;
		Date tomorrow = null;
		try {
			//원하는 포멧에 맞춰 현재 시간 가져온다 ex)2019-03-25 19:10:42
			String now_str = formatTime2.format(new Date());
			//다시 Date로 변환한다.
			now = formatTime2.parse(now_str);
			
			//내일 날짜를 가져온다 ex)2019-03-26 00:00:30 
			Calendar cal = new GregorianCalendar();
		    cal.add(Calendar.DATE, 1);
			String tomorrow_year = cal.get(Calendar.YEAR) + "";
			String tomorrow_month = String.format("%02d", (cal.get(Calendar.MONTH) + 1));
			String tomorrow_date = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
			
			//내일 날짜를 Date로 변환한다.
			tomorrow = formatTime2.parse(tomorrow_year + "-" + tomorrow_month + "-" + tomorrow_date + " " + "00:00:30");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//두 날짜 사이에 남은 밀리세컨드를 구한다.
		//스케줄 타이머에 사용됨
		long diff = tomorrow.getTime() - now.getTime();
		
		return diff;
	}
	
	public void work() throws InterruptedException {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					//오늘의 미팅 예약자 호출
					Contact contact = null;
					try {
						contact = service.getContactBySoonTimer();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//예약자가 있다면 담당자에게 메일발송
					if (contact != null) {
						String reservation_type = "";
						
						//방문 유형 알아볼수있게 구분
						if (contact.getReservation_type() == 0) {
							reservation_type = "방문";
						} else if (contact.getReservation_type() == 1) {
							reservation_type = "전화";
						} else if (contact.getReservation_type() == 2) {
							reservation_type = "메일";
						}
						
						//메일 내용 작성
						String msg = ""+
								 "오늘의 미팅 예약자 : " + contact.getParticipant().getName() + "<br>" +
								 "예약시간 : " + contact.getReservation_date() + "<br>" +
								 "미팅수단 : "+ reservation_type + "<br><br>" +
								 "자세한 내용은 관리자 페이지에서 확인 바랍니다.";
						
						SMTP smtp = new SMTP();
						//메일 발송
						smtp.SendMail("kdyshj700@gmail.com", "[Para&Jooka] "+ formatTime.format(new Date()) + " 미팅 예약이 존재합니다.", msg);
						smtp.SendMail("mt9665@naver.com", "[Para&Jooka] "+ formatTime.format(new Date()) + " 미팅 예약이 존재합니다.", msg);
					}
					
					//재귀문 실행 (발송후 또 다음날 00시 00분 30초에 미팅조회하여 그 날 미팅계획을 발송한다)
					work();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, SleepTime());
	}
}
