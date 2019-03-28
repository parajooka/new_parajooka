package com.para.controller.hire;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
import com.paraframework.common.AuthSMSHandler;
import com.paraframework.common.ControllerCommonMethod;
import com.paraframework.common.CustomMultipartResolver;
import com.paraframework.common.SMTP;

@Controller
@RequestMapping(value=ControllerCommonMethod.customer_page_path + "/hire")
public class UserHireController extends ControllerCommonMethod {
	@Autowired
	HireService hire_service;
	
	@Autowired
	QnHJoinService qn_service;
	
	@Autowired
	AnswerService answer_service;
	
	@Autowired
	VolunteerService volunteer_service;
	
	@Autowired
	VolunteerResultService volunteer_result_service;
	
	@Autowired
	HireResultService hire_result_service;
	
	private final static AuthSMSHandler auth_sms = new AuthSMSHandler();

	@Override
	@RequestMapping("/hire_list")
	public String Index(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		List<Hire> hires = hire_service.getHireByUser(new Date());
		request.setAttribute("hires", hires);
		
		Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
		
		for (Hire h : hires) {
			counts.put(h.getId(), volunteer_result_service.CountVolunteer(h.getId()));
		}
		
		request.setAttribute("counts", counts);
		
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/detail", method=RequestMethod.GET)
	public String Detail(HttpServletRequest request, HttpServletResponse response, int id) {
		Hire hire = hire_service.getHireById(id);
		
		try {
			if (!CheckHire(hire) || hire.getActivation() == 1) {
				ControllerCommonMethod.alertMessageByString("이미 종료된 공고이거나 준비중인 공고입니다.", ControllerCommonMethod.admin_page_path + "/hire/hire_list", request, response);
			} else {
				
				Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
				counts.put(hire.getId(), volunteer_result_service.CountVolunteer(hire.getId()));
				
				request.setAttribute("counts", counts);
				request.setAttribute("hire", hire);
			}
		} catch (Exception e) {
			alertMessage("존재하지 않거나 삭제된 공고입니다.", request, response);
		}
			
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/getTime", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getMinute(HttpServletRequest request, String end_date) throws ParseException {
		AjaxResponse res = new AjaxResponse();
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date end = format.parse(end_date);
		
		res.setMessage(getRemainingTime(end));
		
		return res;
	}
	
	@RequestMapping(value="/survey", method=RequestMethod.GET)
	public String Survey(HttpServletRequest request, HttpServletResponse response, int id) {
		
		try {
			Hire hire = hire_service.getHireById(id);
			
			if (!CheckHire(hire) || hire.getActivation() == 1) {
				ControllerCommonMethod.alertMessageByString("이미 종료된 공고이거나 준비중인 공고입니다.", ControllerCommonMethod.admin_page_path + "/hire/hire_list", request, response);
			} else {
				List<Question> questions = qn_service.getQuestionByQnH(hire.getId());
				
				for (Question q : questions) {
					q.setAnswers(answer_service.getAnswerByQuestionId(q.getId()));
				}
				
				request.setAttribute("questions", questions);
				//본인인증 인증키 발급
				auth_sms.RandomAuthNumber(request);
			}
			
			request.setAttribute("hire", hire);
		} catch (Exception e) {
			// TODO: handle exception
			alertMessage("존재하지않거나 삭제된 공고입니다.", request, response);
		}
		
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/send_sms", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse SendAuthSMS(HttpServletRequest request, String phone, int hire_id) {
		AjaxResponse res = new AjaxResponse();
		
		String reg = "^01(?:0|1[6-9])(?:\\d{3}|\\d{4})\\d{4}$";
		
		Volunteer volunteer = null;
		VolunteerResult v_result = null;
		
		if (!Pattern.matches(reg, phone)) {
			res.setMessage("잘못된 연락처 형식입니다.");
			res.setNext_url("fail");
			res.setProcessing_result(true);
			return res;
		} else if (volunteer_service.CountVolunteerByPhone(phone) > 0) { //이미 참여한 사람인지 검증
			volunteer = volunteer_service.getVolunteerByPhone(phone);
			v_result = new VolunteerResult();
			
			v_result.setVolunteer_id(volunteer.getId());
			v_result.setHire_id(hire_id);
		}
		
		if (v_result != null && volunteer_result_service.CountResultByVolunteerResult(v_result) > 0) {
			res.setMessage("이미 공고에 지원한 연락처입니다.");
			res.setNext_url("fail");
		} else if (auth_sms.SendAuthSMS(request, phone)) {
			res.setMessage("인증번호가 발송 되었습니다.\r\n인증번호는 5분간 유요합니다.");
		} else {
			res.setMessage("이미 발송 처리된 문자가 존재합니다.\r\n확인후 진행 해주세요.");
			res.setNext_url("already");
		}
		
		res.setProcessing_result(true);
		
		return res;
	}
	
	@RequestMapping(value="/check_auth", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse ClearAuth(HttpServletRequest request, int hire_id) {
		AjaxResponse res = new AjaxResponse();
		String reg = "^[0-9]*$";
		
		String auth = request.getParameter("auth_number");
		int auth_checked = 0;
		
		if (auth == null || auth.length() < 1 || !Pattern.matches(reg, auth)) {
			res.setMessage("인증번호는 숫자만 입력 가능합니다.");
			res.setProcessing_result(true);
			return res;
		} else {
			auth_checked = auth_sms.CheckAuthSMSNumber(request, Integer.parseInt(auth));			
		}
		
		if (auth_checked == 0) {
			res.setMessage("인증 문자가 만료되어 재발송 되었습니다.\r\n확인 후 진행 바랍니다.");
		} else if (auth_checked == 1) {
			res.setMessage("인증번호가 일치하지 않습니다.");
		} else if (auth_checked == 2) {
			res.setMessage("인증이 완료 되었습니다.");
			res.setNext_url(auth_checked +"");
		} else {
			res.setMessage("알수없는 오류 발생, 관리자에게 문의 해주세요.");
		}
		
		res.setProcessing_result(true);
		
		return res;
	}
	
	@RequestMapping(value="/send_survey", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse SendSurvey(MultipartHttpServletRequest request, @Valid Volunteer volunteer, BindingResult result) {
		AjaxResponse res = new AjaxResponse();
		HttpSession session = request.getSession();
		
		if (!res.validation_data(result, ControllerCommonMethod.admin_page_path + "/hire/hire_list", "공고에 정상적으로 지원하였습니다.\r\n결과는 지원서 검토후 합격자에게 문자로 개별 통보됩니다.", res)) {
			
			String hire_id = request.getParameter("hire_id");
			String info_acc = request.getParameter("info_acc");
			Hire hire = null;
			//해당 공고 호출
			
			if (hire_id == null || hire_id.length() < 1) {
				res.setMessage("잘못된 접근입니다.");
				res.setProcessing_result(true);
				return res;
			} else {
				hire = hire_service.getHireById(Integer.parseInt(hire_id));
			}
			
			if (hire == null || !CheckHire(hire)) {
				res.setMessage("이미 종료된 공고이거나 존재하지않는 공고입니다.");
				res.setProcessing_result(true);
				return res;
			}
			
			//개인정보 이용방침 동의 체크
			if (info_acc == null || info_acc.length() < 1) {
				res.setMessage("개인정보 이용방침에 동의 해주세요.");
				res.setNext_url(null);
				res.setProcessing_result(true);
				return res;
			}
			
			//답안 도출을 위한 문제 호출
			List<Question> question_list = qn_service.getQuestionByQnH(hire.getId());
			List<HireResult> result_list = new ArrayList<HireResult>();
			
			for (Question q : question_list) {
				//객관식
				if (q.getType() == 0) {
					//해당 문제의 선택한 정답들 호출
					String[] answers = request.getParameterValues("answer_" + q.getId());
					
					//답안이 없을 경우 return
					if (answers == null || answers.length < 1) {
						res.setMessage("모든 문항에 하나 이상의 정답을 선택해주세요.");
						res.setNext_url(null);
						res.setProcessing_result(true);
						return res;
					}
					
					//답안들을 루프
					for (String s : answers) {
						//새로운 hire 생성
						HireResult hire_result = new HireResult();
						//답안 번호 입력
						hire_result.setAnswer_id(Integer.parseInt(s));
						//문제 번호 입력
						hire_result.setQuestion_id(q.getId());
						
						//답안 리스트에 추가
						result_list.add(hire_result);
					}
				//주관식
				} else {
					String free_answer = request.getParameter(q.getId()+"_open_answer");
					
					if (free_answer == null || free_answer.length() < 2) {
						res.setMessage("주관식 문항에 답안을 2글자 이상으로 작성 해주세요.");
						res.setNext_url(null);
						res.setProcessing_result(true);
						return res;
					} else {
						//새로운 hire 생성
						HireResult hire_result = new HireResult();
						//문제 번호 입력
						hire_result.setQuestion_id(q.getId());
						//답안 입력
						hire_result.setOpen_answer(free_answer);

						//답안 리스트에 추가
						result_list.add(hire_result);
					}
				}
			}
			//답안 도출 종료
			
			String email_reg = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
			
			//공고 개인정보 유효성 검사
			if (hire.getGender_use() == 0 && volunteer.getGender() < 0) {
				res.setMessage("성별을 선택 해주세요.");
				res.setNext_url(null);
				res.setProcessing_result(true);
				return res;
			} else if (hire.getEmail_use() == 0 && (volunteer.getEmail() == null || !Pattern.matches(email_reg, volunteer.getEmail()) || volunteer.getEmail().length() < 1)) {
				res.setMessage("잘못된 이메일 형식입니다.");
				res.setNext_url(null);
				res.setProcessing_result(true);
				return res;
			} else if (hire.getAddress_use() == 0 && (volunteer.getPost() == null || volunteer.getPost().length() < 1 || volunteer.getAddress() == null || volunteer.getAddress().length() < 1)) {
				res.setMessage("주소와 우편번호를 입력 해주세요.");
				res.setNext_url(null);
				res.setProcessing_result(true);
				return res;
			}
			
			//핸드폰 인증 여부 체크
			if (hire.getPhone_certified() == 0) {
				if (!auth_sms.CheckUserAuthSMS(request)) {
					res.setMessage("핸드폰 인증을 진행 해주세요.");
					res.setNext_url(null);
					res.setProcessing_result(true);
					return res;
				}
				
				String phone_number = null;
				boolean auth_checked = (boolean) session.getAttribute("AuthSMSResult");
				
				if (!auth_checked) {
					res.setMessage("핸드폰 인증을 진행 해주세요.");
					res.setNext_url(null);
					res.setProcessing_result(true);
					return res;
				} else {
					phone_number = (String)session.getAttribute("AuthSMSPhone");
				}

				
				if (!phone_number.equals(volunteer.getPhone())) {
					res.setMessage("잘못된 접근 입니다.");
					res.setNext_url(null);
					res.setProcessing_result(true);
					return res;
				}
				
			}
			
			VolunteerResult v_result = new VolunteerResult();
			v_result.setHire_id(hire.getId());
			
			//여기서 부터는 DB에 저장 하면된다. 참고로 중복 참여 제크는 해야함 (연락처 중복 여부)
			//순서 : volunteer -> hireResult -> volunteerResult
			
			Volunteer checked_volunteer = volunteer_service.getVolunteerByPhone(volunteer.getPhone());
			
			//이미 등록된 사용자인지 체크
			if (checked_volunteer == null) {
				volunteer_service.InsertVolunteer(volunteer);
			} else {
				volunteer.setId(checked_volunteer.getId());
			}
			
			
			v_result.setVolunteer_id(volunteer.getId());
			
			//해당 사용자가 공고에 참여하였는지 체크
			if (volunteer_result_service.CountResultByVolunteerResult(v_result) > 0) {
				res.setMessage("해당 공고에 이미 참여 하였습니다.");
				res.setNext_url(null);
				res.setProcessing_result(true);
				return res;
			} else {
				//미참가시 입력 시작
				//포트 폴리오 유효성 검사 및 등록
				if (hire.getPortfolio() == 0) {
					//포트폴리오 필수일 경우
					String input_name = "portfolio_files";
					List<MultipartFile> files = request.getFiles(input_name);
					
					if (files == null || files.size() == 0 || files.isEmpty()) {
						res.setMessage("포트폴리오 파일을 필수 업로드 해야합니다.\r\n최대 용량은 "+ CustomMultipartResolver.MaxUploadSize +"MB입니다.");
						res.setNext_url(null);
						res.setProcessing_result(true);
						return res;
					} else {
						List<String> file_names = InjectionFile(request, "C:\\res\\img\\custom\\hire", "portfolio_files");
						
						if (file_names == null) {
							res.setMessage("포트폴리오 파일 용량은 최대 "+ CustomMultipartResolver.MaxUploadSize +"MB입니다.");
							res.setNext_url(null);
							res.setProcessing_result(true);
							return res;
						} else {
							v_result.setFile(file_names.get(0));
						}
					}
				} else if (hire.getPortfolio() == 1) {
					List<String> file_names = InjectionFile(request, "C:\\res\\img\\custom\\hire", "portfolio_files");
					
					if (file_names == null) {
						res.setMessage("포트폴리오 파일 용량은 최대 "+ CustomMultipartResolver.MaxUploadSize +"MB입니다.");
						res.setNext_url(null);
						res.setProcessing_result(true);
						return res;
					} else {
						v_result.setFile(file_names.get(0));
					}
				}
				
				volunteer_result_service.InsertVolunteerResult(v_result);
				
				for (HireResult h : result_list) {
					h.setVolunteer_result_id(v_result.getId());
					hire_result_service.InsertHireResult(h);
				}
			}
			
			auth_sms.ClearAuthSMSLog(request);
			
			
			SMTP smtp = new SMTP();
			
			//지원 메일 관리자에게 발송
			smtp.SendMail("mt9665@naver.com", 
						  volunteer.getName() +"님이 공고에 참여하였습니다.", 
						  "참여 공고명 : <font style='color:red; font-weight:bold'>"+ hire.getTitle() +"</font><br>"+
						  "참여자 관리는 관리자페이지에서 진행할 수 있습니다.<br>" +
						  "<a href='"+ request.getScheme() +"://"+request.getServerName() +ControllerCommonMethod.admin_page_path + "/manage_hire_volunteer/survey?hire_id="+ hire.getId() +"&volunteer_id="+ volunteer.getId() +"'>"+
						  ""+ request.getScheme() +"://"+request.getServerName() +ControllerCommonMethod.admin_page_path + "/manage_hire_volunteer/survey?hire_id="+ hire.getId() +"&volunteer_id="+ volunteer.getId() +
						  "</a>");
		}
		
		
		res.setProcessing_result(true);
		return res;
	}
	
	public boolean CheckHire(Hire hire) {
		if (hire.getEnd_date().compareTo(new Date()) <= 0 && new Date().compareTo(hire.getStart_date()) < 0) {
			return false;
		} else {
			return true;
		}
	}
	
	
	public String getRemainingTime(Date end_date) {
		Calendar cal = Calendar.getInstance(new Locale("Korean", "Korea"));

		double remain_day = 0;
		int day = 0;
		int hh = 0;
		int mm = 0;
		int ss = 0;

		// 현재시간
		long l_currtime = cal.getTime().getTime();

		// 종료시간
		java.util.Date e_time = end_date;
		cal.setTime(e_time);
		java.sql.Date ee_date = new java.sql.Date(cal.getTime().getTime());
		long l_e_time = ee_date.getTime();

		// 남은시간
		double d_remain_time = (double) (l_e_time - l_currtime) / 24 / 60 / 60 / 1000;

		if (d_remain_time > 0) {
			day = (int) (d_remain_time); // 일자
			remain_day = (d_remain_time - (double) day) * 24;

			hh = (int) (remain_day); // 시간
			remain_day = (remain_day - (double) hh) * 60;

			mm = (int) (remain_day); // 분
			remain_day = (remain_day - (double) mm) * 60;

			ss = (int) (remain_day); // 초
		}
		
		String result = day + "일 " + hh + "시간 " + mm + "분 "+ ss +"초";

		return result;
	}

}
