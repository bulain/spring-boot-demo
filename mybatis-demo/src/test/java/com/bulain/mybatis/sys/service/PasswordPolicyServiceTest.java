package com.bulain.mybatis.sys.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PasswordPolicyServiceTest {

    @Autowired
    private PasswordPolicyService passwordPolicyService;

    @Test
    void testPasswordTooShort() {
        String result = passwordPolicyService.validatePassword("Ab1!", "testuser");
        assertNotNull(result);
        assertTrue(result.contains("密码长度"));
    }

    @Test
    void testPasswordContainsUsername() {
        String result = passwordPolicyService.validatePassword("Testuser123!", "testuser");
        assertNotNull(result);
        assertTrue(result.contains("包含用户名"));
    }

    @Test
    void testWeakPassword() {
        // "password123" 是常见弱密码，同时也满足长度和字符类型要求
        String result = passwordPolicyService.validatePassword("Password123", "testuser");
        assertNotNull(result);
        assertTrue(result.contains("简单"));
    }

    @Test
    void testPasswordMissingSpecialChar() {
        // 只有大小写字母和数字，缺少特殊字符
        String result = passwordPolicyService.validatePassword("Test1234", "testuser");
        // 3种类型应该通过
        assertNull(result);
    }

    @Test
    void testPasswordOnlyLowercaseAndNumber() {
        // 只有小写字母和数字，缺少大写字母和特殊字符，只有2种类型
        String result = passwordPolicyService.validatePassword("test1234", "testuser");
        assertNotNull(result);
        assertTrue(result.contains("至少"));
    }

    @Test
    void testValidPassword() {
        String result = passwordPolicyService.validatePassword("Test@1234", "testuser");
        assertNull(result);
    }

}
