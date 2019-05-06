package com.unieap.base.pojo;
// Generated 2017-11-25 11:34:12 by Hibernate Tools 3.5.0.Final

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * MdmOperlog generated by hbm2java
 */
@Entity
@Table(name = "mdm_operlog", catalog = "unieap")
public class MdmOperlog implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false, length = 16)
	private Long id;
	@Column(name = "operatorId", nullable = false, length = 32)
	private String operatorId;
	@Column(name = "operatorName", nullable = false, length = 32)
	private String operatorName;
	@Column(name = "appName", nullable = false, length = 32)
	private String appName;
	@Column(name = "moduleName", nullable = true, length = 32)
	private String moduleName;
	@Column(name = "bizCode", nullable = true, length = 32)
	private String bizCode;
	@Column(name = "bizDesc", nullable = true, length = 255)
	private String bizDesc;
	@Column(name = "operDate", nullable = false)
	private Date operDate;
	@Column(name = "operObj", nullable = false)
	private String operObj;
	@Column(name = "operType", nullable = false)
	private String operType;
	@Column(name = "operDesc", nullable = true, length = 255)
	private String operDesc;
	@Column(name = "displayName", nullable = true, length = 32)
	private String displayName;
	@Column(name = "fieldName", nullable = true, length = 32)
	private String fieldName;
	@Column(name = "oldValue", nullable = true, length = 32)
	private String oldValue;
	@Column(name = "newValue", nullable = true, length = 32)
	private String newValue;
	@Column(name = "recordId", nullable = true, length = 32)
	private String recordId;
	@Column(name = "remark", nullable = true, length = 255)
	private String remark;
	@Column(name = "tenantId", nullable = false, length = 16)
	private Long tenantId;

	public MdmOperlog() {
	}

	public MdmOperlog(Long id, String operatorId, String operatorName, String appName, Date operDate, String operObj,
			String operType, String recordId, Long tenantId) {
		this.id = id;
		this.operatorId = operatorId;
		this.operatorName = operatorName;
		this.appName = appName;
		this.operDate = operDate;
		this.operObj = operObj;
		this.operType = operType;
		this.recordId = recordId;
		this.tenantId = tenantId;
	}

	public MdmOperlog(Long id, String operatorId, String operatorName, String appName, String moduleName, String bizCode,
			String bizDesc, Date operDate, String operObj, String operType, String operDesc, String displayName,
			String fieldName, String oldValue, String newValue, String recordId, String remark, Long tenantId) {
		this.id = id;
		this.operatorId = operatorId;
		this.operatorName = operatorName;
		this.appName = appName;
		this.moduleName = moduleName;
		this.bizCode = bizCode;
		this.bizDesc = bizDesc;
		this.operDate = operDate;
		this.operObj = operObj;
		this.operType = operType;
		this.operDesc = operDesc;
		this.displayName = displayName;
		this.fieldName = fieldName;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.recordId = recordId;
		this.remark = remark;
		this.tenantId = tenantId;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOperatorId() {
		return this.operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return this.operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getBizCode() {
		return this.bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public String getBizDesc() {
		return this.bizDesc;
	}

	public void setBizDesc(String bizDesc) {
		this.bizDesc = bizDesc;
	}

	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public String getOperObj() {
		return this.operObj;
	}

	public void setOperObj(String operObj) {
		this.operObj = operObj;
	}

	public String getOperType() {
		return this.operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getOperDesc() {
		return this.operDesc;
	}

	public void setOperDesc(String operDesc) {
		this.operDesc = operDesc;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getOldValue() {
		return this.oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return this.newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getRecordId() {
		return this.recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getTenantId() {
		return this.tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

}