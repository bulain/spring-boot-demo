package com.bulain.shiro.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class ShiroUtil {
    private ShiroUtil() {
    }

    public static String getUserAddr(HttpServletRequest request) {
        String hForwardedFor = request.getHeader("X-Forwarded-For");
        String userAddr = null;
        if (StringUtils.hasText(hForwardedFor)) {
            userAddr = hForwardedFor.split(",")[0];
        } else {
            userAddr = request.getRemoteAddr();
        }
        return userAddr;
    }

}
