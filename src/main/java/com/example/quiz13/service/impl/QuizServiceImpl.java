package com.example.quiz13.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.quiz13.constants.QuesType;
import com.example.quiz13.constants.ResMessage;
import com.example.quiz13.dao.QuestionDao;
import com.example.quiz13.dao.QuizDao;
import com.example.quiz13.entity.Question;
import com.example.quiz13.entity.Quiz;
import com.example.quiz13.service.ifs.QuizService;
import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.CreateReq;
import com.example.quiz13.vo.DeleteReq;
import com.example.quiz13.vo.GetQuestionsRes;
import com.example.quiz13.vo.SearchReq;
import com.example.quiz13.vo.SearchRes;
import com.example.quiz13.vo.UpdateReq;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;

	// @Transactional �u�����~�o�ͦb RuntimeException(�Ψ�l���O)�ɤ~�|�N��Ʀ^��
	// rollbackFor = Exception.class �O�N��Ʀ^�Ҽh�ū��w��u�n�o�� Exception �ɡA�N�|�N��Ʀ^��
	// �]�� Exception �O�Ҧ� XXXException �������O�A�u�n�O�o�� XXXException �ɡA��Ʀ^�ҳ��|����
	// rollbackFor: import �� library �O org.springframework....
	// rollbackOn: import �� library �O jakarta....
	@Transactional(rollbackFor = Exception.class)
	@Override
	public BasicRes create(CreateReq req) {
		// �Ѽ��ˬd: req ���ѼƤw�z�L @Valid ���򥻪��ˬd
		// �}�l�ɶ�����񵲧��ɶ���
		if (req.getStartDate().isAfter(req.getEndDate())) {
			return new BasicRes(ResMessage.PARAM_DATE_ERROR.getCode(), //
					ResMessage.PARAM_DATE_ERROR.getMessage());
		}
		// �ˬd���D���e
		BasicRes checkRes = checkQuestions(req.getQuestionList());
		if (checkRes != null) {
			return checkRes;
		}
		try {
			// �N��Ƽg�i DB
			// �N��Ƽg�i quiz
			quizDao.insert(req.getName(), req.getDescription(), req.getStartDate(), //
					req.getEndDate(), req.isPublished());
			// ���o�̷s�@����ƪ� quiz_id
			int quizId = quizDao.selectMaxQuizId();
			// �N��Ƽg�i question
			for (Question item : req.getQuestionList()) {
				questionDao.insert(quizId, item.getQuesId(), item.getName(), item.getType(), //
						item.isMust(), item.getOptions());
			}
		} catch (Exception e) {
			// ���~�n��(throw) �X�h�A@Transactional �~�|�ͮ�(�N��Ʀ^��)
			throw e;
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	private BasicRes checkQuestions(List<Question> list) {
		for (Question item : list) {
			// �ˬd question type
			if (!QuesType.checkType(item.getType())) {
				return new BasicRes(ResMessage.QUES_TYPE_ERROR.getCode(), //
						ResMessage.QUES_TYPE_ERROR.getMessage());
			}
			// �ư�: type ���O text(���νƿ�)�ɡA�ﶵ�S����
			if (!item.getType().equalsIgnoreCase(QuesType.TEXT.getType())//
					&& !StringUtils.hasText(item.getOptions())) {
				return new BasicRes(ResMessage.PARAM_OPTIONS_ERROR.getCode(), //
						ResMessage.PARAM_OPTIONS_ERROR.getMessage());
			}
			// �ư�: type �O text �ɡA�ﶵ����
			if (item.getType().equalsIgnoreCase(QuesType.TEXT.getType()) //
					&& StringUtils.hasText(item.getOptions())) {
				return new BasicRes(ResMessage.PARAM_OPTIONS_ERROR.getCode(), //
						ResMessage.PARAM_OPTIONS_ERROR.getMessage());
			}
			// �ݨ��� type = text�Aoptions �S��ơA�ҥH���ΰ�����򪺿ﶵ�Ӽ��ˬd
			if (item.getType().equalsIgnoreCase(QuesType.TEXT.getType())) {
				return null;
			}
			// �ˬd���νƿ��D�A�ﶵ�ܤ֭n��2��
			// �N�ﶵ�r���ন List<String>: �n���T�w���Ыذݨ��ɡA�e�ݪ��h�ӿﶵ�O�}�C�A�B�ϥ� Stringify �ন�r�ꫬ�A
			// �e�ݿﶵ�쥻�榡(�}�C): ["1:aa","2:bb", "3:cc"] �� ["aa","bb", "cc"]
			ObjectMapper mapper = new ObjectMapper();
			try {
				List<String> optionsList = mapper.readValue(item.getOptions(), new TypeReference<>() {
				});
				if (item.getType().equalsIgnoreCase(QuesType.SINGLE.getType()) //
						|| item.getType().equalsIgnoreCase(QuesType.MULTI.getType())) {
					if (optionsList.size() < 2) {
						return new BasicRes(ResMessage.OPTIONS_SIZE_ERROR.getCode(), //
								ResMessage.OPTIONS_SIZE_ERROR.getMessage());
					}
				}
			} catch (Exception e) {
				return new BasicRes(ResMessage.OPTIONS_PARSE_ERROR.getCode(), //
						ResMessage.OPTIONS_PARSE_ERROR.getMessage());
			}
		}
		return null;
	}

	@Override
	public SearchRes getAll() {
		List<Quiz> list = quizDao.getAll();
		return new SearchRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), list);
	}

	@Override
	public GetQuestionsRes getQuestionsByQuizId(int quizId) {
		if (quizId <= 0) {
			return new GetQuestionsRes(ResMessage.PARAM_QUIZ_ID_ERROR.getCode(), //
					ResMessage.PARAM_QUIZ_ID_ERROR.getMessage());
		}
		List<Question> list = questionDao.getByQuizId(quizId);
		return new GetQuestionsRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), list);
	}

	// �]�� update ����k�]�t�F���ާ@�A�ҥH�n�N�o�� Dao ���ާ@���P�@�����欰
	@Transactional(rollbackFor = Exception.class)
	@Override
	public BasicRes update(UpdateReq req) {
		// �n�ˬd quiz �� id �O�_�s�b�A�]���̫�O���N���D����ƧR����A�s�W�A�Ӥ��O�Χ�s�y�k
		// ���ϥΧ�s�y�k���Ҷq��]�O�A��s�e��5�D�A��s���ܦ�4�D�A�Y�O�Χ�s�y�k�A�쥻����5�D�ٷ|�s�b
		// �] quiz �� id ���s�b�A���D�R���ɸ�Ƥ��|�ܧ�A�����۷s�W�ɴN�|�s�W��ơA�B�o�Ǹ�ƨS�������� quiz_id
		int quizId = req.getId();
		int count = quizDao.selectCountById(quizId);
		if(count != 1) {
			return new BasicRes(ResMessage.ID_NOT_FOUND.getCode(), //
					ResMessage.ID_NOT_FOUND.getMessage());
		}
		// �}�l�ɶ�����񵲧��ɶ���
		if (req.getStartDate().isAfter(req.getEndDate())) {
			return new BasicRes(ResMessage.PARAM_DATE_ERROR.getCode(), //
					ResMessage.PARAM_DATE_ERROR.getMessage());
		}
		// �ˬd���D���e
		BasicRes checkRes = checkQuestions(req.getQuestionList());
		if (checkRes != null) {
			return checkRes;
		}
		
		// �ˬd Question ���� quizId �O�_�P Quiz �� id �Ȥ@��
		for(Question item : req.getQuestionList()){
			if(item.getQuizId() != quizId) {
				return new BasicRes(ResMessage.QUIZ_ID_MISMATCH.getCode(), //
						ResMessage.QUIZ_ID_MISMATCH.getMessage());
			}
		}
		try {
			// 1. ��s�ݨ�
			quizDao.updateById(quizId, req.getName(), req.getDescription(), //
					req.getStartDate(), req.getEndDate(), req.isPublished());
			// 2. �R���P�i�ݨ����Ҧ����D
			questionDao.deleteByQuizId(quizId);
			// 3. �s�W���D
			for (Question item : req.getQuestionList()) {
				questionDao.insert(quizId, item.getQuesId(), item.getName(), item.getType(), //
						item.isMust(), item.getOptions());
			}
		} catch (Exception e) {
			throw e;
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	@Override
	public SearchRes getAll(SearchReq req) {
		// �ഫ��
		String quizName = req.getQuizName();
		LocalDate startDate = req.getStartDate();
		LocalDate endDate = req.getEndDate();
		if(!StringUtils.hasText(quizName)) {
			// �N quizName = null �άO quizName = ���ťզr��ɡA�ഫ���Ŧr��
			quizName = "";
		}
		// startDate �S���a�Ȫ�ܤ��O���󤧤@�A���]�� SQL ���y�k�A�i�H�� startDate �������ܦ����ɶ�
		if(startDate == null) {
			startDate = LocalDate.of(1970, 1, 1);
		}
		// endDate �S���a�Ȫ�ܤ��O���󤧤@�A���]�� SQL ���y�k�A�i�H�� endDate �������ܤ[���᪺�ɶ�
		if(endDate == null) {
			endDate = LocalDate.of(2999, 12, 31);
		}
		List<Quiz> list = quizDao.getAll(quizName, startDate, endDate);
		return new SearchRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), list);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BasicRes delete(DeleteReq req) {
		try {
			quizDao.delete(req.getQuizIdList());
			questionDao.delete(req.getQuizIdList());
		} catch (Exception e) {
			throw e;
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

}
