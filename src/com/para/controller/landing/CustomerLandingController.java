package com.para.controller.landing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.para.object.landing.LandingAnswer;
import com.para.object.landing.LandingParticipant;
import com.para.object.landing.LandingQuestion;
import com.para.service.landing.LandingAnswerService;
import com.para.service.landing.LandingParticipantService;
import com.para.service.landing.LandingQuestionService;
import com.paraframework.common.AjaxResponse;
import com.paraframework.common.BaseController;
import com.paraframework.common.StringCryPto;
import com.paraframework.object.Menu;
import com.paraframework.service.MenuService;

@Controller
@RequestMapping(value="/custom/renewal/landing")
public class CustomerLandingController extends BaseController {
	@Autowired
	private LandingQuestionService question_service;
	@Autowired
	private LandingAnswerService answer_service;
	@Autowired
	private LandingParticipantService participant_service;
	@Autowired
	private MenuService menu_service;
	
	private static SimpleDateFormat formatTime = new SimpleDateFormat("yyyy년 MM월 dd일 aa hh시mm분ss초", Locale.KOREAN);
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String Index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().removeAttribute("target_menu");
		String contact = (String) request.getSession().getAttribute("contact");
		
		Menu target_menu = (Menu) request.getSession().getAttribute("target_menu");
		
		if (target_menu == null) {
			request.getSession().setAttribute("target_menu", menu_service.getMenuByName("Contact"));
		}
		request.getSession().setAttribute("menu_move_cmd", false);
		
		//이미 랜딩에 참여한사람
		if (participant_service.ValidParticipant(getIpAddress(request)) > 0) {
			Menu menu = menu_service.getMenuByName("About");
			return RedirectPage(request, "/custom/menu/index?menu_idx="+ menu.getMenu_idx());
		}
		
		if (contact != null && contact.length() > 0) {
			request.setAttribute("contact", true);
		}
		
		return getRequestURI(request);
	}
	
	
	@RequestMapping(value="/send/customer/info", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getCustomerInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxResponse res = new AjaxResponse();
		
		String user_name = request.getParameter("user_name");
		String user_company = request.getParameter("user_company");
		
		if (user_name == null || user_name.length() == 0 || user_company == null || user_company.length() == 0) {
			return res.returnResponse("이름과 소속을 반드시 입력해주세요.", null);
		}
		
		if (participant_service.ValidParticipant(getIpAddress(request)) == 0) {
			//개인정보를 서버에 기억해둔다.
			request.getSession().setAttribute("user_name", user_name);
			request.getSession().setAttribute("user_company", user_company);
			request.getSession().setAttribute("user_ip", getIpAddress(request));
			request.getSession().setAttribute("user_time", formatTime.format(new Date()));
			
			request.getSession().removeAttribute("user_answers");
			
			//최초문항 출력 시작
			getQuestionAndAnswer(request, res, 0);

		} else {
			Menu menu = menu_service.getMenuByName("About");
			return res.returnResponse("랜딩 설문은 1인 1회만 참여가 가능합니다.\r\n공유기 환경은 서로 아이피가 공유됩니다.", "/custom/menu/index?menu_idx="+ menu.getMenu_idx());
		}
			res.setProcessing_result(true);
			return res;
	}
	
	@RequestMapping(value="/send/answer", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getQuestionByAnswerId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxResponse res = new AjaxResponse();
		String answer_idx = request.getParameter("answer_id");
		
		if (answer_idx == null || answer_idx.length() == 0) {
			request.getSession().invalidate();
			return res.returnResponse("잘못된 접근입니다.\r\n처음부터 진행해주세요.", "/");
		}
		
		int answer_id = 0;
		
		try {
			answer_id = Integer.parseInt(answer_idx);
		} catch (Exception e) {
			// TODO: handle exception
			request.getSession().invalidate();
			alertMessageByString("잘못된 접근입니다.\r\n처음부터 진행해주세요.", "/", request, response);
			return null;
		}
		
		if (answer_service.ValidtaionAnswerId(answer_id) == 0) {
			request.getSession().invalidate();
			return res.returnResponse("잘못된 접근입니다.\r\n처음부터 진행해주세요.", "/");
		}
		
		//넘어온 답안을 session에 기록
		@SuppressWarnings("unchecked")
		List<Integer> user_answers = (List<Integer>) request.getSession().getAttribute("user_answers");
		
		if (user_answers == null) {
			user_answers = new ArrayList<>();
			request.getSession().setAttribute("user_answers", user_answers);
		}
		
		user_answers.add(answer_id);
		
		try {
			//더이상 문항이 존재하지 않고 3문제 이상 푼경우 랜딩 종료
			if (getQuestionAndAnswer(request, res, answer_id) == 0 && user_answers.size() >= 3) {
				LandingParticipant participant = new LandingParticipant();
				
				//사용자 정보 암호화
				String cry_pto_key = "CustomerLandingResult";
				String user_company = StringCryPto.encrypt(cry_pto_key, (String) request.getSession().getAttribute("user_company"));
				String user_ip = StringCryPto.encrypt(cry_pto_key, (String) request.getSession().getAttribute("user_ip"));
				String user_name = StringCryPto.encrypt(cry_pto_key, (String) request.getSession().getAttribute("user_name"));
				String user_time = StringCryPto.encrypt(cry_pto_key, (String) request.getSession().getAttribute("user_time"));
				
				String record = "";
				
				//정보 객체에 삽입
				participant.setCompany(user_company);
				participant.setIp_address(user_ip);
				participant.setName(user_name);
				participant.setStart_time(user_time);
				participant.setEnd_time(StringCryPto.encrypt(cry_pto_key, formatTime.format(new Date())));
	
				for (int i = 0; i < user_answers.size(); i++) {
					if (i == user_answers.size() - 1) {
						record += user_answers.get(i) + "";
					} else {
						record += user_answers.get(i) + ",";
					}
				}
				
				//사용자 랜딩설문 참여결과 암호화
				//정보 삽입
				participant.setRecord(StringCryPto.encrypt(cry_pto_key, record));
	
				//참여 기록이 없다면 db에 기록
				if (participant_service.ValidParticipant(getIpAddress(request)) == 0) {
					participant_service.InsertParticipant(participant);
				} else {
					Menu menu = menu_service.getMenuByName("About");
					return res.returnResponse("랜딩 설문은 1인 1회만 참여가 가능합니다.\r\n공유기 환경은 서로 아이피가 공유됩니다.", "/custom/menu/index?menu_idx="+ menu.getMenu_idx());
				}
				
				res.setProcessing_result(true);
				
				String contact = (String) request.getSession().getAttribute("contact");
				
				Menu menu = null;
				
				//컨텍트에서 넘어온 경우에는 컨텍트 페이지로 보낸다
				if (contact != null && contact.length() > 0) {
					menu = menu_service.getMenuByName("Contact");
				} else {
				//그외에는 메인페이지로 보낸다
					menu = menu_service.getMenuByName("About");
				}
	
				//세션 초기화
				request.getSession().invalidate();
				res.setMessage("ClearContact");
				res.setNext_url("/custom/menu/index?menu_idx="+ menu.getMenu_idx());
				return res;
			} else if (getQuestionAndAnswer(request, res, answer_id) == 0 && user_answers.size() < 3) {
				res.setProcessing_result(true);
				Menu menu = menu_service.getMenuByName("About");
				return res.returnResponse("", "/custom/menu/index?menu_idx="+ menu.getMenu_idx());
			}
		} catch (Exception e) {
			// TODO: handle exception
			return res.returnResponse("세션이 만료되었습니다. 처음부터 다시 시동해주세요.", "http://localhost/custom/renewal/landing/index");
		}
		
		res.setProcessing_result(true);
		return res;
	}
	
	
	
	/**
	 * 답안 아이디를 이용해서 문제 호출 및 해당 문제의 답안 호출
	 * @param request
	 * @param answer_id
	 */
	public int getQuestionAndAnswer(HttpServletRequest request, AjaxResponse res, int answer_id) {
		List<LandingQuestion> question = question_service.getQuestionByAnswerId(answer_id);
		Map<Integer, List<LandingAnswer>> answers = new LinkedHashMap<>();
		
		String[] change_code = {"user_name", "user_company", "user_time"};
		String[] change_word = {(String)request.getSession().getAttribute("user_name"),
								(String)request.getSession().getAttribute("user_company"),
								formatTime.format(new Date())};
		
		//치환코드 변경
		for (LandingQuestion q : question) {
			if (q.getQuestion_title().indexOf("%=") >= 0) {
				q.setQuestion_title(q.getQuestion_title().substring(0, q.getQuestion_title().indexOf("%=")));
			}
			
			//문항에서 치환코드 교체
			for (int i = 0; i < change_code.length; i++) {
				q.setQuestion_title(q.getQuestion_title().replaceAll("<="+  change_code[i] +">", change_word[i]));
				
				if (q.getQuestion_contents() != null && q.getQuestion_contents().length() > 0) {
					if (q.getQuestion_contents().indexOf("%=") >= 0) {
						q.setQuestion_contents(q.getQuestion_contents().substring(0, q.getQuestion_title().indexOf("%=")));
					}
					
					q.setQuestion_contents(q.getQuestion_contents().replaceAll("<="+  change_code[i] +">", change_word[i]));
				}
			}
			
			List<LandingAnswer> answer = answer_service.getAnswerByQuestionId(q.getQuestion_id());
			
			for (LandingAnswer a : answer) {
				if (a.getAnswer_title() != null && a.getAnswer_title().length() > 0) {
					
					//답안에서 치환코드 교체
					for (int i = 0; i < change_code.length; i++) {
						if (a.getAnswer_title().indexOf("%=") >= 0) {
							a.setAnswer_title(a.getAnswer_title().substring(0, a.getAnswer_title().indexOf("%=")));
						}
						
						a.setAnswer_title(a.getAnswer_title().replaceAll("<="+  change_code[i] +">", change_word[i]));
					}
				}
			}
			
			answers.put(q.getQuestion_id(), answer);
			
		}
		
		Map<String, Object> result = new HashMap<>();
		
		result.put("question", question);
		result.put("answer", answers);
		
		res.setObject(result);
		
		return question.size();
	}
}
