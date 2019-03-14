package com.para.dao.landing;

import java.util.List;

import com.para.object.landing.LandingAnswer;

public interface LandingAnswerDao {
	public int InsertAnswer(LandingAnswer answer);

	public int UpdateAnswer(LandingAnswer answer);

	public int DeleteAnswer(int id);
	
	public LandingAnswer getAnswerById(int id);

	public List<LandingAnswer> getAnswerByQuestionId(int id);
	
	public List<LandingAnswer> getAnswerByChain(int id);
	
	public int ValidtaionAnswerId(int id);
}
