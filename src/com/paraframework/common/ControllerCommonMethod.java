package com.paraframework.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.paraframework.object.AccessIp;
import com.paraframework.object.Homepage;
import com.paraframework.service.AccessIpService;
import com.paraframework.service.SqlUtilService;

public class ControllerCommonMethod {
	public static boolean HomePageUpload = false;
	public static boolean MenuUpload = false;
	public static boolean AdminCategoryUpload = false;
	private static SimpleDateFormat formatTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.KOREAN);
	private static Timer timer = new Timer();
	
	@Autowired
	private SqlUtilService search_service;
	@Autowired
	private AccessIpService acc_service;
	
	/**
	 * 요청 uri로 이동
	 * @param request
	 * @return
	 */
	public String getRequestURI(HttpServletRequest request) {
		Homepage homepage = (Homepage) request.getServletContext().getAttribute("homepage");
		if (isMobile(request) && homepage.getMobile_use() == 1) {
			return getMobileRequestURI(request.getRequestURI());
		} else {
			//return getMobileRequestURI(request.getRequestURI());
			return request.getRequestURI().substring(1, request.getRequestURI().length()).replaceAll(".do", "");
		}
	}
	
	/**
	 * 모바일 버전 uri이동
	 * @param uri
	 * @return
	 */
	public String getMobileRequestURI(String uri) {
		int last_index = uri.lastIndexOf("/");
		String temp_path = uri.substring(0, last_index + 1);
		String temp_uri = uri.substring(last_index + 1, uri.length());
		
		String orign_uri = temp_path + "mobile_"  + temp_uri;
		
		return orign_uri;
	}
	
	/**
	 * 모바일 버전 체크
	 * @param request
	 * @return
	 */
	public static boolean isMobile(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent").toUpperCase();
        
        String IS_MOBILE = "MOBILE";
        if(userAgent != null && userAgent.indexOf(IS_MOBILE) > -1) {
            return true;
        } else {
        	return false;
        }
    }
	
	public String Index(HttpServletRequest request) {return null;}
	
	public String RedirectPage(HttpServletRequest request ,String url) {
			return "redirect:"+ url;
	}
	
	/**
	 * 이름을 이용해서 이미지 확장자인지 체크
	 * @param name 이미지 풀네임
	 * @return
	 */
	public boolean checkImgfile(String name) {
		name = name.substring(name.lastIndexOf(".") + 1, name.length());
		boolean check = false;
		
		String[] access_file = {"gif", "GIF", "png", "PNG", "jpeg", "JPEG", "jpg", "JPG", "bmp", "BMP"};
		
		for (int i = 0; i < access_file.length; i++) {
			if (name.equals(access_file[i])) {
				check = true;
				break;
			}
		}
		
		return check;
	}
	
	/**
	 * 관리자 체크
	 * @param request
	 * @param access 허용 아이피 리스트
	 * @return
	 */
	public boolean isAdmin(HttpServletRequest request) {
		String ip = request.getHeader("X-FORWARDED-FOR");
		List<AccessIp> access = acc_service.getAccessIp();
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        
        boolean check = false;
		
        if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
			return true;
		}
        
		if (access != null && access.size() > 0) {
			for (AccessIp acc_ip : access) {
				if (ip.indexOf(acc_ip.getAccess_ip()) >= 0) {
					check = true;
					break;
				}
			}
		}
			
		return check;
	}
	
	/**
	 * 인터셉터용 메세지 송출 및 이전 페이지 이동
	 * @param message 메세지
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static boolean alertMessage(String message, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html; charset=" + request.getCharacterEncoding());
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.println("<script>");
			writer.println("alert('" + message + "');"); 
			writer.println("history.back();");
			writer.println("</script>");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 메세지 송출 및 페이지 이동
	 * @param message 메세지
	 * @param url 이동경로
	 * @param request
	 * @param response
	 * @return
	 */
	public static String alertMessageByString(String message, String url, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html; charset=" + request.getCharacterEncoding());
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.println("<script>");
			writer.println("alert('" + message + "');"); 
			writer.println("location.replace('"+ url +"');");
			writer.println("</script>");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 파일 업로드
	 * @param request
	 * @param path 경로
	 * @param input_name 폼에서 넘어올 인풋 파일 명칭
	 * @return null = 파일 용량 초과 혹은 첨부파일 없음
	 */
	public List<String> InjectionFile(MultipartHttpServletRequest request, final String path, String input_name) {
		List<String> filelist = new ArrayList<String>();

		List<MultipartFile> files = request.getFiles(input_name);
		FileOutputStream fos = null;
		
		if (files == null || files.size() == 0) {
			return null;
		}
		
		
		try {
			for (MultipartFile file : files) {
				
				if( file == null ) {
				    return null;
				}
				
				//경로 없으면 자동생성
				File directory_file = new File(path);
				
				if (!directory_file.exists()) {
					directory_file.mkdirs();
				}
				
				byte fileData[] = file.getBytes();
				
				String fileName = file.getOriginalFilename();
				// 파일명이 중복될수 있으므로 밀리세컨드단위의 시간을 파일이름 앞에 붙여주자.
				fileName = System.currentTimeMillis() + "_" + fileName;
				filelist.add(fileName);
				// 스트림에 담은후
				fos = new FileOutputStream(path + "\\" + fileName);
				// 쏘세요.
				fos.write(fileData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}

			}
		}
		
		return filelist;
	}
	
	public static String getBeforeURL(HttpServletRequest request) {
		String before_url = (String)request.getHeader("Referer");
		return before_url;
	}

	/**
	 * @param request
	 * @param response
	 * @param total_item_count 페이징 처리해야할 아이템의 갯수
	 * @param paging_target_url 페이징 처리중인 url
	 * @return
	 * @throws Exception 
	 */
	public static PageUtil AutoPaging(HttpServletRequest request, HttpServletResponse response, int total_item_count) {
		HttpSession session = request.getSession();
		PageUtil page = null;
		
		//기본은 1페이지로 시작
		int page_number = 1;
		
		//사용자의 요청중에 페이지가 존재한다면?
		String custom_paging = request.getParameter("page");
		
		//유효성 검사 시도후
		if (custom_paging != null && custom_paging.length() >= 1 && Integer.parseInt(custom_paging) > 0) {
			//페이지 번호를 사용자 요청 번호로 교체
			page_number = Integer.parseInt(custom_paging);
		} 
		
		int limit = 0; 
		int[] limit_array = {5, 15, 30, 40, 45};
		
		//페이징 호출 혹은 생성
		if (session.getAttribute("paging") == null) {
			session.removeAttribute("paging");
			page = new PageUtil();
			
			//새로운 페이징 갯수 변경 요청이 있다면, 그에 맞게 페이징 변경
			if (session.getAttribute("paging_limit") != null && session.getAttribute("paging_limit_flag") != null) {
				limit = (int) session.getAttribute("paging_limit");
				page.setLimit(limit);
				page_number = 1;
				
				session.removeAttribute("paging_limit_flag");
			}
		} else {
			page = (PageUtil) session.getAttribute("paging");
			
			//새로운 페이징 갯수 변경 요청이 있다면, 그에 맞게 페이징 변경
			if (session.getAttribute("paging_limit") != null && session.getAttribute("paging_limit_flag") != null) {
				limit = (int) session.getAttribute("paging_limit");
				page.setLimit(limit);
				page_number = 1;
				
				session.removeAttribute("paging_limit_flag");
			}
		}
		
		//페이징 요청이 새로운 페이징 요청시(페이지 이동)
		//필요없는 페이징 관련 session을 삭제한다.
		if (page.getPaging_url() != null && page.getPaging_url().length() > 0 && !page.getPaging_url().equals(request.getRequestURI())) {
			//Limit를 기본으로 되돌린다.
			page.setLimit(limit_array[0]);
			//페이징별 글 갯수 기록 삭제
			session.removeAttribute("paging_limit");
		}
		

		//글의 총갯수를 페이징 객체에 입력한다.
		page.setTotal_item_count(total_item_count);
		//페이징 처리 시작
		page.setPaging();
		
		//페이징 처리중인 url을 기록
		page.setPaging_url(request.getRequestURI());
		
		//존재하지 않는 페이지인지 유효성 검사
		if (page_number > page.getTotal_page()) {
			page.setPage(page.getTotal_page());
			alertMessageByString("존재하지 않는 페이지입니다.", page.getPaging_url(), request, response);
		} else {
			page.setPage(page_number);
		}
		
		page.setLimit_count(page.getLimits());
		page.setNumberic();
		
		session.setAttribute("paging", page);
		
		//페이징 레이아웃을 서버에서 만들에서 request에 넣어준다.
		//이유는 사용자가원하는 위치에서 사용할수있게 해주기위해서
		String page_layout = "";
			   page_layout += "<div class='paging_section' style='padding:50px 0;'>";
			   if (page.getTotal_page() > 1) {
				   page_layout += "<a class='paging_before_arrow' class='page_arrow'>&#60;&#60;</a>";
			   }
			   
			   for (int i : page.getView_page()) {
				   page_layout += "<a class='paging_btn page_number'>"+ i +"</a>";
			   }
			   
			   if (page.getTotal_page() > 1) {
				   page_layout += "<a class='paging_next_arrow' class='page_arrow'>&#62;&#62;</a>";
			   }
			   page_layout += "</div>";
		
		request.setAttribute("paging_layout", page_layout);
		
		//페이징의 limit을 변경할수있는 layout을 request에 담아준다
		//원하는 위치에 사용 혹은 원하지 않을수 사용하지않게
		String page_limit_layout = "";
				page_limit_layout += "<select class='paging_limit_change'>";
				try {
					for (int l : limit_array) {
						if (page.getLimit() == l) {
							page_limit_layout += "<option selected='selected' value='"+ StringCryPto.encrypt("pagingLimit", ""+ l) +"'>게시물 "+ l +"개씩 보기</option>";
						} else {
							page_limit_layout += "<option value='"+ StringCryPto.encrypt("pagingLimit", ""+ l) +"'>게시물 "+ l +"개씩 보기</option>";
						}
					}
					
					page_limit_layout += "</select>";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					alertMessage("Error during paging processing", request, response);
				}
		
		request.setAttribute("paging_limit_layout", page_limit_layout);
		
		return page;
		
	}
	
	/**
	 * 
	 * @param table 조회할 테이블명
	 * @param search_word 검색 키워드
	 * @param column 조회할 테이블내의 컬럼명
	 * @param primary_key return 받을 primary key의 명칭
	 * @param mode 0 = 전체 검색, 1 = 페이징 처리 검색
	 * @return 검색 조건에 해당하는 아이템들의 primary key를 return한다.
	 */
	public List<Integer> AutoSearch(HttpServletRequest request, HttpServletResponse response, String table, String keyword, String[] column, String primary_key, int mode) {
		List<Integer> search_result = new ArrayList<>();
		try {
			SqlUtil search = new SqlUtil();
			String sql = null;
			
			//페이징 필요없이 전체 검색
			if (mode == 0) {
				sql = "SELECT "+ primary_key +" FROM "+ table +" WHERE ";
				
				for (int i = 0; i < column.length; i++) {
					if (i == 0) {
						sql += column[i] + " LIKE '%"+ keyword +"%'";
					} else {
						sql += " or "+column[i]  +" LIKE '%"+ keyword +"%'";
					}
				}
			//검색에 및 페이징
			} else if (mode == 1) {
				HttpSession session = request.getSession();
				//사용자의 페이징 객체 호출
				PageUtil page = (PageUtil) session.getAttribute("paging");
				
				//페이징에 따른 검색 결과 조회
				//사용시에는 모드 0번을 이용해서 먼저 키워드에 해당하는 아이템갯수를 구함 -> 갯수를 이용해서 페이징처리를 실시 -> 실시한 페이징을 이용해서 검색목록 확보 -> 이후는 사용자 알아서 처리하길...
				//sql = "SELECT "+ primary_key +" FROM "+ table +" WHERE "+ column +" LIKE '%"+ keyword +"%' ORDER BY ID DESC LIMIT "+ page.getLimit_count() +", "+ page.getLimit() +"";
				sql = "SELECT "+ primary_key +" FROM "+ table +" WHERE ";
				
				for (int i = 0; i < column.length; i++) {
					if (i == 0) {
						sql += column[i] + " LIKE '%"+ keyword +"%'";
					} else {
						sql += " or "+column[i]  +" LIKE '%"+ keyword +"%'";
					}
				}
				
				sql += " ORDER BY "+ primary_key +" DESC LIMIT "+ page.getLimit_count() +", "+ page.getLimit();
			}
			
			search.setSql(sql);
			
			String crypto = request.getParameter("crypto");
			
			if (crypto == null || crypto.length() == 0) {
				request.setAttribute("keyword", keyword);
			} else {
				request.setAttribute("keyword", crypto);
			}
			
			String before_column = null;
			
			for (int i = 0; i < column.length; i++) {
				if (i == 0) {
					before_column = column[i];
				} else {
					before_column += "," + column[i];
				}
			}
			
			request.setAttribute("search_item", before_column);
			
			search_result.addAll(search_service.SearchObjectId(search)); 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			alertMessage("잘못된 검색 조건입니다.", request, response);
		}
		
		return search_result;
	}
	
	/**
	 * 사용자 검색요청 담당 함수
	 * @param request :: 요청
	 * @param response :: 응답
	 * @param table :: 조회할 테이블
	 * @param column_name :: 테이블 내의 컬럼명
	 * @param search_conditions :: 사용자에게 제공할 검색조건들 Map<보여질 명칭, 실제 컬럼명>
	 * @param func :: 조회후 진행될 함수
	 * @return
	 */
	public void LocationSearchCheck(HttpServletRequest request, HttpServletResponse response, String table, String column_name, Map<String, String> search_conditions, LocationSearchResultFunction func) {
		String keyword = request.getParameter("keyword");
		String column = request.getParameter("search_item");
		
		int result = 0;
		
		try {
			//검색 조건이 존재할 경우 겸색을 실행
			if (keyword != null && keyword.length() > 0 && column != null && column.length() > 0) {
				String[] column_array = column.split(","); 
				result = AutoSearch(request, response, table, keyword, column_array, "count("+ column_name +")", 0).get(0);
				
				AutoPaging(request, response, result);
				
				List<Integer> column_value = AutoSearch(request, response, table, keyword, column_array, column_name, 1);
				func.SearchTrue(result, column_value);
			//검색 조건이 존재하지않을경우 일반 페이징 처리후 이동
			} else {
				func.SearchFalse();
			}
			
			String custom_keyword = request.getParameter("keyword");
			String crypto = request.getParameter("crypto");
			
			custom_keyword = custom_keyword == null || custom_keyword.length() == 0 ? "" : custom_keyword;
			
			//검색 레이어 생성 후 사용자에게 입력해준다.
			//원하는 위치에 검색 레이어를 사용하기위해
			String search_layout = "";
			search_layout += "<div id='search_box' style='width:35%; margin:auto;'>";
			search_layout += "	<div class='search_layer'>";
			search_layout += "		<select class='search_items_select'>";
			for (String key : search_conditions.keySet()) {
				search_layout += "<option value='"+ search_conditions.get(key) +"'>"+ key + "</option>";
			}
			search_layout += "		</select>";
			search_layout += "		<input style='margin-left:0.5vw;' class='search_keyword' type='text' value='"+ ((crypto == null || crypto.length() == 0) ? custom_keyword : crypto) +"'>";
			search_layout += "		<a class='btn-darkblue btn_search'>검색</a>";
			search_layout += "	</div>";
			search_layout += "</div>";
		
			request.setAttribute("search_layout", search_layout);
			request.setAttribute("search_conditions", search_conditions);
	
			//검색 링크로 사용할 uri 첨부
			request.setAttribute("uri", request.getRequestURI());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			alertMessage("잘못된 검색 조건입니다.", request, response);
		}
	}

	public abstract class LocationSearchResultFunction {
		/**
		 * 사용자가 검색한 상태일경우를 처리하는 함수
		 * @param result :: 검색 결과의 총 갯수
		 * @param column_value :: 사용자가 검색한 컬럼에서 일치하는 내용이 담긴 정보의 column 번호
		 */
		public abstract void SearchTrue(int result, List<Integer> column_value);
		
		/**
		 * 사용자가 검색하지 않은 상태일경우를 처리하는 함수
		 */
		public abstract void SearchFalse();
	}

	/**
	 * 타이머 가상클래스 사용자 직접구현
	 * @author DaeYeop
	 *
	 */
	public abstract class CustomizingTimer  {
		public abstract void actionFunc();
	}
	
	/**
	 * 타이머 가상클래스를 직접적으로 구현하는 함수
	 */
	public void CustomTimer(CustomizingTimer customizing, int schedule_time) {
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				customizing.actionFunc();
			}
		};
		
		timer.schedule(task, schedule_time);
	}
	
	/**
	 * 해당 경로에 존재하는 파일을 삭제함
	 * 
	 * @param path 파일 경로
	 * @return
	 */
	public boolean removeFile(String path) {
		File file2 = new File(path);

		if (file2.exists() == true) {
			file2.delete();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 해당 경로의 디렉토리를 삭제함
	 * 디렉토리 내부에 파일이 존재할경우 파일도 삭제함
	 * 
	 * @param path 디렉토리 경로
	 * @return
	 */
	public boolean removeDirectory(String path) {
		try {
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
			// 내부에 파일이나 디렉토리가 없으므로 test디렉토리 삭제
			temp.delete();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * String이 숫자인지 확인함
	 * 
	 * @param valid_string 체크하고자하는 문자열
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean validNumber(String valid_string, HttpServletRequest request, HttpServletResponse response) {
		try {
			Integer.parseInt(valid_string);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			alertMessage("잘못된 입력값입니다.", request, response);
			return false;
		}
	}
	
	/**
	 * 랜덤 숫자와 문자를 섞어서 난수를 만들어 return함
	 * @return
	 */
	public String RandomString() {
		Random rnd =new Random();
		StringBuffer buf =new StringBuffer();
		for(int i = 0; i < 20; i++){
		    if(rnd.nextBoolean()){
		        buf.append((char)((int)(rnd.nextInt(26))+97));
		    }else{
		        buf.append((rnd.nextInt(10)));
		    }
		}
		
		return buf.toString();
	}
	
	/**
	 * 쿠키를 찾는 함수
	 * 사용자가 쿠키명을 주면
	 * 암호화하여 해당쿠키가 존재하는지 체크
	 * 존재한다면 해당 쿠키를 return
	 * 없다면 null을 return
	 * 
	 * @param request
	 * @param response
	 * @param cookieName 찾고자하는 쿠키 명
	 * @param key 암호화 키
	 * @return
	 * @throws Exception
	 */
	public Cookie findCookieByName(HttpServletRequest request, HttpServletResponse response, String cookieName, String key) throws Exception {
		//쿠키 호출
		Cookie[] getCookie = request.getCookies();
		for (Cookie c : getCookie) {
			//해당 쿠키가 존재하는지 체크
			if (c.getName().equals(StringCryPto.encrypt(key, cookieName))) {
				return c;
			}
		}
		
		return null;
	}
	
	/**
	 * 쿠키 생성 함수
	 * 사용자가 요청한 이름을 암호화하여 쿠키생성
	 * 
	 * @param response 
	 * @param cookieName 쿠키명
	 * @param contents 쿠키내용
	 * @param key 암호화 키
	 * @param dead_line 기간
	 * @throws Exception
	 */
	public void setCookie(HttpServletResponse response, String cookieName, String contents, String key, int dead_line) throws Exception {
		Cookie setCookie = new Cookie(StringCryPto.encrypt(key, cookieName), contents); // 쿠키 이름을 지정
		setCookie.setMaxAge(60 * 60 * dead_line); // 쿠키 존재기간 3시간
		response.addCookie(setCookie);
	}
	
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        
        return ip;
	}
	
	public String ManageSmartEditorImg(String contents, String movePath) {
		//정규식을 이용한 img src추출
		if (contents.lastIndexOf("<br>") >= 0 && contents.length() - contents.substring(0, contents.lastIndexOf("<br>")).length() == 4) {
			contents = contents.substring(0, contents.lastIndexOf("<br>"));
		}
		
		Pattern pattern = Pattern.compile("<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>"); //img 태그 src 추출 정규표현식
        Matcher matcher = pattern.matcher(contents);
        
        while (matcher.find()) {
        	String file_path = matcher.group(1);
        	String file_name = file_path.substring(file_path.lastIndexOf("/") + 1, file_path.length());
        	
        	String beforeFilePath = "C:"+ file_path; //옮길 대상 경로
        	
			File dir = new File(movePath);

			if (!dir.exists()) { // 폴더 없으면 폴더 생성
				dir.mkdirs();
			}

			try {

				File file = new File(beforeFilePath);
				file.renameTo(new File(movePath + "/" + file_name));

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
			contents = contents.replaceAll(file_path, movePath.substring(movePath.indexOf("/"), movePath.length()) + "/"+ file_name);
		}

		
		return contents;
	}
	
	/**
	 * 현재시간과 특정시간까지의 차이(남은시간) 계산 메서드
	 * @param tomorrow
	 * @return
	 */
	public static long SleepTime(Date date) {
		Date now = null;
		try {
			//원하는 포멧에 맞춰 현재 시간 가져온다 ex)2019-03-25 19:10:42
			String now_str = formatTime2.format(new Date());
			//다시 Date로 변환한다.
			now = formatTime2.parse(now_str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//두 날짜 사이에 남은 밀리세컨드를 구한다.
		//스케줄 타이머에 사용됨
		long diff = date.getTime() - now.getTime();
		return diff;
	}
}
