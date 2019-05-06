package com.unieap.base.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	private MyUserDetailService myUserDetailService;
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailService); // user Details Service验证

	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 解决不允许显示在iframe的问题
		http.headers().frameOptions().disable()
        .and().authorizeRequests()
        .antMatchers("/unieap/service/**").permitAll() // 注册请求不需要验证
        .antMatchers("/unieap/extAction/**").permitAll()
        .antMatchers("/biz/service/**").permitAll()
        .anyRequest().authenticated()
        .and().formLogin().loginPage("/sign_in")
        .loginProcessingUrl("/login").defaultSuccessUrl("/desk",true)
        .failureUrl("/sign_in?error=1").permitAll()
        .and().logout().logoutSuccessUrl("/sign_in?error=2").permitAll()
        .and().csrf().disable();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/images/**", "/js/**");
	}
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// 指定密码加密所使用的加密器为passwordEncoder()
		// 需要将密码加密后写入数据库
		auth.userDetailsService(myUserDetailService).passwordEncoder(passwordEncoder());
		auth.eraseCredentials(false);
	}
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);
	}
}