package com.unieap.base.inf.transform;

import java.util.Map;

public interface TransformMessageHandler {
	/**
	 * 
	 * @param payload
	 * @return
	 */
	public Object transform(Object payload,Map<String, Object> extParameters) throws Exception;
}
