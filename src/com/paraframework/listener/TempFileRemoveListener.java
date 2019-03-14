package com.paraframework.listener;

import java.io.File;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 임시파일 12시간 기준 삭제
 * @author DaeYeop
 *
 */
public class TempFileRemoveListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
		// 실행간격 지정(12시간에 한번)
        int sleepSec = 60 * 60 * 12;
         
        // 주기적인 작업을 위한
        final ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
         
         
        exec.scheduleAtFixedRate(new Runnable(){
             
            public void run(){
                try {
                	try {
            			//해당 디렉토리에있는 임시파일들을 전체 삭제한다.
            			File temp = new File("C:\\res\\temp");

            			// File형 배열로 선언된 a에 담는다.
            			File[] a = temp.listFiles();

            			// test 디렉토리 내부 파일들을 삭제하는 반복문
            			// a배열의 길이만큼 반복한다.
            			for (int i = 0; i < a.length; i++) {
            				a[i].delete();
            			}
            		} catch (Exception e) {
            			e.printStackTrace();
            		}
                     
                } catch (Exception e) {
                     
                    e.printStackTrace();
                     
                     
                    // 에러 발생시 Executor를 중지시킨다
                    exec.shutdown() ;
                }
            }
        }, 0, sleepSec, TimeUnit.SECONDS);
	}

}
