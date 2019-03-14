package com.paraframework.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.paraframework.common.PageUtil;
import com.paraframework.object.AccessIp;
import com.paraframework.object.Menu;
import com.paraframework.service.AccessIpService;
import com.paraframework.service.MenuService;

@Controller
@RequestMapping(value="/jooka/admin/access_ip")
public class AccessIpController extends BaseController {
	@Autowired
	private AccessIpService service;
	@Autowired
	private MenuService m_service;
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String Landing(HttpServletRequest request, HttpServletResponse response, PageUtil page) {
		request.setAttribute("menu", "접근 허용 아이피");
		
		//검색 조건 부여 -- 시작
			//검색 옵션 지정
			Map<String, String> search_items = new LinkedHashMap<String, String>();
			search_items.put("ip", "access_ip");
			search_items.put("등록자", "authorizationer");
			search_items.put("ip + 등록자", "access_ip,authorizationer");
		//검색 조건 부여 -- 종료
		
		LocationSearchCheck(request, response, "accessip", "id", search_items, new LocationSearchResultFunction() {
			@Override
			public void SearchTrue(int result, List<Integer> column_value) {
				// TODO Auto-generated method stub
				List<AccessIp> acc_list = new ArrayList<>();
				
				for (int i : column_value) {
					acc_list.add(service.getAccessIpById(i));
				}	
				
				request.setAttribute("acc_ip_list", acc_list);
				request.setAttribute("acc_ip_count", result);
			}
			
			@Override
			public void SearchFalse() {
				int SearchCount = service.getAccessIpCount();
				List<AccessIp>  acc_list = service.getItemByPage(AutoPaging(request, response, SearchCount));
				
				request.setAttribute("acc_ip_list", acc_list);
				request.setAttribute("acc_ip_count", SearchCount);
			}
		});
		
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/get", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getAcc(HttpServletRequest request, int id) {
		AjaxResponse res = new AjaxResponse();
		AccessIp item = service.getAccessIpById(id);
		
		res.setObject(item);
		
		return res;
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse deleteAcc(HttpServletRequest request, int id) {
		AjaxResponse res = new AjaxResponse();
		
		if (service.getAccessIpById(id) != null) {
			service.deleteAccessIp(id);
			res.setMessage("허용 아이피가 삭제 되었습니다.");
		} else {
			res.setMessage("존재하지않거나 삭제된 아이피입니다.");
		}
		
		return res;
	}
	
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse editAcc(HttpServletRequest request, @Valid AccessIp access_ip, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		
		String next_url = request.getHeader("referer");
		String success_message = "접근 허용 아이피가 수정되었습니다.";
		
		if (!res.validation_data(result, next_url, success_message, res)) {
			service.updateAccessIp(access_ip);
		}
		
		res.setProcessing_result(true);
		
		return res;
	}

	
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse InsertAcc(HttpServletRequest request, @Valid AccessIp access_ip, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		
		
		String next_url = "/jooka/admin/access_ip/index";
		String success_message = "허용 아이피를 등록 하였습니다.";
		
		if (!res.validation_data(result, next_url, success_message, res)) {
			service.insertAccessIp(access_ip);
		}
		
		res.setProcessing_result(true);
		
		return res;
	}

}
