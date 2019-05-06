package com.unieap.base.inf.handler;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.UnieapConstants;
import com.unieap.base.inf.element.RequestInfo;
import com.unieap.base.inf.element.ResponseHeader;
import com.unieap.base.inf.unitls.SoapCallUtils;
import com.unieap.base.pojo.Esblog;
import com.unieap.base.repository.EsbLogCacheMgt;
import com.unieap.base.utils.DateUtils;
import com.unieap.base.utils.JSONUtils;
import com.unieap.base.vo.InfConfigVO;
import com.unieap.base.vo.NumberFilterVO;

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
	 * @param appCode
	 * @param requestInfo
	 * @param extParameters
	 * @return
	 * @throws Exception
	 */
	public ProcessResult callCommonHTTPService(String appName, RequestInfo requestInfo,
			Map<String, Object> extParameters) throws Exception {
		String requestInfoString = JSONUtils.convertBean2JSON(requestInfo).toString();
		return this.callCommonHTTPService(appName, requestInfo, requestInfoString, extParameters);
	}
    /**
     * call http request and record log
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
		requestInfo.getRequestHeader().setRequestTime(DateUtils.getStringDate());
		ProcessResult processResult = new ProcessResult();
		String responseInfoString = "";
		try {
			InfConfigVO infConfigVO = UnieapCacheMgt.getInfHandler(requestInfo.getRequestBody().getBizCode());
			responseInfoString = SoapCallUtils.callHTTPService(infConfigVO.getUrl(),
					infConfigVO.getTimeout().intValue(), infConfigVO.getSOAPAction(), requestInfoString);
			processResult.setResultCode(UnieapConstants.C0);
			processResult.setResultDesc(UnieapConstants.SUCCESS);
			processResult.setVo(responseInfoString);
		} catch (Exception e) {
			processResult.setResultCode("20006");
			processResult.setResultDesc(e.getLocalizedMessage());
			long endTime = System.currentTimeMillis();
			String during = Long.toString(endTime - beginTime);
			Esblog esblog = BizServiceUtils.getEsbLog(requestInfo, processResult, requestInfoString, responseInfoString,
					during, appName);
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
					during, appName);
			EsbLogCacheMgt.setEsbLogVO(esblog);
		}
		requestInfo.getRequestHeader().setResponseTime(DateUtils.getStringDate());
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
			return false;
		} else {
			return true;
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
		long startNumber = Long.parseLong(numberFilterVO.getNumberStart());
		long endNumber = Long.parseLong(numberFilterVO.getNumberEnd());
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
