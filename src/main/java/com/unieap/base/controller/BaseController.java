package com.unieap.base.controller;

import java.beans.PropertyEditor;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.unieap.base.UnieapConstants;
import com.unieap.base.exttools.DateEditor;
import com.unieap.base.pojo.MdmExclog;
import com.unieap.base.repository.ExcLogRespository;

import net.sf.json.JSONObject;

public class BaseController {

	@Autowired
	ExcLogRespository excLogRespository;

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		// 瀵逛簬闇�瑕佽浆鎹负Date绫诲瀷鐨勫睘鎬э紝浣跨敤DateEditor杩涜澶勭悊
		binder.registerCustomEditor(Date.class, (PropertyEditor) new DateEditor());
	}

	/**
	 * 鍩轰簬@ExceptionHandler寮傚父澶勭悊
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@ExceptionHandler
	public String exp(HttpServletRequest request, Exception ex) {
		request.setAttribute("ex", ex);
		MdmExclog log = new MdmExclog();
		log.setId(UnieapConstants.getSequence());
		log.setBizModule("unieap");
		log.setExType("system_exception");
		log.setExCode("");
		log.setExInfo(ex.getLocalizedMessage());
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		try {
			log.setExTracking(sw.toString().getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			StringWriter sw1 = new StringWriter();
			PrintWriter pw1 = new PrintWriter(sw);
			e.printStackTrace(pw1);
			log.setExTracking(sw1.toString().getBytes());
		}
		if (UnieapConstants.getUser() != null) {
			log.setOperatorName(UnieapConstants.getUser().getUserCode());
		} else {
			log.setOperatorName("system error");
		}
		log.setOperationDate(UnieapConstants.getDateTime());
		excLogRespository.save(log);
		// 鏍规嵁涓嶅悓閿欒杞悜涓嶅悓椤甸潰
		/*
		 * if(ex instanceof BusinessException) { return "business_error"; }else if(ex
		 * instanceof ParameterException) { return "parameter_error"; } else { }
		 */
		return "error";
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

	public JSONObject getRequestServerInfo() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = servletRequestAttributes.getRequest();
		return getRequestServerInfo(request);
	}

	public JSONObject getRequestServerInfo(HttpServletRequest request) {
		// X-Forwarded-For，不区分大小写
		String possibleIpStr = request.getHeader("X-Forwarded-For");
		String remoteIp = request.getRemoteAddr();
		String clientIp;
		if (StringUtils.isNotBlank(possibleIpStr) && !"unknown".equalsIgnoreCase(possibleIpStr)) {
			// 可能经过好几个转发流程，第一个是用户的真实ip，后面的是转发服务器的ip
			// clientIp = possibleIpStr.split(",")[0].trim();
			clientIp = possibleIpStr;
		} else {
			// 如果转发头ip为空，说明是直接访问的，没有经过转发
			clientIp = remoteIp;
		}
		String uri = request.getRequestURI();// 返回请求行中的资源名称
		String url = request.getRequestURL().toString();// 获得客户端发送请求的完整url
		String ip = request.getRemoteAddr();// 返回发出请求的IP地址
		String params = request.getQueryString();// 返回请求行中的参数部分
		String host = request.getRemoteHost();// 返回发出请求的客户机的主机名
		int port = request.getRemotePort();// 返回发出请求的客户机的端口号。
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("client.ips", clientIp);
		jsonObj.put("client.uri", uri);
		jsonObj.put("client.url", url);
		jsonObj.put("client.ip", ip);
		jsonObj.put("client.params", params);
		jsonObj.put("client.host", host);
		jsonObj.put("client.port", port);
		return jsonObj;
	}
}
