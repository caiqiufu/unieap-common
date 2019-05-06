package com.unieap.base.file.vo;

import java.util.List;

public class ExcelVO {
	String fileName;
	List<SheetVO> sheets;
	public String getFileName() {
		return fileName;
	}
	public List<SheetVO> getSheets() {
		return sheets;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public void setSheets(List<SheetVO> sheets) {
		this.sheets = sheets;
	}
	
}
