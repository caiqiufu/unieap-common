package com.unieap.base.inf.bizservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unieap.base.ApplicationContextProvider;
import com.unieap.base.controller.BaseController;
import com.unieap.base.file.bo.FileBO;
import com.unieap.base.inf.handler.BizServiceHandler;

@Controller
public class ExtAction extends BaseController {
	@Autowired
	BizServiceHandler bizServiceHandler;
	@Value("${spring.application.name}")
	public String appCode;

	@RequestMapping("unieap/extAction/bizHandle")
	public @ResponseBody String bizHandle(String requestInfo, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> extParameters = new HashMap<String, Object>();
		extParameters.put("HttpServletRequest", request);
		extParameters.put("HttpServletResponse", response);
		String SOAPAction = request.getHeader("SOAPAction");
		// application/xml /application/json/text/xml;charset=UTF-8
		String ContentType = request.getContentType();
		extParameters.put("ContentType", ContentType);
		extParameters.put("SOAPAction", SOAPAction);
		if (StringUtils.isEmpty(requestInfo)) {
			requestInfo = binaryReader(request);
		}
		return bizServiceHandler.process(requestInfo, extParameters);
	}

	@RequestMapping("unieap/extAction/bizFileHandle")
	public @ResponseBody String bizFileHandle(String requestInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		FileBO fileBO = (FileBO) ApplicationContextProvider.getBean("fileBO");
		List<FileItem> items = fileBO.getFileItems(request);
		Map<String, Object> extParameters = new HashMap<String, Object>();
		extParameters.put("files", items);
		extParameters.put("HttpServletRequest", request);
		extParameters.put("HttpServletResponse", response);
		String SOAPAction = request.getHeader("SOAPAction");
		// application/xml /application/json/text/xml;charset=UTF-8
		String ContentType = request.getContentType();
		extParameters.put("ContentType", ContentType);
		extParameters.put("SOAPAction", SOAPAction);
		if (StringUtils.isEmpty(requestInfo)) {
			requestInfo = binaryReader(request);
		}
		return bizServiceHandler.process(requestInfo, extParameters);
	}

	@RequestMapping("unieap/extAction/downloadFileHandle")
	public @ResponseBody String downloadFileHandle(String requestInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> extParameters = new HashMap<String, Object>();
		extParameters.put("HttpServletRequest", request);
		extParameters.put("HttpServletResponse", response);
		String SOAPAction = request.getHeader("SOAPAction");
		// application/xml /application/json/text/xml;charset=UTF-8
		String ContentType = request.getContentType();
		extParameters.put("ContentType", ContentType);
		extParameters.put("SOAPAction", SOAPAction);
		if (StringUtils.isEmpty(SOAPAction)) {
			requestInfo = binaryReader(request);
		}
		return bizServiceHandler.process(requestInfo, extParameters);
	}
}
