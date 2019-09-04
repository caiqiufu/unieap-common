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
import com.unieap.base.ApplicationContextProvider;
import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.UnieapConstants;
import com.unieap.base.inf.verify.FieldVerify;
import com.unieap.base.inf.vo.BizFieldVO;
import com.unieap.base.inf.vo.BizMessageVO;
import com.unieap.base.inf.vo.InfConfigVO;
import com.unieap.base.inf.vo.InfFieldVO;

public class TransformMessageUtils {

	@SuppressWarnings("unchecked")
	public static String getBizMessage2JSON(BizMessageVO bizMessageVO, Map<String, Object> results) throws Exception {
		List<BizFieldVO> fields = new ArrayList<BizFieldVO>();
		net.minidev.json.JSONObject jso = new net.minidev.json.JSONObject();
		ReadContext ctx = JsonPath.parse(jso);
		Map<String, Object> extParameters = new HashMap<String, Object>();
		extParameters.put(UnieapConstants.BIZMESSAGEVO, bizMessageVO);
		if (!bizMessageVO.getFieldList().isEmpty()) {
			BizFieldVO bizFieldVO = bizMessageVO.getRootFieldVO();
			fields.add(bizFieldVO);
			while (!bizFieldVO.isLeaf() || fields.size() > 0) {
				if (!bizFieldVO.isLeaf() && fields.size() == 0) {
					break;
				}
				BizFieldVO parentVO = bizFieldVO.getParentVO();
				InfConfigVO infConfigVO = UnieapCacheMgt.getInfHandler(bizFieldVO.getInfFieldVO().getInfCode());
				if (bizFieldVO.isLeaf()) {
					if (parentVO == null) {
						if ("XML".equals(infConfigVO.getResultType())) {
							String value = getMessageFromXmlSourceValue(
									(org.dom4j.Document) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
									bizFieldVO.getInfFieldVO());
							if (bizFieldVO.getInfFieldVO().getClassName() != null) {
								FieldVerify fieldVerify = (FieldVerify) ApplicationContextProvider
										.getBean(bizFieldVO.getInfFieldVO().getClassName());
								value = (String) fieldVerify.verify(bizFieldVO, bizFieldVO.getInfFieldVO(), value,
										extParameters);
							}
							if (bizFieldVO.getClassName() != null) {
								FieldVerify fieldVerify = (FieldVerify) ApplicationContextProvider
										.getBean(bizFieldVO.getClassName());
								value = (String) fieldVerify.verify(bizFieldVO, bizFieldVO.getInfFieldVO(), value,
										extParameters);
							}
							jso.put(bizFieldVO.getFieldName(), value);
						}
						if ("JSON".equals(infConfigVO.getResultType())) {
							String value = getMessageFromJsonSourceValue(
									(ReadContext) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
									bizFieldVO.getInfFieldVO());
							if (bizFieldVO.getInfFieldVO().getClassName() != null) {
								FieldVerify fieldVerify = (FieldVerify) ApplicationContextProvider
										.getBean(bizFieldVO.getInfFieldVO().getClassName());
								value = (String) fieldVerify.verify(bizFieldVO, bizFieldVO.getInfFieldVO(), value,
										extParameters);
							}
							if (bizFieldVO.getClassName() != null) {
								FieldVerify fieldVerify = (FieldVerify) ApplicationContextProvider
										.getBean(bizFieldVO.getClassName());
								value = (String) fieldVerify.verify(bizFieldVO, bizFieldVO.getInfFieldVO(), value,
										extParameters);
							}
							jso.put(bizFieldVO.getFieldName(), value);
						}

					} else {
						if ("Object".equals(parentVO.getFieldType())) {
							if ("XML".equals(infConfigVO.getResultType())) {
								String value = getMessageFromXmlSourceValue(
										(org.dom4j.Document) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
										bizFieldVO.getInfFieldVO());
								if (bizFieldVO.getInfFieldVO().getClassName() != null) {
									FieldVerify fieldVerify = (FieldVerify) ApplicationContextProvider
											.getBean(bizFieldVO.getInfFieldVO().getClassName());
									value = (String) fieldVerify.verify(bizFieldVO, bizFieldVO.getInfFieldVO(), value,
											extParameters);
								}
								if (bizFieldVO.getClassName() != null) {
									FieldVerify fieldVerify = (FieldVerify) ApplicationContextProvider
											.getBean(bizFieldVO.getClassName());
									value = (String) fieldVerify.verify(bizFieldVO, bizFieldVO.getInfFieldVO(), value,
											extParameters);
								}
								((net.minidev.json.JSONObject) ctx.read(parentVO.getXpath()))
										.put(bizFieldVO.getFieldName(), value);
							}
							if ("JSON".equals(infConfigVO.getResultType())) {
								String value = getMessageFromJsonSourceValue(
										(ReadContext) results.get(bizFieldVO.getInfFieldVO().getInfCode()),
										bizFieldVO.getInfFieldVO());
								if (bizFieldVO.getInfFieldVO().getClassName() != null) {
									FieldVerify fieldVerify = (FieldVerify) ApplicationContextProvider
											.getBean(bizFieldVO.getInfFieldVO().getClassName());
									value = (String) fieldVerify.verify(bizFieldVO, bizFieldVO.getInfFieldVO(), value,
											extParameters);
								}
								if (bizFieldVO.getClassName() != null) {
									FieldVerify fieldVerify = (FieldVerify) ApplicationContextProvider
											.getBean(bizFieldVO.getClassName());
									value = (String) fieldVerify.verify(bizFieldVO, bizFieldVO.getInfFieldVO(), value,
											extParameters);
								}
								((net.minidev.json.JSONObject) ctx.read(parentVO.getXpath()))
										.put(bizFieldVO.getFieldName(), value);
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
							if (bizFieldVO.getInfFieldVO().getClassName() != null) {
								FieldVerify fieldVerify = (FieldVerify) ApplicationContextProvider
										.getBean(bizFieldVO.getInfFieldVO().getClassName());
								values = (List<String>) fieldVerify.verify(bizFieldVO, bizFieldVO.getInfFieldVO(),
										values, extParameters);
							}
							if (bizFieldVO.getClassName() != null) {
								FieldVerify fieldVerify = (FieldVerify) ApplicationContextProvider
										.getBean(bizFieldVO.getClassName());
								values = (List<String>) fieldVerify.verify(bizFieldVO, bizFieldVO.getInfFieldVO(),
										values, extParameters);
							}
							// records list from source
							net.minidev.json.JSONArray allfieldsobj = ctx.read(parentVO.getXpath());
							int i = values.size();
							if (i > 0) {
								if (allfieldsobj.size() == i) {
									for (int j = 0; j < i; j++) {
										((net.minidev.json.JSONObject) allfieldsobj.get(j))
												.put(bizFieldVO.getFieldName(), values.get(j));
									}
								} else {
									for (int j = 0; j < i; j++) {
										net.minidev.json.JSONObject obj = new net.minidev.json.JSONObject();
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
								jso.put(bizFieldVO.getFieldName(), new net.minidev.json.JSONObject());
							}
						}
						if ("List".equals(bizFieldVO.getFieldType())) {
							if (!jso.containsKey(bizFieldVO.getFieldName())) {
								jso.put(bizFieldVO.getFieldName(), new net.minidev.json.JSONArray());
							}
						}
					} else {
						if ("Object".equals(parentVO.getFieldType())) {
							net.minidev.json.JSONObject pjson = ctx.read(bizFieldVO.getParentVO().getXpath());
							if ("Object".equals(bizFieldVO.getFieldType())) {
								if (!pjson.containsKey(bizFieldVO.getFieldName())) {
									pjson.put(bizFieldVO.getFieldName(), new net.minidev.json.JSONObject());
								}
							}
							if ("List".equals(bizFieldVO.getFieldType())) {
								if (!pjson.containsKey(bizFieldVO.getFieldName())) {
									pjson.put(bizFieldVO.getFieldName(), new net.minidev.json.JSONArray());
								} else {
									((net.minidev.json.JSONObject) pjson.get(bizFieldVO.getFieldName()))
											.put(bizFieldVO.getFieldName(), new net.minidev.json.JSONArray());
								}
							}
						}
						if ("List".equals(parentVO.getFieldType())) {
							net.minidev.json.JSONArray pjson = ctx.read(bizFieldVO.getParentVO().getXpath());
							if ("Object".equals(bizFieldVO.getFieldType())) {
								if (!((net.minidev.json.JSONObject) pjson.get(0))
										.containsKey(bizFieldVO.getFieldName())) {
									((net.minidev.json.JSONObject) pjson.get(0)).put(bizFieldVO.getFieldName(),
											new net.minidev.json.JSONObject());
								}
							}
							if ("List".equals(bizFieldVO.getFieldType())) {
								if (!((net.minidev.json.JSONObject) pjson.get(0))
										.containsKey(bizFieldVO.getFieldName())) {
									((net.minidev.json.JSONObject) pjson.get(0)).put(bizFieldVO.getFieldName(),
											new net.minidev.json.JSONArray());
								} else {
									((net.minidev.json.JSONObject) ((net.minidev.json.JSONObject) pjson.get(0))
											.get(bizFieldVO.getFieldName())).put(bizFieldVO.getFieldName(),
													new net.minidev.json.JSONArray());
								}
							}
						}
					}
				}
				if (bizFieldVO.getChildrenList() != null && bizFieldVO.getChildrenList().size() > 0) {
					List<BizFieldVO> childrenList = bizFieldVO.getChildrenList();
					for (BizFieldVO childVO : childrenList) {
						if (childVO.isOptional()) {
							int length = 0;
							if ("JSON".equals(infConfigVO.getResultType())) {
								length = getMessageFromJsonSourceListLength(
										(ReadContext) results.get(childVO.getInfFieldVO().getInfCode()),
										childVO.getInfFieldVO());
							}
							if ("XML".equals(infConfigVO.getResultType())) {
								length = getMessageFromXmlSourceListLength(
										(org.dom4j.Document) results.get(childVO.getInfFieldVO().getInfCode()),
										childVO.getInfFieldVO());
							}
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
			while (!bizFieldVO.isLeaf() || fields.size() > 0) {
				BizFieldVO parentVO = bizFieldVO.getParentVO();
				InfConfigVO infConfigVO = UnieapCacheMgt.getInfHandler(bizFieldVO.getInfFieldVO().getInfCode());
				if (bizFieldVO.isLeaf()) {
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

	public static int getMessageFromJsonSourceListLength(ReadContext ctx, InfFieldVO infFieldVO) {
		Object obj = ctx.read(infFieldVO.getXpath());
		if (obj != null) {
			if (obj instanceof net.minidev.json.JSONArray) {
				return ((net.minidev.json.JSONArray) obj).size();
			} else {
				return 1;
			}
		}
		return 0;
	}

	public static String getMessageFromJsonSourceValue(ReadContext ctx, InfFieldVO infFieldVO) {
		Object obj = ctx.read(infFieldVO.getXpath());
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	public static List<String> getMessageFromJsonSourceList(ReadContext ctx, InfFieldVO infFieldVO) {
		net.minidev.json.JSONArray pjson = ctx.read(infFieldVO.getXpath());
		if (pjson != null && pjson.size() > 0) {
			List<String> values = new ArrayList<String>();
			for (int i = 0; i < pjson.size(); i++) {
				Object obj = pjson.get(i);
				if (obj != null) {
					values.add(obj.toString());
				} else {
					values.add("");
				}
			}
			return values;
		}
		return null;
	}
}
