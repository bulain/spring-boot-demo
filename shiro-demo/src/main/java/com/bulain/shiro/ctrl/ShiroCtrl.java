package com.bulain.shiro.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bulain.shiro.pojo.BaseResp;
import com.bulain.shiro.pojo.DataResp;
import com.bulain.shiro.util.ShiroUtil;

@Controller
public class ShiroCtrl {

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	@ResponseBody
	public DataResp logon(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String host = ShiroUtil.getUserAddr(request);
		UsernamePasswordToken upt = new UsernamePasswordToken(username, password, host);

		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(upt);
		} catch (Exception e) {
			return DataResp.fail(e);
		}

		Object principal = subject.getPrincipal();

		return DataResp.ok(principal);
	}

	@RequestMapping("/logout")
	
	@ResponseBody
	public BaseResp logout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout();
		}
		return BaseResp.ok();
	}

}
