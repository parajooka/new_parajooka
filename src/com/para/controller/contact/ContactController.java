package com.para.controller.contact;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.para.object.contact.Contact;
import com.para.object.landing.LandingParticipant;
import com.para.service.contact.ContactService;
import com.para.service.landing.LandingParticipantService;
import com.paraframework.common.AjaxResponse;
import com.paraframework.common.BaseController;
import com.paraframework.common.SMTP;
import com.paraframework.object.Menu;
import com.paraframework.service.MenuService;

@Controller
@RequestMapping(value="/custom/renewal/contact")
public class ContactController extends BaseController {
	@Autowired
	private ContactService contact_service;
	@Autowired
	private LandingParticipantService landing_service;
	@Autowired
	private MenuService menu_service;
	
	private static SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
	private static SimpleDateFormat formatTime2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREAN);
	private static Pattern phone_pattern = Pattern.compile("(0(2|1(0|1|6|7|8|9)|3(1|2|3)|4(1|2|3)|5(1|2|3|4|5)|6(1|2|3|4)))(\\d{3,4})(\\d{4})");
	
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response) {
		Menu target_menu = (Menu) request.getSession().getAttribute("target_menu");
		
		request.getSession().setAttribute("target_menu", menu_service.getMenuByName("Contact"));
		if (target_menu == null) {
			request.getSession().setAttribute("menu_move_cmd", true);
		} else {
			request.getSession().setAttribute("menu_move_cmd", false);
		}
		
		String today = formatTime.format(new Date());
		String date[] = today.split("-");
		
		Calendar calendar = Calendar.getInstance();
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		List<Contact> thisMonth_contact_list = contact_service.getContactByYearAndMonth(date[0] + "-" + date[1] + "-01", date[0] + "-" + date[1] + "-" + lastDay);
		
		request.setAttribute("contact_list", thisMonth_contact_list);
		request.setAttribute("today", today);
		
		try {
			if (landing_service.ValidParticipant(getIpAddress(request)) == 0) {
				request.getSession().setAttribute("contact", "already");
				return RedirectPage(request, "/custom/renewal/landing/index");
			} else {
				try {
					request.setAttribute("participant", landing_service.getParticipantByIp(getIpAddress(request)));
				} catch (Exception e) {
					// TODO: handle exception
					alertMessage("잘못된 접근입니다.", request, response);
					return null;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getRequestURI(request);
	}
	
	@RequestMapping(value="/moveCalendar", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse moveCalendar(HttpServletRequest request, HttpServletResponse response ) throws ParseException {
		AjaxResponse res = new AjaxResponse();
		
		String today = request.getParameter("moveDate");
		
		if (today == null || today.length() ==0) {
			return res.returnResponse("잘못된 날짜입니다.", "/custom/renewal/contact/index");
		} else {
			try {
				formatTime.parse(today);
			} catch (Exception e) {
				// TODO: handle exception
				return res.returnResponse("잘못된 날짜입니다.", "/custom/renewal/contact/index");
			}
		}
		
		String date[] = today.split("-");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(formatTime.parse(today));
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		List<Contact> thisMonth_contact_list = contact_service.getContactByYearAndMonth(date[0] + "-" + date[1] + "-01", date[0] + "-" + date[1] + "-" + lastDay);
		
		res.setObject(thisMonth_contact_list);
		
		res.setProcessing_result(true);
		return res;
	}
	
	@RequestMapping(value="/call", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse getContactByDate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxResponse res = new AjaxResponse();
		
		String call_date = request.getParameter("call_date");
		
		if (call_date == null || call_date.length() == 0) {
			return res.returnResponse("잘못된 값이 입력되었습니다.", "/custom/renewal/contact/index");
		}
		
		try {
			formatTime.parse(call_date);
		} catch (Exception e) {
			// TODO: handle exception
			return res.returnResponse("잘못된 값이 입력되었습니다.", "/custom/renewal/contact/index");
		}
		
		Contact contact = contact_service.getContactByDate(call_date);
		
		if (contact != null) {
			LandingParticipant participant = contact.getParticipant();
			res.setObject(participant);
		}
		
		res.setProcessing_result(true);
		return res;
	}
	
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse InsertContact(HttpServletRequest request, HttpServletResponse response, @Valid Contact contact, BindingResult result) throws Exception {
		AjaxResponse res = new AjaxResponse();
		
		if (!res.validation_data(result, "/custom/renewal/contact/index", "미팅이 정상적으로 예약되었습니다.", res)) {
			if (contact_service.CountByParticipant(contact.getParticipant_id()) > 0) {
				res.setProcessing_result(true);
				return res.returnResponse("이미 예약하신 기록이 존재합니다.\r\n기간을 변경하시려면 기존의 예약을 수정해주세요.", null);
			}
			
			Date today = null;
			Date reservation_date = null;
			try {
				today = formatTime2.parse(formatTime2.format(new Date()));
				reservation_date = formatTime2.parse(contact.getReservation_date());
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return res.returnResponse("잘못된 값이 입력되었습니다.", "/custom/renewal/contact/index");
			}
			
			if (today.compareTo(reservation_date) > 0) {
				return res.returnResponse("현재 시간보다 이른시간으로 예약 할 수 없습니다.", null);
			} else if (contact_service.getContactByDate(contact.getReservation_date()) != null) {
				return res.returnResponse("해당 날짜에는 이미 미팅예약이 존재합니다.", null);
			}
			
			contact_service.InsertContact(contact);
			
			SMTP smtp = new SMTP();
			smtp.SendMail("mt9665@naver.com", "Para&Jooka 컨텍트 미팅예약이 신규등록 되었습니다.", "등록일시 : "+ formatTime2.format(new Date()) + "<br>관리자 페이지에서 확인바랍니다.");
		}
		
		res.setProcessing_result(true);
		return res;
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse UpdateContact(HttpServletRequest request, HttpServletResponse response, @Valid Contact contact, BindingResult result) throws Exception {
		AjaxResponse res = new AjaxResponse();
		
		if (!res.validation_data(result, "/custom/renewal/contact/index", "미팅예약이 정상적으로 수정되었습니다.", res)) {
			Contact orign_conatct = contact_service.getContactByParticipant(contact.getParticipant_id());
			
			if (orign_conatct.getReservation_pw().equals(contact.getReservation_pw())) {
				Date today = null;
				Date reservation_date = null;
				try {
					today = formatTime2.parse(formatTime2.format(new Date()));
					reservation_date = formatTime2.parse(contact.getReservation_date());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return res.returnResponse("잘못된 값이 입력되었습니다.", "/custom/renewal/contact/index");
				}
				
				if (today.compareTo(reservation_date) > 0) {
					return res.returnResponse("현재 시간보다 이른시간으로 예약 할 수 없습니다.", null);
				}
				
				contact.setContact_id(orign_conatct.getContact_id());
				contact_service.UpdateContact(contact);
			} else {
				res.setProcessing_result(true);
				return res.returnResponse("연럭처가 일치하지 않습니다.\r\n예약시에 작성한 연락처를 입력해주세요.", null);
			}
			
		}
		
		res.setProcessing_result(true);
		return res;
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse UpdateContact(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse res = new AjaxResponse();
		
		String contact_id = request.getParameter("contact_id");
		String password = request.getParameter("password");
		
		Matcher m = phone_pattern.matcher(password);
		
		if (contact_id == null || contact_id.length() == 0) {
			res.setProcessing_result(true);
			return res.returnResponse("잘못된 접근입니다.", "/custom/renewal/contact/index");
		} else if (password == null || password.length() == 0 || !m.find()) {
			res.setProcessing_result(true);
			return res.returnResponse("미팅예약을 삭제하려면 연락처를 입력해주세요.", null);
		}
		
		try {
			Contact contact = contact_service.getContactById(Integer.parseInt(contact_id));
			
			if (contact.getReservation_pw().equals(password)) {
				contact_service.DeleteContact(contact.getContact_id());
				res.setMessage("예약이 정상적으로 취소 되었습니다.");
				res.setNext_url("/custom/renewal/contact/index");
			} else {
				return res.returnResponse("연락처가 일치하지 않습니다.\r\n예약시에 작성한 연락처를 입력해주세요.", null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			res.setProcessing_result(true);
			return res.returnResponse("존재하지않거나 삭제된 미팅예약입니다.", "/custom/renewal/contact/index");
		}
		
		res.setProcessing_result(true);
		return res;
	}
	
	@RequestMapping(value="/checkPW", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse CheckPassword(HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse res = new AjaxResponse();
		
		String participant_id = request.getParameter("participant_id");
		String password = request.getParameter("password");
		
		Matcher m = phone_pattern.matcher(password);
		
		if (participant_id == null || participant_id.length() == 0) {
			return res.returnResponse("잘못된 값이 입력되었습니다.", "/custom/renewal/contact/index");
		} else if (password == null || password.length() == 0 || !m.find()) {
			return res.returnResponse("예약시에 작성한 연락처를 입력해주세요.", null);
		}
		
		try {
			Contact contact = contact_service.getContactByParticipant(Integer.parseInt(participant_id));
			
			if (contact.getReservation_pw().equals(password)) {
				Map<String, Object> obj = new HashMap<>();
				obj.put("participant", contact.getParticipant());
				obj.put("contact", contact);
				res.setObject(obj);
			} else {
				res.setObject(null);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			return res.returnResponse("잘못된 값이 입력되었습니다.", "/custom/renewal/contact/index");
		}
		
		return res;
	}
	
	@RequestMapping(value="/immediately", method=RequestMethod.POST)
	public @ResponseBody AjaxResponse Immediately(HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxResponse res = new AjaxResponse();
		
		LandingParticipant participant = landing_service.getParticipantByIp(getIpAddress(request));
		
		if (participant == null) {
			return res.returnResponse("Contact 질문 참여 기록이 존재하지않습니다.\r\n문의를 위해서는 먼저 질문에 모두 답변해야합니다.", "/custom/renewal/contact/index");
		}

		String contents = request.getParameter("immediately_contents");
		
		if (contents == null || contents.replaceAll(" ", "").length() == 0) {
			return res.returnResponse("내용은 공백을 제외하고 1글자 이상 입력되어야합니다.", null);
		}
		
		String mail_title = "[Para&Jooka] 새로운 '즉시 미팅'이 예약되었습니다.";
		String msg = "신청자 : " + participant.getName() + "<br>"+
					 "소속 : " + participant.getCompany() + "<br><br>"+
					 "<신청자 메모><br>"+
					 ""+ contents;
		
		SMTP smtp = new SMTP();
		smtp.SendMail("mt9665@naver.com", mail_title, msg);
		
		
		return res.returnResponse("'즉시 미팅'신청 메일이 발송되었습니다.\r\n담당자가 확인후 남겨주신 연락처로 직접 연락드리겠습니다.", "/custom/renewal/contact/index");
	}
}
