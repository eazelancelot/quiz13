package com.example.quiz13.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class QuestionId implements Serializable {

	private int quesId;

	private int quizId;

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

}
