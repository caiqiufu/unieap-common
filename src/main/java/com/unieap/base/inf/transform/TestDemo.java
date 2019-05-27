package com.unieap.base.inf.transform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Node;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.unieap.base.inf.vo.InfFieldVO;
import com.unieap.base.utils.XmlJSONUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestDemo {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Document document =pasexml();
		String acc = "http://www.huawei.com/bme/cbsinterface/cbs/accountmgrmsg";
		String com = "http://www.huawei.com/bme/cbsinterface/common";
		String acc1 = "http://www.huawei.com/bme/cbsinterface/cbs/accountmgr";
		String xpath = "//acc:QueryBalanceResultMsg/ResultHeader/com:ResultCode";
		String ResultCode = document.selectSingleNode(xpath).getText();
		System.out.println("ResultCode:" + ResultCode);
		xpath = "//acc:QueryBalanceResultMsg/QueryBalanceResult/acc1:BalanceRecord/acc1:BalanceDesc";
		List<Node> nodes = document.selectNodes(xpath);
		if(nodes!=null&&nodes.size()>0) {
			for(int i = 0 ; i < nodes.size() ; i++) {
				System.out.println("BalanceDesc:" +  nodes.get(i).getText());
			}
		}
	}
	public static Document pasexml() throws Exception {
		String xml = "<ns5:QueryBalanceResultMsg xmlns:ns5=\"http://www.huawei.com/bme/cbsinterface/cbs/accountmgrmsg\" xmlns:ns2=\"http://www.huawei.com/bme/cbsinterface/common\" xmlns:ns4=\"http://www.huawei.com/bme/cbsinterface/cbs/accountmgr\">\r\n" + 
				"         <ResultHeader>\r\n" + 
				"            <ns2:ResultCode>10</ns2:ResultCode>\r\n" + 
				"            <ns2:ResultDesc>10</ns2:ResultDesc>\r\n" + 
				"         </ResultHeader>\r\n" + 
				"         <QueryBalanceResult>\r\n" + 
				"            <ns4:BalanceRecord>\r\n" + 
				"               <ns4:BalanceDesc>10</ns4:BalanceDesc>\r\n" + 
				"               <ns4:Balance>10</ns4:Balance>\r\n" + 
				"               <ns4:MinMeasureId>10</ns4:MinMeasureId>\r\n" + 
				"               <ns4:ExpireTime>10</ns4:ExpireTime>\r\n" + 
				"               <ns4:AccountKey>10</ns4:AccountKey>\r\n" + 
				"               <ns4:ProductID>10</ns4:ProductID>\r\n" + 
				"            </ns4:BalanceRecord>\r\n" + 
				"            <ns4:BalanceRecord>\r\n" + 
				"               <ns4:BalanceDesc>10</ns4:BalanceDesc>\r\n" + 
				"               <ns4:Balance>10</ns4:Balance>\r\n" + 
				"               <ns4:MinMeasureId>10</ns4:MinMeasureId>\r\n" + 
				"               <ns4:ExpireTime>10</ns4:ExpireTime>\r\n" + 
				"               <ns4:AccountKey>10</ns4:AccountKey>\r\n" + 
				"               <ns4:ProductID>10</ns4:ProductID>\r\n" + 
				"            </ns4:BalanceRecord>\r\n" + 
				"         </QueryBalanceResult>\r\n" + 
				"      </ns5:QueryBalanceResultMsg>\r\n" + 
				"";
		Map<String, String> map = new HashMap<String, String>();
		map.put("acc", "http://www.huawei.com/bme/cbsinterface/cbs/accountmgrmsg");
		map.put("com", "http://www.huawei.com/bme/cbsinterface/common");
		map.put("acc1", "http://www.huawei.com/bme/cbsinterface/cbs/accountmgr");
		org.dom4j.Document document = XmlJSONUtils.getSOAPXMLDocumentDom4J(xml, map);
		return document;
	}
	public String getMessageFromSourceValue(org.dom4j.Document document, InfFieldVO infFieldVO) {
		String value = document.selectSingleNode(infFieldVO.getXpath()).getText();
		return value;
	}

	public String[] getMessageFromSourceList(org.dom4j.Document document, InfFieldVO infFieldVO) {
		List<Node> nodes = document.selectNodes(infFieldVO.getXpath());
		if(nodes!=null&&nodes.size()>0) {
			String[] values = new String[nodes.size()];
			for(int i = 0 ; i < nodes.size() ; i++) {
				values[i] = nodes.get(i).getText();
			}
			return values;
		}else {
			return null;
		}
	}
	public static void testJson() {
		JSONObject jso = new JSONObject();
		JSONObject pjso = new JSONObject();
		jso.put("pjso", pjso);
		System.out.println("json=" + jso.toString());
		pjso.put("code", "code");
		pjso.put("desc", "desc");
		System.out.println("json=" + jso.toString());
		jso.getJSONObject("pjso").put("t1", "t1");
		jso.getJSONObject("pjso").put("t2",new JSONObject());
		jso.getJSONObject("pjso").getJSONObject("t2").put("t21", "t21");
		System.out.println("json=" + jso.toString());
		ReadContext ctx = JsonPath.parse(jso);
		JSONObject t2 = ctx.read("$.pjso.t2");
		JSONArray pjson = new JSONArray();
		JSONObject t3 = new JSONObject();
		t3.put("t31", "t31");
		pjson.add(t3);
		JSONObject t4 = new JSONObject();
		t4.put("t31", "t41");
		pjson.add(t4);
		jso.put("list", pjson);
		t2.put("t22", "t22");
		System.out.println("xpathobj=" + t2.toString());
		System.out.println("json=" + jso.toString());
		JSONObject t31 = ctx.read("$.list[-1]");
		System.out.println("list last node=" + t31.toString());
		JSONArray arrt31 = ctx.read("$.list[*].t31");
		ctx.read("$.list[*]");
		System.out.println("arrt31=" + arrt31.toString());
	}
}
