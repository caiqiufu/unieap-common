package com.unieap.base.file.vo;

public class FileVO {
	private String fileName;
	private String fileType;
	private Double fileSize;
	private String filePath;
	private String url;
	private Object records;
	public String getFileName() {
		return fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public Double getFileSize() {
		return fileSize;
	}
	public String getFilePath() {
		return filePath;
	}
	public String getUrl() {
		return url;
	}
	public Object getRecords() {
		return records;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public void setFileSize(Double fileSize) {
		this.fileSize = fileSize;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setRecords(Object records) {
		this.records = records;
	}
	
}
