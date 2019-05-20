package com.unieap.base.message;

import java.text.MessageFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;

@Component("messageSource")
public class DatabaseMessageSource extends AbstractMessageSource {
	Logger logger = LoggerFactory.getLogger(DatabaseMessageSource.class);
	@Autowired
	UnieapResourceBundleMessageSource unieapResourceBundleMessageSource;
	@Override
	protected MessageFormat resolveCode(String key, Locale locale) {
		String message = "There {0,choice,0#are|1#is|2#are} {0} {0,choice,0#apples|1#apple|2#apples} in {1} {1,choice,0#baskets|1#basket|2#baskets}.";
		// TODO Load message from database...
		MessageFormat messageFormat = new MessageFormat(message, locale);
		logger.debug("========================================================================================");
		logger.debug("messageFormat=" + messageFormat.toString());
		return unieapResourceBundleMessageSource.resolveCode(key, locale);
		//return messageFormat;
		
	}
}
