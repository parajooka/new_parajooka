package com.paraframework.object;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Homepage {
	private int id;
	
	@NotNull
	@Size(min=2, max=20, message="홈페이지명은 최소 2자 ~ 최대 20자 입니다.")
	private String name;
	
	//필수
	private String favicon;
	
	@NotNull
	@Size(min=1, message="메인 페이지 내부 주소를 입력해주세요.")
	private String main_url;
	
	@NotNull
	@Size(min=2, max=150, message="홈페이지 설명을 최소 2자 ~ 최대 150자로 입력해주세요.")
	private String info;
	
	private int mobile_use;
	
	//필수
	private String logo;
	
	private String mail;
	private String phone;
	private String fax;
	private String address;
	private String representative;
	
	
	
	public int getMobile_use() {
		return mobile_use;
	}
	public void setMobile_use(int mobile_use) {
		this.mobile_use = mobile_use;
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
	
	public String getFavicon() {
		return favicon;
	}
	
	public void setFavicon(String favicon) {
		this.favicon = favicon;
	}
	
	public String getMain_url() {
		return main_url;
	}
	
	public void setMain_url(String main_url) {
		this.main_url = main_url;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	
	public String getLogo() {
		return logo;
	}
	
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public String getMail() {
		return mail;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getFax() {
		return fax;
	}
	
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getRepresentative() {
		return representative;
	}
	
	public void setRepresentative(String representative) {
		this.representative = representative;
	}
	
	
}
