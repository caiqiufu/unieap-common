package com.unieap.base.handler;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.db.DBManager;
import com.unieap.base.db.EntityRowMapper;
import com.unieap.base.inf.transform.InfFieldVO;

@Service
public class LoadInfFieldConfigDataHandler implements ConfigHandler {

	@Override
	public void deal(Map<String, Object> parameters) throws Exception {
		// TODO Auto-generated method stub
		loadEsbInfFieldConfig();
	}

	@Override
	public boolean reload() {
		// TODO Auto-generated method stub
		return false;
	}

	public void loadEsbInfFieldConfig() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id,inf_code as infCode,seq,inf_code as infCode,field_name as fieldName,");
		sql.append("field_display_name as fieldDisplayName,field_type as fieldType,");
		sql.append("xpath,parent_id as parentId,leaf_flag as leafFlag,remark ");
		sql.append("FROM unieap.esb_inf_config_field");
		List<?> datas = DBManager.getJT().query(sql.toString(), new EntityRowMapper(InfFieldVO.class));
		if (datas != null && datas.size() > 0) {
			List<InfFieldVO> volist = (List<InfFieldVO>) datas;
			for (InfFieldVO vo : volist) {
				UnieapCacheMgt.getInfFieldList().put(vo.getId().toString(), vo);
			}
		}
	}

}
