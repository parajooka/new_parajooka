package com.para.dao.hire;



import java.util.List;

import com.para.object.hire.Question;
import com.paraframework.common.PageUtil;

public interface QuestionDao {
	public int InsertQuestion(Question question);
	
	public int UpdateQuestion(Question question);
	
	public int DeleteQuestion(int id);
	
	public int getCount();
	
	public List<Question> getQuestionByPage(PageUtil page);
	
	public Question getQuestionById(int id);
}
