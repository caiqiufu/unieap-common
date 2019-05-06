package com.unieap.base.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.db.DBManager;
import com.unieap.base.db.EntityRowMapper;
import com.unieap.base.inf.transform.BizFieldVO;
import com.unieap.base.inf.transform.BizMessageVO;
import com.unieap.base.inf.transform.InfFieldVO;

@Service
public class LoadBizMessageConfigDataHandler implements ConfigHandler {
	private final Log logger = LogFactory.getLog(LoadBizMessageConfigDataHandler.class);

	@Override
	public void deal(Map<String, Object> parameters) throws Exception {
		// TODO Auto-generated method stub
		UnieapCacheMgt.setBizMessageList(getBizMessageVO());
	}

	@Override
	public boolean reload() {
		// TODO Auto-generated method stub
		return false;
	}

	public Map<String, BizMessageVO> getBizMessageVO() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT cr.biz_code,cr.number_start,cr.number_end,cr.route_type  ");
		sql.append("FROM unieap.esb_inf_config_route cr,esb_biz_config_field bcf ");
		sql.append("where cr.activate_flag = 'Y' and cr.biz_code = bcf.biz_code ");
		sql.append("group by cr.biz_code,cr.number_start,cr.number_end,cr.route_type");
		List<Map<String, Object>> datas = DBManager.getJT().queryForList(sql.toString());
		Map<String, BizMessageVO> bizMessageList = new HashMap<String, BizMessageVO>();
		if (datas != null && datas.size() > 0) {
			for (Map<String, Object> data : datas) {
				String bizCode = (String) data.get("biz_code");
				String numberStart = (String) data.get("number_start");
				String numberEnd = (String) data.get("number_end");
				String routeType = (String) data.get("route_type");
				BizMessageVO bizMessageVO = new BizMessageVO();
				BizFieldVO bizFieldVO = getRootFieldVO(bizCode, numberStart, numberEnd);
				bizMessageVO.setBizCode(bizCode);
				bizMessageVO.setRootFieldVO(bizFieldVO);
				bizMessageVO.setFieldList(getAllBizFieldList(bizCode, numberStart, numberEnd));
				if ("*".equals(numberStart) || "-1".equals(numberStart)) {
					numberStart = "1";
				}
				if ("*".equals(numberEnd) || "-1".equals(numberEnd)) {
					numberEnd = "9223372036854775807";
				}
				String key = bizCode + "_" + numberStart + "_" + numberEnd + "_" + routeType;
				bizMessageList.put(key, bizMessageVO);
			}
		}
		return bizMessageList;
	}

	public BizFieldVO getRootFieldVO(String bizCode, String numberStart, String numberEnd) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT bcf.id,icr.biz_code as bizCode,bcf.seq,");
		sql.append("bcf.inf_field_id as infFieldId,bcf.field_name as fieldName,");
		sql.append("bcf.field_display_name as fieldDispalyName,bcf.field_type as fieldType,");
		sql.append("bcf.optional_flag as optionalFlag,bcf.xpath,bcf.ns,bcf.parent_id as parentId ,");
		sql.append("bcf.leaf_flag as leafFlag,bcf.class_name as className,bcf.remark ");
		sql.append("from esb_inf_config ic, esb_inf_config_field icf,");
		sql.append("esb_biz_config_field bcf, esb_inf_config_route icr ");
		sql.append(" where ic.inf_code = icf.inf_code and bcf.inf_field_id = icf.id ");
		sql.append(" and icr.biz_code = bcf.biz_code and icr.inf_code = ic.inf_code ");
		sql.append(" and icr.biz_code = ? and icr.number_start = ? and icr.number_end = ? ");
		sql.append(" and bcf.parent_id is null order by icr.biz_code");
		List<?> rootVOList = DBManager.getJT().query(sql.toString(), new Object[] { bizCode, numberStart, numberEnd },
				new EntityRowMapper(BizFieldVO.class));
		if (rootVOList != null && rootVOList.size() > 0) {
			BizFieldVO rootVO = (BizFieldVO) rootVOList.get(0);
			rootVO.setChildrenList(getChildrenList(rootVO.getBizCode(), rootVO.getId(), numberStart, numberEnd));
			rootVO.setInfFieldVO(getInfField(rootVO.getInfFieldId()));
			List<BizFieldVO> fields = new ArrayList<BizFieldVO>();
			fields.add(rootVO);
			while (fields.size() > 0) {
				BizFieldVO fieldVO = fields.get(0);
				logger.debug("============================================================================");
				logger.debug(fieldVO.toJsonString());
				fieldVO.setInfFieldVO(getInfField(fieldVO.getInfFieldId()));
				BizFieldVO parentVO = getBizFieldVO(fieldVO.getBizCode(), fieldVO.getParentId(), numberStart,
						numberEnd);
				if (parentVO != null) {
					fieldVO.setParentId(parentVO.getId());
					fieldVO.setParentVO(parentVO);
				}
				if (!fieldVO.isLeaf()) {
					List<BizFieldVO> childrenList = getChildrenList(fieldVO.getBizCode(), fieldVO.getId(), numberStart,
							numberEnd);
					fieldVO.setChildrenList(childrenList);
					fields.addAll(childrenList);
				}
				fields.remove(fieldVO);
			}
			return rootVO;
		}
		return null;
	}

	public List<BizFieldVO> getChildrenList(String bizCode, Integer id, String numberStart, String numberEnd) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT bcf.id,icr.biz_code as bizCode,bcf.seq,");
		sql.append("bcf.inf_field_id as infFieldId,bcf.field_name as fieldName,");
		sql.append("bcf.field_display_name as fieldDispalyName,bcf.field_type as fieldType,");
		sql.append("bcf.optional_flag as optionalFlag,bcf.xpath,bcf.ns,bcf.parent_id as parentId ,");
		sql.append("bcf.leaf_flag as leafFlag,bcf.class_name as className,bcf.remark ");
		sql.append("from esb_inf_config ic, esb_inf_config_field icf,");
		sql.append("esb_biz_config_field bcf, esb_inf_config_route icr ");
		sql.append("where ic.inf_code = icf.inf_code and bcf.inf_field_id = icf.id ");
		sql.append("and icr.biz_code = bcf.biz_code and icr.inf_code = ic.inf_code ");
		sql.append("and icr.biz_code = ? and icr.number_start = ? and icr.number_end = ? and bcf.parent_id =? ");
		List<?> childrenList = DBManager.getJT().query(sql.toString(),
				new Object[] { bizCode, numberStart, numberEnd, id }, new EntityRowMapper(BizFieldVO.class));
		List<BizFieldVO> volist = (List<BizFieldVO>) childrenList;
		if (volist != null && volist.size() > 0) {
			BizFieldVO vo = (BizFieldVO) volist.get(0);
			vo.setInfFieldVO(getInfField(vo.getInfFieldId()));
			return volist;
		}
		return null;
	}

	public BizFieldVO getBizFieldVO(String bizCode, Integer id, String numberStart, String numberEnd) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT bcf.id,icr.biz_code as bizCode,bcf.seq,");
		sql.append("bcf.inf_field_id as infFieldId,bcf.field_name as fieldName,");
		sql.append("bcf.field_display_name as fieldDispalyName,bcf.field_type as fieldType,");
		sql.append("bcf.optional_flag as optionalFlag,bcf.xpath,bcf.ns,bcf.parent_id as parentId ,");
		sql.append("bcf.leaf_flag as leafFlag,bcf.class_name as className,bcf.remark ");
		sql.append("from esb_inf_config ic, esb_inf_config_field icf,");
		sql.append("esb_biz_config_field bcf, esb_inf_config_route icr ");
		sql.append("where ic.inf_code = icf.inf_code and bcf.inf_field_id = icf.id ");
		sql.append("and icr.biz_code = bcf.biz_code and icr.inf_code = ic.inf_code ");
		sql.append("and icr.biz_code = ? and icr.number_start = ? and icr.number_end = ? and bcf.id =? ");
		List<?> vos = DBManager.getJT().query(sql.toString(), new Object[] { bizCode, numberStart, numberEnd, id },
				new EntityRowMapper(BizFieldVO.class));
		if (vos != null && vos.size() > 0) {
			BizFieldVO vo = (BizFieldVO) vos.get(0);
			vo.setInfFieldVO(getInfField(vo.getInfFieldId()));
			return vo;
		}
		return null;
	}

	public InfFieldVO getInfField(Integer infFieldId) {
		return UnieapCacheMgt.getInfFieldVO(infFieldId.toString());
	}

	public Map<String, BizFieldVO> getAllBizFieldList(String bizCode, String numberStart, String numberEnd) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT bcf.id,icr.biz_code as bizCode,bcf.seq,");
		sql.append("bcf.inf_field_id as infFieldId,bcf.field_name as fieldName,");
		sql.append("bcf.field_display_name as fieldDispalyName,bcf.field_type as fieldType,");
		sql.append("bcf.optional_flag as optionalFlag,bcf.xpath,bcf.ns,bcf.parent_id as parentId ,");
		sql.append("bcf.leaf_flag as leafFlag,bcf.class_name as className,bcf.remark ");
		sql.append("from esb_inf_config ic, esb_inf_config_field icf,");
		sql.append("esb_biz_config_field bcf, esb_inf_config_route icr ");
		sql.append("where ic.inf_code = icf.inf_code and bcf.inf_field_id = icf.id ");
		sql.append("and icr.biz_code = bcf.biz_code and icr.inf_code = ic.inf_code ");
		sql.append("and icr.biz_code = ? and icr.number_start = ? and icr.number_end = ? ");
		List<?> datas = DBManager.getJT().query(sql.toString(), new Object[] { bizCode, numberStart, numberEnd },
				new EntityRowMapper(BizFieldVO.class));
		Map<String, BizFieldVO> bizFieldList = new HashMap<String, BizFieldVO>();
		if (datas != null && datas.size() > 0) {
			for (Object vo : datas) {
				BizFieldVO vob = (BizFieldVO) vo;
				BizFieldVO parentVO = getBizFieldVO(vob.getBizCode(), vob.getParentId(), numberStart, numberEnd);
				if (parentVO != null) {
					vob.setParentId(parentVO.getId());
					vob.setParentVO(parentVO);
				}
				vob.setInfFieldId(vob.getInfFieldId());
				vob.setInfFieldVO(getInfField(vob.getInfFieldId()));
				vob.setChildrenList(getChildrenList(vob.getBizCode(), vob.getId(), numberStart, numberEnd));
				bizFieldList.put(vob.getId().toString(), vob);
			}
		}
		return bizFieldList;
	}
}
