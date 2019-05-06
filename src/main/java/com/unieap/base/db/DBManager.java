package com.unieap.base.db;

import org.springframework.jdbc.core.JdbcTemplate;

import com.unieap.base.ApplicationContextProvider;

public final class DBManager {

	public static JdbcTemplate getJT() {
		return (JdbcTemplate) ApplicationContextProvider.getBean("jdbcTemplate");
	}
}
