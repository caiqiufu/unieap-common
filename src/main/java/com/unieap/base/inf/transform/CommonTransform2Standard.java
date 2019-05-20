package com.unieap.base.inf.transform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.unieap.base.UnieapConstants;
import com.unieap.base.inf.unitls.TransformMessageUtils;
import com.unieap.base.utils.XmlJSONUtils;
import com.unieap.base.vo.BizConfigVO;
import com.unieap.base.vo.InfConfigNSVO;
import com.unieap.base.vo.InfConfigVO;

import net.sf.json.JSONObject;
@Service
public class CommonTransform2Standard implements TransformMessageHandler {

	@Override
	public Object transform(Object payload, Map<String, Object> extParameters) throws Exception {
		// TODO Auto-generated method stub
		BizConfigVO bizConfigVO = (BizConfigVO) extParameters.get(UnieapConstants.BIZCONFIG);
		BizMessageVO bizMessageVO =  (BizMessageVO) extParameters.get("BIZMESSAGEVO");
		if(bizMessageVO ==null) {
			return null;
		}
		List<InfConfigVO> dependInfCodeList = bizConfigVO.getDependInfCodeList();
		Map<String, Object> results = new HashMap<String, Object>();
		Map<String, String> payloads = (Map<String, String>) payload;
		for (InfConfigVO infConfigVO : dependInfCodeList) {
			if(payloads.containsKey(infConfigVO.getInfCode())) {
				List<InfConfigNSVO> NSList = infConfigVO.getNSList();
				Map<String, String> ns = new HashMap<String, String>();
				if (NSList != null && NSList.size() > 0) {
					for (InfConfigNSVO vo : NSList) {
						ns.put(vo.getAlias(), vo.getNs());
					}
					if("xml".equals(infConfigVO.getResultType())) {
						org.dom4j.Document document = XmlJSONUtils
								.getSOAPXMLDocumentDom4J(payloads.get(infConfigVO.getInfCode()), ns);
						results.put(infConfigVO.getInfCode(), document);
					}
					if("xml".equals(infConfigVO.getResultType())) {
						JSONObject jso = JSONObject.fromObject(payloads.get(infConfigVO.getInfCode()));
						ReadContext ctx = JsonPath.parse(jso);
						results.put(infConfigVO.getInfCode(), ctx);
					}
				}
			}
		}
		if(!results.isEmpty()) {
			return TransformMessageUtils.getBizMessage(bizMessageVO, results);
		}
		return null;
	}

}
