package com.unieap.base.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.unieap.base.handler.LoadSysUserDataHandler;
import com.unieap.base.vo.RoleVO;
import com.unieap.base.vo.UserVO;

@Service
public class MyUserDetailService implements UserDetailsService {

	@Autowired
	public LoadSysUserDataHandler loadSysUserDataHandler;

	public UserDetails loadUserByUsername(String userName) {
		UserVO user = loadSysUserDataHandler.loadUserVO(userName);
		if (user != null) {
			List<GrantedAuthority> grantedAuthorities = getRoles(user.getRoles());
			return new User(user.getUserCode(), user.getPassword(), grantedAuthorities);
		} else {
			throw new UsernameNotFoundException("user: " + userName + " do not exist!");
		}
	}

	private List<GrantedAuthority> getRoles(List<RoleVO> roles) {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		if (roles != null) {
			for (RoleVO role : roles) {
				if (role != null && role.getRoleCode() != null) {
					GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleCode());
					grantedAuthorities.add(grantedAuthority);
				}
			}
		}
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("unieap");
		grantedAuthorities.add(grantedAuthority);
		return grantedAuthorities;
	}
}