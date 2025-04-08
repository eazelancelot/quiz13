package com.example.quiz13.vo;

import java.time.LocalDate;

// DTO(Data Transfer Object)
// DTO: �R�W�� DTO ����]�O�o�Ǹ�ƥD�n�O�q��Ʈw�ӡA�B�L�k�u�Τ@�� Entity �˸��A�@��O�� join ���h�i�������
// VO: �]�i�H�R�W�� VO(Value Object): �ΨӸ˸�Ʈe�����κ�
// Entity: �]�O�˸�ƪ��e���A�� Entity �i�H mapping ���ƪ�Ψ������
public class FeedbackDto {

	private int quizId;

	private String quizName;

	private String description;

	private String userName;

	private String phone;

	private String email;

	private int age;

	private int quesId;

	private String quesName;

	private String answer;

	private LocalDate fillinDate;

	public FeedbackDto() {
		super();
	}

	public FeedbackDto(int quizId, String quizName, String description, String userName, String phone, String email,
			int age, int quesId, String quesName, String answer, LocalDate fillinDate) {
		super();
		this.quizId = quizId;
		this.quizName = quizName;
		this.description = description;
		this.userName = userName;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.quesId = quesId;
		this.quesName = quesName;
		this.answer = answer;
		this.fillinDate = fillinDate;
	}

	public int getQuizId() {
		return quizId;
	}

	public String getQuizName() {
		return quizName;
	}

	public String getDescription() {
		return description;
	}

	public String getUserName() {
		return userName;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public int getAge() {
		return age;
	}

	public int getQuesId() {
		return quesId;
	}

	public String getQuesName() {
		return quesName;
	}

	public String getAnswer() {
		return answer;
	}

	public LocalDate getFillinDate() {
		return fillinDate;
	}

}
