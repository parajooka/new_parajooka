package com.para.dao.landing;

import com.para.object.landing.LandingQuestionChain;

public interface LandingQuestionChainDao {
	public int InsertChain(LandingQuestionChain chain);

	public int UpdateChain(LandingQuestionChain chain);

	public int DeleteChain(int id);

	public LandingQuestionChain getChainById(int id);
}
