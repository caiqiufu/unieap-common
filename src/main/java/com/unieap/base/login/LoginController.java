package com.unieap.base.login;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.unieap.base.ApplicationContextProvider;
import com.unieap.base.login.bo.LoginBO;
import com.unieap.base.vo.MenuVO;

@Controller
public class LoginController {

	@Autowired
	public LoginBO loginBO;
	Logger logger = LoggerFactory.getLogger(LoginController.class);

	/**
	 * login success
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param language
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/desk")
	public String desk(HttpServletRequest request, HttpServletResponse response, Model model, String language)
			throws Exception {
		changeLanguage(language);
		request.getSession().setAttribute("language", language);

		loginBO.loadLoginUser();
		Map<String, List<MenuVO>> menus = loginBO.getUserMenu(1);
		model.addAllAttributes(menus);
		model.addAttribute("userName", "Chai");

		return "desk";
	}

	@RequestMapping("/sign_in")
	public String sign_in(String errorCode, HttpServletRequest request) throws Exception {
		return "login";
	}

	@RequestMapping("/applyCheckCode")
	public String applyCheckCode(HttpServletRequest request) {
		request.getSession().setAttribute("CHECK_CODE", "1111"); // 这里只是做demo演示，没有使用插件来生成正成的图片验证码，只是简单的固定将验证码1111写入了session中
		return "checkcode";
	}

	@RequestMapping("/menuhome")
	public String menuhome() {
		return "demo/extjs";
	}

	@RequestMapping("/language")
	public void language(HttpServletRequest request, HttpServletResponse response, String language) {
		Locale locale = request.getLocale();
		logger.debug(locale.toString());
		changeLanguage(language);
		request.getSession().setAttribute("language", language);

	}

	private void changeLanguage(String language) {
		SessionLocaleResolver sessionLocaleResolver = (SessionLocaleResolver) ApplicationContextProvider
				.getBean("localeResolver");
		if (language.equals("zh_cn")) {
			sessionLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
		} else if (language.equals("en_us")) {
			sessionLocaleResolver.setDefaultLocale(Locale.US);
		} else {
			sessionLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
		}
	}

	@RequestMapping("/admin")
	@ResponseBody
	public String hello() {
		return "hello admin";
	}
}