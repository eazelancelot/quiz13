package com.example.quiz13.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "feedback")
@IdClass(value = FeedbackId.class)
public class Feedback {

	@Column(name = "user_name")
	private String userName;

	@Column(name = "phone")
	private String phone;

	@Id
	@Column(name = "email")
	private String email;

	@Column(name = "age")
	private int age;

	@Id
	@Column(name = "quiz_id")
	private int quizId;

	@Id
	@Column(name = "ques_id")
	private int quesId;

	@Column(name = "answers")
	private String answers;

	@Column(name = "fillin_date")
	private LocalDate fillinDate;

	public Feedback() {
		super();
	}

	public Feedback(String userName, String phone, String email, int age, int quizId, int quesId, String answers,
			LocalDate fillinDate) {
		super();
		this.userName = userName;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.quizId = quizId;
		this.quesId = quesId;
		this.answers = answers;
		this.fillinDate = fillinDate;
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

	public int getQuizId() {
		return quizId;
	}

	public int getQuesId() {
		return quesId;
	}

	public String getAnswers() {
		return answers;
	}

	public LocalDate getFillinDate() {
		return fillinDate;
	}

}
