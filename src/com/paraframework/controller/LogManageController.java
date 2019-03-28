package com.paraframework.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paraframework.common.AjaxResponse;
import com.paraframework.common.ControllerCommonMethod;

@Controller
@RequestMapping(value = "/jooka/admin/logManage", method = RequestMethod.GET)
public class LogManageController extends ControllerCommonMethod {
	private static SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);

	@RequestMapping(value = "/visit_logged", method = RequestMethod.GET)
	public String index(HttpServletRequest request) {
		request.setAttribute("menu", "방문자 기록");
		request.setAttribute("logged_today", formatTime.format(new Date()));

		int lineCnt = 0;
		File file = new File("C:\\res\\logged\\para-jooka\\logged_"+ formatTime.format(new Date()) +".txt");
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			while (in.readLine() != null)
				lineCnt++;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		request.setAttribute("logged_line", lineCnt - 1);

		return getRequestURI(request);
	}
	
	@RequestMapping(value="/validLoggedDate", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse ValidLoggedDate(HttpServletRequest request) {
		AjaxResponse res = new AjaxResponse();
		
		String date = request.getParameter("logged_date");
		
		if (date == null || date.length() == 0) {
			return res.returnResponse("잘못된 로그 날짜입니다.", "/jooka/admin/logManage/visit_logged");
		}
		
		int lineCnt = 0;
		File file = new File("C:\\res\\logged\\para-jooka\\logged_"+ date +".txt");
		
		if (file == null || file.isFile() == false || file.exists() == false) {
			return res.returnResponse("선택하신 날짜에 해당하는 로그파일이 존재하지 않습니다.", "/jooka/admin/logManage/visit_logged");
		}
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			while (in.readLine() != null)
				lineCnt++;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		res.setObject(lineCnt - 1);
		res.setProcessing_result(true);
		return res;
	}
}
