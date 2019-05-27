package com.unieap.base.inf.handler;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.springframework.util.StringUtils;
import org.w3c.dom.Document;

import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.UnieapConstants;
import com.unieap.base.inf.element.DeviceInfo;
import com.unieap.base.inf.element.RequestBody;
import com.unieap.base.inf.element.RequestHeader;
import com.unieap.base.inf.element.RequestInfo;
import com.unieap.base.inf.element.ResponseBody;
import com.unieap.base.inf.element.ResponseHeader;
import com.unieap.base.inf.element.ResponseInfo;
import com.unieap.base.pojo.Esblog;
import com.unieap.base.ratelimit.TokenBucket;
import com.unieap.base.repository.EsbLogCacheMgt;
import com.unieap.base.utils.JSONUtils;
import com.unieap.base.vo.UserVO;

import net.sf.json.JSONObject;

public class BizServiceUtils {
	public static int totalRequestAmount = 0;

	public static RequestInfo getRequestInfo(String requestInfoString) throws Exception {
		RequestInfo requestInfo = new RequestInfo();
		RequestHeader header = new RequestHeader();
		RequestBody body = new RequestBody();
		JSONObject jsonResult = JSONObject.fromObject(requestInfoString);
		if (jsonResult.has("requestHeader")) {
			JSONObject requestHeader = (JSONObject) jsonResult.get("requestHeader");
			if (requestHeader.has("appCode")) {
				String appCode = requestHeader.getString("appCode");
				if (StringUtils.isEmpty(appCode)) {
					throw new Exception("appCode is null");
				} else {
					header.setAppCode(appCode);
				}
			} else {
				throw new Exception("missed element:RequestHeader->appCode");
			}
			if (requestHeader.has("accessName")) {
				String accessName = requestHeader.getString("accessName");
				header.setAccessName(accessName);
			}
			if (requestHeader.has("password")) {
				String password = requestHeader.getString("password");
				header.setPassword(password);
			}
			if (requestHeader.has("remoteAddress")) {
				String remoteAddress = requestHeader.getString("remoteAddress");
				header.setRemoteAddress(remoteAddress);
			}
			if (requestHeader.has("channelCode")) {
				String channelCode = requestHeader.getString("channelCode");
				if (StringUtils.isEmpty(channelCode)) {
					throw new Exception("channelCode is null");
				} else {
					header.setChannelCode(channelCode);
				}
			} else {
				throw new Exception("missed element:RequestHeader->channelCode");
			}
			if (requestHeader.has("transactionId")) {
				String transactionId = requestHeader.getString("transactionId");
				if (transactionId == null || "".equals(transactionId)) {
					transactionId = BizServiceUtils.generateTransactionId();
				}
				header.setTransactionId(transactionId);
			}
			if (requestHeader.has("extTransactionId")) {
				String extTransactionId = requestHeader.getString("extTransactionId");
				header.setExtTransactionId(extTransactionId);
			}
			if (requestHeader.has("requestTime")) {
				String requestTime = requestHeader.getString("requestTime");
				header.setRequestTime(requestTime);
			} else {
				String requestTime = UnieapConstants.getCurrentTime();
				header.setRequestTime(requestTime);
			}
			if (requestHeader.has("systemCode")) {
				String systemCode = requestHeader.getString("systemCode");
				if (StringUtils.isEmpty(systemCode)) {
					throw new Exception("systemCode is null");
				} else {
					header.setSystemCode(systemCode);
				}
			} else {
				throw new Exception("missed element:RequestHeader->systemCode");
			}
			if (requestHeader.has("extParameters")) {
				String extParameters = requestHeader.getString("extParameters");
				header.setExtParameters(extParameters);
			}
			if (requestHeader.has("deviceInfo")) {
				JSONObject deviceInfoObj = requestHeader.getJSONObject("deviceInfo");
				DeviceInfo deviceInfo = new DeviceInfo();
				if (deviceInfoObj.has("IMEI")) {
					deviceInfo.setIMEI(deviceInfoObj.getString("IMEI"));
				}
				if (deviceInfoObj.has("OSType")) {
					deviceInfo.setOSType(deviceInfoObj.getString("OSType"));
				}
				if (deviceInfoObj.has("OSVersion")) {
					deviceInfo.setOSVersion(deviceInfoObj.getString("OSVersion"));
				}
				if (deviceInfoObj.has("APKVersion")) {
					deviceInfo.setAPKVersion(deviceInfoObj.getString("APKVersion"));
				}
				if (deviceInfoObj.has("networkType")) {
					deviceInfo.setNetworkType(deviceInfoObj.getString("networkType"));
				}
				if (deviceInfoObj.has("resolution")) {
					deviceInfo.setResolution(deviceInfoObj.getString("resolution"));
				}
				if (deviceInfoObj.has("brand")) {
					deviceInfo.setBrand(deviceInfoObj.getString("brand"));
				}
				if (deviceInfoObj.has("model")) {
					deviceInfo.setModel(deviceInfoObj.getString("model"));
				}
				if (deviceInfoObj.has("extParameters")) {
					deviceInfo.setExtParameters(deviceInfoObj.getString("extParameters"));
				}
				header.setDeviceInfo(deviceInfo);
			}
			requestInfo.setRequestHeader(header);
		} else {
			throw new Exception("missed element:requestHeader");
		}
		if (jsonResult.has("requestBody")) {
			JSONObject requestBody = (JSONObject) jsonResult.get("requestBody");
			if (requestBody.has("serviceNumber")) {
				String serviceNumber = requestBody.getString("serviceNumber");
				body.setServiceNumber(serviceNumber);
			}
			if (requestBody.has("bizCode")) {
				String bizCode = requestBody.getString("bizCode");
				body.setBizCode(bizCode);
			}
			if (requestBody.has("extParameters")) {
				String extParameters = requestBody.getString("extParameters");
				body.setExtParameters(extParameters);
			}
		}
		requestInfo.setRequestBody(body);
		return requestInfo;
	}

	public static String getResposeInfoString(ResponseInfo responseInfo) throws Exception {
		JSONObject jsonResult = new JSONObject();
		ResponseHeader header = responseInfo.getResponseHeader();
		ResponseBody body = responseInfo.getResponseBody();
		JSONObject headerJsonResult = new JSONObject();
		headerJsonResult.put("systemCode", header.getSystemCode());
		headerJsonResult.put("channelCode", header.getChannelCode());
		headerJsonResult.put("extTransactionId", header.getExtTransactionId());
		headerJsonResult.put("transactionId", header.getTransactionId());
		headerJsonResult.put("requestTime", header.getRequestTime());
		headerJsonResult.put("responseTime", header.getResponseTime());
		headerJsonResult.put("resultCode", header.getResultCode());
		headerJsonResult.put("resultDesc", header.getResultDesc());
		jsonResult.put("responseHeader", headerJsonResult);
		JSONObject bodyJsonResult = new JSONObject();
		bodyJsonResult.put("serviceNumber", body.getServiceNumber());
		if (!StringUtils.isEmpty(body.getExtParameters())) {
			JSONObject extParametersJson = JSONObject.fromObject(body.getExtParameters());
			bodyJsonResult.put("extParameters", extParametersJson);
		}
		jsonResult.put("responseBody", bodyJsonResult);
		return jsonResult.toString();
	}

	public static String getSoapMessageString(SOAPMessage message) throws Exception {
		if (message == null) {
			return "";
		}
		TransformerFactory tff = TransformerFactory.newInstance();
		Transformer tf = tff.newTransformer();
		Source source = message.getSOAPPart().getContent();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(100);
		StreamResult result = new StreamResult(bos);
		tf.transform(source, result);
		return new String(bos.toByteArray());
	}

	public static org.w3c.dom.Document getSoapMessageDocument(SOAPMessage message) throws Exception {
		if (message == null) {
			return null;
		}
		String soapString = getSoapMessageString(message);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
		Document document = documentBuilder.parse(new org.xml.sax.InputSource(new StringReader(soapString)));
		return document;

	}

	public static org.dom4j.Document getSoapMessageDocumentDom4J(SOAPMessage message, Map<String, String> namespace)
			throws Exception {
		if (message == null) {
			return null;
		}
		org.dom4j.io.DOMReader xmlReader = new org.dom4j.io.DOMReader();
		xmlReader.getDocumentFactory().setXPathNamespaceURIs(namespace);
		return xmlReader.read(message.getSOAPPart().getEnvelope().getOwnerDocument());

	}

	public static String generateTransactionId() {
		StringBuffer transactionId = new StringBuffer();
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateString = formatter.format(currentTime);
		// double code = Math.random();
		// String strCode = Double.toString(code * 10000);
		// String rundNumber = strCode.substring(0, 4);
		transactionId.append(dateString);
		return transactionId.toString();
	}

	public static ResponseInfo getResponseInfo(RequestInfo requestInfo, ProcessResult processResult) {
		ResponseInfo responseInfo = new ResponseInfo();
		ResponseHeader responseHeader = new ResponseHeader();
		responseHeader.setOperName(requestInfo.getRequestHeader().getOperName());
		responseHeader.setSystemCode(requestInfo.getRequestHeader().getSystemCode());
		responseHeader.setChannelCode(requestInfo.getRequestHeader().getChannelCode());
		responseHeader.setExtTransactionId(requestInfo.getRequestHeader().getExtTransactionId());
		// store log id
		responseHeader.setTransactionId(requestInfo.getRequestHeader().getTransactionId());
		responseHeader.setRequestTime(requestInfo.getRequestHeader().getRequestTime());
		responseHeader.setResponseTime(UnieapConstants.getCurrentTime());
		responseHeader.setResultCode(processResult.getResultCode());
		responseHeader.setResultDesc(processResult.getResultDesc());
		responseInfo.setResponseHeader(responseHeader);
		ResponseBody responseBody = new ResponseBody();
		responseBody.setServiceNumber(requestInfo.getRequestBody().getServiceNumber());
		if (processResult.getVo() != null) {
			responseBody.setExtParameters(processResult.getVo().toString());
		}
		responseInfo.setResponseBody(responseBody);
		return responseInfo;
	}

	public static RequestInfo copyRequestInfo(RequestInfo oldRequestInfo) {
		RequestInfo requestInfo = new RequestInfo();
		RequestHeader requestHeader = new RequestHeader();
		RequestHeader oldRequestHeader = oldRequestInfo.getRequestHeader();
		requestHeader.setAppCode(oldRequestHeader.getAppCode());
		requestHeader.setAccessName(oldRequestHeader.getAccessName());
		requestHeader.setPassword(oldRequestHeader.getPassword());
		requestHeader.setRemoteAddress(oldRequestHeader.getRemoteAddress());
		requestHeader.setOperName(oldRequestHeader.getOperName());
		requestHeader.setSystemCode(oldRequestHeader.getSystemCode());
		requestHeader.setChannelCode(oldRequestHeader.getChannelCode());
		requestHeader.setExtTransactionId(oldRequestHeader.getExtTransactionId());
		requestHeader.setTransactionId(oldRequestHeader.getTransactionId());
		requestHeader.setRequestTime(oldRequestHeader.getRequestTime());
		requestHeader.setResponseTime(oldRequestHeader.getResponseTime());
		requestHeader.setVersion(oldRequestHeader.getVersion());
		RequestBody oldRequestBody = oldRequestInfo.getRequestBody();
		requestInfo.setRequestHeader(requestHeader);
		RequestBody requestBody = new RequestBody();
		requestBody.setBizCode(oldRequestBody.getBizCode());
		requestBody.setServiceNumber(oldRequestBody.getServiceNumber());
		requestBody.setExtParameters(oldRequestBody.getExtParameters());
		requestInfo.setRequestBody(requestBody);
		return requestInfo;
	}

	/**
	 * 
	 * @param requestInfo
	 * @param processResult
	 * @param requestInfoString
	 * @param ResponseInfoString
	 * @param during
	 * @param destSystem
	 * @return
	 */
	public static Esblog getEsbLog(RequestInfo requestInfo, ProcessResult processResult, String requestInfoString,
			String ResponseInfoString, String during, String destSystem) {
		RequestHeader requestHeader = requestInfo.getRequestHeader();
		RequestBody requestBody = requestInfo.getRequestBody();
		Esblog esblog = new Esblog();
		// esblog.setId(UnieapConstants.getSequence());
		esblog.setChannelCode(requestHeader.getChannelCode());
		esblog.setBizCode(requestBody.getBizCode());
		esblog.setServiceNumber(requestInfo.getRequestBody().getServiceNumber());
		esblog.setTransactionId(requestHeader.getTransactionId());
		esblog.setRequestTime(requestHeader.getRequestTime());
		esblog.setResponseTime(requestHeader.getResponseTime());
		esblog.setSystemCode(requestHeader.getSystemCode());
		esblog.setExtTransactionId(requestHeader.getExtTransactionId());
		esblog.setResultCode(processResult.getResultCode());
		String desc = processResult.getResultDesc();
		if (!StringUtils.isEmpty(desc) && desc.length() > 1024) {
			esblog.setResultDesc(desc.substring(0, 1024));
		} else {
			esblog.setResultDesc(desc);
		}
		esblog.setRequestInfo(requestInfoString.getBytes());
		esblog.setResponseInfo(ResponseInfoString.getBytes());
		// esblog.setCreateDate(UnieapConstants.getDateTime());
		esblog.setExecuteTime(during);
		esblog.setSourceSystem(requestHeader.getSystemCode());
		esblog.setDestSystem(destSystem);
		return esblog;
	}

	public static boolean authenticationCheck(RequestInfo requestInfo) {
		UserVO user = UnieapCacheMgt.getUser(requestInfo.getRequestHeader().getAccessName());
		if (user == null) {
			ProcessResult processResult = new ProcessResult();
			processResult.setResultCode("20004");
			processResult.setResultDesc(UnieapConstants.getMessage("20004"));
			return false;
		} else {
			boolean match = org.apache.commons.lang.StringUtils.equals(user.getPassword(),
					requestInfo.getRequestHeader().getPassword());
			if (match) {
				return true;
			} else {
				ProcessResult processResult = new ProcessResult();
				processResult.setResultCode("20004");
				processResult.setResultDesc(UnieapConstants.getMessage("20004"));
				return false;
			}
		}
	}

	public static boolean requestLimitationCheck(RequestInfo requestInfo) {
		int limitationCurrent = TokenBucket.tokens.get("licence.controller.inf.limitation").intValue();
		if (limitationCurrent <= 0) {
			ProcessResult processResult = new ProcessResult();
			processResult.setResultCode("20005");
			processResult.setResultDesc(UnieapConstants.getMessage("20005"));
			return false;
		} else {
			return true;
		}
	}

	public static void decreaseRequestAmount() {
		TokenBucket.decreaseToken();
	}

	public static String successCodeCheck(String resultCode, String SUCCESSCODE) {
		if (SUCCESSCODE != null) {
			String[] SUCCESSCODELIST = SUCCESSCODE.split(",");
			for (String code : SUCCESSCODELIST) {
				if (code.equals(resultCode)) {
					return SUCCESSCODE.split(",")[0];
				}
			}
		}
		return null;
	}

	public static boolean errorCodeCheck(String resultCode, String ERRORCODEIGNOREList) {
		if (ERRORCODEIGNOREList != null) {
			String[] ERRORCODEIGNORELIST = ERRORCODEIGNOREList.split(",");
			for (String code : ERRORCODEIGNORELIST) {
				if (code.equals(resultCode)) {
					return true;
				}
			}
		}
		return false;
	}

	public static org.dom4j.Document getSoapMessageDocumentDom4J(SOAPMessage message) throws Exception {
		if (message == null) {
			return null;
		}
		org.dom4j.io.DOMReader xmlReader = new org.dom4j.io.DOMReader();
		return xmlReader.read(message.getSOAPPart().getEnvelope().getOwnerDocument());

	}

	public static String exceptionEsblog(String appCode, long beginTime, RequestInfo requestInfo, Object request,
			String errorCode, Exception e) throws Exception {
		ResponseInfo responseInfo = new ResponseInfo();
		ResponseHeader responseHeader = new ResponseHeader();
		responseHeader.setResultCode(errorCode);
		if (e == null) {
			responseHeader.setResultDesc(UnieapConstants.getMessage("20004"));
		} else {
			responseHeader.setResultDesc(e.getLocalizedMessage());
		}
		ResponseBody responseBody = new ResponseBody();
		responseBody.setServiceNumber(requestInfo.getRequestBody().getServiceNumber());
		responseInfo.setResponseHeader(responseHeader);
		responseInfo.setResponseBody(responseBody);
		String responseInfoString = getResposeInfoString(responseInfo);
		ProcessResult processResult = new ProcessResult();
		processResult.setResultCode(responseHeader.getResultCode());
		processResult.setResultDesc(responseHeader.getResultDesc());
		long endTime = System.currentTimeMillis();
		String during = "" + (endTime - beginTime);
		String requestInfoString = "";
		if (String.class.getName().equals(request.getClass().getName())) {
			requestInfoString = request.toString();
		} else {
			requestInfoString = JSONUtils.convertBean2JSON(request).toString();
		}
		Esblog esblog = BizServiceUtils.getEsbLog(requestInfo, processResult, requestInfoString, responseInfoString,
				during, appCode);
		EsbLogCacheMgt.setEsbLogVO(esblog);
		return responseInfoString;
	}
}
