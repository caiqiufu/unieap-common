package com.unieap.base.vo;

import java.util.List;
import java.util.Map;

import com.unieap.base.inf.transform.InfFieldVO;

public class InfConfigVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private java.lang.Integer id;
	private String infCode;
	private String infName;
	private String sourceAppName;
	private String destAppName;
	private String url;
	private String SOAPAction;
	private String transformMessageHandler;
	private String resultType;
	private String extParameters;
	private String className;
	private String activateFlag;
	private String accessName;
	private String password;
	private String errorCodeIgnore;
	private String successCode;
	private byte[] responseSample;
	private java.lang.Integer timeout;
	private Map<String,List<NumberRouteVO>> numberRoute;
	private List<NumberFilterVO> numberFilterList;
	private List<NumberRouteVO> numberRouteList;
	private List<InfConfigNSVO> NSList;
	private Map<String,InfFieldVO> fieldList;
	private Map<String, String> bizTransformMessageHandler;
	private boolean isRouted = false;
	public Map<String, InfFieldVO> getFieldList() {
		return fieldList;
	}
	public void setFieldList(Map<String, InfFieldVO> fieldList) {
		this.fieldList = fieldList;
	}

	
	public List<InfConfigNSVO> getNSList() {
		return NSList;
	}
	public void setNSList(List<InfConfigNSVO> nSList) {
		NSList = nSList;
	}
	public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	
	public String getInfCode() {
		return infCode;
	}
	public void setInfCode(String infCode) {
		this.infCode = infCode;
	}
	public String getInfName() {
		return infName;
	}
	public void setInfName(String infName) {
		this.infName = infName;
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
	
	public String getTransformMessageHandler() {
		return transformMessageHandler;
	}
	public void setTransformMessageHandler(String transformMessageHandler) {
		this.transformMessageHandler = transformMessageHandler;
	}
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
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
	
	public String getSourceAppName() {
		return sourceAppName;
	}
	public void setSourceAppName(String sourceAppName) {
		this.sourceAppName = sourceAppName;
	}
	public String getDestAppName() {
		return destAppName;
	}
	public void setDestAppName(String destAppName) {
		this.destAppName = destAppName;
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
	public List<NumberFilterVO> getNumberFilterList() {
		return numberFilterList;
	}
	public void setNumberFilterList(List<NumberFilterVO> numberFilterList) {
		this.numberFilterList = numberFilterList;
	}
	public List<NumberRouteVO> getNumberRouteList() {
		return numberRouteList;
	}
	public void setNumberRouteList(List<NumberRouteVO> numberRouteList) {
		this.numberRouteList = numberRouteList;
	}
	public boolean isRouted() {
		return isRouted;
	}
	public void setRouted(boolean isRouted) {
		this.isRouted = isRouted;
	}
	public Map<String, List<NumberRouteVO>> getNumberRoute() {
		return numberRoute;
	}
	public void setNumberRoute(Map<String, List<NumberRouteVO>> numberRoute) {
		this.numberRoute = numberRoute;
	}
	public String getSOAPAction() {
		return SOAPAction;
	}
	public void setSOAPAction(String sOAPAction) {
		SOAPAction = sOAPAction;
	}
	public Map<String, String> getBizTransformMessageHandler() {
		return bizTransformMessageHandler;
	}
	public void setBizTransformMessageHandler(Map<String, String> bizTransformMessageHandler) {
		this.bizTransformMessageHandler = bizTransformMessageHandler;
	}
	
}
