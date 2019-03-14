package com.para.object.hire;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Volunteer {
	private int id;
	
	@NotNull (message="이름을 입력 해주세요.")
	@Pattern(regexp="^[가-힣]*$", message="이름에는 한글만 입력 가능합니다.")
	@Size(min=2, message="이름은 최소 2글자 이상입니다.")
	private String name;
	
	@NotNull (message="연락처를 입력 해주세요.")
	@Pattern(regexp="^01(?:0|1[6-9])(?:\\d{3}|\\d{4})\\d{4}$", message="잘못된 연락처 형식입니다.")
	private String phone;
	
	private int gender;
	private String email;
	private String address;
	private String post;
	
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public String toString() {
		return "Volunteer [id=" + id + ", name=" + name + ", phone=" + phone + ", gender=" + gender + ", email=" + email
				+ ", address=" + address + ", post=" + post + "]";
	}
	
	
	
	
}
