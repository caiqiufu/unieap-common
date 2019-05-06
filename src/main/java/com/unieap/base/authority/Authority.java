package com.unieap.base.authority;

/**
 * Jan 13, 2011 权限
 */
public interface Authority {
	/**
	 *<p>描述:获取角色下的资源</P>
	 * Jan 13, 2011
	 * @param resId
	 * @param roleId
	 * @return
	 * @throws Exception 
	 */
	public AuthorityVO getRoleAuthority(String resId,String roleId)throws Exception;
	/**
	 *<p>描述:获取用户下的资源</P>
	 * Jan 13, 2011
	 * @param resId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public AuthorityVO getUserAuthority(String resId,String userId)throws Exception;
	/**
	 *<p>描述:获取用户角色下的资源信息</P>
	 * Jan 13, 2011
	 * @param resId
	 * @param userId
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public AuthorityVO getUserAuthority(String resId,String userId,String roleId)throws Exception;
	/**
	 *<p>描述:获取单位资源信息</P>
	 * Jan 13, 2011
	 * @param resId
	 * @param custId
	 * @return
	 * @throws Exception
	 */
	public AuthorityVO getCustAuthority(String resId,String custId)throws Exception;
}
