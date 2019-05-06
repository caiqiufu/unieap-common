package com.unieap.base.handler;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.unieap.base.SYSConfig;
import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.db.DBManager;
import com.unieap.base.db.EntityRowMapper;
import com.unieap.base.vo.DicDataVO;
import com.unieap.base.vo.DicGroupVO;

@Service
public class LoadSysDicDataHandler implements ConfigHandler {

	@Override
	public void deal(Map<String, Object> parameters) throws Exception {
		// TODO Auto-generated method stub
		loadDicdata();
	}

	@Override
	public boolean reload() {
		// TODO Auto-generated method stub
		return false;
	}

	public void loadDicdata() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT dd.dic_code as dicCode,dd.dic_name as dicName,");
		sql.append(" dg.group_code as groupCode,dg.group_name as groupName,");
		sql.append(" dd.activate_flag as activeFlag ,dd.seq as seq ");
		sql.append(" FROM mdm_dic_group dg,mdm_dic_item dd ");
		sql.append("  where dg.id = dd.group_id and dd.activate_flag = 'Y' and dg.tenant_id = ?");
		sql.append("  order by dg.group_name, dd.dic_name,dd.seq ASC ");
		List<Object> datas = DBManager.getJT().query(sql.toString(),
				new Object[] { SYSConfig.getConfig().get("tenantId") }, new EntityRowMapper(DicDataVO.class));
		if (datas != null && datas.size() > 0) {
			for (Object data : datas) {
				DicDataVO vo = (DicDataVO) data;
				String groupCode = vo.getGroupCode();
				if (UnieapCacheMgt.dicDataList.get(groupCode) != null) {
					DicGroupVO group = UnieapCacheMgt.dicDataList.get(groupCode);
					group.getDataList().add(vo);
					group.getDataMap().put(vo.getDicCode(), vo);
				} else {
					DicGroupVO group = new DicGroupVO();
					group.setGroupCode(vo.getGroupCode());
					group.setGroupName(vo.getGroupName());
					group.getDataList().add(vo);
					group.getDataMap().put(vo.getDicCode(), vo);
					UnieapCacheMgt.dicDataList.put(groupCode, group);
				}
			}
		}
	}
}
