package com.unieap.base.inf.bizservice;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unieap.base.ApplicationContextProvider;
import com.unieap.base.controller.BaseController;
import com.unieap.base.file.bo.FileBO;
import com.unieap.base.inf.handler.BizServiceBO;

@Controller
public class ExtAction extends BaseController {
	@RequestMapping("unieap/extAction/queryInfo")
	public @ResponseBody String queryInfo(@RequestBody String requestInfo, HttpServletRequest request,
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
		if (StringUtils.isEmpty(requestInfo)) {
			requestInfo = binaryReader(request);
		}
		return bizServiceBO.process(requestInfo, extParameters);
	}

	@RequestMapping("unieap/extAction/bizHandle")
	public @ResponseBody String bizHandle(String requestInfo, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> extParameters = new HashMap<String, Object>();
		extParameters.put("HttpServletRequest", request);
		extParameters.put("HttpServletResponse", response);
		BizServiceBO bizServiceBO = (BizServiceBO) ApplicationContextProvider.getBean("bizServiceBO");
		String SOAPAction = request.getHeader("SOAPAction");
		//application/xml /application/json/text/xml;charset=UTF-8
		String ContentType = request.getContentType();
		extParameters.put("ContentType", ContentType);
		extParameters.put("SOAPAction", SOAPAction);
		if (StringUtils.isEmpty(requestInfo)) {
			requestInfo = binaryReader(request);
		}
		return bizServiceBO.process(requestInfo, extParameters);
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
		BizServiceBO bizServiceBO = (BizServiceBO) ApplicationContextProvider.getBean("bizServiceBO");
		String SOAPAction = request.getHeader("SOAPAction");
		//application/xml /application/json/text/xml;charset=UTF-8
		String ContentType = request.getContentType();
		extParameters.put("ContentType", ContentType);
		extParameters.put("SOAPAction", SOAPAction);
		if (StringUtils.isEmpty(requestInfo)) {
			requestInfo = binaryReader(request);
		}
		return bizServiceBO.process(requestInfo, extParameters);
	}

	@RequestMapping("unieap/extAction/downloadFileHandle")
	public @ResponseBody String downloadFileHandle(String requestInfo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> extParameters = new HashMap<String, Object>();
		extParameters.put("HttpServletRequest", request);
		extParameters.put("HttpServletResponse", response);
		BizServiceBO bizServiceBO = (BizServiceBO) ApplicationContextProvider.getBean("bizServiceBO");
		String SOAPAction = request.getHeader("SOAPAction");
		//application/xml /application/json/text/xml;charset=UTF-8
		String ContentType = request.getContentType();
		extParameters.put("ContentType", ContentType);
		extParameters.put("SOAPAction", SOAPAction);
		if (StringUtils.isEmpty(SOAPAction)) {
			requestInfo = binaryReader(request);
		}
		return bizServiceBO.process(requestInfo, extParameters);
	}

	/**
	 * 一次流不能读取两次
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String charReader(HttpServletRequest request) throws Exception {
		BufferedReader br = request.getReader();
		String str, wholeStr = "";
		while ((str = br.readLine()) != null) {
			wholeStr += str;
		}
		return wholeStr;
	}

	// 二进制读取
	public String binaryReader(HttpServletRequest request) throws Exception {
		int len = request.getContentLength();
		ServletInputStream iii = request.getInputStream();
		byte[] buffer = new byte[len];
		iii.read(buffer, 0, len);
		return binaryReader(buffer);
	}

	public String binaryReader(byte[] buffer) {
		String inputData = new String(buffer, 0, buffer.length, Charset.forName("UTF-8"));
		return inputData;
	}
}
