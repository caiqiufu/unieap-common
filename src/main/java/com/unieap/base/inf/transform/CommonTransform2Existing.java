package com.unieap.base.inf.transform;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.unieap.base.UnieapConstants;
import com.unieap.base.inf.unitls.TransformMessageUtils;
import com.unieap.base.inf.vo.BizMessageVO;

@Service
public class CommonTransform2Existing implements TransformMessageHandler {

	@Override
	public Object transform(Object payload, Map<String, Object> extParameters) throws Exception {
		BizMessageVO bizMessageVO = (BizMessageVO) extParameters.get(UnieapConstants.BIZMESSAGEVO);
		if (bizMessageVO == null) {
			return null;
		}
		Map<String, Object> results = this.getResults(payload, extParameters);
		if (!results.isEmpty()) {
			return TransformMessageUtils.getBizMessage2XML(bizMessageVO, results);
		}
		return null;
	}

}
