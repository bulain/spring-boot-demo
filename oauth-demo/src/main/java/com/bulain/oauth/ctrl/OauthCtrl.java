package com.bulain.oauth.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OauthCtrl {

	@GetMapping("/login")
	public String login() {
		return "login";
	}

}
