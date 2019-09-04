package com.unieap.base.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private MyUserDetailService myUserDetailService;

	@Autowired
	private MyAuthenticationAccessDeniedHandler myAuthenticationAccessDeniedHandler;

	@Autowired
	private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 指定密码加密所使用的加密器为passwordEncoder()
		// 需要将密码加密后写入数据库
		auth.userDetailsService(myUserDetailService).passwordEncoder(new BCryptPasswordEncoder(4));
		auth.eraseCredentials(false);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/*", "/unieapLogin", "/unieapIndex", "/login", "/login/**", "/actuator/**", "/assets/**",
						"/css/**", "/js/**", "/images/**", "/Ext/**", "/sharefolder/**")
				.permitAll().anyRequest().authenticated().and().formLogin().loginPage("/unieapLogin")
				.loginProcessingUrl("/desk") // 自定义的登录接口
				.usernameParameter("username").passwordParameter("password")
				.successHandler(myAuthenticationSuccessHandler).and().logout().permitAll();
		http.exceptionHandling().accessDeniedHandler(myAuthenticationAccessDeniedHandler);
		http.headers().frameOptions().disable().and().csrf().disable();
		http.csrf().disable();
		// session失效后跳转
		http.sessionManagement().invalidSessionUrl("/unieapLogin");
		// 只允许一个用户登录,如果同一个账户两次登录,那么第一个账户将被踢下线,跳转到登录页面
		http.sessionManagement().maximumSessions(1).expiredUrl("/unieapLogin");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// ingore是完全绕过了spring security的所有filter，相当于不走spring security
		// permitall没有绕过spring security，其中包含了登录的以及匿名的。
		web.ignoring().antMatchers("/actuator/**", "/assets/**", "/unieap/service/**", "/unieap/service/**",
				"/unieap/extAction/**", "/unieap/extAction/**", "/unieap/WSExtAction/**/**", "/unieap/WSExtAction/**/*",
				"/biz/service/**", "/biz/service/**", "/sharefolder/**", "/sharefolder/**");
	}

	/*
	 * 
	 * 这里可以增加自定义的投票器
	 */
	@Bean(name = "accessDecisionManager")
	public AccessDecisionManager accessDecisionManager() {
		List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<AccessDecisionVoter<? extends Object>>();
		decisionVoters.add(new RoleVoter());
		decisionVoters.add(new AuthenticatedVoter());
		decisionVoters.add(webExpressionVoter());// 启用表达式投票器
		AffirmativeBased accessDecisionManager = new AffirmativeBased(decisionVoters);

		return accessDecisionManager;
	}

	/*
	 * 表达式控制器
	 */
	@Bean(name = "expressionHandler")
	public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
		DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
		return webSecurityExpressionHandler;
	}

	/*
	 * 表达式投票器
	 */
	@Bean(name = "expressionVoter")
	public WebExpressionVoter webExpressionVoter() {
		WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
		webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler());
		return webExpressionVoter;
	}
}
