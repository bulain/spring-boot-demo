package com.bulain.shiro.service;

import com.bulain.shiro.ShiroApplication;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShiroApplication.class)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void testGetUserByLoginName() {
        userService.getUserByLoginName("admin");
        userService.getUserByLoginName("admin");
    }

    @Test
    void testFindRoleByLoginName() {
        userService.findRoleByLoginName("admin");
        userService.findRoleByLoginName("admin");
    }

    @Test
    void testFindPermissionByLoginName() {
        userService.findPermissionByLoginName("admin");
        userService.findPermissionByLoginName("admin");
    }

}
