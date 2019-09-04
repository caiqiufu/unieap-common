package com.unieap.base.login;

import java.util.List;
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
import org.springframework.web.servlet.ModelAndView;

import com.unieap.base.login.bo.LoginBO;
import com.unieap.base.vo.MenuVO;

@Controller
public class LoginController {
	Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	public LoginBO loginBO;

	@RequestMapping("/unieapLogin")
	public String unieapLogin(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("myname_zh", "菜菜");
		model.addAttribute("myname_en", "Chai Chai");
		return "unieapLogin";
	}
	@RequestMapping("/unieapIndex")
	public String unieapIndex(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "unieapIndex";
	}
	@RequestMapping("/unieapLogout")
	public String unieapLogout() {
		return "unieapLogout";
	}

	@RequestMapping("/desk")
	public String desk(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		loginBO.initUserButton();
		loginBO.initUserDicdata();
		Map<String, List<MenuVO>> menus = loginBO.getUserMenu();
		model.addAllAttributes(menus);
		model.addAttribute("myname", "Chai");
		return "desk";
	}

	@RequestMapping("/desk2")
	public String desk2(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Map<String, List<MenuVO>> menus = loginBO.getUserMenu();
		model.addAllAttributes(menus);
		return "desk2";
	}

	@RequestMapping("/menu")
	public String menu(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Map<String, List<MenuVO>> menus = loginBO.getUserMenu();
		model.addAllAttributes(menus);
		model.addAttribute("myname", "Chai");
		return "menu";
	}

	@RequestMapping("/admin")
	@ResponseBody
	public String hello() {
		return "hello admin";
	}

	@RequestMapping("/welcome")
	public ModelAndView welcome(HttpServletRequest request, HttpServletResponse response, Model model, String language) {
		model.addAttribute("myname", "Chai");
		model.addAttribute("myname1", "菜菜");
		ModelAndView vmodel = new ModelAndView("welcome");
		vmodel.addObject("myname1.test", "Chai Chai");
		return vmodel;
	}
}