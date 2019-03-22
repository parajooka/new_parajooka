package com.para.controller.landing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.para.object.landing.LandingAnswer;
import com.para.object.landing.LandingParticipant;
import com.para.object.landing.LandingQuestion;
import com.para.service.landing.LandingAnswerService;
import com.para.service.landing.LandingParticipantService;
import com.para.service.landing.LandingQuestionService;
import com.paraframework.common.AjaxResponse;
import com.paraframework.common.ControllerCommonMethod;

@Controller
@RequestMapping(value="/jooka/admin/landing")
public class AdminLandingParticipantController extends ControllerCommonMethod {
	
	@Autowired
	private LandingParticipantService participant_serivce;
	@Autowired
	private LandingAnswerService answer_service;
	@Autowired
	private LandingQuestionService question_service;
	
	@RequestMapping(value="/participant_index", method=RequestMethod.GET)
	public String Index(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("menu", "랜딩 참여자 관리");
		//검색 조건 부여 -- 시작
		//검색 옵션 지정
			Map<String, String> search_items = new LinkedHashMap<String, String>();
			search_items.put("이름", "name");
			search_items.put("소속", "company");
			search_items.put("ip", "ip_address");
		//검색 조건 부여 -- 종료

		LocationSearchCheck(request, response, "landingparticipant", "participant_id", search_items, new LocationSearchResultFunction() {
			@Override
			public void SearchTrue(int result, List<Integer> column_value) {
				// TODO Auto-generated method stub
				List<LandingParticipant> participant_list = new ArrayList<>();
				
				for (int i : column_value) {
					try {
						participant_list.add(participant_serivce.getParticipant(i));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
				
				request.setAttribute("participant_list", participant_list);
				request.setAttribute("participant_count", result);
			}
			
			@Override
			public void SearchFalse() {
				int SearchCount = participant_serivce.CountParticipant();
				List<LandingParticipant> participant_list = null;
				try {
					participant_list = participant_serivce.getParticipantByPaging(AutoPaging(request, response, SearchCount));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				request.setAttribute("participant_list", participant_list);
				request.setAttribute("participant_count", SearchCount);
			}
		});
		
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/participant_manage", method=RequestMethod.GET)
	public String ManageParticipant(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception {
		String participant_id = request.getParameter("participant_id");
		
		if (participant_id == null || participant_id.length() == 0) {
			alertMessage("허용되지않은 접근방법입니다.", request, response);
			return null;
		}
		
		if (!validNumber(participant_id, request, response)) {
			return null;
		}
		
		LandingParticipant participant = participant_serivce.getParticipant(Integer.parseInt(participant_id));
		
		if (participant == null) {
			alertMessage("존재하지않거나 삭제된 참여자입니다.", request, response);
			return null;
		}
		
		//선택한 답안 및 문항 도출
		String[] answers = participant.getRecord().split(",");
		List<LandingQuestion> question_list = new ArrayList<>();
		Map<Integer, LandingAnswer> answer_map = new HashMap<>();
		
		for (String s : answers) {
			LandingAnswer answer = answer_service.getAnswerById(Integer.parseInt(s));
			
			if (answer != null) {
				LandingQuestion question = question_service.getQuestionById(answer.getQuestion_id()); 
				question_list.add(question);
				answer_map.put(question.getQuestion_id(), answer);
			}
		}
		
		
		request.setAttribute("question_list", question_list);
		request.setAttribute("answer_map", answer_map);
		request.setAttribute("menu", "'" + participant.getName() + "'님의 랜딩 참여결과");
		request.setAttribute("participant", participant);
		
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/delete/participant", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse DeleteQuestion(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="delete_participant_targets[]") List<String> delete_targets) {
		AjaxResponse res = new AjaxResponse();
		
		
		try {
			for (String key : delete_targets) {
				participant_serivce.DeleteParticipant(Integer.parseInt(key));
			}
			
			res.setMessage("선택한 참여자들이 정상적으로 삭제되었습니다.");
			res.setNext_url("/jooka/admin/landing/participant_index");
		} catch (Exception e) {
			alertMessage("잘못된 접근입니다.", request, response);
		}
		
		res.setProcessing_result(true);
		
		return res;
	}
}
