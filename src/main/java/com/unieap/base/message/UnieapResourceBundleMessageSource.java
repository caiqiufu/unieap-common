package com.unieap.base.message;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import com.unieap.base.UnieapConstants;
@Service
public class UnieapResourceBundleMessageSource extends ResourceBundleMessageSource {

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
		return UnieapConstants.getMessage(code);
	}

}
