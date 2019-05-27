package com.unieap.base.inf.unitls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.inf.vo.BizFieldVO;
import com.unieap.base.inf.vo.BizMessageVO;
import com.unieap.base.inf.vo.InfConfigVO;
import com.unieap.base.inf.vo.InfFieldVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TransformMessageUtils {

	public static String getBizMessage2JSON(BizMessageVO bizMessageVO, Map<String, Object> results) {
		List<BizFieldVO> fields = new ArrayList<BizFieldVO>();
		JSONObject jso = new JSONObject();
		ReadContext ctx = JsonPath.parse(jso);
		if (!bizMessageVO.getFieldList().isEmpty()) {
			BizFieldVO bizFieldVO = bizMessageVO.getRootFieldVO();
			fields.add(bizFieldVO);
			while (!bizFieldVO.isLeaf || fields.size() > 0) {
				BizFieldVO parentVO = bizFieldVO.getParentVO();
				InfConfigVO infConfigVO = UnieapCacheMgt.getInfHandler(bizFieldVO.getInfFieldVO().getInfCode());
				if (bizFieldVO.isLeaf) {
					if (parentVO == null) {
						if ("XML".equals(infConfigVO.getResultType())) {
							String value = getMessageFromXmlSourceValue(
									(org.dom4j.Document) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
									bizFieldVO.getInfFieldVO());
							jso.put(bizFieldVO.getFieldName(), value);
						}
						if ("JSON".equals(infConfigVO.getResultType())) {
							String value = getMessageFromJsonSourceValue(
									(ReadContext) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
									bizFieldVO.getInfFieldVO());
							jso.put(bizFieldVO.getFieldName(), value);
						}

					} else {
						if ("Object".equals(parentVO.getFieldType())) {
							if ("XML".equals(infConfigVO.getResultType())) {
								String value = getMessageFromXmlSourceValue(
										(org.dom4j.Document) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
										bizFieldVO.getInfFieldVO());
								((JSONObject) ctx.read(parentVO.getXpath())).put(bizFieldVO.getFieldName(), value);
							}
							if ("JSON".equals(infConfigVO.getResultType())) {
								String value = getMessageFromJsonSourceValue(
										(ReadContext) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
										bizFieldVO.getInfFieldVO());
								((JSONObject) ctx.read(parentVO.getXpath())).put(bizFieldVO.getFieldName(), value);
							}
						}
						if ("List".equals(parentVO.getFieldType())) {
							List<String> values = new ArrayList<String>();
							if ("XML".equals(infConfigVO.getResultType())) {
								values = getMessageFromXmlSourceList(
										(org.dom4j.Document) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
										bizFieldVO.getInfFieldVO());
							}
							if ("JSON".equals(infConfigVO.getResultType())) {
								values = getMessageFromJsonSourceList(
										(ReadContext) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
										bizFieldVO.getInfFieldVO());
							}
							// records list from source
							JSONArray allfieldsobj = ctx.read(parentVO.getXpath());
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
					if (parentVO == null) {
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
						if ("Object".equals(parentVO.getFieldType())) {
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
						if ("List".equals(parentVO.getFieldType())) {
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
					List<BizFieldVO> childrenList = bizFieldVO.getChildrenList();
					for (BizFieldVO childVO : childrenList) {
						if (childVO.isOptional()) {
							int length = getMessageFromXmlSourceListLength(
									(org.dom4j.Document) results.get(childVO.getInfFieldVO().getInfCode()),
									childVO.getInfFieldVO());
							if (length > 0) {
								fields.add(childVO);
							}
						} else {
							fields.add(childVO);
						}
					}
					// fields.addAll(bizFieldVO.getChildrenList());
				}
				fields.remove(bizFieldVO);
				if (fields.size() > 0) {
					bizFieldVO = fields.get(0);
				}
			}
		}
		return jso.toString();
	}

	public static String getBizMessage2XML(BizMessageVO bizMessageVO, Map<String, Object> results) {
		List<BizFieldVO> fields = new ArrayList<BizFieldVO>();
		Document document = DocumentHelper.createDocument();
		Map<String, Element> parentElements = new HashMap<String, Element>();
		Element element;
		if (!bizMessageVO.getFieldList().isEmpty()) {
			BizFieldVO bizFieldVO = bizMessageVO.getRootFieldVO();
			Map<String, String> nSList = new HashMap<String, String>();
			nSList.put(bizFieldVO.getNs(), "n0");
			if (bizFieldVO.getNs() != null) {
				Namespace namespace = new Namespace("n0", bizFieldVO.getNs());
				element = document.addElement(new QName(bizFieldVO.getFieldName(), namespace));
			} else {
				element = document.addElement(bizFieldVO.getFieldName());
			}
			fields.add(bizFieldVO);
			while (!bizFieldVO.isLeaf || fields.size() > 0) {
				BizFieldVO parentVO = bizFieldVO.getParentVO();
				InfConfigVO infConfigVO = UnieapCacheMgt.getInfHandler(bizFieldVO.getInfFieldVO().getInfCode());
				if (bizFieldVO.isLeaf) {
					if (parentVO == null) {
						if ("XML".equals(infConfigVO.getResultType())) {
							String value = getMessageFromXmlSourceValue(
									(org.dom4j.Document) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
									bizFieldVO.getInfFieldVO());
							element.setText(value);
						}
						if ("JSON".equals(infConfigVO.getResultType())) {
							String value = getMessageFromJsonSourceValue(
									(ReadContext) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
									bizFieldVO.getInfFieldVO());
							element.setText(value);
						}

					} else {
						if ("Object".equals(parentVO.getFieldType())) {
							Element parentElement = parentElements.get(parentVO.getXpath());
							if ("Object".equals(bizFieldVO.getFieldType())) {
								element = createElement(parentElement, bizFieldVO, nSList);
								if ("XML".equals(infConfigVO.getResultType())) {
									String value = getMessageFromXmlSourceValue(
											(org.dom4j.Document) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
											bizFieldVO.getInfFieldVO());
									element.setText(value);
								}
								if ("JSON".equals(infConfigVO.getResultType())) {
									String value = getMessageFromJsonSourceValue(
											(ReadContext) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
											bizFieldVO.getInfFieldVO());
									element.setText(value);
								}
							}
							if ("List".equals(bizFieldVO.getFieldType())) {
								List<String> values = new ArrayList<String>();
								if ("XML".equals(infConfigVO.getResultType())) {
									values = getMessageFromXmlSourceList(
											(org.dom4j.Document) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
											bizFieldVO.getInfFieldVO());
								}
								if ("JSON".equals(infConfigVO.getResultType())) {
									values = getMessageFromJsonSourceList(
											(ReadContext) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
											bizFieldVO.getInfFieldVO());
								}
								int i = values.size();
								if (i > 0) {
									for (int j = 0; j < i; j++) {
										parentElement = parentElements.get(parentVO.getFieldName() + "_" + j);
										element = createElement(parentElement, bizFieldVO, nSList);
										element.setText(values.get(j));
									}
								}
							}
						}
						if ("List".equals(parentVO.getFieldType())) {
							List<String> values = new ArrayList<String>();
							if ("XML".equals(infConfigVO.getResultType())) {
								values = getMessageFromXmlSourceList(
										(org.dom4j.Document) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
										bizFieldVO.getInfFieldVO());
							}
							if ("JSON".equals(infConfigVO.getResultType())) {
								values = getMessageFromJsonSourceList(
										(ReadContext) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
										bizFieldVO.getInfFieldVO());
							}
							int i = values.size();
							if (i > 0) {
								for (int j = 0; j < i; j++) {
									Element parentElement = parentElements.get(parentVO.getFieldName() + "_" + j);
									element = createElement(parentElement, bizFieldVO, nSList);
									element.setText(values.get(j));
								}
							}
						}
					}
				} else {
					if (parentVO != null) {
						if ("Object".equals(bizFieldVO.getFieldType())) {
							Element parentElement = parentElements.get(parentVO.getXpath());
							element = createElement(parentElement, bizFieldVO, nSList);
							parentElements.put(bizFieldVO.getFieldName(), element);
						}
						if ("List".equals(bizFieldVO.getFieldType())) {
							int length = getMessageFromXmlSourceListLength(
									(org.dom4j.Document) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
									bizFieldVO.getInfFieldVO());
							if (length > 0) {
								Element parentElement = parentElements.get(bizFieldVO.getParentVO().getXpath());
								for (int i = 0; i < length; i++) {
									element = createElement(parentElement, bizFieldVO, nSList);
									parentElements.put(bizFieldVO.getFieldName() + "_" + i, element);
								}
							}
						}
					}
				}
				if (bizFieldVO.getChildrenList() != null && bizFieldVO.getChildrenList().size() > 0) {
					List<BizFieldVO> childrenList = bizFieldVO.getChildrenList();
					for (BizFieldVO childVO : childrenList) {
						if (childVO.isOptional()) {
							int length = getMessageFromXmlSourceListLength(
									(org.dom4j.Document) results.get(childVO.getInfFieldVO().getInfCode()),
									childVO.getInfFieldVO());
							if (length > 0) {
								fields.add(childVO);
							}
						} else {
							fields.add(childVO);
						}
					}
				}
				parentElements.put(bizFieldVO.getXpath(), element);
				fields.remove(bizFieldVO);
				if (fields.size() > 0) {
					bizFieldVO = fields.get(0);
					if (!nSList.containsKey(bizFieldVO.getNs())) {
						nSList.put(bizFieldVO.getNs(), "n" + nSList.size());
					}
				}
			}
		}
		return document.asXML();
	}

	public static Element createElement(Element element, BizFieldVO bizFieldVO, Map<String, String> nsAlias) {
		if (bizFieldVO.getNs() != null) {
			Namespace namespace = new Namespace(nsAlias.get(bizFieldVO.getNs()), bizFieldVO.getNs());
			return element.addElement(new QName(bizFieldVO.getFieldName(), namespace));
		} else {
			return element.addElement(bizFieldVO.getFieldName());
		}
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

	public static int getMessageFromXmlSourceListLength(org.dom4j.Document document, InfFieldVO infFieldVO) {
		List<Node> nodes = document.selectNodes(infFieldVO.getXpath());
		if (nodes != null && nodes.size() > 0) {
			return nodes.size();
		}
		return 0;
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
