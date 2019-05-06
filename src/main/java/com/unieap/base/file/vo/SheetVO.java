package com.unieap.base.file.vo;

import java.util.List;

public class SheetVO {
	public int index = 0;
	public String name;
	public String[] title;
	public List<List<CellVO>> datas;
	public SheetVO() {
	}
	public SheetVO(int index, String name) {
		this.index = index;
		this.name = name;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<List<CellVO>> getDatas() {
		return datas;
	}
	public void setDatas(List<List<CellVO>> datas) {
		this.datas = datas;
	}
	public String[] getTitle() {
		return title;
	}
	public void setTitle(String[] title) {
		this.title = title;
	}
	
	
}
