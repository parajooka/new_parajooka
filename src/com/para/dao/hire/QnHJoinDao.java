package com.para.dao.hire;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.para.object.hire.QnHJoin;
import com.para.object.hire.Question;

public interface QnHJoinDao {
	public int InsertQnH(QnHJoin qnh);
	
	public int UpdateQnH(QnHJoin qnh);
	
	public int DeleteQnH(@Param("hire_id") int hire_id, @Param("question_id") int question_id);
	
	public List<Question> getQuestionByQnH(int id);
	
	public int countQnH(@Param("hire_id") int hire_id, @Param("question_id") int question_id);
}
