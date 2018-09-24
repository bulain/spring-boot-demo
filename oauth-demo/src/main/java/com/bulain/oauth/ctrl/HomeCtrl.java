package com.bulain.oauth.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeCtrl {

	@RequestMapping("/home")
	public String home() {
		return "home";
	}
	
	@RequestMapping("/csrf")
	public String csrf() {
		return "csrf";
	}
	
}
