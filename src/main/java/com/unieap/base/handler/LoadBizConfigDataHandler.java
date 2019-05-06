package com.unieap.base.handler;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unieap.base.SYSConfig;
import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.db.DBManager;
import com.unieap.base.db.EntityRowMapper;
import com.unieap.base.vo.BizConfigVO;
import com.unieap.base.vo.InfConfigVO;

@Service
public class LoadBizConfigDataHandler implements ConfigHandler {
	@Autowired
	LoadInfConfigDataHandler loadInfConfigDataHandler;

	@Override
	public void deal(Map<String, Object> parameters) throws Exception {
		// TODO Auto-generated method stub
		loadEsbBizConfig();
	}

	@Override
	public boolean reload() {
		// TODO Auto-generated method stub
		return false;
	}

	public void loadEsbBizConfig() {
		StringBuffer sql = new StringBuffer();
		sql.append("select id, biz_code as bizCode,biz_desc as bizDesc, app_name as appName,");
		sql.append("class_name as className,SOAP_Action as SOAPAction,url,ext_parameters as extParameters,");
		sql.append("transform_message_handler as transformMessageHandler,activate_flag as activateFlag,");
		sql.append("access_name as accessName,password, error_code_ignore as errorCodeIgnore,");
		sql.append("success_code as successCode,response_sample as responseSample,timeout ");
		sql.append(" from esb_biz_config where activate_flag ='Y' and tenant_id = ?");
		List<?> datas = DBManager.getJT().query(sql.toString(), new Object[] { SYSConfig.getConfig().get("tenantId") },
				new EntityRowMapper(BizConfigVO.class));
		if (datas != null && datas.size() > 0) {
			List<BizConfigVO> volist = (List<BizConfigVO>) datas;
			if (datas != null && datas.size() > 0) {
				for (BizConfigVO vo : volist) {
					vo.setDependInfCodeList(dependInfCodeList(vo.getBizCode()));
					UnieapCacheMgt.getBizHandlerList().put(vo.getBizCode(), vo);
				}
			}
		}
	}

	private List<InfConfigVO> dependInfCodeList(String bizCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ic.id,ic.inf_code as infCode,ic.inf_name as infName,ic.source_app_name as sourceAppName,");
		sql.append("ic.dest_app_name as destAppName,ic.url,ic.SOAP_Action as SOAPAction,");
		sql.append("ic.transform_message_handler as transformMessageHandler,");
		sql.append("ic.result_type as resultType,ic.ext_parameters as extParameters,");
		sql.append("ic.class_name as className,ic.activate_flag as activateFlag,ic.access_name as accessName,");
		sql.append("ic.password, ic.error_code_ignore as errorCodeIgnore,ic.success_code as successCode,");
		sql.append("ic.response_sample as responseSample,ic.timeout ");
		sql.append(" FROM esb_biz_config_inf bci,esb_inf_config ic ");
		sql.append(" where bci.inf_code = ic.inf_code and bci.biz_code = ? and bci.activate_flag ='Y' ");
		sql.append(" order by bci.seq asc ");
		List<?> datas = DBManager.getJT().query(sql.toString(), new Object[] { bizCode },
				new EntityRowMapper(InfConfigVO.class));
		if (datas != null && datas.size() > 0) {
			List<InfConfigVO> volist = (List<InfConfigVO>) datas;
			for (InfConfigVO vo : volist) {
				vo.setNumberRoute(loadInfConfigDataHandler.getNumberRoute(vo.getInfCode()));
				vo.setNumberFilterList(loadInfConfigDataHandler.getNumberFilterList(vo.getInfCode()));
				vo.setNumberRouteList(loadInfConfigDataHandler.getNumberRouteList(vo.getInfCode()));
				vo.setNSList(loadInfConfigDataHandler.getNSList(vo.getInfCode()));
			}
			return volist;
		}
		return null;
	}
}
