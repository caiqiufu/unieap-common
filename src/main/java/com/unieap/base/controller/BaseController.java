package com.unieap.base.controller;

import java.beans.PropertyEditor;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unieap.base.UnieapConstants;
import com.unieap.base.bo.BaseBO;
import com.unieap.base.exttools.DateEditor;
import com.unieap.base.pojo.MdmExclog;
import com.unieap.base.repository.ExcLogRespository;

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
		 * if(ex instanceof BusinessException) { return "business_error"; }else
		 * if(ex instanceof ParameterException) { return "parameter_error"; }
		 * else { }
		 */
		return "error";
	}

	@Autowired
	BaseBO baseBO;

	/**
	 * get Dictionary data list from customization sql
	 * 
	 * @param isOptional
	 * @param parentId
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("baseController/getDicData")
	public @ResponseBody String getDicData(String dicType, String isOptional, String whereby,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		return baseBO.getDicData(dicType, isOptional, whereby);
	}

	/**
	 * get dictionary list by ajax, get dictionary from database
	 * 
	 * @param groupCode
	 * @param whereby
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("baseController/getCommDicList")
	public @ResponseBody String getCommDicList(String groupCode, String isOptional, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return baseBO.getCommDicList(groupCode, isOptional);

	}
}
