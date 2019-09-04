package com.unieap.base.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

@Service
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		request.getSession().setAttribute("loginErrorMessage", "3");
		response.setContentType("application/json;charset=utf-8");
		response.sendRedirect("/login?error=3"); 
		PrintWriter out = response.getWriter();
		out.write("{\"status\":\"error\",\"msg\":\"登录失败\"}");
		out.flush();
		out.close();
	}
}
