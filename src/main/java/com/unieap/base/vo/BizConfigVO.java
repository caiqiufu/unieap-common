package com.unieap.base.vo;

import java.util.List;

public class BizConfigVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private java.lang.Integer id;
	private String bizCode;
	private String bizDesc;
	private String appName;
	private String className;
	private String SOAPAction;
	private String transformMessageHandler;
	private String url;
	private String extParameters;
	private String activateFlag;
	private String accessName;
	private String password;
	private String errorCodeIgnore;
	private String successCode;
	private byte[] responseSample;
	private java.lang.Integer timeout;
	private List<InfConfigVO> dependInfCodeList;

	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public String getBizDesc() {
		return bizDesc;
	}

	public void setBizDesc(String bizDesc) {
		this.bizDesc = bizDesc;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getTransformMessageHandler() {
		return transformMessageHandler;
	}

	public void setTransformMessageHandler(String transformMessageHandler) {
		this.transformMessageHandler = transformMessageHandler;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public java.lang.Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(java.lang.Integer timeout) {
		this.timeout = timeout;
	}

	public String getActivateFlag() {
		return activateFlag;
	}

	public void setActivateFlag(String activateFlag) {
		this.activateFlag = activateFlag;
	}

	public String getErrorCodeIgnore() {
		return errorCodeIgnore;
	}

	public void setErrorCodeIgnore(String errorCodeIgnore) {
		this.errorCodeIgnore = errorCodeIgnore;
	}

	public String getSuccessCode() {
		return successCode;
	}

	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}

	public byte[] getResponseSample() {
		return responseSample;
	}

	public void setResponseSample(byte[] responseSample) {
		this.responseSample = responseSample;
	}

	public String getAccessName() {
		return accessName;
	}

	public void setAccessName(String accessName) {
		this.accessName = accessName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getExtParameters() {
		return extParameters;
	}

	public void setExtParameters(String extParameters) {
		this.extParameters = extParameters;
	}

	public List<InfConfigVO> getDependInfCodeList() {
		return dependInfCodeList;
	}

	public void setDependInfCodeList(List<InfConfigVO> dependInfCodeList) {
		this.dependInfCodeList = dependInfCodeList;
	}

	public String getSOAPAction() {
		return SOAPAction;
	}

	public void setSOAPAction(String sOAPAction) {
		SOAPAction = sOAPAction;
	}

}
