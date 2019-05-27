package com.unieap.base.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.db.DBManager;
import com.unieap.base.db.EntityRowMapper;
import com.unieap.base.inf.vo.InfConfigVO;
import com.unieap.base.inf.vo.InfFieldVO;
import com.unieap.base.utils.XmlJSONUtils;

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

	public void loadEsbInfFieldConfig() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id,inf_code as infCode,seq,inf_code as infCode,field_name as fieldName,");
		sql.append("field_display_name as fieldDisplayName,field_type as fieldType,");
		sql.append("xpath,ns,parent_id as parentId,leaf_flag as leafFlag,class_name as className,remark ");
		sql.append("FROM unieap.esb_inf_config_field");
		sql.append(" where parent_id is null order by inf_code");
		List<?> datas = DBManager.getJT().query(sql.toString(), new EntityRowMapper(InfFieldVO.class));
		if (datas != null && datas.size() > 0) {
			List<InfFieldVO> roots = (List<InfFieldVO>) datas;
			for (InfFieldVO root : roots) {
				InfConfigVO infConfigVO = UnieapCacheMgt.getInfHandlerList().get(root.getInfCode());
				List<InfFieldVO> fields = new ArrayList<InfFieldVO>();
				Map<String, String> nSList = new HashMap<String, String>();
				infConfigVO.setNSList(nSList);
				fields.add(root);
				while (fields.size() > 0) {
					String nsAlias = null;
					InfFieldVO fieldVO = fields.get(0);
					UnieapCacheMgt.getInfFieldList().put(fieldVO.getId().toString(), fieldVO);
					if (fieldVO.getParentId() != null) {
						InfFieldVO parentVO = UnieapCacheMgt.getInfFieldList().get(fieldVO.getParentId().toString());
						fieldVO.setParentVO(parentVO);
					}
					if (fieldVO.getNs() != null) {
						nsAlias = XmlJSONUtils.getNSAlias(nSList, fieldVO.getNs());
					}
					if ("XML".equals(infConfigVO.getResultType())) {
						fieldVO.updateXMLXpath(nsAlias);
					}
					if ("JSON".equals(infConfigVO.getResultType())) {
						fieldVO.updateJSONXpath();
					}
					if (!fieldVO.isLeaf()) {
						List<InfFieldVO> childrenList = getChildrenList(fieldVO.getId());
						fieldVO.setChildrenList(childrenList);
						fields.addAll(childrenList);
					}
					fields.remove(fieldVO);
				}
			}
		}
	}

	public InfFieldVO getInfFieldVO(Integer id) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id,inf_code as infCode,seq,inf_code as infCode,field_name as fieldName,");
		sql.append("field_display_name as fieldDisplayName,field_type as fieldType,");
		sql.append("xpath,ns,parent_id as parentId,leaf_flag as leafFlag,class_name as className,remark ");
		sql.append("FROM unieap.esb_inf_config_field");
		sql.append(" where id = ?");
		List<?> datas = DBManager.getJT().query(sql.toString(), new Object[] { id },
				new EntityRowMapper(InfFieldVO.class));
		if (datas != null && datas.size() > 0) {
			return (InfFieldVO) datas.get(0);
		}
		return null;
	}

	public List<InfFieldVO> getChildrenList(Integer id) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id,inf_code as infCode,seq,inf_code as infCode,field_name as fieldName,");
		sql.append("field_display_name as fieldDisplayName,field_type as fieldType,");
		sql.append("xpath,ns,parent_id as parentId,leaf_flag as leafFlag,class_name as className,remark ");
		sql.append("FROM unieap.esb_inf_config_field");
		sql.append(" where parent_id = ?");
		List<?> datas = DBManager.getJT().query(sql.toString(), new Object[] { id },
				new EntityRowMapper(InfFieldVO.class));
		List<InfFieldVO> childrenList = (List<InfFieldVO>) datas;
		return childrenList;
	}

}
