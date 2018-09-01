package com.bulain.shiro.ctrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bulain.shiro.util.ShiroUtil;

@Controller
public class ShiroCtrl {

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	public String logon(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String host = ShiroUtil.getUserAddr(request);
		UsernamePasswordToken upt = new UsernamePasswordToken(username, password, host);

		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(upt);
		} catch (Exception e) {
			return "login";
		}

		SavedRequest savedRequest = WebUtils.getSavedRequest(request);
		String requestUrl = savedRequest.getRequestUrl();

		return "redirect:" + (StringUtils.hasLength(requestUrl) ? requestUrl : "index");
	}

	@RequestMapping("/logout")
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout();
		}
		return "redirect:index";
	}

}
