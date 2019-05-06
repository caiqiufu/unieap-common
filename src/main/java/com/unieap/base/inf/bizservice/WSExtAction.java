package com.unieap.base.inf.bizservice;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unieap.base.ApplicationContextProvider;
import com.unieap.base.inf.handler.BizServiceBO;

public class WSExtAction extends ExtAction {
	@RequestMapping("unieap/WSExtAction/bizHandle")
	public @ResponseBody String bizHandle(@RequestBody String requestInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> extParameters = new HashMap<String, Object>();
		extParameters.put("HttpServletRequest", request);
		extParameters.put("HttpServletResponse", response);
		String SOAPAction = request.getHeader("SOAPAction");
		//application/xml /application/json/text/xml;charset=UTF-8
		String ContentType = request.getContentType();
		extParameters.put("ContentType", ContentType);
		extParameters.put("SOAPAction", SOAPAction);
		BizServiceBO bizServiceBO = (BizServiceBO) ApplicationContextProvider.getBean("bizServiceBO");
		if (!StringUtils.isEmpty(requestInfo)) {
			requestInfo = binaryReader(request);
		}
		return bizServiceBO.process(SOAPAction,requestInfo, extParameters);
	}
}
