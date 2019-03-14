package com.para.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.paraframework.common.BaseController;

@Controller
@RequestMapping(value="/custom/2018/landing")
public class BeforeLandingController extends BaseController {
	
	@RequestMapping(value="/no")
	public String NoPage(HttpServletRequest request) {
		if (isMobile(request)) {
			return getMobileRequestURI(getRequestURI(request));
		}
		return getRequestURI(request);
	}

}
