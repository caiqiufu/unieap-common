package com.unieap.base.vo;

public class QueryDicVO extends BaseVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String tableName;
	public String dicId;
	public String dicCode;
	public String dicName;
	public String groupId;
	public String seq;
	public String attr1;
	public String attr2;
	public String attr3;
	public String attr4;
	public String attr5;
	public String orderBy;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getDicId() {
		return dicId;
	}
	public void setDicId(String dicId) {
		this.dicId = dicId;
	}
	public String getDicCode() {
		return dicCode;
	}
	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getDicName() {
		return dicName;
	}
	public void setDicName(String dicName) {
		this.dicName = dicName;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	/**
	 * 
	 * @param tableName
	 * @param dicId
	 * @param dicCode
	 * @param dicName
	 * @param parentCode
	 * @param seq
	 * @param attr1
	 * @param attr2
	 * @param attr3
	 * @param attr4
	 * @param attr5
	 * @param orderBy
	 */
	public QueryDicVO(String tableName, String dicId, String dicCode, String groupId, String dicName, String seq,
			String attr1, String attr2, String attr3, String attr4, String attr5, String orderBy) {
		super();
		this.tableName = tableName;
		this.dicId = dicId;
		this.dicCode = dicCode;
		this.groupId = groupId;
		this.dicName = dicName;
		this.seq = seq;
		this.attr1 = attr1;
		this.attr2 = attr2;
		this.attr3 = attr3;
		this.attr4 = attr4;
		this.attr5 = attr5;
		this.orderBy = orderBy;
	}
	public String getAttr1() {
		return attr1;
	}
	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}
	public String getAttr2() {
		return attr2;
	}
	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}
	public String getAttr3() {
		return attr3;
	}
	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}
	public String getAttr4() {
		return attr4;
	}
	public void setAttr4(String attr4) {
		this.attr4 = attr4;
	}
	public String getAttr5() {
		return attr5;
	}
	public void setAttr5(String attr5) {
		this.attr5 = attr5;
	}
	/**
	 * @param tableName
	 * @param dicId
	 * @param dicCode
	 * @param dicName
	 * @param parentCode
	 * @param orderBy
	 */
	public QueryDicVO(String tableName, String dicId, String dicCode, String dicName, String groupId,
			String orderBy) {
		super();
		this.tableName = tableName;
		this.dicId = dicId;
		this.dicCode = dicCode;
		this.groupId = groupId;
		this.dicName = dicName;
		this.orderBy = orderBy;
	}
	
}
