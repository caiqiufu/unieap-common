package com.unieap.base.inf.demo;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.unieap.base.inf.vo.BizFieldVO;
import com.unieap.base.inf.vo.BizMessageVO;
import com.unieap.base.inf.vo.InfFieldVO;

import org.dom4j.Document;

import net.sf.json.JSONObject;

public class InfTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testHttpRequest();
		createXML();
	}

	public static void testHttpRequest() {
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("a", "a");
		jsonParam.put("b", "b");
		// String url = "http://127.0.0.1:8080/extAction/bizHandle";
		String url = "http://127.0.0.1:8080/esb/service/extAction/queryInfo";
		// HTTPUtils.httpPost(url, jsonParam, false);
		// SoapCallUtils.callHTTPService(null, jsonParam.toString());
	}

	public static void createXML() {
		String acc = "http://www.huawei.com/bme/cbsinterface/cbs/accountmgrmsg";
		String com = "http://www.huawei.com/bme/cbsinterface/common";
		String acc1 = "http://www.huawei.com/bme/cbsinterface/cbs/accountmgr";

		Document document = DocumentHelper.createDocument();
		Element QueryBalanceResultMsg = document.addElement("QueryBalanceResultMsg", acc);
		Element ResultHeader = QueryBalanceResultMsg.addElement("ResultHeader");
		Element CommandId = ResultHeader.addElement("CommandId", com);
		CommandId.setText("10");
		Element QueryBalanceResult = QueryBalanceResultMsg.addElement("QueryBalanceResult");
		Element BalanceRecord1 = QueryBalanceResult.addElement("BalanceRecord", acc1);
		Element BalanceDesc1 = BalanceRecord1.addElement("BalanceDesc", acc1);
		BalanceDesc1.setText("10");

		Element BalanceRecord2 = QueryBalanceResult.addElement("BalanceRecord", acc1);
		Element BalanceDesc2 = BalanceRecord2.addElement("BalanceDesc", acc1);
		BalanceDesc2.setText("10");
		System.out.println(document.asXML());
	}

	public static void test2XML() {
		BizMessageVO bizMessageVO = new BizMessageVO();
		BizFieldVO rootFieldVO = new BizFieldVO();
		bizMessageVO.setRootFieldVO(rootFieldVO);
		rootFieldVO.setFieldName("acc:QueryBalanceResultMsg");
		rootFieldVO.setXpath("acc:QueryBalanceResultMsg");
		rootFieldVO.setFieldType("Object");
		InfFieldVO rootInfFieldVO = new InfFieldVO();
		rootInfFieldVO.setFieldName("QueryBalanceResultMsg");
		rootInfFieldVO.setXpath("");
		
	}
}
