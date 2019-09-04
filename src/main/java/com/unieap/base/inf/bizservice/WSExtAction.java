package com.unieap.base.inf.bizservice;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unieap.base.ApplicationContextProvider;
import com.unieap.base.controller.BaseController;
import com.unieap.base.inf.handler.BizServiceHandler;

@Controller
public class WSExtAction extends BaseController {
	
	@Value("${spring.application.name}")
	public String appCode;
	
	@RequestMapping("unieap/WSExtAction/bizHandle/{bizCode}")
	public @ResponseBody String bizHandle(String requestInfo, @PathVariable String bizCode, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> extParameters = new HashMap<String, Object>();
		extParameters.put("HttpServletRequest", request);
		extParameters.put("HttpServletResponse", response);
		String SOAPAction = request.getHeader("SOAPAction");
		// application/xml /application/json/text/xml;charset=UTF-8
		String ContentType = request.getContentType();
		extParameters.put("ContentType", ContentType);
		extParameters.put("SOAPAction", SOAPAction);
		extParameters.put("bizCode", bizCode);
		BizServiceHandler bizServiceHandler = (BizServiceHandler) ApplicationContextProvider
				.getBean("bizServiceHandler");
		if (StringUtils.isEmpty(requestInfo)) {
			requestInfo = binaryReader(request);
		}
		return bizServiceHandler.process(bizCode, SOAPAction, requestInfo, extParameters);
	}
}
