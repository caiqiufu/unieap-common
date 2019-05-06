package com.unieap.base.inf.transform;

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
