package com.unieap.base.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;

@Service
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	/*
	 * 登录请求验证完成之后
	 * UsernamePasswordAuthenticationFilter会调用SavedRequestAwareAuthenticationSuccessHandler的实例loginsuccesshandler来处理登录成功后的处理步骤
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.authentication.
	 * SavedRequestAwareAuthenticationSuccessHandler#onAuthenticationSuccess(javax.
	 * servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.Authentication)
	 */

	// 原请求信息的缓存及恢复
	private RequestCache requestCache = new HttpSessionRequestCache();

	// 用于重定向
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest != null) {
			String targetUrl = savedRequest.getRedirectUrl();
			logger.info("引发跳转的请求是:" + targetUrl);
			if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
				redirectStrategy.sendRedirect(request, response,targetUrl);
			}
		}else {
			redirectStrategy.sendRedirect(request, response,request.getContextPath() + "/desk");
		}
		//response.sendRedirect(request.getContextPath() + "/desk");
		request.getSession().setAttribute("loginErrorMessage", "0");
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write("{\"status\":\"ok\",\"msg\":\"登录成功\"}");
		out.flush();
		out.close();
	}
}
