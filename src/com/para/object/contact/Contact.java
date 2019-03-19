package com.para.object.contact;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.para.object.landing.LandingParticipant;

public class Contact {
	private static SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
	private int contact_id;
	@NotNull(message="세션이 만료되었습니다. 처음부터 다시 시도해주세요.")
	@Min(value=1, message="잘못된 접근입니다. 다시 시도해주세요.")
	private int participant_id; 
	@NotNull(message="미팅 시간을 선택해주세요.")
	@Size(min=15, max=16, message="잘못된 형식의 미팅날짜입니다.")
	private String reservation_date;
	@NotNull(message="미팅 방식을 선택해주세요.")
	@Min(value=0, message="미팅 방식에 잘못된 값이 입력되었습니다.")
	@Max(value=2, message="미팅 방식에 잘못된 값이 입력되었습니다.")
	private int reservation_type;
	private String reservation_memo;
<<<<<<< HEAD
	@NotNull(message="연락처는 필수 입력사항입니다.")
	@Pattern(regexp="(0(2|1(0|1|6|7|8|9)|3(1|2|3)|4(1|2|3)|5(1|2|3|4|5)|6(1|2|3|4)))(\\d{3,4})(\\d{4})", message="연락처는 필수 입력사항입니다.\r\n정확한 연락처를 기입해주세요.")
=======
	@NotNull(message="연락처를 반드시 입력해주세요.")
	@Pattern(regexp="^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", message="잘못된 연락처 형식입니다.\r\n연락처를 반드시 입력해주세요.")
>>>>>>> branch 'master' of https://github.com/parajooka/new_parajooka
	private String reservation_pw;
	private String contact_Date = formatTime.format(new Date());
	private LandingParticipant participant;
	
	
	public LandingParticipant getParticipant() {
		return participant;
	}
	public void setParticipant(LandingParticipant participant) {
		this.participant = participant;
	}
	public int getParticipant_id() {
		return participant_id;
	}
	public void setParticipant_id(int participant_id) {
		this.participant_id = participant_id;
	}
	public int getContact_id() {
		return contact_id;
	}
	public void setContact_id(int contact_id) {
		this.contact_id = contact_id;
	}
	public String getReservation_date() {
		return reservation_date;
	}
	public void setReservation_date(String reservation_date) {
		this.reservation_date = reservation_date;
	}
	public int getReservation_type() {
		return reservation_type;
	}
	public void setReservation_type(int reservation_type) {
		this.reservation_type = reservation_type;
	}
	public String getReservation_memo() {
		return reservation_memo;
	}
	public void setReservation_memo(String reservation_memo) {
		this.reservation_memo = reservation_memo;
	}
	public String getReservation_pw() {
		return reservation_pw;
	}
	public void setReservation_pw(String reservation_pw) {
		this.reservation_pw = reservation_pw;
	}
	public String getContact_Date() {
		return contact_Date;
	}
	public void setContact_Date(String contact_Date) {
		this.contact_Date = contact_Date;
	}
	
	
}
