package com.unieap.base.inf.element;

public class RequestBody {
	public String bizCode = "";
	public String serviceNumber = "";
	public String extParameters = "";
	public String getServiceNumber() {
		return serviceNumber;
	}
	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}
	public String getExtParameters() {
		return extParameters;
	}
	public void setExtParameters(String extParameters) {
		this.extParameters = extParameters;
	}
	public String getBizCode() {
		return bizCode;
	}
	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}
	
}
