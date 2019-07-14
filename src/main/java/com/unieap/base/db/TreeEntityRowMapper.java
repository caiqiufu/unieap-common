package com.unieap.base.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.unieap.base.vo.TreeVO;

public class TreeEntityRowMapper implements RowMapper<Object> {
	public Class<?> object;
	public String[] extendAttri;

	public TreeEntityRowMapper(Class<?> object) {
		this.object = object;
	}

	public TreeEntityRowMapper(Class<?> object, String[] extendAttri) {
		this.object = object;
		this.extendAttri = extendAttri;
	}

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		return convertMapToResultMap(rs, object);
	}

	public Object convertMapToResultMap(ResultSet rs, Class<?> object) throws SQLException {
		Object vo;
		try {
			vo = object.newInstance();
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
		return getTreeVo(rs, vo);
	}

	/**
	 * SQL should include fields: id,leaf,text,parentId If SQL include extend
	 * field, please put the extend field in extendAttri
	 * 
	 * @param rs
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	private Object getTreeVo(ResultSet rs, Object bean) throws SQLException {
		List<String> columns = getColumnNames(rs);
		TreeVO vo = (TreeVO) bean;
		vo.setId(rs.getObject("id").toString());
		vo.setLeafFlag(rs.getString("leafFlag"));
		vo.setText(rs.getString("text"));
		if (columns.contains("parentId") && rs.getObject("parentId") != null) {
			vo.setParentId(rs.getObject("parentId").toString());
		}
		if (columns.contains("iconCls") && rs.getObject("iconCls") != null) {
			vo.setParentId(rs.getObject("iconCls").toString());
		}
		if (columns.contains("attr1") && rs.getObject("attr1") != null) {
			vo.setAttr1(rs.getObject("attr1").toString());
		}
		if (columns.contains("attr2") && rs.getObject("attr2") != null) {
			vo.setAttr2(rs.getObject("attr2").toString());
		}
		if (columns.contains("attr3") && rs.getObject("attr3") != null) {
			vo.setAttr3(rs.getObject("attr3").toString());
		}
		if (columns.contains("attr4") && rs.getObject("attr4") != null) {
			vo.setAttr4(rs.getObject("attr4").toString());
		}
		if (columns.contains("attr5") && rs.getObject("attr5") != null) {
			vo.setAttr5(rs.getObject("attr5").toString());
		}
		if (columns.contains("attr6") && rs.getObject("attr6") != null) {
			vo.setAttr6(rs.getObject("attr6").toString());
		}
		if (columns.contains("attr7") && rs.getObject("attr7") != null) {
			vo.setAttr7(rs.getObject("attr7").toString());
		}
		if (columns.contains("attr8") && rs.getObject("attr8") != null) {
			vo.setAttr8(rs.getObject("attr8").toString());
		}
		if (columns.contains("attr9") && rs.getObject("attr9") != null) {
			vo.setAttr9(rs.getObject("attr9").toString());
		}
		if (columns.contains("attr10") && rs.getObject("attr10") != null) {
			vo.setAttr10(rs.getObject("attr10").toString());
		}
		if (extendAttri != null) {
			Map<String, Object> extendAttriMap = new HashMap<String, Object>();
			for (String extAtt : extendAttri) {
				if ("checked".equals(extAtt)) {
					int val = rs.getInt(extAtt);
					extendAttriMap.put(extAtt, val == 1);
				} else {
					if (extAtt.indexOf(":") > 0) {
						String key = extAtt.split(":")[0];
						String type = extAtt.split(":")[1];
						if ("Boolean".equals(type)) {
							extendAttriMap.put(key, rs.getBoolean(key));
						} else if ("String".equals(type)) {
							extendAttriMap.put(key, rs.getString(key));
						} else {
							extendAttriMap.put(key, rs.getObject(key));
						}
					} else {
						extendAttriMap.put(extAtt, rs.getObject(extAtt));
					}
				}
			}
			vo.setExtendAttri(extendAttriMap);
		}
		vo.setExpanded(false);
		return vo;
	}

	private List<String> getColumnNames(ResultSet rs) throws SQLException {
		java.sql.ResultSetMetaData rsmd = rs.getMetaData();
		List<String> columns = new ArrayList<String>();
		int count = rsmd.getColumnCount();
		for (int i = 1; i <= count; i++) {
			columns.add(rsmd.getColumnLabel(i));
		}
		return columns;
	}
}
