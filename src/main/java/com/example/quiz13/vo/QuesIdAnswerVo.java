package com.example.quiz13.vo;

import java.util.List;

public class QuesIdAnswerVo {
	// 參數不用驗證，因為有可能會這一題是簡答且是非必填的，加上驗證反而會出錯

	private int quesId;

	private List<String> answers;

	public QuesIdAnswerVo() {
		super();
	}

	public QuesIdAnswerVo(int quesId, List<String> answers) {
		super();
		this.quesId = quesId;
		this.answers = answers;
	}

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

}
