package com.unieap.base.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unieap.base.UnieapConstants;
import com.unieap.base.pojo.MdmExclog;
import com.unieap.base.repository.ExcLogRespository;

@Component
// @Aspect
public class ExceptionHandler implements ThrowsAdvice {
	@Autowired
	ExcLogRespository excLogRespository;
	/*
	 * @Pointcut(
	 * "execution(* com.test.spring.aop.pointcutexp..JoinPointObjP2.*(..))")
	 * public void pointcut1() { }
	 * 
	 * @AfterReturning(value = "pointcut1()") public void
	 * afterReturningAdvice(Object obj) {
	 * 
	 * }
	 */

	public void afterThrowing(Exception e) throws Throwable {
		exclog(e);
	}

	// 所有方法的执行作为切入点
	// @AfterReturning(pointcut = "execution(public * *(..))")
	public void exclog(Object obj) {
		Exception ex = (Exception) obj;
		MdmExclog log = new MdmExclog();
		log.setId(UnieapConstants.getSequence());
		log.setBizModule("unieap");
		log.setExType("system_exception");
		log.setExCode("");
		log.setExInfo(StringUtils.substring(ex.getLocalizedMessage(),0,1000));
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		try {
			log.setExTracking(sw.toString().getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			StringWriter sw1 = new StringWriter();
			PrintWriter pw1 = new PrintWriter(sw);
			e.printStackTrace(pw1);
			log.setExTracking(sw1.toString().getBytes());
		}
		if (UnieapConstants.getUser() != null) {
			log.setOperatorName(UnieapConstants.getUser().getUserCode());
		} else {
			log.setOperatorName("system error");
		}
		log.setOperationDate(UnieapConstants.getDateTime());
		excLogRespository.save(log);
	}
}
