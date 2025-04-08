package com.example.quiz13.vo;

import java.util.List;

import com.example.quiz13.entity.Question;

public class GetQuestionsRes extends BasicRes {

	private List<Question> questionList;

	public GetQuestionsRes() {
		super();
	}

	public GetQuestionsRes(int code, String message) {
		super(code, message);
	}

	public GetQuestionsRes(int code, String message, List<Question> questionList) {
		super(code, message);
		this.questionList = questionList;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

}
