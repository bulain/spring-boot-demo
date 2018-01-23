package com.bulain.main;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeleafController {

	@Value("${application.message:Hello World}")
	private String message = "Hello World";

	@GetMapping("/")
	public String index(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", this.message);
		return "index";
	}

}
