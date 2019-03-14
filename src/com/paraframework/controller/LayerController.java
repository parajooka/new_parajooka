package com.paraframework.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.paraframework.common.BaseController;
@Controller
@RequestMapping(value="/jooka/layer")
public class LayerController extends BaseController {
	@RequestMapping(value="/*", method=RequestMethod.GET)
	public String main(HttpServletRequest request) {
		return getRequestURI(request);
	}
}
