package com.unieap.base.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.unieap.base.UnieapConstants;

public class UserVO extends BaseVO implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String userCode;
	private String userName;
	private String password;
	private String enable;
	private String expired;
	private String locked;
	private Long tenantId;
	private List<RoleVO> roles = new ArrayList<RoleVO>();
	private Integer deptId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserCode() {
		return userCode;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getEnable() {
		return enable;
	}

	public String getExpired() {
		return expired;
	}

	public String getLocked() {
		return locked;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public void setExpired(String expired) {
		this.expired = expired;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public void setRoles(List<RoleVO> roles) {
		this.roles = roles;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		List<RoleVO> userRoles = this.getRoles();

		if (userRoles != null) {
			for (RoleVO role : userRoles) {
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleCode());
				authorities.add(authority);
			}
		}
		return authorities;
	}

	public List<RoleVO> getRoles() {
		RoleVO vo1 = new RoleVO();
		vo1.setId(Integer.valueOf("1"));
		vo1.setRoleCode("unieap");
		vo1.setRoleName("unieap");
		this.roles.add(vo1);
		RoleVO vo2 = new RoleVO();
		vo2.setId(Integer.valueOf("2"));
		vo2.setRoleCode("admin");
		vo2.setRoleName("admin");
		this.roles.add(vo2);
		return this.roles;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.userCode;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return UnieapConstants.YES.equals(expired);
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return !UnieapConstants.YES.equals(locked);
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return !UnieapConstants.YES.equals(expired);
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return UnieapConstants.YES.equals(enable);
	}

}
