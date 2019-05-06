package com.unieap.base.authority;

/**
 * Jan 13, 2011
 */
public class AuthorityImpl implements Authority{
	
	public AuthorityVO getCustAuthority(String resId, String custId) throws Exception {
	    // TODO Auto-generated method stub
	    return null;
    }

	public AuthorityVO getRoleAuthority(String resId, String roleId) throws Exception {
	    // TODO Auto-generated method stub
	    return null;
    }

	public AuthorityVO getUserAuthority(String resId, String userId) throws Exception {
		AuthorityVO vo = new AuthorityVO();
		vo.setIds("'1605','1606'");
		vo.setResId(resId);
		vo.setUserId(userId);
	    return vo;
    }

	public AuthorityVO getUserAuthority(String resId, String userId, String roleId) throws Exception {
	    // TODO Auto-generated method stub
	    return null;
    }

}
