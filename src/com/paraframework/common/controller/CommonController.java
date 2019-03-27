package com.paraframework.common.controller;


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
public class CommonController extends ControllerCommonMethod {
	
	@RequestMapping(value="/paging/changeLimit", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse PagingLimitChange(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse res = new AjaxResponse();
		String paging_limit = request.getParameter("paging_limit");
		
		if (paging_limit != null && paging_limit.length() > 0) {
			try {
				//limit암호화 해체
				paging_limit = StringCryPto.decrypt("pagingLimit", paging_limit);
				//사용자가 요청한 limit로 page값  변경
				request.getSession().setAttribute("paging_limit", Integer.parseInt(paging_limit));
				//Limit 교체시 1페이지로 보내기위함인데 1회만 실행해야 하므로 flag생성
				request.getSession().setAttribute("paging_limit_flag", true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				alertMessage("잘못된 페이징 요청입니다.", request, response);
			}
		}
		
		res.setProcessing_result(true);
		res.setNext_url(getBeforeURL(request));
		return res;
	}
}
