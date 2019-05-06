package com.unieap.base.vo;

public class InfConfigNSVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private java.lang.Integer id;
	private String infCode;
	private String ns;
	private String alias;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getNs() {
		return ns;
	}
	public void setNs(String ns) {
		this.ns = ns;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
}
