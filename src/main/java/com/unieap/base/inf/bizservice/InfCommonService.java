package com.unieap.base.inf.bizservice;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.unieap.base.UnieapConstants;
import com.unieap.base.inf.element.RequestInfo;
import com.unieap.base.inf.handler.BizHandler;
import com.unieap.base.inf.handler.ProcessResult;
@Service
public class InfCommonService  extends BizHandler{
	@Value("${spring.application.name}")
	public String appCode;

	@Override
	public ProcessResult process(RequestInfo requestInfo, Map<String, Object> extParameters) throws Exception {
		Object payload =  extParameters.get(UnieapConstants.PAYLOAD);
		String requestMessage = this.getSOAPRequestMessageFromPayload(payload);
		requestInfo.getRequestHeader().setSystemCode(appCode);
		ProcessResult processResult = this.callCommonHTTPService(appCode, requestInfo, requestMessage, extParameters);
		return processResult;
	}

}
