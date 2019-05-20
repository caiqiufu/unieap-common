package com.unieap.base.vo;

import org.springframework.util.StringUtils;

public class QueryDicVO extends BaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String tableName;
	public String dicId;
	public String dicCode;
	public String dicName;
	public String whereby;
	public String seq;
	public String attr1;
	public String attr2;
	public String attr3;
	public String attr4;
	public String attr5;

	/**
	 * 
	 * @param tableName
	 * @param dicId
	 * @param dicCode
	 * @param dicName
	 * @param whereby
	 * @param seq
	 */
	public QueryDicVO(String tableName, String dicId, String dicCode, String dicName, String whereby, String seq) {
		super();
		this.tableName = tableName;
		this.dicId = dicId;
		this.dicCode = dicCode;
		this.dicName = dicName;
		this.whereby = whereby;
		this.seq = seq;
	}

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

	public String getWhereby() {
		return whereby;
	}

	public void setWhereby(String whereby) {
		this.whereby = whereby;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSql() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select " + this.dicId + " as dicId," + this.dicCode + " as dicCode,'root' as parentCode,");
		if (!StringUtils.isEmpty(this.attr1)) {
			sql.append(this.attr1 + " as attr1,");
		}
		if (!StringUtils.isEmpty(this.attr2)) {
			sql.append(this.attr2 + " as attr2,");
		}
		if (!StringUtils.isEmpty(this.attr3)) {
			sql.append(this.attr3 + " as attr3,");
		}
		if (!StringUtils.isEmpty(this.attr4)) {
			sql.append(this.attr4 + " as attr4,");
		}
		if (!StringUtils.isEmpty(this.attr5)) {
			sql.append(this.attr5 + " as attr5,");
		}
		sql.append(this.dicName + " as dicName from " + this.tableName);
		if (!StringUtils.isEmpty(getWhereby())) {
			sql.append(" where " + getWhereby());
		}
		sql.append(" order by " + getSeq());
		return sql.toString();
	}

}
