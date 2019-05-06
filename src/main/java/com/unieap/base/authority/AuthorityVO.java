package com.unieap.base.authority;

import com.unieap.base.vo.BaseVO;

/**
 * Jan 13, 2011 权限数据
 */
public class AuthorityVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String resId;
	private String userId;
	private String ids;//1,2,3 等
	/**
	 * @return String
	 */
	public String getResId() {
    	return resId;
    }
	/**
	 * @param resId
	 */
	public void setResId(String resId) {
    	this.resId = resId;
    }
	/**
	 * @return String
	 */
	public String getUserId() {
    	return userId;
    }
	/**
	 * @param userId
	 */
	public void setUserId(String userId) {
    	this.userId = userId;
    }
	/**
	 * @return String
	 */
	public String getIds() {
    	return ids;
    }
	/**
	 * @param ids
	 */
	public void setIds(String ids) {
    	this.ids = ids;
    }
}
