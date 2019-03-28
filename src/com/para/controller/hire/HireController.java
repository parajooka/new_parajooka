package com.para.controller.hire;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.para.object.hire.Hire;
import com.para.service.hire.HireService;
import com.paraframework.common.AjaxResponse;
import com.paraframework.common.ControllerCommonMethod;

@Controller
@RequestMapping(value= {ControllerCommonMethod.admin_page_path + "/hire"})
public class HireController extends ControllerCommonMethod {
	
	@Autowired
	HireService service;
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String hire(HttpServletRequest request, HttpServletResponse response, Model model, Hire hire) {
		request.setAttribute("menu", "채용 공고 관리");
		
		Date now = new Date();
		Date tomorrow = new Date();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(tomorrow);
		cal.add(Calendar.DATE, 1);
		

		request.setAttribute("now", format.format(now));
		request.setAttribute("tomorrow", format.format(cal.getTime()));
		
		model.addAttribute("hire", hire);
		
		//PageUtil page = AutoPaging(request, response, count, 0, request.getRequestURI());
		
		int count = service.CountAllHire();
		
		request.setAttribute("hires", service.getHireByPage(AutoPaging(request, response, count)));
		request.setAttribute("hire_count", service.CountAllHire());
		request.setAttribute("pr_hire", service.CountHireByProceeding(new Date()));
		
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse InsertHire(HttpServletRequest request, @Valid Hire hire, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		
		String next_url = ControllerCommonMethod.admin_page_path + "/hire/index";
		String success_message = "공고를 정상적으로 등록 하였습니다.";
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -1);
		
		
		if (!res.validation_data(result, next_url, success_message, res)) {
			if (hire.getStart_date().compareTo(hire.getEnd_date()) <= 0 && cal.getTime().compareTo(hire.getStart_date()) < 0) {
				service.InsertHire(hire);
				hire.setInfo(ManageSmartEditorImg(hire.getInfo(), "C:/res/img/admin/hire/" + hire.getId()));
				service.UpdateHire(hire);
			} else {
				res.setMessage("공고 종료일이 시작일보다 작을수는 없고\r\n공고 시작일은 오늘 날짜보다 작을수 없습니다.");
			}
		}
		
		res.setProcessing_result(true);
		
		return res;
	}
	
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse UpdateHire(HttpServletRequest request, @Valid Hire hire, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		
		String next_url = ControllerCommonMethod.admin_page_path + "/hire/index";
		String success_message = "공고를 정상적으로 수정 하였습니다.";

		Hire orign_hire = service.getHireById(hire.getId());
		
		if (!res.validation_data(result, next_url, success_message, res)) {
			if (hire.getStart_date().compareTo(hire.getEnd_date()) <= 0 && orign_hire.getStart_date().compareTo(hire.getStart_date()) <= 0) {
				hire.setInfo(ManageSmartEditorImg(hire.getInfo(), "C:/res/img/admin/hire/" + hire.getId()));
				service.UpdateHire(hire);
			} else {
				res.setMessage("공고 종료일이 시작일보다 작을수는 없고\r\n공고 시작일은 수정 이전의 시작일보다 작을수 없습니다.");
			}
		}
		
		res.setProcessing_result(true);
		
		return res;
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse DeleteHire(HttpServletRequest request, int id) {
		AjaxResponse res = new AjaxResponse();
		
		Hire hire = service.getHireById(id);
		
		if (id == 0 || hire == null) {
			res.setMessage("존재하지 않거나 삭제된 공고입니다.");
		} else {
			removeDirectory("C:\\res\\img\\admin\\hire\\"+ hire.getId());
			service.DeleteHire(id);
			res.setMessage("공고가 정상적으로 삭제 되었습니다.");
		}
		
		res.setNext_url(ControllerCommonMethod.admin_page_path + "/hire/index");
		res.setProcessing_result(true);
			
		return res;
	}
	
	@RequestMapping(value="/get", method = RequestMethod.POST)
	public @ResponseBody AjaxResponse getHire(HttpServletRequest request, int id) {
		AjaxResponse res = new AjaxResponse();
		
		Hire hire = service.getHireById(id);
		
		res.setObject(hire);
		res.setMessage(hire.getKrDate(hire.getStart_date()));
		res.setNext_url(hire.getKrDate(hire.getEnd_date()));
		
		return res;
	}
}
