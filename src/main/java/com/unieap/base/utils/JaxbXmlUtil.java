package com.unieap.base.utils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

public class JaxbXmlUtil {
	private static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * 调用示例
	 */
	public static void main(String[] args) {
	}

	/**
	 * pojo转换成xml 默认编码UTF-8
	 */
	public static String convertBeanToXml(Object obj) throws Exception {
		return convertBeanToXml(obj, DEFAULT_ENCODING);
	}

	/**
	 * pojo转换成xml
	 */
	public static String convertBeanToXml(Object obj, String encoding) throws Exception {
		String result;
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = context.createMarshaller();
		// 生成报文的格式化
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
		StringWriter writer = new StringWriter();
		marshaller.marshal(obj, writer);
		result = writer.toString();
		return result;
	}

	/**
	 * xml转换成pojo
	 */
	public static <T> T convertXmlToJavaBean(String xml, String elementName, Class<T> t) throws Exception {
		T obj;
		Document document = DocumentHelper.parseText(xml);
		String beanXml = xml;
		// 如果不是SOAP返回的报文，是XML字符串则不需要这行代码
		if (document.getRootElement().element("Body")!= null) {
			beanXml = document.getRootElement().element("Body").element(elementName).asXML();
		}
		JAXBContext context = JAXBContext.newInstance(t);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		@SuppressWarnings("unchecked")
		T unmarshal = (T) unmarshaller.unmarshal(new StringReader(beanXml));
		obj = unmarshal;
		return obj;
	}

}
