package com.unieap.base.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private MyUserDetailService myUserDetailService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 指定密码加密所使用的加密器为passwordEncoder()
		// 需要将密码加密后写入数据库
		auth.userDetailsService(myUserDetailService).passwordEncoder(new BCryptPasswordEncoder(4));
		auth.eraseCredentials(false);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/login", "/css/**", "/js/**", "/images/**", "/Ext/**").permitAll()
				.anyRequest().authenticated().and().formLogin().loginPage("/login").usernameParameter("username")
				.passwordParameter("password").permitAll().loginProcessingUrl("/desk").and().logout().permitAll();
		http.headers().frameOptions().disable().and().csrf().disable();
		// session失效后跳转
		http.sessionManagement().invalidSessionUrl("/login");
		// 只允许一个用户登录,如果同一个账户两次登录,那么第一个账户将被踢下线,跳转到登录页面
		http.sessionManagement().maximumSessions(1).expiredUrl("/login");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// ingore是完全绕过了spring security的所有filter，相当于不走spring security
		// permitall没有绕过spring security，其中包含了登录的以及匿名的。
		web.ignoring().antMatchers("/unieap/service/**", "/unieap/extAction/**", "/biz/service/**");
	}
}
