package com.para.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paraframework.common.AjaxResponse;
import com.paraframework.common.BaseController;
import com.paraframework.common.SMTP;
import com.paraframework.object.AccessIp;
import com.paraframework.service.AccessIpService;

@Controller
@RequestMapping(value="/temp_file")
public class TempAminAccessIpInjectionController extends BaseController {
	
	private static SimpleDateFormat formatTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREAN);

	@Autowired
	private AccessIpService service;
	
	@RequestMapping(value="/temp_acc_injection", method=RequestMethod.GET)
	public String tempAccInjection(HttpServletRequest request, HttpServletResponse response) {
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/temp_acc_injection/insert", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse TempInjection(HttpServletRequest request, HttpServletResponse response, @Valid AccessIp access_ip, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		
		if (!res.validation_data(result, "/", "임시 관리자 아이피가 등록되었습니다.", res)) {
			service.insertAccessIp(access_ip);
			
			String ip_add = getIpAddress(request);
			
			SMTP smtp = new SMTP();
			smtp.SendMail("kdyshj700@gmail.com", "[Para & Jooka] 임시 아이피가 새로 등록 되었습니다.", "등록자 IP : "+ ip_add + "<br>허용 아이피 : " + access_ip.getAccess_ip() + "<br>등록일시 : " + access_ip.getAuth_date());
			
			CustomTimer(new CustomizingTimer() {
				
				@Override
				public void actionFunc() {
					// TODO Auto-generated method stub
					service.deleteAccessIp(access_ip.getId());
					smtp.SendMail("kdyshj700@gmail.com", "[Para & Jooka] 임시 아이피가 만료 되었습니다.", "등록자 IP : "+ ip_add + "<br>허용 아이피 : " + access_ip.getAccess_ip() + "<br>만료일시 : " + formatTime2.format(new Date()));
				}
			}, 1000 * 10);
		}
		
		
		
		res.setProcessing_result(true);
		return res;
	}
}
