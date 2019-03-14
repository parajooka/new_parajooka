package com.para.controller.hire;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.para.object.hire.Hire;
import com.para.object.hire.QnHJoin;
import com.para.service.hire.HireService;
import com.para.service.hire.QnHJoinService;
import com.para.service.hire.QuestionService;
import com.paraframework.common.AjaxResponse;
import com.paraframework.common.BaseController;
import com.paraframework.common.PageUtil;

@Controller
@RequestMapping(value= {"/jooka/admin/hirejoin"})
public class HireJoinController extends BaseController {
	@Autowired
	private HireService hire_service;
	@Autowired
	private QnHJoinService qnh_service;
	@Autowired
	private QuestionService question_service;

	@Override
	public String Index(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String Join(HttpServletRequest request, HttpServletResponse response, Model model) {
		request.setAttribute("menu", "공고 문항 관리");
		
		List<Hire> hire_list = hire_service.getHireByProceeding(new Date());
		hire_list.addAll(hire_service.getHireByExpected(new Date()));
		
		int count = question_service.getCount();

		PageUtil page = AutoPaging(request, response, count);
		
		request.setAttribute("hire_list", hire_list);
		request.setAttribute("question_list", question_service.getQuestionByPage(page));
		request.setAttribute("pageUtil", page);
		request.setAttribute("question_count", count);
		
		String hire_id = request.getParameter("hire_id");
		
		if (hire_id != null && hire_id.length() > 0) {
			request.setAttribute("hire_id", hire_id);
		}
		
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/get_qeustion", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getQuestionByHire(HttpServletRequest request, int id) {
		AjaxResponse res = new AjaxResponse();
		
		if (id == 0) {
			res.setMessage("잘못된 입력값입니다.");
		} else {
			res.setObject(qnh_service.getQuestionByQnH(id));
		}
		
		res.setProcessing_result(true);
		
		return res;
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse deleteQnH(HttpServletRequest request, int hire_id, int question_id) {
		AjaxResponse res = new AjaxResponse();
		
		res.setMessage("해당 문항을 공고에서 제외 하였습니다.");
		qnh_service.DeleteQnH(hire_id, question_id);
		
		return res;
	}
	
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse inserQnH(HttpServletRequest request, int hire_id, int question_id) {
		AjaxResponse res = new AjaxResponse();
		
		if (hire_id == 0 || question_id == 0) {
			res.setMessage("잘못된 접근입니다.");
			res.setNext_url("");
			res.setProcessing_result(true);
			return res;
		} else if (qnh_service.countQnH(hire_id, question_id) > 0) {
			res.setMessage("이미 등록된 문항입니다.");
			res.setNext_url("");
			res.setProcessing_result(true);
			return res;
		} else {
			QnHJoin join = new QnHJoin();
			join.setHire_id(hire_id);
			join.setQuestion_id(question_id);
			
			qnh_service.InsertQnH(join);
			
			res.setMessage("해당 문항이 선택한 공고에 추가 되었습니다.");
			res.setProcessing_result(true);
			return res;
		}
	}
	
	

}
