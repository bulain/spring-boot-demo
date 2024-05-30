package com.bulain.oauth.service;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bulain.oauth.pojo.User;

@Service
public class UserServiceImpl implements UserService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	@Cacheable(value = "service:user:loginName", key = "#root.args[0]")
	public User getUserByLoginName(String loginName) {

		logger.info("getUserByLoginName({})", loginName);

		User user = new User();

		user.setLoginName(loginName);
		user.setPasswordHash("b38e2bf274239ff5dd2b45ee9ae099c9");
		user.setSlat("1234");
		user.setFullName("Bulain Peng");

		user.setActiveFlag("Y");

		return user;
	}

	@Override
	@Cacheable(value = "service:roles:loginName", key = "#root.args[0]")
	public List<String> findRoleByLoginName(String loginName) {

		logger.info("findRoleByLoginName({})", loginName);

		return Arrays.asList(new String[] { "ROLE_A", "ROLE_B" });
	}

	@Override
	@Cacheable(value = "service:permissions:loginName", key = "#root.args[0]")
	public List<String> findPermissionByLoginName(String loginName) {

		logger.info("findPermissionByLoginName({})", loginName);

		return Arrays.asList(new String[] { "PERM_A", "PERM_B" });
	}

}
