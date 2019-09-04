package com.unieap.base.inf.handler;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.UnieapConstants;
import com.unieap.base.db.DBManager;
import com.unieap.base.inf.element.RequestInfo;
import com.unieap.base.inf.element.ResponseHeader;
import com.unieap.base.inf.unitls.SoapCallUtils;
import com.unieap.base.inf.vo.InfConfigVO;
import com.unieap.base.inf.vo.InfSQLConfigVO;
import com.unieap.base.pojo.Esblog;
import com.unieap.base.repository.EsbLogCacheMgt;
import com.unieap.base.utils.DateUtils;
import com.unieap.base.vo.NumberFilterVO;
import com.unieap.base.vo.PaginationSupport;

import net.sf.json.JSONObject;

/**
 * 主要是流程编排逻辑
 * 
 * @author Chai
 *
 */
public class ConnectionHandler {

	@Value("${spring.profiles.active}")
	public String springProfilesActive;

	public Logger log = Logger.getLogger(ConnectionHandler.class);

	/**
	 * call http request and record log
	 * 
	 * @param appName
	 * @param requestInfo
	 * @param requestInfoString
	 * @param extParameters
	 * @return
	 * @throws Exception
	 */
	public ProcessResult callCommonHTTPService(String appName, RequestInfo requestInfo, String requestInfoString,
			Map<String, Object> extParameters) throws Exception {
		long beginTime = System.currentTimeMillis();
		extParameters.put(UnieapConstants.REQUEST_MESSAGE, requestInfoString);
		requestInfo.getRequestHeader().setRequestTime(DateUtils.getStringDate());
		ProcessResult processResult = new ProcessResult();
		String responseInfoString = "";
		try {
			InfConfigVO infConfigVO = UnieapCacheMgt.getInfHandler(requestInfo.getRequestBody().getBizCode());
			boolean filter = this.isFilterNumber(requestInfo.getRequestBody().getServiceNumber(),
					infConfigVO.getInfCode());
			if (filter) {
				responseInfoString = new String(infConfigVO.getResponseSample(), "UTF-8");
				processResult.setResultCode("20011");
				processResult.setResultDesc(UnieapConstants.getMessage("20011",
						new String[] { requestInfo.getRequestBody().getServiceNumber() }));
			} else {
				responseInfoString = SoapCallUtils.callService(infConfigVO, infConfigVO.getSOAPAction(),
						requestInfoString);
				processResult.setResultCode(UnieapConstants.C0);
				processResult.setResultDesc(UnieapConstants.SUCCESS);
			}
			extParameters.put(UnieapConstants.RESPONSE_MESSAGE, responseInfoString);
			processResult.setVo(responseInfoString);
		} catch (Exception e) {
			processResult.setResultCode("20006");
			processResult.setResultDesc(e.getLocalizedMessage());
			long endTime = System.currentTimeMillis();
			String during = Long.toString(endTime - beginTime);
			Esblog esblog = BizServiceUtils.getEsbLog(requestInfo, processResult, requestInfoString, responseInfoString,
					during, appName, (String) extParameters.get("ProcessServerInfo"));
			EsbLogCacheMgt.setEsbLogVO(esblog);
			return processResult;
		}
		try {
		} catch (Exception e) {
			processResult.setResultCode(UnieapConstants.C99999);
			processResult.setResultDesc(e.getLocalizedMessage());
			requestInfo.getRequestHeader().setResponseTime(DateUtils.getStringDate());
			long endTime = System.currentTimeMillis();
			String during = Long.toString(endTime - beginTime);
			Esblog esblog = BizServiceUtils.getEsbLog(requestInfo, processResult, requestInfoString, responseInfoString,
					during, appName, (String) extParameters.get("ProcessServerInfo"));
			EsbLogCacheMgt.setEsbLogVO(esblog);
		}
		requestInfo.getRequestHeader().setResponseTime(DateUtils.getStringDate());
		return processResult;
	}

	public ProcessResult callCommonSQLService(String appName, RequestInfo requestInfo, InfConfigVO infConfigVO,
			InfSQLConfigVO infSQLConfigVO, PaginationSupport ps, Map<String, Object> extParameters) throws Exception {
		String pageSize = "100";
		String startIndex = "0";
		String orderBy = "";
		String queryParaString = "";
		if (!StringUtils.isEmpty(requestInfo.getRequestBody().getExtParameters())) {
			JSONObject jsonResult = JSONObject.fromObject(requestInfo.getRequestBody().getExtParameters());
			if (jsonResult.containsKey("pageSize")) {
				pageSize = jsonResult.getString("pageSize");
			}
			if (jsonResult.containsKey("startIndex")) {
				startIndex = jsonResult.getString("startIndex");
			}
			if (jsonResult.containsKey("orderBy")) {
				orderBy = jsonResult.getString("orderBy");
			}
			if (!StringUtils.isEmpty(orderBy)) {
				infSQLConfigVO.setOrderBy(orderBy);
			}
			if (jsonResult.containsKey("queryPara")) {
				queryParaString = jsonResult.getString("queryPara");
			}
			ps.setStartIndex(Integer.parseInt(startIndex));
			ps.setPageSize(Integer.parseInt(pageSize));
			if (!StringUtils.isEmpty(orderBy)) {
				infSQLConfigVO.setOrderBy(orderBy);
			}
		}
		String querySql = infSQLConfigVO.getQuerySql();
		if ("MYSQL".equals(infSQLConfigVO.getDsType())) {
			querySql = querySql + " limit " + ps.getStartIndex() + "," + ps.getPageSize();
		}
		if ("ORACLE".equals(infSQLConfigVO.getDsType())) {
			// to do
		}
		if (StringUtils.isEmpty(queryParaString)) {
			extParameters.put(UnieapConstants.REQUEST_MESSAGE, querySql);
			int totalCount = DBManager.getBizJT(infSQLConfigVO.getDsName()).queryForObject(infSQLConfigVO.getCountSql(),
					Integer.class);
			ps.setTotalCount(totalCount);
			List<Map<String, Object>> items = DBManager.getBizJT(infSQLConfigVO.getDsName()).queryForList(querySql);
			ps.setItems(items);
		} else {
			extParameters.put(UnieapConstants.REQUEST_MESSAGE, querySql + ";" + queryParaString);
			if (queryParaString.contains(",")) {
				Object[] queryPara = StringUtils.split(queryParaString, ",");
				int totalCount = DBManager.getBizJT(infSQLConfigVO.getDsName())
						.queryForObject(infSQLConfigVO.getCountSql(), queryPara, Integer.class);
				ps.setTotalCount(totalCount);
				List<Map<String, Object>> items = DBManager.getBizJT(infSQLConfigVO.getDsName()).queryForList(querySql,
						queryPara);
				ps.setItems(items);
			} else {
				Object[] queryPara = new Object[] { queryParaString };
				int totalCount = DBManager.getBizJT(infSQLConfigVO.getDsName())
						.queryForObject(infSQLConfigVO.getCountSql(), queryPara, Integer.class);
				ps.setTotalCount(totalCount);
				List<Map<String, Object>> items = DBManager.getBizJT(infSQLConfigVO.getDsName()).queryForList(querySql,
						queryPara);
				ps.setItems(items);
			}
		}
		extParameters.put(UnieapConstants.RESPONSE_MESSAGE, ps.toJsonString());
		ProcessResult processResult = new ProcessResult();
		processResult.setResultCode(UnieapConstants.C0);
		processResult.setResultDesc(UnieapConstants.SUCCESS);
		processResult.setVo(ps);
		return processResult;
	}

	/**
	 * 
	 * @param serviceNumber
	 * @param infCode
	 * @return
	 */
	public boolean isFilterNumber(String serviceNumber, String infCode) {
		if (getFilterNumber(serviceNumber, infCode) == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param serviceNumber
	 * @param infCode
	 * @return
	 */
	public NumberFilterVO getFilterNumber(String serviceNumber, String infCode) {
		InfConfigVO infConfigVO = UnieapCacheMgt.getInfHandler(infCode);
		List<NumberFilterVO> numberFilterList = infConfigVO.getNumberFilterList();
		if (numberFilterList == null) {
			return null;
		} else {
			for (NumberFilterVO numberFilterVO : numberFilterList) {
				if (isInFilterNumberRange(serviceNumber, numberFilterVO)) {
					return numberFilterVO;
				}
			}
			return null;
		}
	}

	/**
	 * 
	 * @param serviceNumber
	 * @param numberFilterVO
	 * @return
	 */
	public boolean isInFilterNumberRange(String serviceNumber, NumberFilterVO numberFilterVO) {
		long myServiceNumber = Long.parseLong(serviceNumber);
		String numberStart = numberFilterVO.getNumberStart();
		String numberEnd = numberFilterVO.getNumberEnd();
		if ("*".equals(numberStart) || "-1".equals(numberStart)) {
			numberStart = "1";
		}
		if ("*".equals(numberEnd) || "-1".equals(numberEnd)) {
			numberEnd = "9223372036854775807";
		}
		long startNumber = Long.parseLong(numberStart);
		long endNumber = Long.parseLong(numberStart);
		if (myServiceNumber >= startNumber && myServiceNumber <= endNumber) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param requestInfo
	 * @param serviceNumber
	 * @return
	 */
	public ResponseHeader verify(RequestInfo requestInfo, String serviceNumber) {
		if (!BizServiceUtils.requestLimitationCheck(requestInfo)) {
			ResponseHeader responseHeader = new ResponseHeader();
			responseHeader.setResultCode("20005");
			responseHeader.setResultDesc(UnieapConstants.getMessage("20005"));
			return responseHeader;
		}
		return null;
	}

	/**
	 * 
	 * @param processResult
	 */
	public void errorCodeIgnore(ProcessResult processResult) {
		String SUCCESSCODEList = processResult.getExtParameters().get(UnieapConstants.SUCCESS_CODE).toString();
		String SUCCESSCODE = SUCCESSCODEList.split(",")[0];
		String ERRORCODEIGNOREList = processResult.getExtParameters().get(UnieapConstants.ERRORCODE_IGNORE).toString();
		if (BizServiceUtils.errorCodeCheck(processResult.getResultCode(), ERRORCODEIGNOREList)) {
			processResult.setResultCode(SUCCESSCODE);
			processResult.setResultDesc(UnieapConstants.getMessage("SUCCESSCODE"));
		} else {
			processResult.setResultCode(processResult.getResultCode());
			processResult.setResultDesc(processResult.getResultDesc());
		}
	}
}
