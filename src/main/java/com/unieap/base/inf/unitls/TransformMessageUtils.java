package com.unieap.base.inf.unitls;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Node;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.inf.transform.BizFieldVO;
import com.unieap.base.inf.transform.BizMessageVO;
import com.unieap.base.inf.transform.InfFieldVO;
import com.unieap.base.vo.InfConfigVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TransformMessageUtils {
	private static final Log logger = LogFactory.getLog(TransformMessageUtils.class);

	public static String getBizMessage(BizMessageVO bizMessageVO, Map<String, Object> results) {
		List<BizFieldVO> fields = new ArrayList<BizFieldVO>();
		JSONObject jso = new JSONObject();
		ReadContext ctx = JsonPath.parse(jso);
		if (!bizMessageVO.getFieldList().isEmpty()) {
			BizFieldVO bizFieldVO = bizMessageVO.getRootFieldVO();
			fields.add(bizFieldVO);
			while (!bizFieldVO.isLeaf || fields.size() > 0) {
				InfConfigVO infConfigVO = UnieapCacheMgt.getInfHandler(bizFieldVO.getInfFieldVO().getInfCode());
				if (bizFieldVO.isLeaf) {
					if (bizFieldVO.getParentVO() == null) {
						if ("xml".equals(infConfigVO.getResultType())) {
							String value = getMessageFromXmlSourceValue(
									(org.dom4j.Document) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
									bizFieldVO.getInfFieldVO());
							jso.put(bizFieldVO.getFieldName(), value);
						}
						if ("json".equals(infConfigVO.getResultType())) {
							String value = getMessageFromJsonSourceValue(
									(ReadContext) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
									bizFieldVO.getInfFieldVO());
							jso.put(bizFieldVO.getFieldName(), value);
						}

					} else {
						if ("Object".equals(bizFieldVO.getParentVO().getFieldType())) {
							if ("xml".equals(infConfigVO.getResultType())) {
								String value = getMessageFromXmlSourceValue(
										(org.dom4j.Document) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
										bizFieldVO.getInfFieldVO());
								((JSONObject) ctx.read(bizFieldVO.getParentVO().getXpath()))
										.put(bizFieldVO.getFieldName(), value);
							}
							if ("json".equals(infConfigVO.getResultType())) {
								String value = getMessageFromJsonSourceValue(
										(ReadContext) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
										bizFieldVO.getInfFieldVO());
								((JSONObject) ctx.read(bizFieldVO.getParentVO().getXpath()))
										.put(bizFieldVO.getFieldName(), value);
							}
						}
						if ("List".equals(bizFieldVO.getParentVO().getFieldType())) {
							List<String> values = new ArrayList<String>();
							if ("xml".equals(infConfigVO.getResultType())) {
								values = getMessageFromXmlSourceList(
										(org.dom4j.Document) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
										bizFieldVO.getInfFieldVO());
							}
							if ("json".equals(infConfigVO.getResultType())) {
								values = getMessageFromJsonSourceList(
										(ReadContext) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
										bizFieldVO.getInfFieldVO());
							}
							// records list from source
							JSONArray allfieldsobj = ctx.read(bizFieldVO.getParentVO().getXpath());
							int i = values.size();
							if (i > 0) {
								if (allfieldsobj.size() == i) {
									for (int j = 0; j < i; j++) {
										allfieldsobj.getJSONObject(j).put(bizFieldVO.getFieldName(), values.get(j));
									}
								} else {
									for (int j = 0; j < i; j++) {
										JSONObject obj = new JSONObject();
										obj.put(bizFieldVO.getFieldName(), values.get(j));
										allfieldsobj.add(obj);
									}
								}
							}
						}
					}
				} else {
					if (bizFieldVO.getParentVO() == null) {
						if ("Object".equals(bizFieldVO.getFieldType())) {
							if (!jso.containsKey(bizFieldVO.getFieldName())) {
								jso.put(bizFieldVO.getFieldName(), new JSONObject());
							}
						}
						if ("List".equals(bizFieldVO.getFieldType())) {
							if (!jso.containsKey(bizFieldVO.getFieldName())) {
								jso.put(bizFieldVO.getFieldName(), new JSONArray());
							}
						}
					} else {
						BizFieldVO pvo = bizFieldVO.getParentVO();
						if ("Object".equals(pvo.getFieldType())) {
							JSONObject pjson = ctx.read(bizFieldVO.getParentVO().getXpath());
							if ("Object".equals(bizFieldVO.getFieldType())) {
								if (!pjson.containsKey(bizFieldVO.getFieldName())) {
									pjson.put(bizFieldVO.getFieldName(), new JSONObject());
								}
							}
							if ("List".equals(bizFieldVO.getFieldType())) {
								if (!pjson.containsKey(bizFieldVO.getFieldName())) {
									pjson.put(bizFieldVO.getFieldName(), new JSONArray());
								} else {
									pjson.getJSONObject(bizFieldVO.getFieldName()).put(bizFieldVO.getFieldName(),
											new JSONArray());
								}
							}
						}
						if ("List".equals(pvo.getFieldType())) {
							JSONArray pjson = ctx.read(bizFieldVO.getParentVO().getXpath());
							if ("Object".equals(bizFieldVO.getFieldType())) {
								if (!pjson.getJSONObject(0).containsKey(bizFieldVO.getFieldName())) {
									pjson.getJSONObject(0).put(bizFieldVO.getFieldName(), new JSONObject());
								}
							}
							if ("List".equals(bizFieldVO.getFieldType())) {
								if (!pjson.getJSONObject(0).containsKey(bizFieldVO.getFieldName())) {
									pjson.getJSONObject(0).put(bizFieldVO.getFieldName(), new JSONArray());
								} else {
									pjson.getJSONObject(0).getJSONObject(bizFieldVO.getFieldName())
											.put(bizFieldVO.getFieldName(), new JSONArray());
								}
							}
						}
					}
				}
				if (bizFieldVO.getChildrenList() != null && bizFieldVO.getChildrenList().size() > 0) {
					fields.addAll(bizFieldVO.getChildrenList());
				}
				fields.remove(bizFieldVO);
				if (fields.size() > 0) {
					bizFieldVO = fields.get(0);
				}
			}
		}
		String json = jso.toString();
		logger.debug("json=" + json);
		return json;
	}

	public static String getMessageFromXmlSourceValue(org.dom4j.Document document, InfFieldVO infFieldVO) {
		return document.selectSingleNode(infFieldVO.getXpath()).getText();
	}

	public static List<String> getMessageFromXmlSourceList(org.dom4j.Document document, InfFieldVO infFieldVO) {
		List<Node> nodes = document.selectNodes(infFieldVO.getXpath());
		if (nodes != null && nodes.size() > 0) {
			List<String> values = new ArrayList<String>();
			for (int i = 0; i < nodes.size(); i++) {
				values.add(nodes.get(i).getText());
			}
			return values;
		}
		return null;
	}

	public static String getMessageFromJsonSourceValue(ReadContext ctx, InfFieldVO infFieldVO) {
		JSONObject pjson = ctx.read(infFieldVO.getXpath());
		return pjson.getString(infFieldVO.getFieldName());
	}

	public static List<String> getMessageFromJsonSourceList(ReadContext ctx, InfFieldVO infFieldVO) {
		JSONArray pjson = ctx.read(infFieldVO.getXpath());
		if (pjson != null && pjson.size() > 0) {
			List<String> values = new ArrayList<String>();
			for (int i = 0; i < pjson.size(); i++) {
				JSONObject obj = pjson.getJSONObject(i);
				values.add(obj.getString(infFieldVO.getFieldName()));
			}
			return values;
		}
		return null;
	}
}
