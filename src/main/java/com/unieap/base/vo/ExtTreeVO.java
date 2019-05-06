package com.unieap.base.vo;

public class ExtTreeVO extends TreeVO{
	private boolean checkBoxTree = false;
	private boolean checked;
	
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
		if(checkBoxTree){
			jsonObj.put("checked",checked);
		}
		//jsonObj.put("activateFlag", activateFlag);
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
}
