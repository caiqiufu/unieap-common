package com.unieap.base.handler;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.UnieapConstants;
import com.unieap.base.inf.element.RequestInfo;
import com.unieap.base.inf.handler.BizHandler;
import com.unieap.base.inf.handler.ProcessResult;
import com.unieap.base.vo.NumberFilterVO;

import net.sf.json.JSONObject;
@Service
public class SyncInfConfigFilterBizHandler extends BizHandler{
	@Autowired
	LoadInfConfigDataHandler loadInfConfigDataHandler;
	@Override
	public ProcessResult process(RequestInfo requestInfo, Map<String, Object> extParameters) throws Exception {
		// TODO Auto-generated method stub
		requestInfo.getRequestBody().getExtParameters();
		JSONObject jsonObj = JSONObject.fromObject(requestInfo.getRequestBody().getExtParameters());
		String infCode = jsonObj.getString("infCode");
		List<NumberFilterVO> numberFilterList= loadInfConfigDataHandler.getNumberFilterList(infCode);
		UnieapCacheMgt.getInfHandlerList().get(infCode).setNumberFilterList(numberFilterList);
		ProcessResult processResult = new ProcessResult();
		processResult = new ProcessResult();
		processResult.setResultCode(UnieapConstants.C0);
		processResult.setResultDesc(UnieapConstants.SUCCESS);
		return processResult;
	}

}
