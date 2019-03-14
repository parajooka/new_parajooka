package com.para.object.hire;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public class Hire {
	private int id;
	
	@NotNull(message="공고의 제목을 입력해주세요.")
	@Size(min=2, message="곻고의 제목은 최소 2글자 이상입니다.")
	private String title;
	
	@NotNull(message="공고의 정보를 입력해주세요.")
	@Size(min=2, message="공고의 정보는 최소 2글자 이상입니다.")
	private String info;
	
	@NotNull(message="채용인원을 입력해주세요.")
	@Min(value=0, message="채용인원 값은 최소 0(무제한)입니다.")
	@Max(value=99, message="채용인원은 최대 99명입니다.")
	private int personnel;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date start_date;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date end_date;
	
	@NotNull(message="핸드폰 본인인증 사용여부를 선택해주세요.")
	@Min(value=0, message="잘못된 접근입니다.")
	@Max(value=1, message="잘못된 접근입니다.")
	private int phone_certified;

	@NotNull(message="공고 지원후 면담 사용 여부를 선택해주세요.")
	@Min(value=0, message="잘못된 접근입니다.")
	@Max(value=1, message="잘못된 접근입니다.")
	private int interview;
	
	@NotNull(message="포트폴리의 필수 여부를 선택해주세요.")
	@Min(value=0, message="잘못된 접근입니다.")
	@Max(value=2, message="잘못된 접근입니다.")
	private int portfolio;
	
	@NotNull(message="성별 수집 여부를 선택해주세요.")
	@Min(value=0, message="잘못된 접근입니다.")
	@Max(value=2, message="잘못된 접근입니다.")
	private int gender_use;
	
	@NotNull(message="이메일 수집 여부를 선택해주세요.")
	@Min(value=0, message="잘못된 접근입니다.")
	@Max(value=2, message="잘못된 접근입니다.")
	private int email_use;
	
	@NotNull(message="주소 수집 여부를 선택해주세요.")
	@Min(value=0, message="잘못된 접근입니다.")
	@Max(value=2, message="잘못된 접근입니다.")
	private int address_use;

	@NotNull(message="공고 활성화 여부를 선택해주세요.")
	@Min(value=0, message="잘못된 접근입니다.")
	@Max(value=1, message="잘못된 접근입니다.")
	private int activation;
	
	
	
	public int getActivation() {
		return activation;
	}
	public void setActivation(int activation) {
		this.activation = activation;
	}
	public int getGender_use() {
		return gender_use;
	}
	public void setGender_use(int gender_use) {
		this.gender_use = gender_use;
	}
	public int getEmail_use() {
		return email_use;
	}
	public void setEmail_use(int email_use) {
		this.email_use = email_use;
	}
	public int getAddress_use() {
		return address_use;
	}
	public void setAddress_use(int address_use) {
		this.address_use = address_use;
	}
	public int getPortfolio() {
		return portfolio;
	}
	public void setPortfolio(int portfolio) {
		this.portfolio = portfolio;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getPersonnel() {
		return personnel;
	}
	public void setPersonnel(int personnel) {
		this.personnel = personnel;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public int getPhone_certified() {
		return phone_certified;
	}
	public void setPhone_certified(int phone_certified) {
		this.phone_certified = phone_certified;
	}
	public int getInterview() {
		return interview;
	}
	public void setInterview(int interview) {
		this.interview = interview;
	}
	
	public String getKrDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		return format.format(date);
	}
	
	public int getStatus() {
		if (new Date().compareTo(getStart_date()) < 0) {
			return 2;
		} else if (getEnd_date().compareTo(new Date()) < 0) {
			return 0;
		} else {
			return 1;
		}
	}
	
	@Override
	public String toString() {
		return "Hire [id=" + id + ", title=" + title + ", info=" + info + ", personnel=" + personnel + ", start_date="
				+ start_date + ", end_date=" + end_date + ", phone_certified=" + phone_certified + ", interview="
				+ interview;
	}
	
	
	
}
