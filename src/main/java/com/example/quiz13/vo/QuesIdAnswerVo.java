package com.example.quiz13.vo;

import java.util.List;

public class QuesIdAnswerVo {
	// �ѼƤ������ҡA�]�����i��|�o�@�D�O²���B�O�D���񪺡A�[�W���ҤϦӷ|�X��

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
