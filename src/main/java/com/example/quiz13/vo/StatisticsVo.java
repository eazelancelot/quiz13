package com.example.quiz13.vo;

import java.util.List;

public class StatisticsVo {

	private int quesId;

	private String quesName;

	private String type;

	private boolean must;

	private List<OptionCountVo> optionCountVoList;

	public StatisticsVo() {
		super();
	}

	public StatisticsVo(int quesId, String quesName, String type, boolean must, List<OptionCountVo> optionCountVoList) {
		super();
		this.quesId = quesId;
		this.quesName = quesName;
		this.type = type;
		this.must = must;
		this.optionCountVoList = optionCountVoList;
	}

	public StatisticsVo(int quesId, String quesName, String type, boolean must) {
		super();
		this.quesId = quesId;
		this.quesName = quesName;
		this.type = type;
		this.must = must;
	}

	public int getQuesId() {
		return quesId;
	}

	public String getQuesName() {
		return quesName;
	}

	public String getType() {
		return type;
	}

	public boolean isMust() {
		return must;
	}

	public List<OptionCountVo> getOptionCountVoList() {
		return optionCountVoList;
	}

	public void setOptionCountVoList(List<OptionCountVo> optionCountVoList) {
		this.optionCountVoList = optionCountVoList;
	}

}
