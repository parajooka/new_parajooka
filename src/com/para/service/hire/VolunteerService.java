package com.para.service.hire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.dao.hire.VolunteerDao;
import com.para.object.hire.Volunteer;

@Service
public class VolunteerService {

	@Autowired
	private VolunteerDao dao;
	
	public int InsertVolunteer(Volunteer volunteer) {
		// TODO Auto-generated method stub
		return dao.InsertVolunteer(volunteer);
	}

	public int UpdateVolunteer(Volunteer volunteer) {
		// TODO Auto-generated method stub
		return dao.UpdateVolunteer(volunteer);
	}

	public int DeleteVolunteer(int id) {
		// TODO Auto-generated method stub
		return dao.DeleteVolunteer(id);
	}

	public Volunteer getVolunteerById(int id) {
		// TODO Auto-generated method stub
		return dao.getVolunteerById(id);
	}

	public Volunteer getVolunteerByPhone(String phone) {
		// TODO Auto-generated method stub
		return dao.getVolunteerByPhone(phone);
	}

	public int CountVolunteerByPhone(String phone) {
		// TODO Auto-generated method stub
		return dao.CountVolunteerByPhone(phone);
	}

}
