package com.unieap.base.handler;

import java.util.List;

import com.unieap.base.UnieapConstants;
import com.unieap.base.vo.DicDataVO;
import com.unieap.base.vo.QueryDicVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public abstract class DicHandler {
	public abstract String getDicList(String isOptional, String whereby) throws Exception;

	public String getSql(QueryDicVO vo, String whereby) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select " + vo.getDicId() + " as dicId," + vo.getDicCode() + " as dicCode,'root' as parentCode,");
		sql.append(vo.getDicCode() + " as dicName," + vo.getSeq() + " as seq from " + vo.getTableName() + " where "
				+ whereby + " order by " + vo.getOrderBy());
		return sql.toString();

	}

	public String getDicData(List<?> datas, String isOptional) throws Exception {
		JSONArray ja = new JSONArray();
		if (UnieapConstants.YES.equals(isOptional)) {
			JSONObject jac = new JSONObject();
			jac.put("dicCode", "");
			jac.put("dicName", "Please select...");
			jac.put("parentCode", "");
			ja.add(jac);
		}
		if (datas != null && datas.size() > 0) {
			DicDataVO data;
			for (int i = 0; i < datas.size(); i++) {
				data = (DicDataVO) datas.get(i);
				JSONObject jac = new JSONObject();
				jac.put("dicCode", data.getDicCode());
				jac.put("dicName", data.getDicName());
				jac.put("parentCode", data.getGroupCode());
				jac.put("attr1", data.getAttr1());
				jac.put("attr2", data.getAttr2());
				jac.put("attr3", data.getAttr3());
				jac.put("attr4", data.getAttr4());
				jac.put("attr5", data.getAttr5());
				ja.add(jac);
			}
		}
		String dicString = ja.toString();
		return dicString;
	}

}
