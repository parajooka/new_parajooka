package com.para.controller.hire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.para.object.hire.Hire;
import com.para.object.hire.HireResult;
import com.para.object.hire.Question;
import com.para.object.hire.Volunteer;
import com.para.object.hire.VolunteerResult;
import com.para.service.hire.AnswerService;
import com.para.service.hire.HireResultService;
import com.para.service.hire.HireService;
import com.para.service.hire.QnHJoinService;
import com.para.service.hire.VolunteerResultService;
import com.para.service.hire.VolunteerService;
import com.paraframework.common.AjaxResponse;
import com.paraframework.common.ControllerCommonMethod;
import com.paraframework.common.SendSMS;

@Controller
@RequestMapping(value=ControllerCommonMethod.admin_page_path + "/manage_hire_volunteer")
public class VolunteerManageController extends ControllerCommonMethod {
	@Autowired
	private HireService hire_service;
	
	@Autowired
	private VolunteerResultService result_service;
	
	@Autowired
	private AnswerService answer_service;
	
	@Autowired
	private HireResultService hire_reuslt_service;
	
	@Autowired
	private VolunteerService volunteer_service;
	
	@Autowired
	private QnHJoinService qn_service;
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String index(HttpServletRequest request) {
		request.setAttribute("menu", "공고 지원자 관리");
		List<Hire> hire_list = hire_service.getHire();
		request.setAttribute("hire_list", hire_list);
		
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/getVolunteer", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getVolunteer(HttpServletRequest request, int hire_id) {
		AjaxResponse res = new AjaxResponse();
		
		List<Volunteer> volunteers = result_service.getVolunteerByHireId(hire_id);
		
		VolunteerResult result = new VolunteerResult();
		
		Map<Integer, VolunteerResult> survey_list = new HashMap<Integer, VolunteerResult>();
		
		for (Volunteer v : volunteers)  {
			result.setVolunteer_id(v.getId());
			result.setHire_id(hire_id);
			
			survey_list.put(v.getId(), result_service.getVolunteerResultByHireAndVolunteeer(result));
		}
		
		List<Object> obj = new ArrayList<Object>();
		
		obj.add(volunteers);
		obj.add(survey_list);
		
		res.setObject(obj);
		
		return res;
	}
	
	@RequestMapping(value="/survey", method=RequestMethod.GET)
	public String ManageVolunteer(HttpServletRequest request, HttpServletResponse response, Model model, int hire_id, int volunteer_id) {
		request.setAttribute("menu", "지원자 관리");
		
		try {
			request.setAttribute("hire", hire_service.getHireById(hire_id));
			
			VolunteerResult result = new VolunteerResult();
			result.setVolunteer_id(volunteer_id);
			result.setHire_id(hire_id);
			
			VolunteerResult orign_result = result_service.getVolunteerResultByHireAndVolunteeer(result);
			
			request.setAttribute("volunteer_result", orign_result);
			
			List<Question> questions = qn_service.getQuestionByQnH(hire_id);
			
			List<HireResult> results = hire_reuslt_service.getHireResultByVolunteerResult(orign_result.getId());
			Map<Integer, String> hire_results = new HashMap<Integer, String>();
			
			for (HireResult h : results) {
				if (h.getOpen_answer() == null || h.getOpen_answer().length() < 1) {
					hire_results.put(h.getAnswer_id(), "true");
				} else {
					hire_results.put(h.getQuestion_id(), h.getOpen_answer());
				}
			}
			
			for (Question q : questions) {
				q.setAnswers(answer_service.getAnswerByQuestionId(q.getId()));
			}
			
			request.setAttribute("hire_results", hire_results);
			request.setAttribute("questions", questions);
			model.addAttribute("volunteer", volunteer_service.getVolunteerById(volunteer_id));
		} catch (Exception e) {
			alertMessage("존재하지 않거나 삭제된 지원서입니다.", request, response);
		}
				
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/survey_result", method=RequestMethod.GET)
	public String SurveyResult(HttpServletRequest request, HttpServletResponse response, int id, int value) {
		
		VolunteerResult result = new VolunteerResult();
		VolunteerResult orign_result = result_service.getVolunteerResultById(id);
		
		String old_url = request.getHeader("referer");
		
		if (orign_result != null) {
			result.setId(id);
			result.setResult(value);
			result.setFile(orign_result.getFile());
			
			result_service.UpdateVolunteerResult(result);
			
			String result_value = null;
			
			if (value == 1) {
				result_value = "합격";
				Volunteer volunteer = volunteer_service.getVolunteerById(orign_result.getVolunteer_id());
				String[] names = volunteer.getName().split("");
				String name = "";
				
				for (int i = 0; i < names.length; i++) {
					if (i != 1) {
						name += names[i];
					} else {
						name += "*";
					}
				}
						
				SendSMS sms = new SendSMS();
				sms.sendMMS(volunteer.getPhone(), "안녕하세요? Para & Jooka입니다.\n"+
												  "먼저 저희 Para & Jooka에 지원해주신 부분에대하여 감사의 말씀 전해드립니다.\n"+
												  name +"님께서 지원해주신 지원서 검토 결과 따로 면담을 진행하고자 합니다.\n"+
												  "면담 형태는 1:1면담이며 카카오톡 플러스친구를 이용하여 진행됩니다.\n"+
												  "https://pf.kakao.com/_BwxkTj/friend\n"+
												  "해당 링크에서 저희 Para & Jooka 친구 추가 후 면담 가능한 시간을 카카오톡으로 남겨주세요.");
			} else if (value == 2) {
				result_value = "불합격";
			} else {
				alertMessageByString("잘못된 접근입니다.", old_url, request, response);
			}
			
			alertMessageByString("해당 공고를 "+ result_value +"처리 하였습니다.", old_url, request, response);
		} else {
			alertMessageByString("존재하지않거나 삭제된 공고입니다.", old_url, request, response);
		}
		
		
		
		return null;
	}

}
