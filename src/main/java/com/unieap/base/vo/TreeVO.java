package com.unieap.base.vo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;



public class TreeVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id ;
	public String parentId ;
	public String text ;
	public String iconCls;
	public String qtip;
	public boolean leaf;
	public boolean expanded ;
	public List<TreeVO> children;
	public Map<String,Object> extendAttri;
	public String attr1;
	public String attr2;
	public String attr3;
	public String attr4;
	public String attr5;
	public String attr6;
	public String attr7;
	public String attr8;
	public String attr9;
	public String attr10;
	
	private boolean checkBoxTree = false;
	private boolean checked;
	
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public List<TreeVO> getChildren() {
		return children;
	}
	public void setChildren(List<TreeVO> children) {
		this.children = children;
	}
	public Map<String,Object> getExtendAttri() {
		return extendAttri;
	}
	public void setExtendAttri(Map<String,Object> extendAttri) {
		this.extendAttri = extendAttri;
	}
	
	
	public String getQtip() {
		return qtip;
	}
	public void setQtip(String qtip) {
		this.qtip = qtip;
	}
	public boolean isCheckBoxTree() {
		return checkBoxTree;
	}
	public void setCheckBoxTree(boolean checkBoxTree) {
		this.checkBoxTree = checkBoxTree;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	/*public String getJsonString() throws Exception{
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("id", id);
		jsonObj.put("parentId", parentId);
		jsonObj.put("text", text);
		jsonObj.put("leaf", leaf);
		jsonObj.put("iconCls", iconCls);
		jsonObj.put("expanded", expanded);
		putExtendAttri(extendAttri,jsonObj);
		if(children!=null&&children.size()>0){
			JSONArray jarr = new JSONArray();
			for(TreeVO vo:children){
				JSONObject child = new JSONObject();
				child.put("id", vo.getId());
				child.put("parentId", parentId);
				child.put("text", vo.getText());
				child.put("leaf", vo.isLeaf());
				//child.put("activateFlag", vo.getActivateFlag());
				child.put("expanded", vo.isExpanded());
				putExtendAttri(vo.getExtendAttri(),child);
				jarr.put(child);
			}
			jsonObj.put("children", jarr);
		}
		String jsonString = jsonObj.toString();
		if (SYSConfig.isDebug) {
			log.debug("jsonString:" + jsonString);
		}
		return jsonString;
	}*/
	
	
	public void putExtendAttri(Map<String,Object> exAttri,JSONObject jsonObj) throws JSONException{
		if(exAttri!=null){
			Iterator<String> iter = exAttri.keySet().iterator();
			while (iter.hasNext()){
				String key = (String) iter.next();
				Object val = exAttri.get(key);
				if (val == null) {
					jsonObj.put(key,null);
					continue;
				}else{
					jsonObj.put(key, val);
				}
			}
			
		}
	}
	public String getAttr1() {
		return attr1;
	}
	public String getAttr2() {
		return attr2;
	}
	public String getAttr3() {
		return attr3;
	}
	public String getAttr4() {
		return attr4;
	}
	public String getAttr5() {
		return attr5;
	}
	public String getAttr6() {
		return attr6;
	}
	public String getAttr7() {
		return attr7;
	}
	public String getAttr8() {
		return attr8;
	}
	public String getAttr9() {
		return attr9;
	}
	public String getAttr10() {
		return attr10;
	}
	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}
	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}
	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}
	public void setAttr4(String attr4) {
		this.attr4 = attr4;
	}
	public void setAttr5(String attr5) {
		this.attr5 = attr5;
	}
	public void setAttr6(String attr6) {
		this.attr6 = attr6;
	}
	public void setAttr7(String attr7) {
		this.attr7 = attr7;
	}
	public void setAttr8(String attr8) {
		this.attr8 = attr8;
	}
	public void setAttr9(String attr9) {
		this.attr9 = attr9;
	}
	public void setAttr10(String attr10) {
		this.attr10 = attr10;
	}
	
	
}
