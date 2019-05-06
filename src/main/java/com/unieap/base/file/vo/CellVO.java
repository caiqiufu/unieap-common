package com.unieap.base.file.vo;
public class CellVO {
	public int index;
	public String cellName;
	public String cellValue;
	
	public CellVO() {
	}
	
	public CellVO(int index, String cellName, String cellValue) {
		super();
		this.index = index;
		this.cellName = cellName;
		this.cellValue = cellValue;
	}

	public CellVO(String cellName, String cellValue) {
		super();
		this.cellName = cellName;
		this.cellValue = cellValue;
	}
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public String getCellValue() {
		return cellValue;
	}
	public void setCellValue(String cellValue) {
		this.cellValue = cellValue;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	
}
