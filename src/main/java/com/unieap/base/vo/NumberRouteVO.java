package com.unieap.base.vo;

public class NumberRouteVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String bizCode;
	private String infCode;
	private String numberStart;
	private String numberEnd;
	private String extParameters;
	private String routeType;
	private String activateFlag;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBizCode() {
		return bizCode;
	}
	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}
	public String getInfCode() {
		return infCode;
	}
	public void setInfCode(String infCode) {
		this.infCode = infCode;
	}
	public String getNumberStart() {
		return numberStart;
	}
	public void setNumberStart(String numberStart) {
		this.numberStart = numberStart;
	}
	public String getNumberEnd() {
		return numberEnd;
	}
	public void setNumberEnd(String numberEnd) {
		this.numberEnd = numberEnd;
	}
	public String getExtParameters() {
		return extParameters;
	}
	public void setExtParameters(String extParameters) {
		this.extParameters = extParameters;
	}
	public String getRouteType() {
		return routeType;
	}
	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}
	public String getActivateFlag() {
		return activateFlag;
	}
	public void setActivateFlag(String activateFlag) {
		this.activateFlag = activateFlag;
	}


}
