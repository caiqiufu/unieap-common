package com.unieap.base.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.unieap.base.UnieapConstants;

public class MenuVO extends BaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Integer menuId;
	public String menuCode;
	public String menuName;
	public String parentId;
	public String href;
	public String activateFlag;
	public String remark;
	public String seqNum;
	public String imgSrc;
	public MenuVO parent;
	public String title;
	public String isDefault;
	public String menuType;
	public String separateLine;
	public Integer id;
	public String text;
	public String iconCls;
	public String qtip;
	public String leafFlag;
	public boolean isLeaf;
	public List<MenuVO> childrenContainer = new ArrayList<MenuVO>();
	public List<MenuVO> buttonList = new ArrayList<MenuVO>();

	public List<MenuVO> getChildrenContainer() {
		return childrenContainer;
	}

	public void setChildrenContainer(List<MenuVO> childrenContainer) {
		this.childrenContainer = childrenContainer;
	}

	public void addChildren(MenuVO menu) {
		this.childrenContainer.add(menu);
	}

	public List<MenuVO> getChildren() {
		return this.childrenContainer;
	}

	public MenuVO getParent() {
		return parent;
	}

	public void setParent(MenuVO parent) {
		this.parent = parent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public MenuVO(Integer menuId, String menuCode, String menuName, String title) {
		super();
		this.menuId = menuId;
		this.menuCode = menuCode;
		this.menuName = menuName;
		this.title = title;
	}

	public MenuVO(Integer menuId, String menuCode, String menuName, String title, String href) {
		super();
		this.menuId = menuId;
		this.menuCode = menuCode;
		this.menuName = menuName;
		this.title = title;
		this.href = href;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getActivateFlag() {
		return activateFlag;
	}

	public void setActivateFlag(String activateFlag) {
		this.activateFlag = activateFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public boolean isSeparateLine() {
		return StringUtils.equals(separateLine, "Y");
	}

	public void setSeparateLine(String separateLine) {
		this.separateLine = separateLine;
	}

	public boolean isLeafNode() {
		return this.getChildrenContainer() == null || this.getChildrenContainer().size() == 0;
	}

	public MenuVO() {
		super();
	}

	public boolean isHaveChild() {
		if (this.getChildrenContainer() != null && this.getChildrenContainer().size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return "menuId=" + menuId + ";menuCode=" + menuCode + ";menuName=" + menuName + ";parentId=" + parentId
				+ ";title=" + title + ";href=" + href;
	}

	public List<MenuVO> getButtonList() {
		return buttonList;
	}

	public void setButtonList(List<MenuVO> buttonList) {
		this.buttonList = buttonList;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getSeparateLine() {
		return separateLine;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getQtip() {
		return qtip;
	}

	public void setQtip(String qtip) {
		this.qtip = qtip;
	}

	public String getLeafFlag() {
		return leafFlag;
	}

	public void setLeafFlag(String leafFlag) {
		this.leafFlag = leafFlag;
	}

	public boolean isLeaf() {
		this.isLeaf = UnieapConstants.YES.equals(this.leafFlag);
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
