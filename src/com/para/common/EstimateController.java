package com.para.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paraframework.common.AjaxResponse;
import com.paraframework.common.ControllerCommonMethod;
import com.paraframework.common.StringCryPto;

@Controller
@RequestMapping(value="/estimate")
public class EstimateController extends ControllerCommonMethod {
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse reponse) {
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/validLogin", method =RequestMethod.POST)
	public @ResponseBody AjaxResponse validLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxResponse res = new AjaxResponse();
		
		Map<String, Boolean> company = new HashMap<String, Boolean>();
		company.put("monolabs", true);
		company.put("costory", true);
		
		Map<String, String> password = new HashMap<String, String>();
		password.put("monolabs", "mono12@");
		password.put("costory", "coscos12@");
		
		
				
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		if (id == null || id.length() == 0 || pw == null || pw.length() == 0) {
			return res.returnResponse("아이디와 비밀번호를 입력해주세요.", null);
		}
		
		if (company.get(id) != null && company.get(id)) {
			String psw = password.get(id);
			if (pw.equals(psw)) {
				res.setObject(StringCryPto.encrypt("estimate", id));
				return res.returnResponse(null, null);
			} else {
				return res.returnResponse("암호가 일치하지 않습니다.", null);
			}
		} else {
			return res.returnResponse("일치하는 파트너가 존재하지않습니다.", null);
		}
	}
}
