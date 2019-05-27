package com.unieap.base.inf.transform;

import com.unieap.base.inf.vo.BizFieldVO;
import com.unieap.base.inf.vo.BizMessageVO;

public interface BizFieldProcess {
	/**
	 * 
	 * @param bizVO
	 * @param bizFieldVO
	 * @return
	 * @throws Exception
	 */
	public String process(BizMessageVO bizMessageVO, BizFieldVO bizFieldVO) throws Exception;
}
