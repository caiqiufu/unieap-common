package com.unieap.base.utils;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

public class XmlJSONUtils {
	private static final Log logger = LogFactory.getLog(XmlJSONUtils.class);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String json = "...";

		ReadContext ctx = JsonPath.parse(json);

		List<String> authorsOfBooksWithISBN = ctx.read("$.store.book[*].author");
	}

	/**
	 * 将xml字符串<STRONG>转换</STRONG>为JSON字符串
	 * 
	 * @param xmlString xml字符串
	 * @return JSON<STRONG>对象</STRONG>
	 */
	public static String xml2json(String xmlString) {
		XMLSerializer xmlSerializer = new XMLSerializer();
		JSON json = xmlSerializer.read(xmlString);
		return json.toString(1);
	}

	/**
	 * 将xmlDocument<STRONG>转换</STRONG>为JSON<STRONG>对象</STRONG>
	 * 
	 * @param xmlDocument XML Document
	 * @return JSON<STRONG>对象</STRONG>
	 */
	public static String xml2json(Document xmlDocument) {
		return xml2json(xmlDocument.toString());
	}

	/**
	 * JSON(数组)字符串<STRONG>转换</STRONG>成XML字符串
	 * 
	 * @param jsonString
	 * @return
	 */
	public static String json2xml(String jsonString) {
		XMLSerializer xmlSerializer = new XMLSerializer();
		return xmlSerializer.write(JSONSerializer.toJSON(jsonString));
		// return xmlSerializer.write(JSONArray.fromObject(jsonString));//这种方式只支持JSON数组
	}
	public static org.dom4j.Document getSOAPXMLDocumentDom4J(String soapXMLMessage, Map<String, String> namespace)
			throws Exception {
		if (soapXMLMessage == null) {
			return null;
		}
		logger.debug("soapXMLMessage="+soapXMLMessage);
		SAXReader saxReader = new SAXReader();
		saxReader.getDocumentFactory().setXPathNamespaceURIs(namespace);
		org.dom4j.Document document = saxReader.read(new ByteArrayInputStream(soapXMLMessage.getBytes("UTF-8")));
		return document;

	}
}