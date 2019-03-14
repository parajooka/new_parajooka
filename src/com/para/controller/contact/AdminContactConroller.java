package com.para.controller.contact;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.para.object.contact.Contact;
import com.para.object.landing.LandingParticipant;
import com.para.service.contact.ContactService;
import com.paraframework.common.AjaxResponse;
import com.paraframework.common.BaseController;

@Controller
@RequestMapping(value="/jooka/admin/contact")
public class AdminContactConroller extends BaseController {
	@Autowired
	private ContactService contact_service;
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("menu", "미팅 신청자 관리");
		
		List<Contact> contact_list = contact_service.getContactByPaging(AutoPaging(request, response, contact_service.CountContact()));
		List<LandingParticipant> participant_list = new ArrayList<>();
		if (contact_list != null && contact_list.size() > 0) {
			for (Contact c : contact_list) {
				if (c != null) {
					participant_list.add(c.getParticipant());
				}
			}
		}
		
		request.setAttribute("contact_list", contact_list);
		request.setAttribute("participant_list", participant_list);
		
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse DeleteQuestion(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="delete_contact_targets[]") List<String> delete_targets) {
		AjaxResponse res = new AjaxResponse();
		
		
		try {
			for (String key : delete_targets) {
				contact_service.DeleteContact(Integer.parseInt(key));
			}
			
			res.setMessage("선택한 미팅들이 정상적으로 삭제되었습니다.");
			res.setNext_url("/jooka/admin/contact/index");
		} catch (Exception e) {
			alertMessage("잘못된 접근입니다.", request, response);
		}
		
		res.setProcessing_result(true);
		
		return res;
	}
	
	@RequestMapping(value="/getMemo", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getMemoByContactId(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse res = new AjaxResponse();
		
		String contact_id = request.getParameter("contact_id");
		
		if (contact_id == null || contact_id.length() == 0) {
			return res.returnResponse("존재하지않거나 삭제된 미팅예약입니다.", "/jooka/admin/contact/index");
		}
		
		try {
			res.setObject(contact_service.getMemoByContactId(Integer.parseInt(contact_id)));
		} catch(Exception e) {
			e.printStackTrace();
			return res.returnResponse("존재하지않거나 삭제된 미팅예약입니다.", "/jooka/admin/contact/index");
		}
		
		res.setProcessing_result(true);
		return res;
	}
}
