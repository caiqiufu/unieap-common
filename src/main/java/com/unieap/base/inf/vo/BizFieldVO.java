package com.unieap.base.inf.vo;

import java.util.List;

import com.unieap.base.UnieapConstants;
import com.unieap.base.vo.BaseVO;

public class BizFieldVO extends BaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Integer id;
	public String bizCode;
	public Integer infFieldId;
	public Integer seq;
	public String fieldName;
	public String fieldDisplayName;
	public String fieldType;
	public String optionalFlag;
	public boolean isOptional;
	public String xpath;
	public String ns;
	public Integer parentId;
	public String leafFlag;
	public boolean isLeaf;
	public BizFieldVO parentVO;
	public List<BizFieldVO> childrenList;
	public InfFieldVO infFieldVO;
	public String className;
	public String remark;

	public Integer getInfFieldId() {
		return infFieldId;
	}

	public void setInfFieldId(Integer infFieldId) {
		this.infFieldId = infFieldId;
	}

	public boolean isOptional() {
		if (UnieapConstants.YES.equals(optionalFlag)) {
			isOptional = true;
		} else {
			isOptional = false;
		}
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public BizFieldVO getParentVO() {
		return parentVO;
	}

	public void setParentVO(BizFieldVO parentVO) {
		this.parentVO = parentVO;
	}

	public List<BizFieldVO> getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(List<BizFieldVO> childrenList) {
		this.childrenList = childrenList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
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

	public InfFieldVO getInfFieldVO() {
		return infFieldVO;
	}

	public void setInfFieldVO(InfFieldVO infFieldVO) {
		this.infFieldVO = infFieldVO;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOptionalFlag() {
		return optionalFlag;
	}

	public void setOptionalFlag(String optionalFlag) {
		this.optionalFlag = optionalFlag;
	}

	public String getLeafFlag() {
		return leafFlag;
	}

	public void setLeafFlag(String leafFlag) {
		this.leafFlag = leafFlag;
	}

}
