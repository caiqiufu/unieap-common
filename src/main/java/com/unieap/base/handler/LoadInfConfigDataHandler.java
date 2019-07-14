package com.unieap.base.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import com.unieap.base.SYSConfig;
import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.db.DBManager;
import com.unieap.base.db.EntityRowMapper;
import com.unieap.base.inf.vo.InfConfigVO;
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
				vo.setBizTransformMessageHandler(getBizTransformMessageHandler(vo.getInfCode()));
				UnieapCacheMgt.getInfHandlerList().put(vo.getInfCode(), vo);
			}
		}
	}

	public Map<String, String> getBizTransformMessageHandler(String infCode) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM esb_inf_config_transform where inf_code =? and activate_flag = 'Y'");
		List<?> datas = DBManager.getJT().queryForList(sql.toString(), new Object[] { infCode });
		if (datas != null && datas.size() > 0) {
			Map<String, String> transfers = new HashMap<String, String>();
			Map<String, Object> data = (Map<String, Object>) datas.get(0);
			transfers.put((String) data.get("biz_code"), (String) data.get("transform_message_handler"));
			return transfers;
		}
		return null;
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
			return getFinialNumberFilter(vos);
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
			for (String key : numberRoute.keySet()) {
				numberRoute.put(key, getFinialNumberRoute(numberRoute.get(key)));
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

	public List<NumberRouteVO> getFinialNumberRoute(List<NumberRouteVO> existingList) {
		if (existingList.size() > 0) {
			List<NumberRouteVO> datas = new CopyOnWriteArrayList<NumberRouteVO>();
			datas.add((NumberRouteVO) existingList.get(0).clone());
			while (true) {
				for (NumberRouteVO vo : datas) {
					for (NumberRouteVO existingVo : existingList) {
						long numberStart = Long.parseLong(vo.getNumberStart());
						long numberEnd = Long.parseLong(vo.getNumberEnd());
						long existingNumberStart = Long.parseLong(existingVo.getNumberStart());
						long existingNumberEnd = Long.parseLong(existingVo.getNumberEnd());
						if (existingNumberStart < numberStart || existingNumberStart > numberEnd) {
							if (!isExistingRoute(datas, existingVo)) {
								datas.add((NumberRouteVO) existingVo.clone());
							}
						}
						if (existingNumberStart > numberStart && existingNumberStart < numberEnd
								&& existingNumberEnd > numberEnd) {
							vo.setNumberEnd(existingVo.getNumberEnd());
							deleteSameNumberRouteVO(datas, vo);
						}
						if (existingNumberEnd > numberStart && existingNumberEnd < numberEnd
								&& existingNumberStart < numberStart) {
							vo.setNumberStart(existingVo.getNumberStart());
							deleteSameNumberRouteVO(datas, vo);
						}
						if ((existingNumberStart >= numberStart && existingNumberEnd < numberEnd)
								|| (existingNumberStart > numberStart && existingNumberEnd <= numberEnd)) {
							deleteSameNumberRouteVO(datas, vo);
						}
					}
				}
				if (verifyNoOverlappingRoute(datas)) {
					break;
				}
			}
			return datas;
		}
		return null;
	}

	private void deleteSameNumberRouteVO(List<NumberRouteVO> datas, NumberRouteVO vo) {
		for (NumberRouteVO data : datas) {
			long numberStart = Long.parseLong(vo.getNumberStart());
			long numberEnd = Long.parseLong(vo.getNumberEnd());
			long existingNumberStart = Long.parseLong(data.getNumberStart());
			long existingNumberEnd = Long.parseLong(data.getNumberEnd());
			if (numberStart == existingNumberStart && numberEnd == existingNumberEnd) {
				datas.remove(data);
			}
		}
	}

	private boolean verifyNoOverlappingRoute(List<NumberRouteVO> datas) {
		if (datas != null && datas.size() > 0) {
			for (NumberRouteVO ovo : datas) {
				long oNumberStart = Long.parseLong(ovo.getNumberStart());
				long oNumberEnd = Long.parseLong(ovo.getNumberEnd());
				for (NumberRouteVO cvo : datas) {
					long cNumberStart = Long.parseLong(cvo.getNumberStart());
					long cNumberEnd = Long.parseLong(cvo.getNumberEnd());
					// 被包含
					if (oNumberStart > cNumberStart && oNumberEnd < cNumberEnd) {
						return false;
					}
					// 左包含
					if (oNumberStart > cNumberStart && oNumberStart < cNumberEnd) {
						return false;
					}
					// 右包含
					if (oNumberEnd > cNumberStart && oNumberEnd < cNumberEnd) {
						return false;
					}
					// 包含其他
					if (oNumberStart < cNumberStart && oNumberEnd > cNumberEnd) {
						return false;
					}
				}
			}
			return true;
		}
		return true;
	}

	private boolean isExistingRoute(List<NumberRouteVO> datas, NumberRouteVO vo) {
		for (NumberRouteVO data : datas) {
			long numberStart = Long.parseLong(vo.getNumberStart());
			long numberEnd = Long.parseLong(vo.getNumberEnd());
			long existingNumberStart = Long.parseLong(data.getNumberStart());
			long existingNumberEnd = Long.parseLong(data.getNumberEnd());
			if (numberStart == existingNumberStart && numberEnd == existingNumberEnd) {
				return true;
			}
		}
		return false;
	}

	public List<NumberFilterVO> getFinialNumberFilter(List<NumberFilterVO> existingList) {
		if (existingList.size() > 0) {
			List<NumberFilterVO> datas = new CopyOnWriteArrayList<NumberFilterVO>();
			datas.add((NumberFilterVO) existingList.get(0).clone());
			while (true) {
				for (NumberFilterVO vo : datas) {
					for (NumberFilterVO existingVo : existingList) {
						long numberStart = Long.parseLong(vo.getNumberStart());
						long numberEnd = Long.parseLong(vo.getNumberEnd());
						long existingNumberStart = Long.parseLong(existingVo.getNumberStart());
						long existingNumberEnd = Long.parseLong(existingVo.getNumberEnd());
						if (existingNumberStart < numberStart || existingNumberStart > numberEnd) {
							if (!isExistingFilter(datas, existingVo)) {
								datas.add((NumberFilterVO) existingVo.clone());
							}
						}
						if (existingNumberStart > numberStart && existingNumberStart < numberEnd
								&& existingNumberEnd > numberEnd) {
							vo.setNumberEnd(existingVo.getNumberEnd());
							deleteSameNumberFilterVO(datas, vo);
						}
						if (existingNumberEnd > numberStart && existingNumberEnd < numberEnd
								&& existingNumberStart < numberStart) {
							vo.setNumberStart(existingVo.getNumberStart());
							deleteSameNumberFilterVO(datas, vo);
						}
						if ((existingNumberStart >= numberStart && existingNumberEnd < numberEnd)
								|| (existingNumberStart > numberStart && existingNumberEnd <= numberEnd)) {
							deleteSameNumberFilterVO(datas, vo);
						}
					}
				}
				if (verifyNoOverlappingFilter(datas)) {
					break;
				}
			}
			return datas;
		}
		return null;
	}

	private void deleteSameNumberFilterVO(List<NumberFilterVO> datas, NumberFilterVO vo) {
		for (NumberFilterVO data : datas) {
			long numberStart = Long.parseLong(vo.getNumberStart());
			long numberEnd = Long.parseLong(vo.getNumberEnd());
			long existingNumberStart = Long.parseLong(data.getNumberStart());
			long existingNumberEnd = Long.parseLong(data.getNumberEnd());
			if (numberStart == existingNumberStart && numberEnd == existingNumberEnd) {
				datas.remove(data);
			}
		}
	}

	private boolean verifyNoOverlappingFilter(List<NumberFilterVO> datas) {
		if (datas != null && datas.size() > 0) {
			for (NumberFilterVO ovo : datas) {
				long oNumberStart = Long.parseLong(ovo.getNumberStart());
				long oNumberEnd = Long.parseLong(ovo.getNumberEnd());
				for (NumberFilterVO cvo : datas) {
					long cNumberStart = Long.parseLong(cvo.getNumberStart());
					long cNumberEnd = Long.parseLong(cvo.getNumberEnd());
					// 被包含
					if (oNumberStart > cNumberStart && oNumberEnd < cNumberEnd) {
						return false;
					}
					// 左包含
					if (oNumberStart > cNumberStart && oNumberStart < cNumberEnd) {
						return false;
					}
					// 右包含
					if (oNumberEnd > cNumberStart && oNumberEnd < cNumberEnd) {
						return false;
					}
					// 包含其他
					if (oNumberStart < cNumberStart && oNumberEnd > cNumberEnd) {
						return false;
					}
				}
			}
			return true;
		}
		return true;
	}

	private boolean isExistingFilter(List<NumberFilterVO> datas, NumberFilterVO vo) {
		for (NumberFilterVO data : datas) {
			long numberStart = Long.parseLong(vo.getNumberStart());
			long numberEnd = Long.parseLong(vo.getNumberEnd());
			long existingNumberStart = Long.parseLong(data.getNumberStart());
			long existingNumberEnd = Long.parseLong(data.getNumberEnd());
			if (numberStart == existingNumberStart && numberEnd == existingNumberEnd) {
				return true;
			}
		}
		return false;
	}
}
