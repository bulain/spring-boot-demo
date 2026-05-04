package com.bulain.mybatis.sys.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginSecurityServiceTest {

    @Autowired
    private LoginSecurityService loginSecurityService;

    @Autowired
    private RedissonClient redissonClient;

    private String testUserId = "test-user-123";
    private String testIp = "192.168.1.100";

    @BeforeEach
    void setUp() {
        // 清理测试数据
        loginSecurityService.recordLoginSuccess(testUserId, testIp);
    }

    @Test
    void testLoginFailureCounting() {
        // 初始状态应该不锁定
        assertFalse(loginSecurityService.isUserLocked(testUserId));
        assertFalse(loginSecurityService.isCaptchaRequired(testUserId, testIp));

        // 记录一次失败
        loginSecurityService.recordLoginFailure(testUserId, testIp);

        // 1次失败还不需要验证码
        assertFalse(loginSecurityService.isCaptchaRequired(testUserId, testIp));
    }

    @Test
    void testUserCaptchaThreshold() {
        // 记录5次失败，达到验证码阈值
        for (int i = 0; i < 5; i++) {
            loginSecurityService.recordLoginFailure(testUserId, testIp);
        }

        assertTrue(loginSecurityService.isCaptchaRequired(testUserId, testIp));
        assertTrue(loginSecurityService.isUserCaptchaThresholdReached(testUserId));
    }

    @Test
    void testUserLockThreshold() {
        // 记录10次失败，达到锁定阈值
        for (int i = 0; i < 10; i++) {
            loginSecurityService.recordLoginFailure(testUserId, testIp);
        }

        assertTrue(loginSecurityService.isUserLocked(testUserId));
    }

    @Test
    void testLoginSuccessResetsAll() {
        // 先记录一些失败
        for (int i = 0; i < 6; i++) {
            loginSecurityService.recordLoginFailure(testUserId, testIp);
        }

        // 确认需要验证码
        assertTrue(loginSecurityService.isCaptchaRequired(testUserId, testIp));

        // 登录成功，重置计数
        loginSecurityService.recordLoginSuccess(testUserId, testIp);

        // 验证所有状态都被重置
        assertFalse(loginSecurityService.isCaptchaRequired(testUserId, testIp));
        assertFalse(loginSecurityService.isUserLocked(testUserId));
        assertFalse(loginSecurityService.isUserCaptchaThresholdReached(testUserId));
    }

    @Test
    void testUnlockUser() {
        // 锁定用户
        for (int i = 0; i < 10; i++) {
            loginSecurityService.recordLoginFailure(testUserId, testIp);
        }
        assertTrue(loginSecurityService.isUserLocked(testUserId));

        // 解锁
        loginSecurityService.unlockUser(testUserId);

        // 验证解锁成功
        assertFalse(loginSecurityService.isUserLocked(testUserId));
        // 用户维度的验证码要求也应该被清除
        assertFalse(loginSecurityService.isUserCaptchaThresholdReached(testUserId));
    }

    @Test
    void testIpCaptchaThreshold() {
        // 使用不同的用户ID，但相同的IP
        for (int i = 0; i < 10; i++) {
            loginSecurityService.recordLoginFailure("user-" + i, testIp);
        }

        assertTrue(loginSecurityService.isIpCaptchaThresholdReached(testIp));
        assertTrue(loginSecurityService.isCaptchaRequired(null, testIp));
    }

}
