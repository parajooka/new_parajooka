package com.para.dao.hire;


import java.util.List;

import com.para.object.hire.Answer;

public interface AnswerDao {
	public int InsertAnswer(Answer answer);
	
	public int UpdateAnswer(Answer answer);
	
	public int DeleteAnswer(int id);

	public List<Answer> getAnswerByQuestionId(int id);
	
	public int CountAnswerById(int id);
}
