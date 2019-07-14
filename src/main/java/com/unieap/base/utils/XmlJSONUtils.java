package com.unieap.base.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

public class XmlJSONUtils {
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
	}

	public static org.dom4j.Document getSOAPXMLDocumentDom4J(String soapXMLMessage, Map<String, String> namespace)
			throws Exception {
		if (soapXMLMessage == null) {
			return null;
		}
		SAXReader saxReader = new SAXReader();
		saxReader.getDocumentFactory().setXPathNamespaceURIs(namespace);
		org.dom4j.Document document = saxReader.read(new ByteArrayInputStream(soapXMLMessage.getBytes("UTF-8")));
		return document;
	}

	public static String getNSAlias(Map<String, String> NSList, String ns) {
		if (NSList.containsValue(ns)) {
			for (String key : NSList.keySet()) {
				String value = NSList.get(key);
				if (ns.equals(value)) {
					return key;
				}
			}
		}
		String NSAlias = "ns" + NSList.size();
		NSList.put(NSAlias, ns);
		return NSAlias;
	}
	public static String formatXML(String inputXML) throws IOException{
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new StringReader(inputXML));
		} catch (DocumentException e1) {
			return inputXML;
		}
		String requestXML = null;
		XMLWriter xw = null;
		if (document != null) {
			try {
				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setEncoding("UTF-8");
				StringWriter sw = new StringWriter();
				xw = new XMLWriter(sw, format);
				xw.setEscapeText(false);
				xw.write(document);
				requestXML = sw.toString();
				xw.flush();
			} finally {
				if (xw != null) {
					try {
						xw.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return requestXML;
	}
}
