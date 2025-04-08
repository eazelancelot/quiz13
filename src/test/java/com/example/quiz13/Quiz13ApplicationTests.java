package com.example.quiz13;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.quiz13.dao.FeedbackDao;
import com.example.quiz13.dao.QuizDao;
import com.example.quiz13.entity.Question;
import com.example.quiz13.service.ifs.FeedbackService;
import com.example.quiz13.service.ifs.QuizService;
import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.CreateReq;
import com.example.quiz13.vo.FeedbackDto;
import com.example.quiz13.vo.SearchReq;
import com.example.quiz13.vo.SearchRes;
import com.fasterxml.jackson.databind.ObjectMapper;

//@SpringBootTest
class Quiz13ApplicationTests {

	@Autowired
	private QuizDao quizDao;
	
	@Autowired
	private QuizService quizService;
	
	@Autowired
	private FeedbackDao feedbackDao;
	
	@Autowired
	private FeedbackService feedbackService;
	
	@Test
	void contextLoads() {
		int res = quizDao.selectMaxQuizId();
		System.out.println(res);
	}
	
	@Test
	public void insertTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String str = mapper.writeValueAsString(List.of("AAA", "BBB", "CCC"));
		
		List<Question> list = new ArrayList<>();
		list.add(new Question(1,1,"A","single",true, str));
		LocalDate date = LocalDate.of(2025, 3, 27);
		LocalDate endDate = LocalDate.of(2025, 3, 28);
		CreateReq req = new CreateReq("AA","AAA",date,endDate,true,list);
		BasicRes res = quizService.create(req);
		System.out.println(res.getCode() + res.getMessage());

	}
	
	@Test
	public void searchTest() {
		SearchReq req = new SearchReq(null, null, null);
		SearchRes res = quizService.getAll(req);
		System.out.println(res.getQuizList().size());
	}
	
	@Test
	public void listTest() {
		List<String> list = new ArrayList<>();
		List<String> strList = List.of("1:AAA", "2:BBB");
		for(String item : strList) {
			String[] strArray = item.split(":");
			list.addAll(List.of(strArray));
		}
		System.out.println(list.size());
		List<String> strList1 = List.of("CCC", "DDD");
		for(String item : strList1) {
			String[] strArray = item.split(":");
			list.addAll(List.of(strArray));
		}
		System.out.println(list.size());
		System.out.println(list.contains("AAA"));
		System.out.println(list.contains("AA"));
		System.out.println(list.contains("C"));
		System.out.println("=========================");
	}
	
	@Test
	public void joinTest() {
		List<FeedbackDto> res = feedbackDao.selectFeedback(1);
		System.out.println(res.size());
		System.out.println("======================");
	}
	
	@Test
	public void listTest1() {
		List<String> list = new ArrayList<>(List.of("AAA", "BBB", "CCC", "DDD"));
		List<String> strList = new ArrayList<>(List.of("AAA", "AAA", "BBB", "CCC", "DDD", "BBB"));
		int size = strList.size();
		Map<String, Integer> map = new HashMap<>();
		for(String item : list) {
			strList.removeAll(List.of(item));
			int itemSize = size - strList.size();
			map.put(item, itemSize);
			size = size - itemSize;
		}
		System.out.println(map);
	}
	
	@Test
	public void statisticsTest() {
		
	}

}
