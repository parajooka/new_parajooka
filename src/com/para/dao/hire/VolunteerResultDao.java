package com.para.dao.hire;

import java.util.List;

import com.para.object.hire.Volunteer;
import com.para.object.hire.VolunteerResult;

public interface VolunteerResultDao {
	public int InsertVolunteerResult(VolunteerResult volunteer_result);
	
	public int UpdateVolunteerResult(VolunteerResult volunteer_result);
	
	public int DeleteVolunteerResult(int id);
	
	public List<VolunteerResult> getVolunteerRerultsByVolunteerId(int id);
	
	public VolunteerResult getVolunteerResultById(int id);
	
	public VolunteerResult getVolunteerResultByHireAndVolunteeer(VolunteerResult volunteer_result);

	public int CountResultByVolunteerResult(VolunteerResult volunteer_result);

	public List<Volunteer> getVolunteerByHireId(int id);

	public int CountVolunteer(int hire_id);
	
}
