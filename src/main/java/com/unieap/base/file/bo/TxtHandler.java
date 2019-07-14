package com.unieap.base.file.bo;

import java.util.List;
import java.util.Map;

import com.unieap.base.pojo.MdmFileArchive;

import net.sf.json.JSONObject;

public abstract class TxtHandler extends FileHandler {
	/**
	 * 
	 * @param parameters
	 * @return
	 */
	public List<String> getExportData(JSONObject parameters) throws Exception {
		return null;
	}

	/**
	 * 
	 * @param parameters
	 * @param fileArchive
	 * @param records
	 * @return
	 */
	public Map<String, Object> importData(JSONObject parameters, MdmFileArchive fileArchive, List<String> records)  throws Exception{
		return null;
	}
}
