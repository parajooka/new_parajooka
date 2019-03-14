package com.paraframework.object;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AccessIp {
	private int id;
	
	@NotNull(message="아이피 주소는 필수 입력 사항입니다.")
	@Pattern(regexp="^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$", message="아이피 주소 양식이 잘못되었습니다.")
	private String access_ip;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String auth_date = sdf.format(new Date());
	
	@NotNull(message="허용권한 부여자는 필수 입력 사항입니다.")
	@Pattern(regexp="^[가-힣]*$", message="이름에는 한글만 입력가능합니다.")
	private String authorizationer;
	
	
	@Override
	public String toString() {
		return "AccessIp [id=" + id + ", access_ip=" + access_ip + ", sdf=" + sdf + ", auth_date=" + auth_date
				+ ", authorizationer=" + authorizationer + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccess_ip() {
		return access_ip;
	}
	public void setAccess_ip(String access_ip) {
		this.access_ip = access_ip;
	}
	public String getAuthorizationer() {
		return authorizationer;
	}
	public void setAuthorizationer(String authorizationer) {
		this.authorizationer = authorizationer;
	}
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}
	public String getAuth_date() {
		return auth_date;
	}
	public void setAuth_date(String auth_date) {
		this.auth_date = auth_date;
	}
	
	
	

}
