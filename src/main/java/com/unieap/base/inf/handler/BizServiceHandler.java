package com.unieap.base.inf.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.unieap.base.ApplicationContextProvider;
import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.UnieapConstants;
import com.unieap.base.controller.BaseController;
import com.unieap.base.inf.element.RequestInfo;
import com.unieap.base.inf.element.ResponseBody;
import com.unieap.base.inf.element.ResponseHeader;
import com.unieap.base.inf.element.ResponseInfo;
import com.unieap.base.inf.vo.BizConfigVO;
import com.unieap.base.pojo.Esblog;
import com.unieap.base.pojo.MdmExclog;
import com.unieap.base.repository.EsbLogCacheMgt;
import com.unieap.base.repository.ExcLogRespository;
import com.unieap.base.utils.JSONUtils;
import com.unieap.base.utils.JaxbXmlUtil;

import net.sf.json.JSONObject;

/**
 * standard http request with json format
 * 
 * @author Chai
 *
 */
@Service
public class BizServiceHandler extends BaseController {
	public Logger logger = Logger.getLogger(BizServiceHandler.class);
	@Autowired
	ExcLogRespository excLogRespository;
	@Value("${spring.application.name}")
	public String appCode;
	@Value("${server.address}")
	public String address;
	@Value("${server.port}")
	public String port;

	public String process(String bizCode, String SOAPAction, String requestInfoString,
			Map<String, Object> extParameters) {
		long beginTime = System.currentTimeMillis();
		extParameters.put("ProcessServerInfo", this.getRequestServerInfo().toString());
		BizConfigVO bizConfigVO = UnieapCacheMgt.getBizHandler(bizCode);
		RequestInfo requestInfo = null;
		try {
			JSONObject jsonObj = JSONObject.fromObject(bizConfigVO.getExtParameters());
			requestInfo = BizServiceUtils.getRequestInfo(bizCode, requestInfoString,
					jsonObj.getJSONObject("requestParameterInfo"));
			try {
				requestInfo = BizServiceUtils.getRequestInfo(requestInfoString);
				return process(requestInfo, beginTime, requestInfoString, extParameters);
			} catch (Exception e) {
				return BizServiceUtils.exceptionEsblog(appCode, beginTime, requestInfo, requestInfoString,
						UnieapConstants.C99999, e);
			}
		} catch (Exception e) {
			try {
				return BizServiceUtils.exceptionEsblog(appCode, beginTime, requestInfo, requestInfoString,
						UnieapConstants.C99999, e);
			} catch (Exception e1) {
				try {
					saveException(e1);
				} catch (Exception e2) {
					JSONObject jsonResult = new JSONObject();
					JSONObject headerJsonResult = new JSONObject();
					headerJsonResult.put("resultCode", UnieapConstants.C99999);
					headerJsonResult.put("resultDesc", e2.getLocalizedMessage());
					jsonResult.put("responseHeader", headerJsonResult);
					JSONObject bodyJsonResult = new JSONObject();
					jsonResult.put("responseBody", bodyJsonResult);
					return jsonResult.toString();
				}
				JSONObject jsonResult = new JSONObject();
				JSONObject headerJsonResult = new JSONObject();
				headerJsonResult.put("resultCode", UnieapConstants.C99999);
				headerJsonResult.put("resultDesc", e1.getLocalizedMessage());
				jsonResult.put("responseHeader", headerJsonResult);
				JSONObject bodyJsonResult = new JSONObject();
				jsonResult.put("responseBody", bodyJsonResult);
				return jsonResult.toString();
			}
		}
	}

	/**
	 * @param requestInfoString
	 * @param extParameters
	 * @return
	 */
	public String process(String requestInfoString, Map<String, Object> extParameters) {
		long beginTime = System.currentTimeMillis();
		extParameters.put("ProcessServerInfo", this.getRequestServerInfo().toString());
		RequestInfo requestInfo = null;
		try {
			try {
				requestInfo = BizServiceUtils.getRequestInfo(requestInfoString);
				return process(requestInfo, beginTime, requestInfoString, extParameters);
			} catch (Exception e) {
				return BizServiceUtils.exceptionEsblog(appCode, beginTime, requestInfo, requestInfoString,
						UnieapConstants.C99999, e);
			}
		} catch (Exception e) {
			try {
				return BizServiceUtils.exceptionEsblog(appCode, beginTime, requestInfo, requestInfoString,
						UnieapConstants.C99999, e);
			} catch (Exception e1) {
				try {
					saveException(e1);
				} catch (Exception e2) {
					JSONObject jsonResult = new JSONObject();
					JSONObject headerJsonResult = new JSONObject();
					headerJsonResult.put("resultCode", UnieapConstants.C99999);
					headerJsonResult.put("resultDesc", e2.getLocalizedMessage());
					jsonResult.put("responseHeader", headerJsonResult);
					JSONObject bodyJsonResult = new JSONObject();
					jsonResult.put("responseBody", bodyJsonResult);
					return jsonResult.toString();
				}
				JSONObject jsonResult = new JSONObject();
				JSONObject headerJsonResult = new JSONObject();
				headerJsonResult.put("resultCode", UnieapConstants.C99999);
				headerJsonResult.put("resultDesc", e1.getLocalizedMessage());
				jsonResult.put("responseHeader", headerJsonResult);
				JSONObject bodyJsonResult = new JSONObject();
				jsonResult.put("responseBody", bodyJsonResult);
				return jsonResult.toString();
			}
		}
	}

	public String process(RequestInfo requestInfo, long beginTime, String requestInfoString,
			Map<String, Object> extParameters) throws Exception {
		try {
			try {
				if (!BizServiceUtils.authenticationCheck(requestInfo)) {
					return BizServiceUtils.exceptionEsblog(appCode, beginTime, requestInfo, requestInfoString, "20004",
							null);
				}
				if (!BizServiceUtils.requestLimitationCheck(requestInfo)) {
					return BizServiceUtils.exceptionEsblog(appCode, beginTime, requestInfo, requestInfoString, "20005",
							null);
				}
			} catch (Exception e) {
				return BizServiceUtils.exceptionEsblog(appCode, beginTime, requestInfo, requestInfoString,
						UnieapConstants.C99999, e);
			}

			String bizCode = requestInfo.getRequestBody().getBizCode();
			BizHandler bizHandler = getProcessHandler(bizCode);
			if (bizHandler == null) {
				String errorDesc = UnieapConstants.getMessage("10001", new String[] { bizCode });
				return BizServiceUtils.exceptionEsblog(appCode, beginTime, requestInfo, requestInfoString, "10001",
						new Exception(errorDesc));
			}
			ProcessResult processResult;
			try {
				RequestInfo newRequestInfo = BizServiceUtils.copyRequestInfo(requestInfo);
				processResult = bizHandler.process(newRequestInfo, extParameters);
			} catch (Exception e) {
				return BizServiceUtils.exceptionEsblog(appCode, beginTime, requestInfo, requestInfoString,
						UnieapConstants.C99999, e);
			}
			ResponseInfo responseInfo = BizServiceUtils.getResponseInfo(requestInfo, processResult);
			String responseInfoString = BizServiceUtils.getResposeInfoString(responseInfo);
			String responseTime = UnieapConstants.getCurrentTime();
			responseInfo.getResponseHeader().setResponseTime(responseTime);
			requestInfo.getRequestHeader().setResponseTime(responseTime);
			long endTime = System.currentTimeMillis();
			String during = "" + (endTime - beginTime);
			Esblog esblog = BizServiceUtils.getEsbLog(requestInfo, processResult, requestInfoString, responseInfoString,
					during, appCode, this.getRequestServerInfo().toString());
			esblog.setResponseTime(responseTime);
			EsbLogCacheMgt.setEsbLogVO(esblog);
			return responseInfoString;
		} catch (Exception e) {
			try {
				return BizServiceUtils.exceptionEsblog(appCode, beginTime, requestInfo, requestInfoString,
						UnieapConstants.C99999, e);
			} catch (Exception e1) {
				try {
					saveException(e1);
				} catch (Exception e2) {
					JSONObject jsonResult = new JSONObject();
					JSONObject headerJsonResult = new JSONObject();
					headerJsonResult.put("resultCode", UnieapConstants.C99999);
					headerJsonResult.put("resultDesc", e2.getLocalizedMessage());
					jsonResult.put("responseHeader", headerJsonResult);
					JSONObject bodyJsonResult = new JSONObject();
					jsonResult.put("responseBody", bodyJsonResult);
					return jsonResult.toString();
				}
				JSONObject jsonResult = new JSONObject();
				JSONObject headerJsonResult = new JSONObject();
				headerJsonResult.put("resultCode", UnieapConstants.C99999);
				headerJsonResult.put("resultDesc", e1.getLocalizedMessage());
				jsonResult.put("responseHeader", headerJsonResult);
				JSONObject bodyJsonResult = new JSONObject();
				jsonResult.put("responseBody", bodyJsonResult);
				return jsonResult.toString();
			}
		}
	}

	public String overFlowResonse() throws Exception {
		ResponseInfo responseInfo = new ResponseInfo();
		ResponseHeader responseHeader = new ResponseHeader();
		ResponseBody responseBody = new ResponseBody();
		responseInfo.setResponseHeader(responseHeader);
		responseInfo.setResponseBody(responseBody);
		responseHeader.setResultCode("20005");
		responseHeader.setResultDesc(UnieapConstants.getMessage("20005"));
		String responseInfoString = BizServiceUtils.getResposeInfoString(responseInfo);
		return responseInfoString;
	}

	public void saveException(Exception ex) throws Exception {
		MdmExclog log = new MdmExclog();
		log.setId(UnieapConstants.getSequence());
		log.setBizModule("unieap");
		log.setExType("system_exception");
		log.setExCode("");
		log.setExInfo(ex.getLocalizedMessage());
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		log.setExTracking(sw.toString().getBytes());
		if (UnieapConstants.getUser() != null) {
			log.setOperatorName(UnieapConstants.getUser().getUserCode());
		} else {
			log.setOperatorName("system error");
		}
		log.setOperationDate(UnieapConstants.getDateTime());
		excLogRespository.save(log);
	}

	public BizHandler getProcessHandler(String code) throws Exception {
		if (UnieapCacheMgt.bizHandlerList.get(code) == null && UnieapCacheMgt.infHandlerList.get(code) == null) {
			throw new Exception(UnieapConstants.getMessage("10001", new String[] { code }));
		}
		/*
		 * if (UnieapCacheMgt.bizHandlerList.get(code) != null &&
		 * UnieapCacheMgt.infHandlerList.get(code) != null) { throw new
		 * Exception(UnieapConstants.getMessage("10002", new String[] { code })); }
		 */
		if (UnieapCacheMgt.bizHandlerList.get(code) != null) {
			String bizHandlerName = UnieapCacheMgt.bizHandlerList.get(code).getClassName();
			return (BizHandler) ApplicationContextProvider.getBean(bizHandlerName);
		}
		if (UnieapCacheMgt.infHandlerList.get(code) != null) {
			String infHandlerName = UnieapCacheMgt.infHandlerList.get(code).getClassName();
			return (BizHandler) ApplicationContextProvider.getBean(infHandlerName);
		}
		throw new Exception(UnieapConstants.getMessage("10001", new String[] { code }));
	}

	/**
	 * 
	 * @param payload
	 * @param requestInfo
	 * @param rootElementName
	 * @param cl
	 * @return
	 * @throws Exception
	 */
	public ProcessResult process(Object payload, RequestInfo requestInfo, String rootElementName, Class<?> cl)
			throws Exception {
		BizHandler bizHandler = getProcessHandler(requestInfo.getRequestBody().getBizCode());
		Map<String, Object> extParameters = new HashMap<String, Object>();
		extParameters.put(UnieapConstants.PAYLOAD, payload);
		extParameters.put("ProcessServerInfo", this.getRequestServerInfo().toString());
		ProcessResult processResult = bizHandler.process(requestInfo, extParameters);
		if (processResult.getVo() != null) {
			processResult
					.setVo(JaxbXmlUtil.convertXmlToJavaBean(processResult.getVo().toString(), rootElementName, cl));
		}
		return processResult;
	}

	/**
	 * 
	 * @param beginTime
	 * @param requestInfo
	 * @param resultCode
	 * @param resultDesc
	 * @param payload
	 * @param response
	 * @param processServerInfo
	 * @throws Exception
	 */
	public void saveEsbLog(long beginTime, RequestInfo requestInfo, String resultCode, String resultDesc,
			String requestInfoString, String responseInfoString, String processServerInfo) throws Exception {
		ProcessResult processResult = new ProcessResult();
		processResult.setResultCode(resultCode);
		processResult.setResultDesc(resultDesc);
		long endTime = System.currentTimeMillis();
		String during = "" + (endTime - beginTime);
		Esblog esblog = BizServiceUtils.getEsbLog(requestInfo, processResult, requestInfoString, responseInfoString,
				during, appCode, processServerInfo);
		String responseTime = UnieapConstants.getCurrentTime();
		esblog.setResponseTime(responseTime);
		EsbLogCacheMgt.setEsbLogVO(esblog);
	}

	public void saveEsbLogJSONObj(long beginTime, RequestInfo requestInfo, String resultCode, String resultDesc,
			Object payload, Object response, String processServerInfo) throws Exception {
		String requestInfoString = JSONUtils.convertBean2JSON(payload).toString();
		String responseInfoString = JSONUtils.convertBean2JSON(response).toString();
		this.saveEsbLog(beginTime, requestInfo, resultCode, resultDesc, requestInfoString, responseInfoString,
				processServerInfo);
	}

	public void saveEsbLogXMLObj(long beginTime, RequestInfo requestInfo, String resultCode, String resultDesc,
			Object payload, Object response, String processServerInfo) throws Exception {
		String requestInfoString = JaxbXmlUtil.convertBeanToXml(payload);
		String responseInfoString = JaxbXmlUtil.convertBeanToXml(response);
		this.saveEsbLog(beginTime, requestInfo, resultCode, resultDesc, requestInfoString, responseInfoString,
				processServerInfo);
	}

	@Override
	public JSONObject getRequestServerInfo() {
		JSONObject jsonObj = super.getRequestServerInfo();
		jsonObj.put("appCode", appCode);
		jsonObj.put("server.address", address);
		jsonObj.put("server.port", port);
		return jsonObj;
	}

	@Override
	public JSONObject getRequestServerInfo(HttpServletRequest request) {
		JSONObject jsonObj = super.getRequestServerInfo(request);
		jsonObj.put("appCode", appCode);
		jsonObj.put("server.address", address);
		jsonObj.put("server.port", port);
		return jsonObj;
	}

}
