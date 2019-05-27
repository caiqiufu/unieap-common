package com.unieap.base.inf.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.unieap.base.inf.vo.BizFieldVO;
import com.unieap.base.inf.vo.BizMessageVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TransformTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BizMessageVO bizMessageVO = testBizVO();
		List<BizFieldVO> fields = new ArrayList<BizFieldVO>();
		JSONObject jso = new JSONObject();
		ReadContext ctx = JsonPath.parse(jso);
		if (!bizMessageVO.getFieldList().isEmpty()) {
			BizFieldVO bizFieldVO = bizMessageVO.getRootFieldVO();
			fields.add(bizFieldVO);
			while (!bizFieldVO.isLeaf || fields.size() > 0) {
				if (bizFieldVO.isLeaf) {
					if (bizFieldVO.getParentVO() == null) {
						jso.put(bizFieldVO.getFieldName(), "set value:" + bizFieldVO.getFieldName());
					} else {
						if ("Object".equals(bizFieldVO.getParentVO().getFieldType())) {
							((JSONObject) ctx.read(bizFieldVO.getParentVO().getXpath())).put(bizFieldVO.getFieldName(),
									"set value:" + bizFieldVO.getFieldName());
						}
						if ("List".equals(bizFieldVO.getParentVO().getFieldType())) {
							// there are 5 records in the list
							JSONArray allfieldsobj = ctx.read(bizFieldVO.getParentVO().getXpath());
							int i = 5;
							if (allfieldsobj.size() == i) {
								for (int j = 0; j < i; j++) {
									allfieldsobj.getJSONObject(j).put(bizFieldVO.getFieldName(),
											"set list" + bizFieldVO.getFieldName() + j);
								}
							} else {
								for (int j = 0; j < i; j++) {
									JSONObject obj = new JSONObject();
									obj.put(bizFieldVO.getFieldName(), "set list" + bizFieldVO.getFieldName() + j);
									allfieldsobj.add(obj);
								}
							}
						}
					}
				} else {
					if (bizFieldVO.getParentVO() == null) {
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
						BizFieldVO pvo = bizFieldVO.getParentVO();
						if ("Object".equals(pvo.getFieldType())) {
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
						if ("List".equals(pvo.getFieldType())) {
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
					fields.addAll(bizFieldVO.getChildrenList());
				}
				fields.remove(bizFieldVO);
				if (fields.size() > 0) {
					bizFieldVO = fields.get(0);
				}
			}
		}
	}

	public static BizMessageVO testBizVO() {
		Map<String, BizFieldVO> fieldList = new HashMap<String, BizFieldVO>();
		BizMessageVO bizVO = new BizMessageVO();
		bizVO.setFieldList(fieldList);
		BizFieldVO rootFieldVO = new BizFieldVO();
		fieldList.put(rootFieldVO.getFieldName(), rootFieldVO);
		rootFieldVO.setBizCode("E001");
		rootFieldVO.setFieldName("QueryBalanceResultMsg");
		rootFieldVO.setFieldDisplayName("QueryBalanceResultMsg");
		rootFieldVO.setFieldType("Object");
		rootFieldVO.setId(1);
		rootFieldVO.setLeaf(false);
		rootFieldVO.setSeq(1);
		rootFieldVO.setXpath("$.QueryBalanceResultMsg");
		bizVO.setRootFieldVO(rootFieldVO);
		List<BizFieldVO> queryBalanceResultMsgList = new ArrayList<BizFieldVO>();
		rootFieldVO.setChildrenList(queryBalanceResultMsgList);
		BizFieldVO resultHeader = new BizFieldVO();

		fieldList.put(resultHeader.getFieldName(), resultHeader);
		resultHeader.setBizCode("E001");
		resultHeader.setFieldName("ResultHeader");
		resultHeader.setFieldDisplayName("ResultHeader");
		resultHeader.setFieldType("Object");
		resultHeader.setId(2);
		resultHeader.setLeaf(false);
		resultHeader.setSeq(1);
		resultHeader.setXpath("$.QueryBalanceResultMsg.ResultHeader");
		queryBalanceResultMsgList.add(resultHeader);
		resultHeader.setParentVO(rootFieldVO);
		List<BizFieldVO> resultHeaderList = new ArrayList<BizFieldVO>();
		resultHeader.setChildrenList(resultHeaderList);
		BizFieldVO resultCode = new BizFieldVO();

		fieldList.put(resultCode.getFieldName(), resultCode);
		resultCode.setBizCode("E001");
		resultCode.setFieldName("ResultCode");
		resultCode.setFieldDisplayName("ResultCode");
		resultCode.setFieldType("Object");
		resultCode.setId(2);
		resultCode.setLeaf(true);
		resultCode.setSeq(1);
		resultCode.setXpath("$.QueryBalanceResultMsg.ResultHeader.ResultCode");
		resultCode.setParentVO(resultHeader);
		resultHeaderList.add(resultCode);
		BizFieldVO resultDesc = new BizFieldVO();
		fieldList.put(resultDesc.getFieldName(), resultDesc);
		resultDesc.setBizCode("E001");
		resultDesc.setFieldName("ResultDesc");
		resultDesc.setFieldDisplayName("ResultDesc");
		resultDesc.setFieldType("Object");
		resultDesc.setId(2);
		resultDesc.setLeaf(true);
		resultDesc.setSeq(1);
		resultDesc.setXpath("$.QueryBalanceResultMsg.ResultHeader.ResultDesc");
		resultDesc.setParentVO(resultHeader);
		resultHeaderList.add(resultDesc);

		BizFieldVO queryBalanceResult = new BizFieldVO();
		fieldList.put(queryBalanceResult.getFieldName(), queryBalanceResult);
		queryBalanceResult.setBizCode("E001");
		queryBalanceResult.setFieldName("QueryBalanceResult");
		queryBalanceResult.setFieldDisplayName("QueryBalanceResult");
		queryBalanceResult.setFieldType("Object");
		queryBalanceResult.setId(1);
		queryBalanceResult.setLeaf(false);
		queryBalanceResult.setSeq(1);
		queryBalanceResult.setXpath("$.QueryBalanceResultMsg.QueryBalanceResult");
		queryBalanceResult.setParentVO(rootFieldVO);
		queryBalanceResultMsgList.add(queryBalanceResult);

		List<BizFieldVO> queryBalanceResultList = new ArrayList<BizFieldVO>();
		queryBalanceResult.setChildrenList(queryBalanceResultList);

		BizFieldVO balanceRecord = new BizFieldVO();
		fieldList.put(balanceRecord.getFieldName(), balanceRecord);
		balanceRecord.setBizCode("E001");
		balanceRecord.setFieldName("BalanceRecord");
		balanceRecord.setFieldDisplayName("BalanceRecord");
		balanceRecord.setFieldType("List");
		balanceRecord.setId(1);
		balanceRecord.setLeaf(false);
		balanceRecord.setSeq(1);
		balanceRecord.setXpath("$.QueryBalanceResultMsg.QueryBalanceResult.BalanceRecord");
		balanceRecord.setParentVO(queryBalanceResult);
		queryBalanceResultList.add(balanceRecord);

		List<BizFieldVO> balanceRecordList = new ArrayList<BizFieldVO>();
		balanceRecord.setChildrenList(balanceRecordList);

		BizFieldVO balanceDesc = new BizFieldVO();
		fieldList.put(balanceDesc.getFieldName(), balanceDesc);
		balanceDesc.setBizCode("E001");
		balanceDesc.setFieldName("BalanceDesc");
		balanceDesc.setFieldDisplayName("BalanceDesc");
		balanceDesc.setFieldType("String");
		balanceDesc.setId(1);
		balanceDesc.setLeaf(true);
		balanceDesc.setSeq(1);
		balanceDesc.setXpath("$.QueryBalanceResultMsg.QueryBalanceResult.BalanceRecord[*].BalanceDesc");
		balanceDesc.setParentVO(balanceRecord);
		balanceRecordList.add(balanceDesc);

		BizFieldVO balance = new BizFieldVO();
		fieldList.put(balance.getFieldName(), balance);
		balance.setBizCode("E001");
		balance.setFieldName("Balance");
		balance.setFieldDisplayName("Balance");
		balance.setFieldType("String");
		balance.setId(1);
		balance.setLeaf(true);
		balance.setSeq(1);
		balance.setXpath("$.QueryBalanceResultMsg.QueryBalanceResult.BalanceRecord[*].Balance");
		balance.setParentVO(balanceRecord);
		balanceRecordList.add(balance);

		return bizVO;
	}

}
