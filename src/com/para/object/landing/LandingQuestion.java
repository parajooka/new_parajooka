package com.para.object.landing;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LandingQuestion {
	private int question_id;
	@NotNull(message="문항은 필수 입력사항입니다.")
	@Size(min=1, message="문항은 최소 1글자 이상 입력해야합니다.")
	private String question_title;
	private String question_contents;
	
	public int getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}
	public String getQuestion_title() {
		return question_title;
	}
	public void setQuestion_title(String question_title) {
		this.question_title = question_title;
	}
	public String getQuestion_contents() {
		return question_contents;
	}
	public void setQuestion_contents(String question_contents) {
		this.question_contents = question_contents;
	}
	@Override
	public String toString() {
		return "LandingQuestion [question_id=" + question_id + ", question_title=" + question_title
				+ ", question_contents=" + question_contents + "]";
	}
}
