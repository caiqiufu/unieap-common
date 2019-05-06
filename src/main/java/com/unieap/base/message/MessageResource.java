package com.unieap.base.message;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class MessageResource extends AbstractMessageSource implements ResourceLoaderAware {

	private ResourceLoader resourceLoader;
	@Autowired
	UnieapResourceBundleMessageSource unieapResourceBundleMessageSource;
	/*
	 * private ResourceLoader resourceLoader;
	 *//**
		 * Map切分字符
		 */
	/*
	 * protected final String MAP_SPLIT_CODE = "|"; private final Map<String,
	 * String> properties = new HashMap<String, String>();
	 * 
	 * public MessageResource() { reload(); }
	 * 
	 * public void reload() { properties.clear(); properties.putAll(loadTexts()); }
	 * 
	 *//**
		 * 
		 * 描述：TODO 查询数据 虚拟数据，可以从数据库读取信息，此处省略
		 * 
		 * @return
		 */
	/*
	 * private List<MessageVO> getResource() { List<MessageVO> resources = new
	 * ArrayList<MessageVO>(); MessageVO re = new MessageVO(); MessageVO re1 = new
	 * MessageVO(); re.setId(1); re.setName("common.name"); re.setValue("name");
	 * re.setLanguage("en"); resources.add(0, re); re1.setId(2);
	 * re1.setName("common.name"); re1.setValue("\u59D3\u540D");
	 * re1.setLanguage("cn"); resources.add(1, re1); return resources; }
	 * 
	 *//**
		 * 
		 * 描述：TODO 加载数据
		 * 
		 * @return
		 */

	/*
	 * protected Map<String, String> loadTexts() { Map<String, String> mapResource =
	 * new HashMap<String, String>(); List<MessageVO> resources =
	 * this.getResource(); for (MessageVO item : resources) { String code =
	 * item.getName() + MAP_SPLIT_CODE + item.getLanguage(); mapResource.put(code,
	 * item.getValue()); } return mapResource; }
	 * 
	 *//**
		 * 
		 * 描述：TODO
		 * 
		 * @param code
		 * @param locale 本地化语言
		 * @return
		 *//*
			 * private String getText(String code, Locale locale) { String localeCode =
			 * locale.getLanguage(); String key = code + MAP_SPLIT_CODE + localeCode; String
			 * localeText = properties.get(key); String resourceText = code; if (localeText
			 * != null) { resourceText = localeText; } else { localeCode =
			 * Locale.ENGLISH.getLanguage(); key = code + MAP_SPLIT_CODE + localeCode;
			 * localeText = properties.get(key); if (localeText != null) { resourceText =
			 * localeText; } else { resourceText = "No message configured"; } } return
			 * resourceText; }
			 * 
			 * @Override public void setResourceLoader(ResourceLoader resourceLoader) {
			 * this.resourceLoader = (resourceLoader != null ? resourceLoader : new
			 * DefaultResourceLoader()); }
			 */

	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		// String msg = getText(code, locale);
		// MessageFormat result = createMessageFormat(msg, locale);
		// return result;
		return unieapResourceBundleMessageSource.resolveCode(code, locale);
	}

	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		// String result = getText(code, locale);
		// return result;
		return unieapResourceBundleMessageSource.resolveCodeWithoutArguments(code, locale);
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
	}

}
