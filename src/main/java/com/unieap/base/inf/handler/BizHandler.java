package com.unieap.base.inf.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.unieap.base.ApplicationContextProvider;
import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.UnieapConstants;
import com.unieap.base.inf.element.RequestInfo;
import com.unieap.base.inf.transform.BizMessageVO;
import com.unieap.base.inf.transform.TransformMessageHandler;
import com.unieap.base.pojo.Esblog;
import com.unieap.base.repository.EsbLogCacheMgt;
import com.unieap.base.utils.DateUtils;
import com.unieap.base.utils.JSONUtils;
import com.unieap.base.utils.JaxbXmlUtil;
import com.unieap.base.vo.BizConfigVO;
import com.unieap.base.vo.InfConfigVO;
import com.unieap.base.vo.NumberRouteVO;

import net.sf.json.JSONObject;

/**
 * 主要是流程编排逻辑
 * 
 * @author Chai
 *
 */
public abstract class BizHandler extends ConnectionHandler {

	public Logger logger = Logger.getLogger(BizHandler.class);

	/**
	 * esb comman interface
	 * 
	 * @param requestInfo
	 * @param extParameters
	 * @return
	 * @throws Exception
	 */
	public abstract ProcessResult process(RequestInfo requestInfo, Map<String, Object> extParameters) throws Exception;

	/**
	 * 
	 * @param requestInfo
	 * @return
	 */
	public Map<String, Object> getExtParameters(RequestInfo requestInfo) {
		Map<String, Object> extParameters = new HashMap<String, Object>();
		long beginTime = System.currentTimeMillis();
		extParameters.put("requestInfo", BizServiceUtils.copyRequestInfo(requestInfo));
		extParameters.put("beginTime", beginTime);
		return extParameters;
	}

	/**
	 * inf route from a to b based on number range
	 * 
	 * @param bizCode
	 * @param infCode
	 * @param serviceNumber
	 * @return
	 */
	public InfConfigVO getRouteInfConfig(String bizCode, String infCode, String serviceNumber) {
		InfConfigVO infConfigVO = UnieapCacheMgt.getInfHandler(infCode);
		Map<String, List<NumberRouteVO>> numberRoute = infConfigVO.getNumberRoute();
		if (numberRoute == null) {
			infConfigVO.setRouted(false);
			return infConfigVO;
		}
		List<NumberRouteVO> numberRouteList = infConfigVO.getNumberRoute().get(bizCode);
		if (numberRouteList == null) {
			infConfigVO.setRouted(false);
			return infConfigVO;
		} else {
			for (NumberRouteVO numberRouteVO : numberRouteList) {
				if (isInRouteNumberRange(serviceNumber, numberRouteVO)) {
					infConfigVO.setRouted(true);
					return infConfigVO;
				}
			}
			infConfigVO.setRouted(false);
			return infConfigVO;
		}
	}

	public BizMessageVO getBizMessageVO(String bizCode, String infCode, String serviceNumber) {
		InfConfigVO infConfigVO = UnieapCacheMgt.getInfHandler(infCode);
		List<NumberRouteVO> numberRouteList = infConfigVO.getNumberRoute().get(bizCode);
		for (NumberRouteVO numberRouteVO : numberRouteList) {
			if (isInRouteNumberRange(serviceNumber, numberRouteVO)) {
				String key = bizCode + "_" + numberRouteVO.getNumberStart() + "_" + numberRouteVO.getNumberEnd() + "_"
						+ numberRouteVO.getRouteType();
				return UnieapCacheMgt.getBizMessageVO(key);
			}
		}
		return null;

	}

	/**
	 * 
	 * @param serviceNumber
	 * @param numberRouteVO
	 * @return
	 */
	public boolean isInRouteNumberRange(String serviceNumber, NumberRouteVO numberRouteVO) {
		String numberStart = numberRouteVO.getNumberStart();
		String numberEnd = numberRouteVO.getNumberEnd();
		if ("*".equals(numberStart) || "-1".equals(numberStart)) {
			numberStart = "1";
		}
		if ("*".equals(numberEnd) || "-1".equals(numberEnd)) {
			numberEnd = "9223372036854775807";
		}
		long myServiceNumber = Long.parseLong(serviceNumber);
		long startNumber = Long.parseLong(numberStart);
		long endNumber = Long.parseLong(numberEnd);
		if ("IN".equals(numberRouteVO.getRouteType())) {
			if (myServiceNumber >= startNumber && myServiceNumber <= endNumber) {
				return true;
			} else {
				return false;
			}
		} else if ("NOTIN".equals(numberRouteVO.getRouteType())) {
			if (myServiceNumber < startNumber || myServiceNumber > endNumber) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public BizHandler getProcessHandler(String code) throws Exception {
		return BizServiceBO.getProcessHandler(code);
	}

	/**
	 * 
	 * @param beginTime
	 * @param requestInfo
	 * @param processResult
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void saveEsbLog(long beginTime, RequestInfo requestInfo, ProcessResult processResult, Object request,
			Object response) throws Exception {
		long endTime = System.currentTimeMillis();
		String during = Long.toString(endTime - beginTime);
		requestInfo.getRequestHeader().setResponseTime(DateUtils.getStringDate());
		Esblog esblog = BizServiceUtils.getEsbLog(requestInfo, processResult,
				JSONUtils.convertBean2JSON(request).toString(), JSONUtils.convertBean2JSON(response).toString(), during,
				UnieapConstants.ESB);
		EsbLogCacheMgt.setEsbLogVO(esblog);
	}

	/**
	 * 
	 * @param requestInfo
	 * @param appCode
	 * @param extParameters
	 * @return
	 * @throws Exception
	 */
	public ProcessResult process(RequestInfo requestInfo, String appCode, Map<String, Object> extParameters)
			throws Exception {
		requestInfo.getRequestHeader().setSystemCode(appCode);
		BizConfigVO bizConfigVO = UnieapCacheMgt.getBizHandler(requestInfo.getRequestBody().getBizCode());
		ProcessResult processResult = new ProcessResult();
		processResult.setResultCode(UnieapConstants.C0);
		processResult.setResultDesc(UnieapConstants.getMessage(UnieapConstants.C0));
		List<InfConfigVO> dependInfCodeList = bizConfigVO.getDependInfCodeList();
		if (dependInfCodeList != null && dependInfCodeList.size() > 0) {
			extParameters.put(UnieapConstants.BIZCONFIG, bizConfigVO);
			Map<String, Object> payloads = new HashMap<String, Object>();
			BizMessageVO bizMessageVO = null;
			for (InfConfigVO subInfConfigVO : dependInfCodeList) {
				RequestInfo newRequestInfo = BizServiceUtils.copyRequestInfo(requestInfo);
				newRequestInfo.getRequestBody().setBizCode(subInfConfigVO.getInfCode());
				InfConfigVO infConfigVO = this.getRouteInfConfig(requestInfo.getRequestBody().getBizCode(),
						subInfConfigVO.getInfCode(), newRequestInfo.getRequestBody().getServiceNumber());
				if (!infConfigVO.isRouted()) {
					continue;
				}
				bizMessageVO = this.getBizMessageVO(requestInfo.getRequestBody().getBizCode(), infConfigVO.getInfCode(),
						newRequestInfo.getRequestBody().getServiceNumber());
				extParameters.put("BIZMESSAGEVO", bizMessageVO);
				BizHandler bizHandler = (BizHandler) this.getProcessHandler(infConfigVO.getInfCode());
				ProcessResult infProcessResult = bizHandler.process(newRequestInfo, extParameters);
				if (UnieapConstants.C0.equals(infProcessResult.getResultCode())) {
					if (!StringUtils.isEmpty(infConfigVO.getTransformMessageHandler())) {
						TransformMessageHandler transformMessageHandler = (TransformMessageHandler) ApplicationContextProvider
								.getBean(infConfigVO.getTransformMessageHandler());
						infProcessResult
								.setVo(transformMessageHandler.transform(infProcessResult.getVo(), extParameters));
					}
					if (infConfigVO.getBizTransformMessageHandler() != null) {
						if (infConfigVO.getBizTransformMessageHandler().containsKey(bizConfigVO.getBizCode())) {
							TransformMessageHandler transformMessageHandler = (TransformMessageHandler) ApplicationContextProvider
									.getBean(infConfigVO.getBizTransformMessageHandler().get(bizConfigVO.getBizCode()));
							infProcessResult
									.setVo(transformMessageHandler.transform(infProcessResult.getVo(), extParameters));
						}
					}
					processResult = infProcessResult;
					payloads.put(infConfigVO.getInfCode(), infProcessResult.getVo());
				} else {
					throw new Exception(infProcessResult.getResultCode() + ":" + infProcessResult.getResultDesc());
				}
			}
			if (!StringUtils.isEmpty(bizConfigVO.getTransformMessageHandler())) {
				TransformMessageHandler transformMessageHandler = (TransformMessageHandler) ApplicationContextProvider
						.getBean(bizConfigVO.getTransformMessageHandler());
				Object obj = transformMessageHandler.transform(payloads, extParameters);
				if (obj != null) {
					processResult = processAfterBizTransformMessage(obj.toString());
				}
			}
			return processResult;
		} else {
			processResult.setResultCode("20009");
			processResult.setResultDesc(UnieapConstants.getMessage("20009", new String[] { bizConfigVO.getBizCode() }));
		}
		return processResult;
	}

	public ProcessResult processAfterBizTransformMessage(String transformMessage) {
		ProcessResult processResult = new ProcessResult();
		JSONObject obj = JSONObject.fromObject(transformMessage);
		JSONObject resultHeader = obj.getJSONObject("ResultMsg").getJSONObject("ResultHeader");
		JSONObject resultRecord = obj.getJSONObject("ResultMsg").getJSONObject("ResultRecord");
		processResult.setResultCode(resultHeader.getString("ResultCode"));
		processResult.setResultDesc(resultHeader.getString("ResultDesc"));
		processResult.setVo(resultRecord.toString());
		return processResult;
	}
	public String getSOAPRequestMessageFromPayload(Object payload) throws Exception {
		String requestMessage = JaxbXmlUtil.convertBeanToXml(payload);
		requestMessage = requestMessage.substring( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>".length());
		StringBuffer sb = new StringBuffer();
		sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<soapenv:Header/>");
		sb.append("<soapenv:Body>");
		sb.append(requestMessage);
		sb.append("</soapenv:Body>");
		sb.append("</soapenv:Envelope>");
		return sb.toString();
	}
}
