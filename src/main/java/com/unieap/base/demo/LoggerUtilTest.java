package com.unieap.base.demo;

import org.apache.log4j.Logger;

public class LoggerUtilTest {
	private static final Logger file = Logger.getLogger("file");
	private static final Logger register = Logger.getLogger("unieapcommon");
	private static final Logger login = Logger.getLogger("login");
	private static final Logger goldcoin = Logger.getLogger("goldcoin");
	private static final Logger recharge = Logger.getLogger("recharge");
	private static final Logger jjj = Logger.getLogger(LoggerUtilTest.class.getName());
	private static final Logger FILE = Logger.getLogger("appender1");
	private static org.apache.log4j.Logger log = Logger.getLogger(LoggerUtilTest.class);

	public static void logInfo(String log) {
		file.info(log);
	}

	public static void registerInfo() {
		register.info("[register] ddd ");
	}

	public static void loginInfo() {
		login.info("[login] 222");
	}

	public static void main(String[] args) {
		/*
		 * logInfo("11"); registerInfo(); loginInfo();
		 */
		/*
		 * login.info("[login] 大大大大大大大大"); register.debug("2222");
		 * register.info("[register] 人人人人人人人人人人");
		 */
//jjj.info("test");
//log.info(222);
		FILE.info("334343");
		register.info("2222");
	}
}
