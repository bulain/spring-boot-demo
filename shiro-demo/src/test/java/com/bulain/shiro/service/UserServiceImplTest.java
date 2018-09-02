package com.bulain.shiro.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bulain.shiro.ShiroApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShiroApplication.class)
public class UserServiceImplTest {

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
