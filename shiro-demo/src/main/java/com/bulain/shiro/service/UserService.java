package com.bulain.shiro.service;

import com.bulain.shiro.pojo.User;

public interface UserService {

	User getByLoginName(String loginName);
	
}
