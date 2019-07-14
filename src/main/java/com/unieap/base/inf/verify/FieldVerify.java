package com.unieap.base.inf.verify;

import java.util.Map;

import com.unieap.base.inf.vo.BizFieldVO;
import com.unieap.base.inf.vo.InfFieldVO;

public interface FieldVerify {
	/**
	 * 
	 * @param bizFieldVO
	 * @param infFieldVO
	 * @param value
	 * @param extParameters
	 * @return
	 * @throws Exception
	 */
	public Object verify(BizFieldVO bizFieldVO, InfFieldVO infFieldVO,Object value, Map<String, Object> extParameters)
			throws Exception;
}
