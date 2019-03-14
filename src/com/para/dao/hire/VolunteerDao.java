package com.para.dao.hire;

import com.para.object.hire.Volunteer;

public interface VolunteerDao {
	public int InsertVolunteer(Volunteer volunteer);
	
	public int UpdateVolunteer(Volunteer volunteer);
	
	public int DeleteVolunteer(int id);
	
	public Volunteer getVolunteerById(int id);
	
	public Volunteer getVolunteerByPhone(String phone);
	
	public int CountVolunteerByPhone(String phone);
}
