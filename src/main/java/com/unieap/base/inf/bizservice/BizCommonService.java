package com.unieap.base.inf.bizservice;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.unieap.base.inf.element.RequestInfo;
import com.unieap.base.inf.handler.BizHandler;
import com.unieap.base.inf.handler.ProcessResult;

/**
 * common business service
 * 
 * @author Chai
 *
 */

@Service
public class BizCommonService extends BizHandler {

	/**
	 * 该实现定义在esb_biz_config表中
	 * 业务综合处理类，如果需要多API组合并增加业务逻辑，则自定义该类即可
	 */
	@Override
	public ProcessResult process(RequestInfo requestInfo, Map<String, Object> extParameters) throws Exception {
		return this.process(requestInfo, appCode, extParameters);
	}
}
