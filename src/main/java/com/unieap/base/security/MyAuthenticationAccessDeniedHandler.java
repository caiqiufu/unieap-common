package com.unieap.base.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MyAuthenticationAccessDeniedHandler implements AccessDeniedHandler {
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		//httpServletRequest.getSession().setAttribute("loginErrorMessage", "2");
		//RequestDispatcher dispatcher = httpServletRequest.getRequestDispatcher(httpServletRequest.getRequestURI());
		//dispatcher.forward(httpServletRequest, httpServletResponse);
		// httpServletResponse.sendRedirect(httpServletRequest.+"/login?error=2");
		response.setContentType("application/json;charset=UTF-8");
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", "403");
		map.put("msg", accessDeniedException.getMessage());
		map.put("data", "");
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(objectMapper.writeValueAsString(map));
	}
}