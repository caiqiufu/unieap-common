package com.unieap.base.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.db.DBManager;
import com.unieap.base.db.EntityRowMapper;
import com.unieap.base.vo.ResourceVO;
import com.unieap.base.vo.RoleVO;
import com.unieap.base.vo.UserVO;

@Service
public class LoadSysUserDataHandler implements ConfigHandler {

	@Override
	public void deal(Map<String, Object> parameters) throws Exception {
		// TODO Auto-generated method stub
		loadUserList();
	}

	@Override
	public boolean reload() {
		// TODO Auto-generated method stub
		return false;
	}

	public void loadUserList() {
		Map<String, UserVO> userList = new HashMap<String, UserVO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT u.enable,u.locked,u.user_code as userCode,u.user_name as userName,");
		sql.append("u.id ,u.password,expired,locked,tenant_id as tenantId FROM mdm_user u");
		List<?> datas = DBManager.getJT().query(sql.toString(), new EntityRowMapper(UserVO.class));
		if (datas != null && datas.size() > 0) {
			List<UserVO> volist = (List<UserVO>) datas;
			for (UserVO vo : volist) {
				vo.setRoles(getUserRoleList(vo.getId()));
				userList.put(vo.getUserCode(), vo);
			}
			UnieapCacheMgt.setUserList(userList);
		}
	}

	public UserVO loadUserVO(String userCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT u.enable,u.locked,u.user_code as userCode,u.user_name as userName,");
		sql.append("u.id ,u.password,expired,locked FROM mdm_user u where user_code=?");
		List<?> datas = DBManager.getJT().query(sql.toString(), new Object[] { userCode },
				new EntityRowMapper(UserVO.class));
		if (datas != null && datas.size() > 0) {
			UserVO vo = (UserVO) datas.get(0);
			List<UserVO> volist = (List<UserVO>) datas;
			vo.setRoles(getUserRoleList(vo.getId()));
			return vo;
		}
		return null;
	}

	public List<RoleVO> getUserRoleList(Integer userId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT r.id,r.role_code as roleCode,r.role_name as roleName,r.tenant_id as tenantId ");
		sql.append("FROM mdm_user_role ur, mdm_role r where r.id = ur.role_id and ur.id = ?");
		List<?> datas = DBManager.getJT().query(sql.toString(), new Object[] { userId },
				new EntityRowMapper(RoleVO.class));
		List<RoleVO> volist = (List<RoleVO>) datas;
		if (datas != null && datas.size() > 0) {
			for (RoleVO vo : volist) {
				vo.setResList(getRoleResList(vo.getId()));
			}
		}
		addDefaultRoles(volist);
		return volist;
	}

	private void addDefaultRoles(List<RoleVO> roles) {
		RoleVO vo1 = new RoleVO();
		vo1.setId(Integer.valueOf("1"));
		vo1.setRoleCode("unieap");
		vo1.setRoleName("unieap");
		roles.add(vo1);
		RoleVO vo2 = new RoleVO();
		vo2.setId(Integer.valueOf("2"));
		vo2.setRoleCode("admin");
		vo2.setRoleName("admin");
		roles.add(vo2);
	}

	public List<ResourceVO> getRoleResList(Integer roleId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ri.id, ri.res_code as resCode,ri.res_name as resName,ri.res_Type_Id as resTypeId,");
		sql.append("rt.res_Type_Code as resTypeCode,rt.res_Type_Name as resTypeName");
		sql.append(" FROM unieap.mdm_role_resource rr,mdm_res_item ri,mdm_res_type rt ");
		sql.append(" where ri.id = rr.resource_id and rr.res_type_id = rt.id and rr.role_id = ?");
		List<?> datas = DBManager.getJT().query(sql.toString(), new Object[] { roleId },
				new EntityRowMapper(ResourceVO.class));
		if (datas != null && datas.size() > 0) {
			List<ResourceVO> volist = (List<ResourceVO>) datas;
			return volist;
		}
		return null;
	}
}
