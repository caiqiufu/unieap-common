package com.unieap.base.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unieap.base.UnieapConstants;
import com.unieap.base.inf.element.RequestInfo;
import com.unieap.base.inf.handler.BizHandler;
import com.unieap.base.inf.handler.ProcessResult;
@Service
public class SyncBizMessageConfigBizHandler  extends BizHandler{
	@Autowired
	LoadBizMessageConfigDataHandler loadBizMessageConfigDataHandler;
	@Override
	public ProcessResult process(RequestInfo requestInfo, Map<String, Object> extParameters) throws Exception {
		loadBizMessageConfigDataHandler.deal(null);
		ProcessResult processResult = new ProcessResult();
		processResult = new ProcessResult();
		processResult.setResultCode(UnieapConstants.C0);
		processResult.setResultDesc(UnieapConstants.SUCCESS);
		return processResult;
	}

}
