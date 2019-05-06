package com.unieap.base.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.db.DBManager;
@Service
public class LoadSysResRoleDataHandler implements ConfigHandler{

	@Override
	public void deal(Map<String, Object> parameters) throws Exception {
		// TODO Auto-generated method stub
		loadUrlAccessRoleList();
	}

	@Override
	public boolean reload() {
		// TODO Auto-generated method stub
		return false;
	}
	public void loadUrlAccessRoleList() {
		Map<String, List<String>> resRoleList = new HashMap<String, List<String>>();
		StringBuffer sb = new StringBuffer();
		sb.append("select r.role_code,ri.res_code from mdm_role_resource rs,mdm_role r,mdm_res_item ri");
		sb.append(" where rs.role_id = r.id and rs.resource_id = ri.res_id and rs.res_type_id = 4");
		List<Map<String, Object>> datas = DBManager.getJT().queryForList(sb.toString());
		if (datas != null && datas.size() > 0) {
			for (Map<String, Object> data : datas) {
				String roleCode = (String) data.get("role_code");
				String resCode = (String) data.get("res_code");
				List<String> roles = resRoleList.get(resCode);
				if (roles == null) {
					roles = new ArrayList<String>();
					roles.add(roleCode);
					resRoleList.put(resCode, roles);
				} else {
					roles.add(roleCode);
				}
			}
		}
		UnieapCacheMgt.setResRoleList(resRoleList);
	}

}
