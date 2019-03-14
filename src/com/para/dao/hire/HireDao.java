package com.para.dao.hire;

import java.util.Date;
import java.util.List;

import com.para.object.hire.Hire;
import com.paraframework.common.PageUtil;

public interface HireDao {
	public int InsertHire(Hire hire);
	
	public int UpdateHire(Hire hire);
	
	public int DeleteHire(int id);
	
	public int CountAllHire();
	
	public int CountHireByProceeding(Date date);
	
	public int CountHireByuser(Date date);
	
	public int CountHireByExpected(Date date);
	
	public Hire getHireById(int id);
	
	public List<Hire> getHireByPage(PageUtil page);
	
	public List<Hire> getHireByProceeding(Date date);
	
	public List<Hire> getHireByUser(Date date);
	
	public List<Hire> getHireByExpected(Date date);
	
	public List<Hire> getHire();
}
