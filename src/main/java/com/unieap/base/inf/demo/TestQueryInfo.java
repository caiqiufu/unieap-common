package com.unieap.base.inf.demo;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.unieap.base.UnieapConstants;
import com.unieap.base.inf.vo.BizConfigVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestQueryInfo {
	private static final String APPLICATION_JSON = "application/json";

	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

	public static void main(String[] args) throws Exception {
		// callQueryService(getResConfigJson());
		/*
		 * callQueryService(sendLoginVerifyCode()); callQueryService(verifyCodeLogin());
		 */
		// callQueryService(voucherCardRecharge());
		// callQueryService(queryOfferingCategory());
		// callQueryService(changeSupplementaryOfferings());
		// callQueryService(changePrimaryOffering());
		// callQueryService(queryOfferings());
		// callQueryService(queryMyBills());
		// callQueryService(queryAllBalance());
		// callQueryService(queryDataBalance());
		// callQueryService(queryMyRechargeLogs());
		// callQueryService(servicePasswordLogin());
		// callQueryService(servicePasswordLogin());
		// callQueryService(queryUpgardeInfo());
		// callQueryService(queryPushMessage());
		// callQueryService(queryShops());
		// callQueryService(queryTransferBalanceLogs());
		// callQueryService(transferBalance());
		// callQueryService(queryXchangeLogs());
		// callQueryService(xchangePromotion());
		String requestMessage = queryDataBalance();
		BizConfigVO bizConfigVO = new BizConfigVO();
		bizConfigVO.setActivateFlag(UnieapConstants.YES);
		// bizConfigVO.setUrl("http://127.0.0.1:8100/unieap/extAction/queryInfo");
		bizConfigVO.setUrl("http://172.30.8.253:8765/unieap/extAction/queryInfo");
/*		String responseInfo = SoapCallUtils.callHTTPService(bizConfigVO.getUrl(), bizConfigVO.getTimeout().intValue(),
				bizConfigVO.getSOAPAction(), requestMessage);*/
		//System.out.println("responseInfo=" + responseInfo);

	}

	public static void performance() throws Exception {
		String json = queryAllBalance();
		callQueryService(json);
	}

	public static void callQueryService(String json) throws Exception {
		String encoderJson = "requestInfo=" + URLEncoder.encode(json, HTTP.UTF_8);
		// String encoderJson = URLEncoder.encode(json, HTTP.UTF_8);请求链接
		String url = "http://127.0.0.1:8100/unieap-esb/extAction.do?method=queryInfo&" + encoderJson;
		// String url =
		// "http://127.0.0.1:8081/Unieap/extAction.do?method=queryInfo&" +
		// encoderJson;
		// http://127.0.0.1:8100/unieap-esb/service?requestInfo=111
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
		// StringEntity se = new StringEntity(encoderJson);
		// se.setContentType(CONTENT_TYPE_TEXT_JSON);
		// se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
		// APPLICATION_JSON));
		// httpPost.setEntity(se);
		CloseableHttpResponse response = httpClient.execute(httpPost);
		try {
			HttpEntity entity = response.getEntity();
			System.out.println(json);
			System.out.println("----------------------------------------");
			// System.out.println(response.getStatusLine());
			if (entity != null) {
				// System.out.println("Response content length: " +
				// entity.getContentLength());
				System.out.println(EntityUtils.toString(entity));
				EntityUtils.consume(entity);
			}
		} finally {
			response.close();
		}
	}

	public static String getRequestHeaderJson(JSONObject jsonBody, String bizCode) throws Exception {
		JSONObject jsonRequest = new JSONObject();
		JSONObject requestHeader = new JSONObject();
		requestHeader.put("appCode", "unieap");
		requestHeader.put("accessName", "unieap");
		requestHeader.put("password", "$2a$10$i75BB3Tx4WZJBsW0oHW1ru2fwZqNVsXM.yAv/HETC/mYcMbzsfLNm");
		requestHeader.put("remoteAddress", "unieap");
		requestHeader.put("operName", "unieap");
		requestHeader.put("systemCode", "unieap");
		requestHeader.put("channelCode", "unieap");
		requestHeader.put("extTransactionId", "unieap");
		requestHeader.put("transactionId", "unieap");
		requestHeader.put("requestTime", "unieap");
		requestHeader.put("version", "unieap");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dt = sdf.format(date);
		requestHeader.put("requestTime", dt);
		jsonRequest.put("requestHeader", requestHeader);
		jsonRequest.put("requestBody", jsonBody);
		return jsonRequest.toString();
	}

	public static String getResConfigJson() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "962138364");
		JSONObject password = new JSONObject();
		password.put("page", "-1");
		password.put("type", "A");
		jsonBody.put("extParameters", password);
		String jstringRequest = getRequestHeaderJson(jsonBody, "C001");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String servicePasswordLogin() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "962138364");
		JSONObject password = new JSONObject();
		password.put("password", "123456");
		jsonBody.put("extParameters", password);
		String jstringRequest = getRequestHeaderJson(jsonBody, "C002");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String sendLoginVerifyCode() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "962138364");
		String jstringRequest = getRequestHeaderJson(jsonBody, "C003");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String verifyCodeLogin() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "962138364");
		JSONObject verifyCode = new JSONObject();
		verifyCode.put("verifyCode", "095921");
		jsonBody.put("extParameters", verifyCode);
		String jstringRequest = getRequestHeaderJson(jsonBody, "C004");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String voucherCardRecharge() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "962138364");
		JSONObject cardPinNumber = new JSONObject();
		cardPinNumber.put("cardPinNumber", "XgX0czMIrX2d6rP1mHQ9f3zUO7nsXk43mlWkntZxESI=");
		jsonBody.put("extParameters", cardPinNumber);
		String jstringRequest = getRequestHeaderJson(jsonBody, "C007");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String queryMyRechargeLogs() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "93268659");
		// JSONObject cardPinNumber = new JSONObject();
		// cardPinNumber.put("cardPinNumber", "11122345678877765554");
		// jsonBody.put("extParameters", cardPinNumber);
		String jstringRequest = getRequestHeaderJson(jsonBody, "C008");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String queryOfferingCategory() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "962138364");
		JSONObject categoryType = new JSONObject();
		categoryType.put("categoryType", "ct_Voice,ct_Data");
		jsonBody.put("extParameters", categoryType);
		String jstringRequest = getRequestHeaderJson(jsonBody, "C009");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String queryOfferings() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "962138364");
		JSONObject categoryId = new JSONObject();
		categoryId.put("categoryId", "2");
		jsonBody.put("extParameters", categoryId);
		String jstringRequest = getRequestHeaderJson(jsonBody, "C010");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String queryDataBalance() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "962138364");
		jsonBody.put("bizCode", "E001");
		String jstringRequest = getRequestHeaderJson(jsonBody, "C011");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String queryShops() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "962138364");
		jsonBody.put("bizCode", "E001");
		JSONObject categoryId = new JSONObject();
		categoryId.put("cityId", "1");
		// categoryId.put("shopName", "西丽镇");
		jsonBody.put("extParameters", categoryId);
		String jstringRequest = getRequestHeaderJson(jsonBody, "C016");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String queryComplains() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "962138364");
		String jstringRequest = getRequestHeaderJson(jsonBody, "C017");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String querySubscribedOfferings() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "962138364");
		String jstringRequest = getRequestHeaderJson(jsonBody, "C013");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String queryAllBalance() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "962138364");
		String jstringRequest = getRequestHeaderJson(jsonBody, "C015");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String queryMyBills() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "93268659");
		String jstringRequest = getRequestHeaderJson(jsonBody, "C012");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String changeSupplementaryOfferings() throws Exception {
		JSONObject offering1 = new JSONObject();
		offering1.put("offeringId", "1290134625");
		offering1.put("offeringType", "S");
		offering1.put("actionType", "1");
		JSONArray supplementaryOfferings = new JSONArray();
		supplementaryOfferings.add(offering1);
		JSONObject offeringInfo = new JSONObject();
		offeringInfo.put("isChangePrimarryOffering", "N");
		offeringInfo.put("supplementaryOfferings", supplementaryOfferings);
		JSONObject jsonBody = new JSONObject();
		JSONObject extParameters = new JSONObject();
		extParameters.put("offeringInfo", offeringInfo);
		jsonBody.put("serviceNumber", "962138364");
		jsonBody.put("extParameters", extParameters);
		String jstringRequest = getRequestHeaderJson(jsonBody, "C014");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String changePrimaryOffering() throws Exception {

		JSONObject offering1 = new JSONObject();
		offering1.put("offeringId", "1290134625");
		offering1.put("offeringType", "S");
		offering1.put("actionType", "2");
		JSONArray supplementaryOfferings = new JSONArray();
		supplementaryOfferings.add(offering1);
		JSONObject offeringInfo = new JSONObject();
		offeringInfo.put("isChangePrimarryOffering", "Y");
		offeringInfo.put("primaryOfferingId", "111222233");
		offeringInfo.put("supplementaryOfferings", supplementaryOfferings);
		JSONObject jsonBody = new JSONObject();
		JSONObject extParameters = new JSONObject();
		extParameters.put("offeringInfo", offeringInfo);
		jsonBody.put("serviceNumber", "962138364");
		jsonBody.put("extParameters", extParameters);
		String jstringRequest = getRequestHeaderJson(jsonBody, "C014");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String queryUpgardeInfo() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "93268659");
		String jstringRequest = getRequestHeaderJson(jsonBody, "C025");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String queryPushMessage() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "93268659");
		String jstringRequest = getRequestHeaderJson(jsonBody, "C026");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String queryTransferBalanceLogs() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "93268659");
		String jstringRequest = getRequestHeaderJson(jsonBody, "C022");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String transferBalance() throws Exception {
		JSONObject jsonBody = new JSONObject();
		JSONObject extParameters = new JSONObject();
		extParameters.put("transferorNumber", "962138364");
		extParameters.put("transfereeNumber", "962138386");
		extParameters.put("transferAmount", "20000000");
		jsonBody.put("extParameters", extParameters);
		String jstringRequest = getRequestHeaderJson(jsonBody, "C021");
		return jstringRequest;
	}

	public static String queryXchangeLogs() throws Exception {
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("serviceNumber", "93268659");
		String jstringRequest = getRequestHeaderJson(jsonBody, "C020");
		// System.out.println(jstringRequest);
		return jstringRequest;
	}

	public static String xchangePromotion() throws Exception {
		JSONObject jsonBody = new JSONObject();
		JSONObject extParameters = new JSONObject();
		extParameters.put("xChangeType", "0");
		extParameters.put("applierNumber", "962138386");
		extParameters.put("receiverNumber", "962138386");
		extParameters.put("exchangeAmount", "20000000");
		jsonBody.put("extParameters", extParameters);
		String jstringRequest = getRequestHeaderJson(jsonBody, "C019");
		return jstringRequest;
	}
}
