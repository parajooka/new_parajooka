
package com.para.object.hire;

public class HireResult {
	private int id;
	private int question_id; //공고에 해당하는 문제
	private int answer_id; //객관식일 경우의 답변
	private int volunteer_result_id;
	private String open_answer;//주관식일 경우의 답변
	
	
	
	public int getVolunteer_result_id() {
		return volunteer_result_id;
	}
	public void setVolunteer_result_id(int volunteer_result_id) {
		this.volunteer_result_id = volunteer_result_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	public int getAnswer_id() {
		return answer_id;
	}
	public void setAnswer_id(int answer_id) {
		this.answer_id = answer_id;
	}
	public String getOpen_answer() {
		return open_answer;
	}
	public void setOpen_answer(String open_answer) {
		this.open_answer = open_answer;
	}
	
	
}
