package com.bulain.shiro.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class ShiroUtil {

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
