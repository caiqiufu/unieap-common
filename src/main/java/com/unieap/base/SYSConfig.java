package com.unieap.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Jan 25, 2011 系统参数配置
 */
public class SYSConfig {
	/**
	 * <p>
	 * 描述:调试状态
	 * </p>
	 */
	public static Map<String,String> isDebug = new HashMap<String,String>();
	public static Map<String, String> config = new HashMap<String,String>();

	public static Map<String, String> getConfig() {
		return config;
	}

	public static void setConfig(Map<String, String> config) {
		SYSConfig.config = config;
		wfPath = config.get("wfPath");
		rpPath = config.get("rpPath");
		filePath = config.get("filePath");
		selectMaxNum = Integer.parseInt(config.get("selectMaxNum"));
		pageSize = Integer.parseInt(config.get("pageSize"));
		defaultLanguage = config.get("language");
	}

	/**
	 * <p>
	 * 描述:工作流配置路径
	 * </p>
	 */
	public static String wfPath;
	/**
	 * <p>
	 * 描述:报表配置路径
	 * </p>
	 */
	public static String rpPath;
	/**
	 * <p>
	 * 描述:filePath
	 * </p>
	 */
	public static String filePath;
	/**
	 * /**
	 * <p>
	 * 描述:一次性查询最多纪录数
	 * </p>
	 */
	public static int selectMaxNum;
	/**
	 * <p>
	 * 描述:分页默认条数
	 * </p>
	 */
	public static int pageSize;

	/**
	 * 默认语言
	 */
	public static String defaultLanguage = "en_US";
}
