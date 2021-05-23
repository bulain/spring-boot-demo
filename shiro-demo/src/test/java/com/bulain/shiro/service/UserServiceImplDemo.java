package com.bulain.shiro.service;

import com.bulain.shiro.ShiroApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShiroApplication.class)
public class UserServiceImplDemo {

	@Autowired
	private UserService userService;

	@Test
	public void testGetUserByLoginName() {
		userService.getUserByLoginName("admin");
		userService.getUserByLoginName("admin");
	}

	@Test
	public void testFindRoleByLoginName() {
		userService.findRoleByLoginName("admin");
		userService.findRoleByLoginName("admin");
	}

	@Test
	public void testFindPermissionByLoginName() {
		userService.findPermissionByLoginName("admin");
		userService.findPermissionByLoginName("admin");
	}

}
