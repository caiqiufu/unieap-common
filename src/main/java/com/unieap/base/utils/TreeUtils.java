package com.unieap.base.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.unieap.base.vo.TreeVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TreeUtils {
	public static JSONObject convertBean2JSON(TreeVO vo) throws Exception {
		JSONObject json = new JSONObject();
		json.put("id", vo.getId());
		json.put("parentId", vo.getParentId());
		json.put("text", vo.getText());
		json.put("qtip", vo.getQtip());
		json.put("iconCls", vo.getIconCls());
		json.put("leaf", vo.isLeaf());
		json.put("attr1", vo.getAttr1());
		json.put("attr2", vo.getAttr2());
		json.put("attr3", vo.getAttr3());
		json.put("attr4", vo.getAttr4());
		json.put("attr5", vo.getAttr5());
		json.put("attr6", vo.getAttr6());
		json.put("attr7", vo.getAttr7());
		json.put("attr8", vo.getAttr8());
		json.put("attr9", vo.getAttr9());
		json.put("attr10", vo.getAttr10());
		json.put("expanded", vo.isExpanded());
		if(vo.isCheckBoxTree()){
			json.put("checked", vo.isChecked());
		}
		List<TreeVO> child = vo.getChildren(); 
		if(child!=null&&child.size()>0){
			JSONArray jarrchild = new JSONArray();
			for(int i = 0; i < child.size(); i++){
				jarrchild.add(convertBean2JSON(child.get(i)));
			}
			json.put("children", jarrchild);
		}
		Map<String, ?> extAtt = vo.getExtendAttri();
		if(extAtt!=null&&!extAtt.isEmpty()){
			Iterator<String> iter = extAtt.keySet().iterator();
			JSONObject jsonext = new JSONObject();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				Object val = extAtt.get(key);
				jsonext.put(key, val);
			}
			json.put("extendAttri", jsonext);
		}
		return json;
	}
}
