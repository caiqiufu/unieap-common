package com.unieap.base.pojo;
// default package
// Generated 2015-8-8 16:02:23 by Hibernate Tools 4.3.1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * ExLog generated by hbm2java
 */
@Entity
@Table(name = "mdm_file_process_details", catalog = "unieap")
public class MdmFileProcessDetails implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false, length = 16)
	private Long id;
	@Column(name = "processId", unique = true, nullable = false, length = 16)
	private Long processId;
	@Column(name = "rowNumber", unique = true, nullable = false, length = 16)
	private Long rowNumber;
	@Column(name = "originalContent", nullable = false, length = 4000)
	private String originalContent;
	@Column(name = "resultCode", nullable = false, length = 512)
	private String resultCode;
	@Column(name = "resultDesc", nullable = false, length = 45)
	private String resultDesc;
	@Column(name = "createDate", nullable = false)
	private Date createDate;
	@Column(name = "createBy", nullable = false, length = 32)
	private String createBy;
	@Column(name = "remark", nullable = false, length = 512)
	private String remark;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProcessId() {
		return processId;
	}
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	public Long getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(Long rowNumber) {
		this.rowNumber = rowNumber;
	}
	public String getOriginalContent() {
		return originalContent;
	}
	public void setOriginalContent(String originalContent) {
		this.originalContent = originalContent;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
