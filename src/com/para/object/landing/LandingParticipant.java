package com.para.object.landing;

public class LandingParticipant {
	private int participant_id;
	private String name;
	private String company;
	private String ip_address;
	private String record;
	private String start_time;
	private String end_time;
	
	
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public int getParticipant_id() {
		return participant_id;
	}
	public void setParticipant_id(int participant_id) {
		this.participant_id = participant_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	public String getRecord() {
		return record;
	}
	public void setRecord(String record) {
		this.record = record;
	}
	@Override
	public String toString() {
		return "LandingParticipant [participant_id=" + participant_id + ", name=" + name + ", company=" + company
				+ ", ip_address=" + ip_address + ", record=" + record + ", start_time=" + start_time + ", end_time="
				+ end_time + "]";
	}
	
}
