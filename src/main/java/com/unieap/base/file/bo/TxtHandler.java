package com.unieap.base.file.bo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.unieap.base.pojo.MdmFileArchive;

public abstract class TxtHandler {
	public List<String> getExportData(Map<String, Object> parameters) {
		return null;
	}

	public Map<String, Object> importData(Map<String, Object> parameters,MdmFileArchive fileArchive, List<String> records) {
		return null;
	}

	@SuppressWarnings("deprecation")
	public String getFileName() {
		String filename = new Date().toLocaleString().replace(" ", "_") + ".txt";
		return filename;
	}
}
