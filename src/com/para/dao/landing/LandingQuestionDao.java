package com.para.dao.landing;

import java.util.List;

import com.para.object.landing.LandingQuestion;
import com.paraframework.common.PageUtil;

public interface LandingQuestionDao {
	public int InsertQuestion(LandingQuestion question);

	public int UpdateQuestion(LandingQuestion question);

	public int DeleteQuestion(int id);

	public int CountQuestion();
	
	public int ValidationQuestion(int id);
	
	public LandingQuestion getQuestionById(int id);

	public List<LandingQuestion> getQuestionByAnswerId(int answer_id); // chain테이블에서 Join필요

	public List<LandingQuestion> getQuestionByPaging(PageUtil paging);
	
	public List<LandingQuestion> getParentQuestionById(int question_id);
}
