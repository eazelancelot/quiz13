package com.example.quiz13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz13.service.ifs.FeedbackService;
import com.example.quiz13.service.ifs.QuizService;
import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.CreateReq;
import com.example.quiz13.vo.DeleteReq;
import com.example.quiz13.vo.FillinReq;
import com.example.quiz13.vo.GetQuestionsRes;
import com.example.quiz13.vo.SearchReq;
import com.example.quiz13.vo.SearchRes;
import com.example.quiz13.vo.StatisticsRes;
import com.example.quiz13.vo.UpdateReq;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
public class QuizServiceController {
	
	@Autowired
	private QuizService quizService;
	
	@Autowired
	private FeedbackService feedbackService;
	
	@PostMapping(value = "quiz/create")
	public BasicRes create(@Valid @RequestBody CreateReq req) {
		return quizService.create(req);
	}
	
	@GetMapping(value = "quiz/get_all")
	public SearchRes getAll() {
		return quizService.getAll();
	}
	
	@PostMapping(value = "quiz/get_all")
	public SearchRes getAll(@RequestBody SearchReq req) {
		return quizService.getAll(req);
	}
	
	// 呼叫此 API 的路徑: localhost:8080/quiz/get_by_quiz_id?quizId=編號
	// ?後面接著的是 key=value，key 是要 mapping 的變數名稱，value 是參數值；多個 key=value 用 & 串接
	@PostMapping(value = "quiz/get_by_quiz_id")
	public GetQuestionsRes getQuestionsByQuizId(//
			@RequestParam(value = "quizId")int quizId) {
		return quizService.getQuestionsByQuizId(quizId);
	}
	
	@PostMapping(value = "quiz/update")
	public BasicRes update(@Valid @RequestBody UpdateReq req) {
		return quizService.update(req);
	}
	
	@PostMapping(value = "quiz/delete")
	public BasicRes delete(@Valid @RequestBody DeleteReq req) {
		return quizService.delete(req);
	}
	
	@PostMapping(value = "quiz/fillin")
	public BasicRes fillin(@Valid @RequestBody FillinReq req) {
		return feedbackService.fillin(req);
	}
	
	@PostMapping(value = "quiz/statistics")
	public StatisticsRes statistics(@RequestParam(value = "quizId")int quizId) {
		return feedbackService.statistics(quizId);
	}

}
