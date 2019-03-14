package com.para.object.hire;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Question {
	private int id;
	@NotNull
	@Min(value=0, message="잘못된 접근입니다.")
	@Max(value=1, message="잘못된 접근입니다.")
	private int type;
	
	@NotNull
	@Size(min=2, max=500, message="문항은 최소 2글자에서 최대 500글자입니다.")
	private String content;

	@Size(min=0, max=1000, message="문항은 최대 1000글자입니다.")
	private String example;
	
	private List<Answer> answers;
	
	public List<Answer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getExample() {
		return example;
	}
	@Override
	public String toString() {
		return "Question [id=" + id + ", type=" + type + ", content=" + content + ", example=" + example + "]";
	}
	public void setExample(String example) {
		this.example = example;
	}
	
	
}
