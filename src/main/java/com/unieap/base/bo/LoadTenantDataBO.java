package com.unieap.base.bo;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.unieap.base.SYSConfig;
import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.db.DBManager;
import com.unieap.base.db.EntityRowMapper;
import com.unieap.base.vo.DicDataVO;
import com.unieap.base.vo.DicGroupVO;
import com.unieap.base.vo.MessageVO;

@Service
public class LoadTenantDataBO extends BaseBO {
	public void loadDicDataList() {
		if (UnieapCacheMgt.dicDataList == null) {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT dd.dic_code as dicCode,dd.dic_name as dicName,");
			sql.append(" dg.group_code as groupCode,dg.group_name as groupName,");
			sql.append(" dd.activate_flag as activateFlag ,dd.seq as seq ");
			sql.append(" FROM mdm_dic_group dg,mdm_dic_item dd ");
			sql.append("  where dg.id = dd.group_id and dd.activate_flag = 'Y' ");
			sql.append("  order by dg.group_name, dd.dic_name,dd.seq ASC ");
			List<Object> datas = DBManager.getJT().query(sql.toString(), new EntityRowMapper(DicDataVO.class));
			if (datas != null && datas.size() > 0) {
				for (Object data : datas) {
					DicDataVO vo = (DicDataVO) data;
					String groupCode = vo.getGroupCode();
					if (groupCode != null) {
						if (UnieapCacheMgt.dicDataList.get(groupCode) != null) {
							DicGroupVO group = UnieapCacheMgt.dicDataList.get(groupCode);
							group.getDataList().add(vo);
						} else {
							DicGroupVO group = new DicGroupVO();
							group.getDataList().add(vo);
							UnieapCacheMgt.dicDataList.put(groupCode, group);
						}
					}
				}
			}
		}
	}

	public void loadMessageList() {
		if (UnieapCacheMgt.messageList == null) {
			StringBuffer sql = new StringBuffer();
			sql.append(
					"SELECT CONCAT(tenant_id,'_',app_name,'_',language,'_',name) as name,app_name,language,value FROM mdm_message where activate_flag ='Y' and language=?");
			List<Map<String, Object>> datas = DBManager.getJT().queryForList(sql.toString(),
					new Object[] { SYSConfig.getConfig().get("language") });
			if (datas != null & !datas.isEmpty()) {
				Map<String, Object> data;
				for (int i = 0; i < datas.size(); i++) {
					data = datas.get(i);
					MessageVO vo = new MessageVO();
					vo.setAppName(data.get("app_name").toString());
					vo.setLanguage(data.get("language").toString());
					vo.setName(data.get("name").toString());
					vo.setValue(data.get("value").toString());
					UnieapCacheMgt.messageList.put(vo.getName(), vo);
				}
				log.info("load message end...");
			}
		}
	}
}
