package com.para.service.hire;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.para.dao.hire.HireDao;
import com.para.object.hire.Hire;
import com.paraframework.common.PageUtil;

@Service
public class HireService {
	
	@Autowired
	HireDao dao;

	public int InsertHire(Hire hire) {
		// TODO Auto-generated method stub
		return dao.InsertHire(hire);
	}

	public int UpdateHire(Hire hire) {
		// TODO Auto-generated method stub
		return dao.UpdateHire(hire);
	}

	public int DeleteHire(int id) {
		// TODO Auto-generated method stub
		return dao.DeleteHire(id);
	}

	public Hire getHireById(int id) {
		// TODO Auto-generated method stub
		return dao.getHireById(id);
	}

	public List<Hire> getHireByPage(PageUtil page) {
		// TODO Auto-generated method stub
		return dao.getHireByPage(page);
	}

	public List<Hire> getHireByProceeding(Date date) {
		// TODO Auto-generated method stub
		return dao.getHireByProceeding(date);
	}

	public int CountAllHire() {
		// TODO Auto-generated method stub
		return dao.CountAllHire();
	}

	public int CountHireByProceeding(Date date) {
		// TODO Auto-generated method stub
		return dao.CountHireByProceeding(date);
	}
	
	public List<Hire> getHireByExpected(Date date) {
		return dao.getHireByExpected(date);
	}
	
	public int CountHireByExpected(Date date) {
		return dao.CountHireByExpected(date);
	}

	public List<Hire> getHireByUser(Date date) {
		return dao.getHireByUser(date);
	}
	
	public int CountHireByuser(Date date) {
		return dao.CountHireByuser(date);
	}
	
	public List<Hire> getHire() {
		return dao.getHire();
	}

}
