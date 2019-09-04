package com.unieap.base.inf.transform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.UnieapConstants;
import com.unieap.base.inf.vo.BizConfigVO;
import com.unieap.base.inf.vo.InfConfigVO;
import com.unieap.base.utils.XmlJSONUtils;

public interface TransformMessageHandler {
	/**
	 * 
	 * @param payload
	 * @return
	 */
	public Object transform(Object payload, Map<String, Object> extParameters) throws Exception;

	/**
	 * 
	 * @param payload
	 * @param extParameters
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public default Map<String, Object> getResults(Object payload, Map<String, Object> extParameters) throws Exception {
		BizConfigVO bizConfigVO = (BizConfigVO) extParameters.get(UnieapConstants.BIZCONFIG);
		List<InfConfigVO> dependInfCodeList = bizConfigVO.getDependInfCodeList();
		Map<String, Object> results = new HashMap<String, Object>();
		Map<String, Object> payloads = (Map<String, Object>) payload;
		for (InfConfigVO infConfigVO : dependInfCodeList) {
			if (payloads.containsKey(infConfigVO.getInfCode())) {
				infConfigVO = UnieapCacheMgt.getInfHandler(infConfigVO.getInfCode());
				Map<String, String> ns = infConfigVO.getNSList();
				if ("XML".equals(infConfigVO.getResultType())) {
					org.dom4j.Document document = XmlJSONUtils
							.getSOAPXMLDocumentDom4J(payloads.get(infConfigVO.getInfCode()).toString(), ns);
					results.put(infConfigVO.getInfCode(), document);
				}
				if ("JSON".equals(infConfigVO.getResultType())) {
					String json = payloads.get(infConfigVO.getInfCode()).toString();
					ReadContext ctx = JsonPath.parse(json);
					results.put(infConfigVO.getInfCode(), ctx);
				}
			}
		}
		return results;
	}
}
