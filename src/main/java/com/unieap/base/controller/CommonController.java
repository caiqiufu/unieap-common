package com.unieap.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unieap.base.bo.BaseBO;

@Controller
public class CommonController {
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
	@RequestMapping("commonController/getDicData")
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
	@RequestMapping("commonController/getCommDicList")
	public @ResponseBody String getCommDicList(String groupCode, String isOptional, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return baseBO.getCommDicList(groupCode, isOptional);

	}
}
