package com.unieap.base.vo;

public class ButtonVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private Long buttonId;
	private String buttonCode;
	private String buttonName;
	private boolean isAbled;

	public Long getButtonId() {
		return buttonId;
	}

	public void setButtonId(Long buttonId) {
		this.buttonId = buttonId;
	}

	public String getButtonCode() {
		return buttonCode;
	}

	public void setButtonCode(String buttonCode) {
		this.buttonCode = buttonCode;
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public boolean isAbled() {
		return isAbled;
	}

	public void setAbled(boolean isAbled) {
		this.isAbled = isAbled;
	}

}
