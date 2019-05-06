package com.unieap.base.db;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

/**
 * <p>
 * Description: [POJO反射组装]
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: 深圳市天源迪科技术股份有限公司
 * </p>
 * 
 * @author <a href="mailto: caiqiufu@sohu.com">蔡秋伏</a>
 * @version $Revision$
 */
public class EntityRowMapper implements RowMapper<Object> {
	public Class<?> object;

	public EntityRowMapper(Class<?> object) {
		this.object = object;
	}

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		return convertMapToResultMap(rs, object);
	}

	/**
	 * <p>
	 * 描述:把ResultSet结果转换成SqlMap中配置的resultMap对象
	 * </P>
	 * Dec 2, 2010
	 * 
	 * @param rs
	 * @param resultMapId
	 * @return
	 * @throws SQLException
	 */
	public Object convertMapToResultMap(ResultSet rs, Class<?> object) throws SQLException {
		Object vo;
		try {
			vo = object.newInstance();
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
		return getBean(rs, vo);

	}

	private Object getBean(ResultSet rs, Object bean) throws SQLException {
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < props.length; i++) {
				Method getter = props[i].getReadMethod();
				Method setter = props[i].getWriteMethod();
				String property = props[i].getName();
				if ("class".equals(property)) {
					continue;
				}
				if (getter == null) {
					continue;
				}
				if (setter == null) {
					continue;
				}
				Class<?> retType = getter.getReturnType();
				try {
					rs.getObject(property);
					// Blob blob = rs.getBlob(property);
					// int bolblen = (int) blob.length();
					// byte[] data = blob.getBytes(1, bolblen);
					// String content = new String(data);
				} catch (SQLException e) {
					continue;
				}
				Object value = rs.getObject(property);
				String str = "";
				try {
					if (value != null) {
						// value.getClass().getCanonicalName();
						if (("byte[]".equals(value.getClass().getName())) && (String.class == retType)) {
							Blob blob = rs.getBlob(property);
							int bolblen = (int) blob.length();
							byte[] data = blob.getBytes(1, bolblen);
							String content = new String(data, "utf-8");
							setter.invoke(bean, content);
						} else if (String.class == retType && value.toString() == null) {
							str = value.toString();
							setter.invoke(bean, str);
						} else {
							if ((java.util.Date.class == retType || Timestamp.class == retType
									|| java.sql.Date.class == retType)) {
								Date date = rs.getTimestamp(property);
								setter.invoke(bean, date);
							} else if (java.lang.Long.class == retType) {
								setter.invoke(bean, Long.valueOf(value.toString()));
							} else if (boolean.class == retType) {
								if (value.getClass() == Long.class) {
									setter.invoke(bean, ((Long) value).intValue() == 1);
								} else if (value.getClass() == Integer.class) {
									setter.invoke(bean, ((Integer) value).intValue() == 1);
								}
							} else if (java.lang.Integer.class == retType) {
								setter.invoke(bean, Integer.valueOf(value.toString()));
							} else if (java.lang.Double.class == retType) {
								setter.invoke(bean, Double.valueOf(value.toString()));
							} else {
								setter.invoke(bean, value);
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("Error to get value,error:" + e.getMessage());
				}

			}
		} catch (IntrospectionException e1) {
			throw new SQLException(e1.getMessage());
		}

		return bean;
	}
}
