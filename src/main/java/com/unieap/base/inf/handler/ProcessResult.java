package com.unieap.base.inf.handler;

import java.util.HashMap;
import java.util.Map;

public class ProcessResult {
	public String resultCode = "";
	public String resultDesc = "";
	public Map<String, Object> extParameters = new HashMap<String, Object>();
	public Object vo;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		if (resultDesc == null) {
			resultDesc = "system error";
		}
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public Object getVo() {
		return vo;
	}

	public void setVo(Object vo) {
		this.vo = vo;
	}

	public Map<String, Object> getExtParameters() {
		return extParameters;
	}

	public void setExtParameters(Map<String, Object> extParameters) {
		this.extParameters = extParameters;
	}

}
