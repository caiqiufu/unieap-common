package com.unieap.base.db;

import org.springframework.jdbc.core.JdbcTemplate;

import com.unieap.base.ApplicationContextProvider;
import com.unieap.base.UnieapCacheMgt;

public final class DBManager {
	public static JdbcTemplate getJT() {
		return (JdbcTemplate) ApplicationContextProvider.getBean("jdbcTemplate");
	}

	public static JdbcTemplate getBizJT(String dsName) {
		return UnieapCacheMgt.getJtList().get(dsName);
	}
}
