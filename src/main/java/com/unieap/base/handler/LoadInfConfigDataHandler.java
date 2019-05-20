package com.unieap.base.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.unieap.base.SYSConfig;
import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.db.DBManager;
import com.unieap.base.db.EntityRowMapper;
import com.unieap.base.vo.InfConfigNSVO;
import com.unieap.base.vo.InfConfigVO;
import com.unieap.base.vo.NumberFilterVO;
import com.unieap.base.vo.NumberRouteVO;

@Service
public class LoadInfConfigDataHandler implements ConfigHandler {

	@Override
	public void deal(Map<String, Object> parameters) throws Exception {
		// TODO Auto-generated method stub
		loadEsbInfConfig();
	}

	@Override
	public boolean reload() {
		// TODO Auto-generated method stub
		return false;
	}

	public void loadEsbInfConfig() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ic.id,ic.inf_code as infCode,inf_name as infName,source_app_name as sourceAppName,");
		sql.append("dest_app_name as destAppName,url,SOAP_Action as SOAPAction,");
		sql.append("transform_message_handler as transformMessageHandler,");
		sql.append("result_type as resultType,ext_parameters as extParameters,");
		sql.append("class_name as className,activate_flag as activateFlag,access_name as accessName,");
		sql.append("password, error_code_ignore as errorCodeIgnore,success_code as successCode,");
		sql.append("response_sample as responseSample,timeout ");
		sql.append(" FROM esb_inf_config ic ");
		sql.append(" where activate_flag ='Y'");
		List<?> datas = DBManager.getJT().query(sql.toString(), new EntityRowMapper(InfConfigVO.class));
		if (datas != null && datas.size() > 0) {
			List<InfConfigVO> volist = (List<InfConfigVO>) datas;
			for (InfConfigVO vo : volist) {
				vo.setNumberRoute(getNumberRoute(vo.getInfCode()));
				vo.setNumberFilterList(getNumberFilterList(vo.getInfCode()));
				vo.setNumberRouteList(getNumberRouteList(vo.getInfCode()));
				vo.setNSList(getNSList(vo.getInfCode()));
				vo.setBizTransformMessageHandler(getBizTransformMessageHandler(vo.getInfCode()));
				UnieapCacheMgt.getInfHandlerList().put(vo.getInfCode(), vo);
			}
		}
	}

	public Map<String, String> getBizTransformMessageHandler(String infCode) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM esb_inf_config_transform where inf_code =? and activate_flag = 'Y'");
		List<?> datas = DBManager.getJT().queryForList(sql.toString(), new Object[] {infCode });
		if(datas!=null&&datas.size()>0) {
			Map<String,String> transfers = new HashMap<String,String>();
			Map<String, Object> data = (Map<String, Object>) datas.get(0);
			transfers.put((String) data.get("biz_code"), (String) data.get("transform_message_handler"));
			return transfers;
		}
		return null;
	}
	public List<InfConfigNSVO> getNSList(String infCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT id, inf_code as infCode, ns, alias FROM esb_inf_config_field_ns where inf_code=?");
		List<?> childrenList = DBManager.getJT().query(sql.toString(), new Object[] { infCode },
				new EntityRowMapper(InfConfigNSVO.class));
		List<InfConfigNSVO> vos = (List<InfConfigNSVO>) childrenList;
		return vos;
	}

	public List<NumberFilterVO> getNumberFilterList(String infCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("select id,inf_code as infCode,number_start as numberStart,");
		sql.append("number_end as numberEnd,response_sample as responseSample,");
		sql.append("ext_parameters as extParameters ");
		sql.append(" from esb_inf_config_filter where activate_flag='Y' and inf_code = ?");
		List<?> datas = DBManager.getJT().query(sql.toString(), new Object[] { infCode },
				new EntityRowMapper(NumberFilterVO.class));
		if (datas != null && datas.size() > 0) {
			List<NumberFilterVO> vos = (List<NumberFilterVO>) datas;
			return vos;
		}
		return null;
	}

	public Map<String, List<NumberRouteVO>> getNumberRoute(String infCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("select id,biz_code as bizCode,inf_code as infCode,number_start as numberStart,");
		sql.append("number_end as numberEnd, ext_parameters as extParameters,route_type as routeType ");
		sql.append("from esb_inf_config_route ");
		sql.append("where activate_flag='Y' and tenant_id =? and inf_code=?");
		List<?> datas = DBManager.getJT().query(sql.toString(),
				new Object[] { SYSConfig.getConfig().get("tenantId"), infCode },
				new EntityRowMapper(NumberRouteVO.class));
		if (datas != null && datas.size() > 0) {
			List<NumberRouteVO> vos = (List<NumberRouteVO>) datas;
			Map<String, List<NumberRouteVO>> numberRoute = new HashMap<String, List<NumberRouteVO>>();
			List<NumberRouteVO> numberRouteList;
			for (NumberRouteVO vo : vos) {
				numberRouteList = numberRoute.get(vo.getBizCode());
				if (numberRouteList == null) {
					numberRouteList = new ArrayList<NumberRouteVO>();
					numberRoute.put(vo.getBizCode(), numberRouteList);
				}
				numberRouteList.add(vo);
			}
			return numberRoute;
		}
		return null;
	}

	public List<NumberRouteVO> getNumberRouteList(String infCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("select id,biz_code as bizCode,inf_code as infCode,number_start as numberStart,");
		sql.append("number_end as numberEnd, ext_parameters as extParameters,route_type as routeType ");
		sql.append("from esb_inf_config_route ");
		sql.append("where activate_flag='Y' and tenant_id =? and inf_code=?");
		List<?> datas = DBManager.getJT().query(sql.toString(),
				new Object[] { SYSConfig.getConfig().get("tenantId"), infCode },
				new EntityRowMapper(NumberRouteVO.class));
		if (datas != null && datas.size() > 0) {
			List<NumberRouteVO> vos = (List<NumberRouteVO>) datas;
			return vos;
		}
		return null;
	}
}
