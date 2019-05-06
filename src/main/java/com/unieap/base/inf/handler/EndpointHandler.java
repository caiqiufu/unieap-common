package com.unieap.base.inf.handler;

import java.util.Map;

public abstract class EndpointHandler extends BizHandler {

	/**
	 * 
	 * @param payload
	 * @return
	 */
	public abstract Object transformToExisting(Object payload,Map<String, Object> extParameters) throws Exception;

	/**
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	public abstract Object transformToStandard(Object payload,Map<String, Object> extParameters) throws Exception;
}
