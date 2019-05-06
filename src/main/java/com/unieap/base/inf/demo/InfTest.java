package com.unieap.base.inf.demo;

import net.sf.json.JSONObject;

public class InfTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testHttpRequest();
	}
	public static void testHttpRequest() {
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("a", "a");
		jsonParam.put("b", "b");
		//String url = "http://127.0.0.1:8080/extAction/bizHandle";
		String url = "http://127.0.0.1:8080/esb/service/extAction/queryInfo";
		//HTTPUtils.httpPost(url, jsonParam, false);
		//SoapCallUtils.callHTTPService(null, jsonParam.toString());
	}

}
