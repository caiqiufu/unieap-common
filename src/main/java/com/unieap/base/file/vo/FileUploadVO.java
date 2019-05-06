package com.unieap.base.file.vo;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileUploadVO {
	private CommonsMultipartFile file;
	public CommonsMultipartFile getFile() {
		return file;
	}

	public void setFile(CommonsMultipartFile file) {
		this.file = file;
	}
}
