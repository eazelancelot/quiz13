package com.example.quiz13.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz13.entity.Question;
import com.example.quiz13.entity.QuestionId;

@Repository
public interface QuestionDao extends JpaRepository<Question, QuestionId> {

	// SQL 語法中冒號 : 後面的 name 對應的變數名稱是 @Param 括號中的字串 name，其餘皆同
	@Modifying
	@Transactional
	@Query(value = "insert into question (quiz_id, ques_id, name, type, is_must, options) "
			+ " values (:quizId, :quesId, :quesName, :type, :must, :options)", nativeQuery = true)
	public void insert(//			
			@Param("quizId")int quizId, //
			@Param("quesId")int quesId, //
			@Param("quesName")String quesName, //
			@Param("type")String type, //
			@Param("must")boolean must, //
			@Param("options")String options);

	@Query(value = "select * from question where quiz_id = ?1", nativeQuery = true)
	public List<Question> getByQuizId(int quizId);
	
	@Modifying
	@Transactional
	@Query(value = "delect from question where quiz_id = ?1", nativeQuery = true)
	public void deleteByQuizId(int quizId);
	
	@Modifying
	@Transactional
	@Query(value = "delete from question where quiz_id in (?1)", nativeQuery = true)
	public void delete(List<Integer> quizIdList);
}
