package com.unieap.base.inf.element;

import net.sf.json.JSONObject;

public class RequestInfo {
	public RequestHeader requestHeader;
	public RequestBody requestBody;

	public RequestHeader getRequestHeader() {
		if (requestHeader == null) {
			this.requestHeader = new RequestHeader();
		}
		return requestHeader;
	}

	public void setRequestHeader(RequestHeader requestHeader) {
		this.requestHeader = requestHeader;
	}

	public RequestBody getRequestBody() {
		if (requestBody == null) {
			this.requestBody = new RequestBody();
		}
		return requestBody;
	}

	public void setRequestBody(RequestBody requestBody) {
		this.requestBody = requestBody;
	}

	public String getJsonString() throws Exception {
		JSONObject data = new JSONObject();
		try {
			JSONObject requestHeaderObj = new JSONObject();
			requestHeaderObj.put("appCode", requestHeader.getAppCode());
			requestHeaderObj.put("accessName", requestHeader.getAccessName());
			requestHeaderObj.put("password", requestHeader.getPassword());
			requestHeaderObj.put("remoteAddress", requestHeader.getRemoteAddress());
			requestHeaderObj.put("operName", requestHeader.getOperName());
			requestHeaderObj.put("systemCode", requestHeader.getSystemCode());
			requestHeaderObj.put("channelCode", requestHeader.getChannelCode());
			requestHeaderObj.put("extTransactionId", requestHeader.getExtTransactionId());
			requestHeaderObj.put("transactionId", requestHeader.getTransactionId());
			requestHeaderObj.put("requestTime", requestHeader.getRequestTime());
			requestHeaderObj.put("responseTime", requestHeader.getResponseTime());
			requestHeaderObj.put("version", requestHeader.getVersion());
			DeviceInfo deviceInfo = requestHeader.getDeviceInfo();
			if (deviceInfo != null) {
				JSONObject deviceInfoObj = new JSONObject();
				deviceInfoObj.put("IMEI", deviceInfo.getIMEI());
				deviceInfoObj.put("OSType", deviceInfo.getOSType());
				deviceInfoObj.put("OSVersion", deviceInfo.getOSVersion());
				deviceInfoObj.put("APKVersion", deviceInfo.getAPKVersion());
				deviceInfoObj.put("networkType", deviceInfo.getNetworkType());
				deviceInfoObj.put("resolution", deviceInfo.getResolution());
				deviceInfoObj.put("brand", deviceInfo.getBrand());
				deviceInfoObj.put("model", deviceInfo.getModel());
				deviceInfoObj.put("extParameters", deviceInfo.getExtParameters().toCharArray());
				requestHeaderObj.put("deviceInfo", deviceInfoObj);
			}
			data.put("requestHeader", requestHeaderObj);
			JSONObject requestBodyObj = new JSONObject();
			requestBodyObj.put("bizCode", requestBody.getBizCode());
			requestBodyObj.put("serviceNumber", requestBody.getServiceNumber());
			requestBodyObj.put("extParameters", requestBody.getExtParameters().toCharArray());
			data.put("requestBody", requestBodyObj);

		} catch (Exception e) {
			throw new Exception("items data conver to json error:" + e.getMessage(), e);
		}
		String jsonString = data.toString();
		return jsonString;
	}

}
