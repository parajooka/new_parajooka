package com.para.service.hire;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.dao.hire.VolunteerResultDao;
import com.para.object.hire.Volunteer;
import com.para.object.hire.VolunteerResult;

@Service
public class VolunteerResultService {
	
	@Autowired
	VolunteerResultDao dao;

	public int InsertVolunteerResult(VolunteerResult volunteer_result) {
		// TODO Auto-generated method stub
		return dao.InsertVolunteerResult(volunteer_result);
	}

	public int UpdateVolunteerResult(VolunteerResult volunteer_result) {
		// TODO Auto-generated method stub
		return dao.UpdateVolunteerResult(volunteer_result);
	}

	public int DeleteVolunteerResult(int id) {
		// TODO Auto-generated method stub
		return dao.DeleteVolunteerResult(id);
	}

	public List<VolunteerResult> getVolunteerRerultsByVolunteerId(int id) {
		// TODO Auto-generated method stub
		return dao.getVolunteerRerultsByVolunteerId(id);
	}

	public VolunteerResult getVolunteerResultById(int id) {
		// TODO Auto-generated method stub
		return dao.getVolunteerResultById(id);
	}
	
	public int CountResultByVolunteerResult(VolunteerResult volunteer_result) {
		return dao.CountResultByVolunteerResult(volunteer_result);
	}
	
	public List<Volunteer> getVolunteerByHireId(int id) {
		return dao.getVolunteerByHireId(id);
	}
	
	public VolunteerResult getVolunteerResultByHireAndVolunteeer(VolunteerResult volunteer_result) {
		// TODO Auto-generated method stub
		return dao.getVolunteerResultByHireAndVolunteeer(volunteer_result);
	}
	
	public int CountVolunteer(int hire_id) {
		return dao.CountVolunteer(hire_id);
	}

}
