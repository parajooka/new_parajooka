package com.para.object.hire;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Answer {
	private int id;
	private int question_id;
	
	@NotNull
	@Size(min=2, max=1000, message="보기의 문항은 최소 2글자에서 최대 1000글자입니다.")
	private String content;
	
	@NotNull
	@Min(value=0, message="잘못된 접근입니다.")
	@Max(value=0, message="잘못된 접근입니다.")
	private int reply;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuestion_id() {
		return question_id;
	}
	@Override
	public String toString() {
		return "Answer [id=" + id + ", question_id=" + question_id + ", content=" + content + ", reply=" + reply + "]";
	}
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getReply() {
		return reply;
	}
	public void setReply(int reply) {
		this.reply = reply;
	}
	
	
}
