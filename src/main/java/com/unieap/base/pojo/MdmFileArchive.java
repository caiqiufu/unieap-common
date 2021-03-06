package com.unieap.base.pojo;
// Generated 2017-11-25 11:34:12 by Hibernate Tools 3.5.0.Final

import java.text.DecimalFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * MdmFileArchive generated by hbm2java
 */
@Entity
@Table(name = "mdm_file_archive", catalog = "unieap")
public class MdmFileArchive implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 16)
	private Long id;
	@Column(name = "fileName", nullable = false, length = 32)
	private String fileName;
	@Column(name = "fileType", nullable = false, length = 32)
	private String fileType;
	@Column(name = "fileSize", nullable = false)
	private Long fileSize;
	@Column(name = "filePath", nullable = false, length = 512)
	private String filePath;
	@Column(name = "url", nullable = true, length = 512)
	private String url;
	@Column(name = "extKey", nullable = false, length = 32)
	private String extKey;
	@Column(name = "fileCategory", nullable = true, length = 32)
	private String fileCategory;
	@Column(name = "archiveDate", nullable = false)
	private Date archiveDate;
	@Column(name = "remark", nullable = false, length = 512)
	private String remark;
	@Column(name = "tenantId", nullable = false)
	private Long tenantId;

	public String getFileSizeMB() {
		DecimalFormat df = new DecimalFormat(".00");
		return df.format(this.fileSize.longValue() / 1024);
	}

	public MdmFileArchive() {
	}

	public MdmFileArchive(Long id, String fileName, String fileType, String filePath, String extKey, Date archiveDate,
			Long tenantId) {
		this.id = id;
		this.fileName = fileName;
		this.fileType = fileType;
		this.filePath = filePath;
		this.extKey = extKey;
		this.archiveDate = archiveDate;
		this.tenantId = tenantId;
	}

	public MdmFileArchive(Long id, String fileName, String fileType, Long fileSize, String filePath, String url,
			String extKey, String fileCategory, Date archiveDate, String remark, Long tenantId) {
		this.id = id;
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.filePath = filePath;
		this.url = url;
		this.extKey = extKey;
		this.fileCategory = fileCategory;
		this.archiveDate = archiveDate;
		this.remark = remark;
		this.tenantId = tenantId;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getExtKey() {
		return this.extKey;
	}

	public void setExtKey(String extKey) {
		this.extKey = extKey;
	}

	public String getFileCategory() {
		return this.fileCategory;
	}

	public void setFileCategory(String fileCategory) {
		this.fileCategory = fileCategory;
	}

	public Date getArchiveDate() {
		return this.archiveDate;
	}

	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
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
