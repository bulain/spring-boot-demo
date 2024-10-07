package com.bulain.shiro.ctrl;

import com.bulain.shiro.util.ShiroUtil;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShiroCtrl {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String logon(@RequestParam("username") String username,
                        @RequestParam("password") String password, HttpServletRequest request) {
        String host = ShiroUtil.getUserAddr(request);
        UsernamePasswordToken upt = new UsernamePasswordToken(username, password, host);

        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(upt);
        } catch (Exception e) {
            return "login";
        }

        SavedRequest savedRequest = WebUtils.getSavedRequest(request);
        String requestUrl = null;
        if (savedRequest != null) {
            requestUrl = savedRequest.getRequestUrl();
        }

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
