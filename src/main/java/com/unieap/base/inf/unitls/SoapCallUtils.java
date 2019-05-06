package com.unieap.base.inf.unitls;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import com.unieap.base.UnieapCacheMgt;
import com.unieap.base.UnieapConstants;
import com.unieap.base.inf.element.RequestHeader;
import com.unieap.base.inf.element.RequestInfo;
import com.unieap.base.inf.handler.BizServiceUtils;
import com.unieap.base.inf.handler.ProcessResult;
import com.unieap.base.pojo.Esblog;
import com.unieap.base.vo.InfConfigVO;

public class SoapCallUtils {
	private static MessageFactory messageFactory;
	private static SOAPConnectionFactory connectionFactory;

	private static void factory() throws Exception {
		if (messageFactory == null) {
			messageFactory = MessageFactory.newInstance();
		}
		if (connectionFactory == null) {
			connectionFactory = SOAPConnectionFactory.newInstance();
		}
	}

	public static MessageFactory getMessageFactory() throws Exception {
		factory();
		return messageFactory;

	}

	public static SOAPConnectionFactory getConnectionFactory() throws Exception {
		factory();
		return connectionFactory;
	}

	public static SOAPMessage callWebService(String url, SOAPMessage request, int timeout) throws Exception {
		SOAPConnection connection = getConnectionFactory().createConnection();
		SOAPMessage response = connection.call(request, getURL(url, timeout));
		return response;
	}

	public static SOAPMessage callWebService(InfConfigVO infConfigVO, SOAPMessage request) throws Exception {
		if (UnieapConstants.YES.equals(infConfigVO.getActivateFlag())) {
			SOAPConnection connection = getConnectionFactory().createConnection();
			SOAPMessage response = connection.call(request, getURL(infConfigVO.getUrl(), infConfigVO.getTimeout()));
			return response;
		} else {
			SOAPMessage response = SoapCallUtils
					.formartSoapString(new String(infConfigVO.getResponseSample(), "UTF-8"));
			return response;
		}
	}

	/**
	 * 
	 * @param bizConfigVO
	 * @param requestMessage
	 * @return
	 * @throws Exception
	 */
	public static String callHTTPService(String requestUrl, int timeout, String SOAPAction, String requestMessage)
			throws Exception {
		URL url = new URL(requestUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// 3：设置连接参数
		// 3.1设置发送方式：POST必须大写
		connection.setRequestMethod("POST");
		// 3.2设置数据格式：Content-type
		connection.setRequestProperty("content-type", "text/xml;charset=utf-8");
		// 3.3设置输入输出，新创建的connection默认是没有读写权限的，
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setConnectTimeout(timeout);
		connection.setRequestProperty("SOAPAction", SOAPAction);
		// 4：组织SOAP协议数据，发送给服务端
		OutputStream os = connection.getOutputStream();
		os.write(requestMessage.getBytes());
		// 5：接收服务端的响应
		// int responseCode = connection.getResponseCode();
		InputStream is = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		String temp = null;
		while (null != (temp = br.readLine())) {
			sb.append(temp);
		}
		is.close();
		isr.close();
		br.close();
		os.close();
		return sb.toString();
	}

	public static URL getURL(String url, final int timeout) throws Exception {
		URL urlval = new URL(new URL(url), "", new URLStreamHandler() {
			@Override
			protected URLConnection openConnection(URL url) throws IOException {
				URL target = new URL(url.toString());
				URLConnection connection = target.openConnection();
				// Connection settings
				connection.setConnectTimeout(timeout);
				connection.setReadTimeout(timeout);
				return (connection);
			}
		});
		return urlval;
	}

	public static SOAPMessage callService(RequestInfo requestInfo, SOAPMessage request) throws Exception {
		String requestTime = UnieapConstants.getCurrentTime();
		RequestHeader requestHeader = requestInfo.getRequestHeader();
		requestHeader.setRequestTime(requestTime);
		requestHeader.setSystemCode(UnieapConstants.ESB);
		String infCode = requestInfo.getRequestBody().getBizCode();
		InfConfigVO vo = UnieapCacheMgt.infHandlerList.get(infCode);
		String url = vo.getUrl();
		String appName = vo.getDestAppName();
		SOAPMessage response;
		try {
			response = callWebService(url, request, 10000);
		} catch (Exception e) {
			ProcessResult processResult = new ProcessResult();
			processResult.setResultCode(UnieapConstants.C99999);
			processResult.setResultDesc(e.getLocalizedMessage());
			String requestInfoString = BizServiceUtils.getSoapMessageString(request);
			Esblog esblog = BizServiceUtils.getEsbLog(requestInfo, processResult, requestInfoString, "", "", appName);
			UnieapCacheMgt.setPersistenceData(UnieapConstants.PERSISTENCE_TYPE.ESB, esblog);
			throw e;
		}
		return response;

	}

	public static SOAPMessage formartSoapString(String soapString) throws Exception, SOAPException, Exception {
		SOAPMessage reqMsg = getMessageFactory().createMessage(new MimeHeaders(),
				new ByteArrayInputStream(soapString.getBytes(Charset.forName("UTF-8"))));
		reqMsg.saveChanges();
		return reqMsg;
	}

	public static String replaceParameters(String xml, Map<String, Object> inputParameters) {
		for (Entry<String, Object> entry : inputParameters.entrySet()) {
			String replacedString = "{" + entry.getKey() + "}";
			if (xml.indexOf(replacedString) > 0) {
				xml = xml.replaceAll("\\{" + entry.getKey() + "\\}", entry.getValue().toString());
			}
		}
		return xml;
	}

	public static String coverSOAPMessageToStr(SOAPMessage message) throws Exception {
		TransformerFactory tff = TransformerFactory.newInstance();
		Transformer tf = tff.newTransformer();
		// Get reply content
		Source source = message.getSOAPPart().getContent();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
		StreamResult result = new StreamResult(bos);
		tf.transform(source, result);
		return new String(bos.toByteArray());
	}

	public static boolean heardCheck(String url) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:uni=\"http://www.unieap.easy.com\">");
		sb.append("<soapenv:Header/>");
		sb.append("<soapenv:Body>");
		sb.append("<uni:bizHandle>");
		sb.append("<requestInfo>{}</requestInfo>");
		sb.append("</uni:bizHandle>");
		sb.append("</soapenv:Body>");
		sb.append("</soapenv:Envelope>");
		String requestMessage = sb.toString();
		SOAPMessage requestSOAPMessage = SoapCallUtils.formartSoapString(requestMessage);
		try {
			callWebService(url, requestSOAPMessage, 50);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
