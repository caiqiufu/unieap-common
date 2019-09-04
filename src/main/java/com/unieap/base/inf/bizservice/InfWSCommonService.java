package com.unieap.base.inf.bizservice;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.unieap.base.UnieapConstants;
import com.unieap.base.inf.element.RequestInfo;
import com.unieap.base.inf.handler.BizHandler;
import com.unieap.base.inf.handler.ProcessResult;
/**
 * common inferface call
 * when call API via webservice, payload is request message Obejct.
 * @author Chai
 *
 */

@Service
public class InfWSCommonService extends BizHandler {
	@Override
	public ProcessResult process(RequestInfo requestInfo, Map<String, Object> extParameters) throws Exception {
		Object payload = extParameters.get(UnieapConstants.PAYLOAD);
		String requestMessage = (String)extParameters.get(UnieapConstants.REQUEST_MESSAGE);
		if(payload!=null) {
			requestMessage = this.getSOAPRequestMessageFromPayload(payload);
		}
		requestInfo.getRequestHeader().setSystemCode(appCode);
		ProcessResult processResult = this.callCommonHTTPService(appCode, requestInfo, requestMessage, extParameters);
		return processResult;
	}

}
