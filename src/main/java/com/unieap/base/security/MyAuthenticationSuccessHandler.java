package com.unieap.base.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
@Service
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		request.getSession().setAttribute("loginErrorMessage","0");
		response.sendRedirect(request.getContextPath() + "/home");
		response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write("{\"status\":\"ok\",\"msg\":\"登录成功\"}");
        out.flush();
        out.close();
	}
}
