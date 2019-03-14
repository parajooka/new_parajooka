package com.para.object.hire;

import java.util.Date;

public class VolunteerResult {
	private int id;
	private int volunteer_id;
	private int hire_id;
	private String file;
	private int result;
	private Date insert_date = new Date();
	private Date modify_date = new Date();
	
	
	public int getVolunteer_id() {
		return volunteer_id;
	}
	public void setVolunteer_id(int volunteer_id) {
		this.volunteer_id = volunteer_id;
	}
	public Date getInsert_date() {
		return insert_date;
	}
	public void setInsert_date(Date insert_date) {
		this.insert_date = insert_date;
	}
	public Date getModify_date() {
		return modify_date;
	}
	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHire_id() {
		return hire_id;
	}
	public void setHire_id(int hire_id) {
		this.hire_id = hire_id;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int reuslt) {
		this.result = reuslt;
	}
}
