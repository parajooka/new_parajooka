package com.para.service.hire;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.dao.hire.HireResultDao;
import com.para.object.hire.HireResult;

@Service
public class HireResultService {
	
	@Autowired
	private HireResultDao dao;

	public int InsertHireResult(HireResult hire_result) {
		// TODO Auto-generated method stub
		return dao.InsertHireResult(hire_result);
	}

	public int UpdateHireResult(HireResult hire_result) {
		// TODO Auto-generated method stub
		return dao.UpdateHireResult(hire_result);
	}

	public int DeleteHireResult(int id) {
		// TODO Auto-generated method stub
		return DeleteHireResult(id);
	}

	public List<HireResult> getHireResultByVolunteerResult(int volunteer_result_id) {
		// TODO Auto-generated method stub
		return dao.getHireResultByVolunteerResult(volunteer_result_id);
	}

}
