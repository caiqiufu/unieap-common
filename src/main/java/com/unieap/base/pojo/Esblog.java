package com.unieap.base.pojo;
// Generated 2015-9-19 22:30:44 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.unieap.base.UnieapCacheMgt;

/**
 * Esblog generated by hbm2java
 */
@Entity
@Table(name = "esb_log", catalog = "unieap")
public class Esblog implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false, length = 16)
	private Long id;
	@Column(name = "channelCode", nullable = true, length = 32)
	private String channelCode;
	@Column(name = "bizCode", nullable = true, length = 32)
	private String bizCode;
	private String bizCodeDesc;
	@Column(name = "serviceNumber", nullable = true, length = 32)
	private String serviceNumber;
	@Column(name = "transactionId", nullable = true, length = 32)
	private String transactionId;
	@Column(name = "requestTime", nullable = true, length = 32)
	private String requestTime;
	@Column(name = "responseTime", nullable = true, length = 32)
	private String responseTime;
	private String systemCode;
	@Column(name = "extTransactionId", nullable = true, length = 32)
	private String extTransactionId;
	@Lob
	@Column(name = "requestInfo", nullable = true)
	private byte[] requestInfo;
	@Lob
	@Column(name = "responseInfo", nullable = true)
	private byte[] responseInfo;
	@Column(name = "createDate", nullable = false)
	private java.sql.Timestamp createDate;
	@Column(name = "resultCode", nullable = true, length = 32)
	private String resultCode;
	@Column(name = "resultDesc", nullable = true, length = 1024)
	private String resultDesc;
	@Column(name = "executeTime", nullable = true, length = 32)
	private String executeTime;
	@Column(name = "sourceSystem", nullable = true, length = 32)
	private String sourceSystem;
	@Column(name = "destSystem", nullable = true, length = 32)
	private String destSystem;
	
	@Column(name = "createTimeEnd", nullable = true, length = 255)
	private String createTimeEnd;
	@Column(name = "createTimeStart", nullable = true, length = 255)
	private String createTimeStart;
	@Column(name = "requestTimeStart", nullable = true, length = 255)
	private String requestTimeStart;
	@Column(name = "requestTimeEnd", nullable = true, length = 255)
	private String requestTimeEnd;
	@Column(name = "processServerInfo", nullable = true, length = 1024)
	private String processServerInfo;

	public Esblog() {
	}

	public Esblog(Long id) {
		this.id = id;
	}

	public Esblog(Long id, String channelCode, String bizCode, String serviceNumber, String transactionId,
			String requestTime, String responseTime, String systemCode, String extTransactionId, byte[] requestInfo,
			byte[] responseInfo, java.sql.Timestamp createDate, String resultCode, String resultDesc, String executeTime,
			String sourceSystem, String destSystem) {
		this.id = id;
		this.channelCode = channelCode;
		this.bizCode = bizCode;
		this.serviceNumber = serviceNumber;
		this.transactionId = transactionId;
		this.requestTime = requestTime;
		this.responseTime = responseTime;
		this.systemCode = systemCode;
		this.extTransactionId = extTransactionId;
		this.requestInfo = requestInfo;
		this.responseInfo = responseInfo;
		this.createDate = createDate;
		this.resultCode = resultCode;
		this.resultDesc = resultDesc;
		this.executeTime = executeTime;
		this.sourceSystem = sourceSystem;
		this.destSystem = destSystem;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChannelCode() {
		return this.channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getBizCode() {
		return this.bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public String getBizCodeDesc() {
		bizCodeDesc = UnieapCacheMgt.getDicName("bizHandler", bizCode);
		return bizCodeDesc;
	}

	public void setBizCodeDesc(String bizCodeDesc) {
		this.bizCodeDesc = bizCodeDesc;
	}

	public String getServiceNumber() {
		return this.serviceNumber;
	}

	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getRequestTime() {
		return this.requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getResponseTime() {
		return this.responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String getSystemCode() {
		return this.systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getExtTransactionId() {
		return this.extTransactionId;
	}

	public void setExtTransactionId(String extTransactionId) {
		this.extTransactionId = extTransactionId;
	}

	public byte[] getRequestInfo() {
		return this.requestInfo;
	}

	public void setRequestInfo(byte[] requestInfo) {
		this.requestInfo = requestInfo;
	}

	public byte[] getResponseInfo() {
		return this.responseInfo;
	}

	public void setResponseInfo(byte[] responseInfo) {
		this.responseInfo = responseInfo;
	}

	public java.sql.Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(java.sql.Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getResultCode() {
		return this.resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return this.resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getDestSystem() {
		return destSystem;
	}

	public void setDestSystem(String destSystem) {
		this.destSystem = destSystem;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getRequestTimeStart() {
		return requestTimeStart;
	}

	public void setRequestTimeStart(String requestTimeStart) {
		this.requestTimeStart = requestTimeStart;
	}

	public String getRequestTimeEnd() {
		return requestTimeEnd;
	}

	public void setRequestTimeEnd(String requestTimeEnd) {
		this.requestTimeEnd = requestTimeEnd;
	}

	public String getProcessServerInfo() {
		return processServerInfo;
	}

	public void setProcessServerInfo(String processServerInfo) {
		this.processServerInfo = processServerInfo;
	}

}
