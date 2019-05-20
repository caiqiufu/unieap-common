package com.unieap.base.message;

import java.text.MessageFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import com.unieap.base.UnieapConstants;
@Service
public class UnieapResourceBundleMessageSource extends ResourceBundleMessageSource {
	Logger logger = LoggerFactory.getLogger(UnieapResourceBundleMessageSource.class);

	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		String msg = getText(code, locale);
		return msg;
	}

	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		String msg = getText(code, locale);
		MessageFormat result = createMessageFormat(msg, locale);
		return result;
	}

	private String getText(String code, Locale locale) {
		logger.debug("=================================================================================");
		logger.debug("code="+code+",locale="+locale.toString());
		String text = UnieapConstants.getMessage(code,locale.toString());
		logger.debug("text="+text);
		return text;
	}

}
