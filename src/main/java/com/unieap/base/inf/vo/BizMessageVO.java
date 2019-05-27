package com.unieap.base.inf.vo;

import java.util.Map;

import com.unieap.base.vo.BaseVO;

public class BizMessageVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String bizCode;
	public BizFieldVO rootFieldVO;
	public Map<String,BizFieldVO> fieldList;
	public String getBizCode() {
		return bizCode;
	}
	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}
	public BizFieldVO getRootFieldVO() {
		return rootFieldVO;
	}
	public void setRootFieldVO(BizFieldVO rootFieldVO) {
		this.rootFieldVO = rootFieldVO;
	}
	public Map<String, BizFieldVO> getFieldList() {
		return fieldList;
	}
	public void setFieldList(Map<String, BizFieldVO> fieldList) {
		this.fieldList = fieldList;
	}	
}
