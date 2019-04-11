package com.paraframework.common.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.paraframework.common.ControllerCommonMethod;
import com.paraframework.object.Homepage;
import com.paraframework.object.Menu;
import com.paraframework.object.MenuHtml;
import com.paraframework.service.HomepageService;
import com.paraframework.service.MenuHtmlService;
import com.paraframework.service.MenuService;

public class TempJspHandler extends ControllerCommonMethod {
	@Autowired
	private MenuService service;
	@Autowired
	private MenuHtmlService html_service;
	@Autowired
	private HomepageService homepage_service;
	
	public TempJspHandler() {
		//해당 클래스의 객체가 생성될때 Autowired를 채워준다.
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	/**
	 * 모든 임시 jsp를 최초 혹은 재등록하는 메서드
	 * @param path
	 */
	public void ReCreateAllTempJsp(ServletContext context) {
		String path = null;
		try {
			//WEB-INF 경로를 구한다.
			path = context.getResource("/WEB-INF/views/custom").getPath();
			path = path + "/temp_jsp";
			//경로 없으면 자동생성
			File directory_file = new File(path);
			
			if (!directory_file.exists()) {
				directory_file.mkdirs();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// C드라이브의 test 디렉토리에 대한 File객체를 생성한다.
		File temp = new File(path);

		// 생성한 File객체의 서브 디렉토리와 파일 목록을 가져와서
		// File형 배열로 선언된 a에 담는다.
		File[] a = temp.listFiles();

		// test 디렉토리 내부 파일들을 삭제하는 반복문
		// a배열의 길이만큼 반복한다.
		for (int i = 0; i < a.length; i++) {
			a[i].delete();
		}
		
		//모두 삭제가되면 새로 임시 jsp파일을 생성한다.
		
		//직접 작성 메뉴를 모두 불러온다.
		List<Menu> menu_list = service.getMenuByType(0);
		
		for (Menu m : menu_list) {
			if (m.getUse_yn() == 0) {
				try { // 파일 객체 생성 File file = new File(real_path); BufferedWriter
					BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "menu_" + m.getMenu_idx() +".jsp"), "UTF-8"));
	
					MenuHtml html = html_service.getHTMLByMenuId(m.getMenu_idx());
						String jspTag = "<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>\r\n";
							   jspTag += "<%@taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>\r\n";
							   jspTag += "<%@ taglib uri=\"http://java.sun.com/jsp/jstl/fmt\" prefix=\"fmt\"%>\r\n";
						bufferedWriter2.write(jspTag + "" +html.getMenu_html());
						bufferedWriter2.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
		
		//홈페이지 모바일 버전 사용여부 체크
		Homepage homepage = homepage_service.getHomepage();
		//사용한다면 모바일용 jsp도 만든다.
		if (homepage.getMobile_use() == 1) {
			for (Menu m : menu_list) {
				if (m.getUse_yn() == 0) {
					try { // 파일 객체 생성 File file = new File(real_path); BufferedWriter
						BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "mobile_menu_" + m.getMenu_idx() +".jsp"), "UTF-8"));
	
						MenuHtml html = html_service.getHTMLByMenuId(m.getMenu_idx());
						String jspTag = "<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>";
							   jspTag += "<%@taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>";
							   jspTag += "<%@ taglib uri=\"http://java.sun.com/jsp/jstl/fmt\" prefix=\"fmt\"%>";
						bufferedWriter2.write(jspTag + "" +html.getMobile_menu_html());
						bufferedWriter2.close();
					} catch (IOException e) {
						System.out.println(e);
					}
				}
			}
		}
		
	}
	
	/**
	 * 특정 메뉴의 임시jsp만 재등록한다.
	 * @param path
	 * @param menu
	 */
	public void ReCreateTempJsp(ServletContext context, Menu menu) {
		String path = null;
		try {
			//WEB-INF 경로를 구한다.
			path = context.getResource("/WEB-INF/views/custom/temp_jsp").getPath();
			path = path.substring(1, path.length()); 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (menu.getUse_yn() == 0) {
			try { // 파일 객체 생성 File file = new File(real_path); BufferedWriter
				//기존의 파일 삭제
				removeFile(path + "menu_" + menu.getMenu_idx() +".jsp");
				//신규 파일 등록
				BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "menu_" + menu.getMenu_idx() +".jsp"), "UTF-8"));

				MenuHtml html = html_service.getHTMLByMenuId(menu.getMenu_idx());
				String jspTag = "<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>\r\n";
					   jspTag += "<%@taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>\r\n";
					   jspTag += "<%@ taglib uri=\"http://java.sun.com/jsp/jstl/fmt\" prefix=\"fmt\"%>\r\n";
				bufferedWriter.write(jspTag + "" +html.getMenu_html());
				bufferedWriter.close();

				//홈페이지 모바일 버전 사용여부 체크
				Homepage homepage = homepage_service.getHomepage();
				if (homepage.getMobile_use() == 1) {
					//기존의 파일 삭제
					removeFile(path + "mobile_menu_" + menu.getMenu_idx() +".jsp");
					//신규 파일 등록
					BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "mobile_menu_" + menu.getMenu_idx() +".jsp"), "UTF-8"));

					String mobile_jspTag = "<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>\r\n";
						   mobile_jspTag += "<%@taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>\r\n";
						   mobile_jspTag += "<%@ taglib uri=\"http://java.sun.com/jsp/jstl/fmt\" prefix=\"fmt\"%>\r\n";
					bufferedWriter2.write(mobile_jspTag + "" +html.getMobile_menu_html());
					bufferedWriter2.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
	
	/**
	 * 임시 jsp 개별 삭제처리
	 * @param context
	 * @param menu
	 */
	public void RemoveTempJsp(ServletContext context, int menu_idx) {
		String path = null;
		try {
			//WEB-INF 경로를 구한다.
			path = context.getResource("/WEB-INF/views/custom/temp_jsp").getPath();
			path = path.substring(1, path.length()); 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		removeFile(path + "mobile_menu_" + menu_idx +".jsp");
		removeFile(path + "menu_" + menu_idx +".jsp");
	}
	
	/**
	 * 임시 파일명, jsp내용까지 사용자가 한번에 파라미터로 넣어서 넘겨주는 메서드
	 * @param context
	 * @param tempFileName
	 * @param contents
	 */
	public void CustomazingTemoJsp(ServletContext context, String tempFileName, String contents) {
		String path = null;
		try {
			//WEB-INF 경로를 구한다.
			path = context.getResource("/WEB-INF/views/custom/temp_jsp").getPath();
			path = path.substring(1, path.length()); 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//기존의 파일 삭제
		removeFile(path + "" + tempFileName);
		//신규 파일 등록
		BufferedWriter bufferedWriter = null;
		
		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + ""+ tempFileName), "UTF-8"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String jspTag = "<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>\r\n";
			   jspTag += "<%@taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>\r\n";
			   jspTag += "<%@ taglib uri=\"http://java.sun.com/jsp/jstl/fmt\" prefix=\"fmt\"%>\r\n";
		try {
			bufferedWriter.write(jspTag + "" + contents);
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}