package com.unieap.base.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicGroupVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String groupCode;
	private String groupName;
	private String activateFlag;
	private String activateFlagDesc;
	private List<DicDataVO> dataList = new ArrayList<DicDataVO>();
	private Map<String,DicDataVO> dataMap = new HashMap<String,DicDataVO>();;

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getActivateFlag() {
		return activateFlag;
	}

	public void setActivateFlag(String activateFlag) {
		this.activateFlag = activateFlag;
	}

	public String getActivateFlagDesc() {
		return activateFlagDesc;
	}

	public void setActivateFlagDesc(String activateFlagDesc) {
		this.activateFlagDesc = activateFlagDesc;
	}

	public List<DicDataVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<DicDataVO> dataList) {
		this.dataList = dataList;
	}

	public Map<String, DicDataVO> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, DicDataVO> dataMap) {
		this.dataMap = dataMap;
	}

}
