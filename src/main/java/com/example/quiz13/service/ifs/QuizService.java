package com.example.quiz13.service.ifs;

import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.CreateReq;
import com.example.quiz13.vo.DeleteReq;
import com.example.quiz13.vo.GetQuestionsRes;
import com.example.quiz13.vo.SearchReq;
import com.example.quiz13.vo.SearchRes;
import com.example.quiz13.vo.UpdateReq;

public interface QuizService {
	
	public BasicRes create(CreateReq req);
	
	public SearchRes getAll();
	
	public SearchRes getAll(SearchReq req);
	
	public GetQuestionsRes getQuestionsByQuizId(int quizId);
	
	public BasicRes update(UpdateReq req);
	
	public BasicRes delete(DeleteReq req);

}
