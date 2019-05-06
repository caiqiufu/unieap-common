package com.unieap.base.vo;

import java.util.List;

public class RoleVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private java.lang.Integer id;
	private String roleCode;
	private String roleName;
	private java.lang.Integer tenantId;
	private List<ResourceVO> resList;
	public java.lang.Integer getId() {
		return id;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public String getRoleName() {
		return roleName;
	}
	public java.lang.Integer getTenantId() {
		return tenantId;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public void setTenantId(java.lang.Integer tenantId) {
		this.tenantId = tenantId;
	}
	
	public String getResFilterSql(){
		return null;
	}
	public List<ResourceVO> getResList() {
		return resList;
	}
	public void setResList(List<ResourceVO> resList) {
		this.resList = resList;
	}
	
}
