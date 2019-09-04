package com.unieap.base.inf.vo;

import org.apache.commons.lang.StringUtils;

import com.unieap.base.vo.BaseVO;

public class InfSQLConfigVO extends BaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private java.lang.Integer id;
	private String sqlBizCode;
	private String dsName;
	private String dsType;
	private String tableName;
	private String[] fields;
	private String[] aliasFields;
	private String queryFields;
	private String filterSql;
	private String orderBy;

	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public String getSqlBizCode() {
		return sqlBizCode;
	}

	public void setSqlBizCode(String sqlBizCode) {
		this.sqlBizCode = sqlBizCode;
	}

	public String getDsName() {
		return dsName;
	}

	public void setDsName(String dsName) {
		this.dsName = dsName;
	}

	public String getDsType() {
		return dsType;
	}

	public void setDsType(String dsType) {
		this.dsType = dsType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public String[] getAliasFields() {
		return aliasFields;
	}

	public void setAliasFields(String[] aliasFields) {
		this.aliasFields = aliasFields;
	}

	public String getFilterSql() {
		return filterSql;
	}

	public void setFilterSql(String filterSql) {
		this.filterSql = filterSql;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getQueryFields() {
		return queryFields;
	}

	public void setQueryFields(String queryFields) {
		this.queryFields = queryFields;
	}

	public String getQuerySql() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ").append(queryFields).append(" FROM ").append(tableName);
		if (!StringUtils.isEmpty(filterSql)) {
			sql.append(" WHERE ").append(filterSql);
		}
		if (!StringUtils.isEmpty(orderBy)) {
			sql.append(" ORDER BY ").append(orderBy);
		}
		return sql.toString();
	}

	public String getCountSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) ").append(" FROM ").append(tableName);
		if (!StringUtils.isEmpty(filterSql)) {
			sql.append(" WHERE ").append(filterSql);
		}
		return sql.toString();
	}
}
