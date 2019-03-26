package com.paraframework.listener;

import java.io.IOException;
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

import com.paraframework.common.ControllerCommonMethod;

/**
 * 임시파일 12시간 기준 삭제
 * @author DaeYeop
 *
 */
public class ServerRestartListener implements ServletContextListener {
	
	private SimpleDateFormat formatTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREAN);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Calendar cal = new GregorianCalendar();
		String year = cal.get(Calendar.YEAR) + "";
		String month = String.format("%02d", (cal.get(Calendar.MONTH) + 1));
		String date = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
		
		//내일 날짜를 Date로 변환한다.
		Date tomorrow = null;
		
		try {
			//오늘 새벽 4시를 지정
			tomorrow = formatTime2.parse(year + "-" + month + "-" + date + " " + "04:00:00");
			
			//이미 오늘새벽 4시를 넘었다면? 내일 새벽 4시로 연기
			if (ControllerCommonMethod.SleepTime(tomorrow) <= 0) {
				cal.add(Calendar.DATE, 1);
				String tomorrow_year = cal.get(Calendar.YEAR) + "";
				String tomorrow_month = String.format("%02d", (cal.get(Calendar.MONTH) + 1));
				String tomorrow_date = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
				
				tomorrow = formatTime2.parse(tomorrow_year + "-" + tomorrow_month + "-" + tomorrow_date + " " + "04:00:00");
			}
			

			System.out.println(tomorrow);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Timer reboot_timer = new Timer();
		
		reboot_timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("==== Server Reboot ====");
	            String [] commands = {"cmd.exe", "/c", "C:\\apache-tomcat-8.5.32\\restart.bat"};
	            try {
					Process proc = Runtime.getRuntime().exec(commands);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, ControllerCommonMethod.SleepTime(tomorrow));
	}

}
