package com.example.quiz13.vo;

public class OptionCountVo {

	private String option;

	private int count;

	public OptionCountVo() {
		super();
	}

	public OptionCountVo(String option, int count) {
		super();
		this.option = option;
		this.count = count;
	}

	public String getOption() {
		return option;
	}

	public int getCount() {
		return count;
	}

}
