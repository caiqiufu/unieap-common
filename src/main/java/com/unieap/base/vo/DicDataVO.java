package com.unieap.base.vo;

import org.springframework.util.StringUtils;

import com.unieap.base.UnieapConstants;

public class DicDataVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String groupCode;
	private String groupName;
	private String dicCode;
	private String dicName;
	private Integer seq;
	private String activateFlag;
	private String activateFlagDesc;
	private String attr1;
	private String attr2;
	private String attr3;
	private String attr4;
	private String attr5;

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getDicCode() {
		return dicCode;
	}

	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}

	public String getDicName() {
		return dicName;
	}

	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getActivateFlag() {
		return activateFlag;
	}

	public void setActivateFlag(String activateFlag) {
		this.activateFlag = activateFlag;
	}

	public String getActivateFlagDesc() {
		if (!StringUtils.isEmpty(this.activateFlag)) {
			this.activateFlagDesc = UnieapConstants.getDicName("activateFlag", activateFlag);
		}
		return activateFlagDesc;
	}

	public void setActivateFlagDesc(String activateFlagDesc) {
		this.activateFlagDesc = activateFlagDesc;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getAttr1() {
		return attr1;
	}

	public String getAttr2() {
		return attr2;
	}

	public String getAttr3() {
		return attr3;
	}

	public String getAttr4() {
		return attr4;
	}

	public String getAttr5() {
		return attr5;
	}

	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}

	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}

	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}

	public void setAttr4(String attr4) {
		this.attr4 = attr4;
	}

	public void setAttr5(String attr5) {
		this.attr5 = attr5;
	}
	
}
