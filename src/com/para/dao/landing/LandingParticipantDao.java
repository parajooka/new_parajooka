package com.para.dao.landing;

import java.util.List;

import com.para.object.landing.LandingParticipant;
import com.paraframework.common.PageUtil;

public interface LandingParticipantDao {
	public int InsertParticipant(LandingParticipant participant);
	
	public int UpdateParticipant(LandingParticipant participant);
	
	public int DeleteParticipant(int id);
	
	public LandingParticipant getParticipant(int id) throws Exception;
	
	public List<LandingParticipant> getParticipantByPaging(PageUtil page) throws Exception;
	
	public int CountParticipant();
	
	public int ValidParticipant(String ip) throws Exception;
	
	public LandingParticipant getParticipantByIp(String ip) throws Exception;
}
