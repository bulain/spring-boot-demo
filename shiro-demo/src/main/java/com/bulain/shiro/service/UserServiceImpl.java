package com.bulain.shiro.service;

import org.springframework.stereotype.Service;

import com.bulain.shiro.pojo.User;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public User getByLoginName(String loginName) {
		User user = new User();
		
		user.setLoginName(loginName);
		user.setPasswordHash("b38e2bf274239ff5dd2b45ee9ae099c9");
		user.setSlat("1234");
		user.setFullName("Bulain Peng");;
		user.setActiveFlag("Y");
		
		return user;
	}

}
