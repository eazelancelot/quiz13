package com.example.quiz13.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz13.entity.Feedback;
import com.example.quiz13.entity.FeedbackId;
import com.example.quiz13.vo.FeedbackDto;

@Repository
public interface FeedbackDao extends JpaRepository<Feedback, FeedbackId>{
	
	@Query(value = "select count(quiz_id) from feedback where quiz_id = ?1 and email = ?2", 
			nativeQuery = true)
	public int selectCount(int quizId, String email);
	
	
	@Modifying
	@Transactional
	@Query(value = "insert into feedback (user_name, phone, email, age, quiz_id, ques_id, "
			+ " answers, fillin_date) values "
			+ " (:name, :phone, :email, :age, :quizId, :quesId, :answer, now())", 
			nativeQuery = true)
	public void insert(//
			@Param("quizId") int quizId, //
			@Param("quesId") int quesId,//
			@Param("name") String name, //
			@Param("phone") String phone, //
			@Param("email") String email, //
			@Param("age") int age, //
			@Param("answer") String answer //
			);
	
	
	/**
	 * 1. join �|���h�i��A�ҥH�˸�ƪ��e���L�k�u�Τ@�� entity �Ӹ˸��A�u��Ыطs���e��(Dto)</br>
	 * 2. �s�إߪ� Dto �S���Q spring boot �U��(�b���O�W�[�W Annotation)�A�ҥH�n�z�L new �ӷs�ءA�B�n��������|</br>
	 * 3. �]���O�z�L new �ӳЫ� Dto�A�ҥH nativeQuery �N�o�n�ܦ� false</br>
	 * 4. nativeQuery = false �ɡASQL �y�k���A���W�r�n�ܦ� Entity class ���W�١A���W�ٴN�|�O�ݩ��ܼƦW��
	 */
	@Query(value = "select new com.example.quiz13.vo.FeedbackDto("
			+ " Qz.id, Qz.name, Qz.description, F.userName, F.phone, F.email, F.age, " //
			+ " Qu.quesId, Qu.name, F.answers, F.fillinDate)" //
			+ " from Quiz as Qz " //
			+ " join Question as Qu on Qz.id = Qu.quizId " //
			+ " join Feedback as F on Qz.id = F.quizId where Qz.id = ?1 and Qu.quesId = F.quesId", 
			nativeQuery = false)
	public List<FeedbackDto> selectFeedback(int quizId);
	
	@Query(value = "select * from feedback where quiz_id = ?1", 
			nativeQuery = true)
	public List<Feedback> selectByQuizId(int quizId);

}
