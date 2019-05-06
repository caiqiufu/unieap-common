package com.unieap.base.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unieap.base.bo.VerifyCodeBO;


@Controller
public class VerifyCodeController {
	@Autowired
	VerifyCodeBO verifyCodeBO;
	@RequestMapping("verifyCodeController/getVerifyCode")
	public @ResponseBody Map<String, String> getVerifyCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return verifyCodeBO.getVerifyCode("1", request, response);
	}
}
