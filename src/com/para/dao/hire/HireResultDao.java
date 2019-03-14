package com.para.dao.hire;

import java.util.List;

import com.para.object.hire.HireResult;

public interface HireResultDao {
	public int InsertHireResult(HireResult hire_result);
	
	public int UpdateHireResult(HireResult hire_result);
	
	public int DeleteHireResult(int id);
	
	public List<HireResult> getHireResultByVolunteerResult(int volunteer_result_id);
}
