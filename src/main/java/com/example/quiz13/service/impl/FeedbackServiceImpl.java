package com.example.quiz13.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.quiz13.constants.QuesType;
import com.example.quiz13.constants.ResMessage;
import com.example.quiz13.dao.FeedbackDao;
import com.example.quiz13.dao.QuestionDao;
import com.example.quiz13.dao.QuizDao;
import com.example.quiz13.entity.Feedback;
import com.example.quiz13.entity.Question;
import com.example.quiz13.service.ifs.FeedbackService;
import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.FeedbackDto;
import com.example.quiz13.vo.FeedbackRes;
import com.example.quiz13.vo.FeedbackVo;
import com.example.quiz13.vo.FillinReq;
import com.example.quiz13.vo.OptionCountVo;
import com.example.quiz13.vo.QuesAnswerVo;
import com.example.quiz13.vo.QuesIdAnswerVo;
import com.example.quiz13.vo.StatisticsRes;
import com.example.quiz13.vo.StatisticsVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private FeedbackDao feedbackDao;

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;

	@Override
	public BasicRes fillin(FillinReq req) {
		int quizId = req.getQuizId();
		// 1. �ˬd quiz
		// �O�_�w�o���H�η�e���ɶ��O�_����}�l�ɶ��M�����ɶ���
		int count = quizDao.selectCountById(quizId, LocalDate.now());
		if (count != 1) {
			return new BasicRes(ResMessage.OUT_OF_FILLIN_DATE_RANGE.getCode(), //
					ResMessage.OUT_OF_FILLIN_DATE_RANGE.getMessage());
		}
		// 2. �ˬd�^�X���
		// �P�@�� email �u���g�P�@�i�ݨ��@��
		count = feedbackDao.selectCount(quizId, req.getEmail());
		if (count != 0) { // 0 ��ܦP�@�� email �S����g�L�P�@�i�ݨ�
			return new BasicRes(ResMessage.EMAIL_DUPLICATED.getCode(), //
					ResMessage.EMAIL_DUPLICATED.getMessage());
		}
		// 3. �ˬd����
		List<QuesIdAnswerVo> quesIdAnswerList = req.getQuesIdAnswerList();
		// 3.1 �qDB�����D
		List<Question> questionList = questionDao.getByQuizId(quizId);
		// 3.2 �����D�ﶵ�P����

		for (Question quesItem : questionList) {
			int quesId = quesItem.getQuesId();
			String type = quesItem.getType();
			boolean must = quesItem.isMust();
			List<String> optionsList = new ArrayList<>();
			try {
				// �u�ഫ type �D text ���A�]�� text �S���ﶵ�i�H�ഫ
				if (!type.equalsIgnoreCase(QuesType.TEXT.getType())) {
					optionsList = mapper.readValue(quesItem.getOptions(), new TypeReference<>() {
					});
				}
			} catch (Exception e) {
				return new BasicRes(ResMessage.OPTIONS_PARSE_ERROR.getCode(), //
						ResMessage.OPTIONS_PARSE_ERROR.getMessage());
			}
			for (QuesIdAnswerVo voItem : quesIdAnswerList) {
				List<String> answerList = new ArrayList<>();
				if (quesId == voItem.getQuesId()) {
					answerList = voItem.getAnswers();
					// �g�L�W���� if �P�_���AanswerList �i��|�����סA�]�i��S������(�]���S����)
					// �ư�: ����B�S������
					if (must && answerList.isEmpty()) { // must ���P�� must == true
						return new BasicRes(ResMessage.ANSWER_REQUIRED.getCode(), //
								ResMessage.ANSWER_REQUIRED.getMessage());
					}
					// ���L type �O text ���A�]���S���ﶵ�i�H���
					if (type.equalsIgnoreCase(QuesType.TEXT.getType())) {
						continue;
					}
					// ���ɵ��פ��঳�h��
					if (type.equalsIgnoreCase(QuesType.SINGLE.getType()) && answerList.size() > 1) {
						return new BasicRes(ResMessage.ONE_OPTION_IS_ALLOWED.getCode(), //
								ResMessage.ONE_OPTION_IS_ALLOWED.getMessage());
					}
					// ���P�@�D�����׬O�_���b�ﶵ��
					boolean checkRes = checkAnswer(optionsList, answerList);
					if (!checkRes) {
						return new BasicRes(ResMessage.ANSWER_OPTION_MISMATCH.getCode(), //
								ResMessage.ANSWER_OPTION_MISMATCH.getMessage());
					}
				}
			}
		}
		// �s���
		for (QuesIdAnswerVo voItem : quesIdAnswerList) {
			// �N List<String> answerList �ഫ�� String
			try {
				String answerStr = mapper.writeValueAsString(voItem.getAnswers());
				feedbackDao.insert(quizId, voItem.getQuesId(), req.getName(), req.getPhone(), //
						req.getEmail(), req.getAge(), answerStr);
			} catch (JsonProcessingException e) {
				return new BasicRes(ResMessage.ANSWER_PARSE_ERROR.getCode(), //
						ResMessage.ANSWER_PARSE_ERROR.getMessage());
			} catch (Exception e) {
				throw e;
			}
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	private boolean checkAnswer(List<String> optionsList, List<String> answerList) {
		// ���걵���r��
		List<String> newOptionsList = new ArrayList<>();
		for (String item : optionsList) {
			String[] strArray = item.split(":");
			newOptionsList.addAll(List.of(strArray));
		}
		// ���ﶵ�M����
		for (String answer : answerList) {
			// ���]�@�ر��p�A optionsList = ["A", "B", "C", "D"], answerList = ["A", "a"]
			// �Y�U�誺 if �O�S����ĸ����P�_���A if(optionsList.contains(answer))�A�B�^�� true
			// �� answerList ���Ĥ@�ӵ��� A �P�_��N�|�^�� true�A�᭱���� a �N�|�S�P�_��
			if (!optionsList.contains(answer)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public FeedbackRes feedback(int quizId) {
		// �ˬd�Ѽ�
		if (quizId <= 0) {
			return new FeedbackRes(ResMessage.PARAM_QUIZ_ID_ERROR.getCode(), //
					ResMessage.PARAM_QUIZ_ID_ERROR.getMessage());
		}
		// �����
		List<FeedbackDto> res = feedbackDao.selectFeedback(quizId);
		// ��z���: �z�L�ۦP�� email ����D�M�^����z�b�@�_
		// Map<String, List<QuesAnswerVo>> ���� key �O�� email
		Map<String, List<QuesAnswerVo>> map = new HashMap<>();
		String quizName = "";
		String description = "";
		for (FeedbackDto dto : res) {
			quizName = dto.getQuizName();
			description = dto.getDescription();
			String email = dto.getEmail();
			List<QuesAnswerVo> voList = new ArrayList<>();
			if (map.containsKey(email)) {
				// �Y map ���w�s�b�ۦP�� key(email)�A�N�q map ��������� value ���X
				voList = map.get(email);
			}
			QuesAnswerVo vo = new QuesAnswerVo();
			vo.setQuesId(dto.getQuesId());
			vo.setQuesName(dto.getQuesName());
			try {
				// �N DB ���� answer String �ন List<String>
				List<String> answerList = mapper.readValue(dto.getAnswer(), new TypeReference<>() {
				});
				vo.setAnswers(answerList);
			} catch (Exception e) {
				return new FeedbackRes(ResMessage.ANSWER_PARSE_ERROR.getCode(), //
						ResMessage.ANSWER_PARSE_ERROR.getMessage());
			}
			voList.add(vo);
			map.put(email, voList);
		}

		List<FeedbackVo> feedbackList = new ArrayList<>();
		a: for (FeedbackDto dto : res) {
			String email = dto.getEmail();
			for (FeedbackVo vo : feedbackList) {
				if (email.equalsIgnoreCase(vo.getEmail())) {
					continue a;
				}
			}
			FeedbackVo feedbackVo = new FeedbackVo();
			feedbackVo.setUserName(dto.getUserName());
			feedbackVo.setPhone(dto.getPhone());
			feedbackVo.setEmail(dto.getEmail());
			feedbackVo.setAge(dto.getAge());
			feedbackVo.setFillinDate(dto.getFillinDate());
			feedbackVo.setQuesAnswerList(map.get(dto.getEmail()));
			feedbackList.add(feedbackVo);
		}
		return new FeedbackRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), quizName, description, feedbackList);
	}

	@Override
	public StatisticsRes statistics(int quizId) {
		// �q question ��ﶵ: �������q���ק�D�n�w���i��|���Y�ӿﶵ���S�H��
		List<Question> questionList = questionDao.getByQuizId(quizId);
		List<StatisticsVo> statisticsVoList = new ArrayList<>();
		// Map<���D�s��, �h�ӿﶵ>
		Map<Integer, List<String>> quesIdOptionsMap = new HashMap<>();
		for (Question item : questionList) {
			// �N DB ���� question ������T�]�w�� StatisticsVo ���A���ư��έp
			StatisticsVo vo = new StatisticsVo(item.getQuesId(), item.getName(), //
					item.getType(), item.isMust());
			statisticsVoList.add(vo);
			// ��X�C�@�D�D text ���ﶵ
			if (!item.getType().equalsIgnoreCase(QuesType.TEXT.getType())) {
				// �N�ﶵ�r���ഫ�� List<String>
				try {
					List<String> optionList = mapper.readValue(item.getOptions(), new TypeReference<>() {
					});
					quesIdOptionsMap.put(item.getQuesId(), optionList);
				} catch (Exception e) {
					return new StatisticsRes(ResMessage.OPTIONS_PARSE_ERROR.getCode(), //
							ResMessage.OPTIONS_PARSE_ERROR.getMessage());
				}
			} else {
				quesIdOptionsMap.put(item.getQuesId(), null);
			}
		}
		Map<Integer, List<String>> quesIdAnswersMap = getAnswers(quizId);
		if (quesIdAnswersMap == null) {
			return new StatisticsRes(ResMessage.ANSWER_PARSE_ERROR.getCode(), //
					ResMessage.ANSWER_PARSE_ERROR.getMessage());
		}
		// �p��ﶵ���^������
		Map<Integer, List<OptionCountVo>> map = computeAnswerCount(quesIdOptionsMap, quesIdAnswersMap);
		for (StatisticsVo vo : statisticsVoList) {
			int quesId = vo.getQuesId();
			vo.setOptionCountVoList(map.get(quesId));
		}
		return new StatisticsRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage(), statisticsVoList);
	}

	private Map<Integer, List<OptionCountVo>> computeAnswerCount(Map<Integer, List<String>> quesIdOptionsMap, //
			Map<Integer, List<String>> quesIdAnswersMap) {
		// Map<���D�s��, List<OptionCountVo>>
		Map<Integer, List<OptionCountVo>> map = new HashMap<>();
		for (Entry<Integer, List<String>> mapItem : quesIdOptionsMap.entrySet()) {
			List<OptionCountVo> optionCountVoList = new ArrayList<>();
			int quesId = mapItem.getKey();
			List<String> optionsList = mapItem.getValue();
			// ���ץ]�t�F: 1. ��諸�ﶵ 2. �h�諸�ﶵ 3. ²�����e
			if (quesIdAnswersMap.containsKey(quesId) && optionsList != null) {
				List<String> answersList = quesIdAnswersMap.get(quesId);
				int size = answersList.size();
				for (String option : optionsList) {
					answersList.removeAll(List.of(option));
					int optionSize = size - answersList.size();
					OptionCountVo vo = new OptionCountVo(option, optionSize);
					// ���� answersList size
					size = size - optionSize;
					optionCountVoList.add(vo);
				}
			}
			map.put(quesId, optionCountVoList);
		}
		return map;
	}

	private Map<Integer, List<String>> getAnswers(int quizId) {
		// ������
		List<Feedback> feedbackList = feedbackDao.selectByQuizId(quizId);
		// Map<���D�s��, �h�ӵ���>
		Map<Integer, List<String>> quesIdAnswersMap = new HashMap<>();
		for (Feedback item : feedbackList) {
			if (StringUtils.hasText(item.getAnswers())) {
				int quesId = item.getQuesId();
				// �N���צr���ഫ�� List<String>
				try {
					List<String> answersList = mapper.readValue(item.getAnswers(), new TypeReference<>() {
					});
					if (quesIdAnswersMap.containsKey(quesId)) {
						// ���X�w�s�b map ���� answer List
						List<String> list = quesIdAnswersMap.get(quesId);
						// ���� List �ۥ[
						list.addAll(answersList);
						// �⵲�G��^�� map ��
						quesIdAnswersMap.put(quesId, list);
					} else {
						quesIdAnswersMap.put(quesId, answersList);
					}
				} catch (Exception e) {
					return null;
				}
			}
		}
		return quesIdAnswersMap;
	}

}
