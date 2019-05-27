package com.unieap.base.inf.vo;

import java.util.List;

import com.unieap.base.UnieapConstants;
import com.unieap.base.vo.BaseVO;

public class InfFieldVO extends BaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Integer id;
	public String infCode;
	public Integer seq;
	public String fieldName;
	public String fieldDisplayName;
	public String fieldType;
	public String xpath;
	public String ns;
	public Integer parentId;
	public InfFieldVO parentVO;
	public List<InfFieldVO> childrenList;
	public boolean isLeaf;
	public String leafFlag;
	public String className;
	public String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInfCode() {
		return infCode;
	}

	public void setInfCode(String infCode) {
		this.infCode = infCode;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldDisplayName() {
		return fieldDisplayName;
	}

	public void setFieldDisplayName(String fieldDisplayName) {
		this.fieldDisplayName = fieldDisplayName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getXpath() {
		return xpath;
	}

	public void updateXMLXpath(String nsAlias) {
		if (this.parentVO != null) {
			String parentXpath = this.parentVO.getXpath();
			if (this.ns != null) {
				xpath = parentXpath + "/" + nsAlias + ":" + this.fieldName;
			} else {
				xpath = parentXpath + "/" + this.fieldName;
			}
		} else {
			if (this.ns != null) {
				xpath = "//" + nsAlias + ":" + this.fieldName;
			} else {
				xpath = "//" + this.fieldName;
			}
		}
	}

	public void updateJSONXpath() {
		if (this.parentVO != null) {
			String parentXpath = this.parentVO.getXpath();
			if ("List".equals(this.parentVO.getFieldType())) {
				xpath = parentXpath + "[*]." + this.fieldName;
			} else {
				xpath = parentXpath + "." + this.fieldName;
			}
		} else {
			xpath = "$." + this.fieldName;
		}
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getNs() {
		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public InfFieldVO getParentVO() {
		return parentVO;
	}

	public void setParentVO(InfFieldVO parentVO) {
		this.parentVO = parentVO;
	}

	public List<InfFieldVO> getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(List<InfFieldVO> childrenList) {
		this.childrenList = childrenList;
	}

	public boolean isLeaf() {
		if (UnieapConstants.YES.equals(leafFlag)) {
			isLeaf = true;
		} else {
			isLeaf = false;
		}
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getLeafFlag() {
		return leafFlag;
	}

	public void setLeafFlag(String leafFlag) {
		this.leafFlag = leafFlag;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
