package com.unieap.base.handler;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * 
 * @author <a href="mailto: caiqiufu@sohu.com">蔡秋伏</a>
 * @version $Revision: 1.1 $
 */
public interface ConfigHandler {
	/**
	 * 
	 */
	final Log log = LogFactory.getLog(ConfigHandler.class);

	/**
	 * @param event
	 * @throws Exception
	 */
	public void deal(Map<String,Object> parameters)throws Exception;
	
	/**
	 * whether need to relaod auto
	 * @return
	 */
	public boolean reload();
	
}
