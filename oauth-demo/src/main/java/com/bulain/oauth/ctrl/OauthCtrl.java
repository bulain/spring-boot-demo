package com.bulain.oauth.ctrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OauthCtrl {

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	public String logon(HttpServletRequest request, HttpServletResponse response) {
		return "redirect:index";
	}

	@RequestMapping("/logout")
	public String logout() {
		return "redirect:index";
	}

}
