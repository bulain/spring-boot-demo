package com.bulain.mybatis.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.config.Profiles;
import com.bulain.mybatis.sys.common.DingtalkLoginConstants;
import com.bulain.mybatis.sys.dao.SysUserMapper;
import com.bulain.mybatis.sys.dto.BindDingtalkDTO;
import com.bulain.mybatis.sys.dto.DingtalkLoginDTO;
import com.bulain.mybatis.sys.dto.DingtalkQrCodeResponse;
import com.bulain.mybatis.sys.dto.UnbindDingtalkDTO;
import com.bulain.mybatis.sys.entity.SysUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
class DingtalkLoginServiceTest {

    @Autowired
    private DingtalkLoginService dingtalkLoginService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedissonClient redissonClient;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        sysUserMapper.directDelete(new LambdaQueryWrapper<>());
    }

    @Test
    void testGetQrCode() {
        DingtalkQrCodeResponse qrCode = dingtalkLoginService.getQrCode();

        assertNotNull(qrCode);
        assertNotNull(qrCode.getQrCodeUrl());
        assertNotNull(qrCode.getState());
        assertTrue(qrCode.getExpireSeconds() > 0);

        String stateKey = DingtalkLoginConstants.DINGTALK_LOGIN_STATE_PREFIX + qrCode.getState();
        RBucket<String> bucket = redissonClient.getBucket(stateKey);
        assertTrue(bucket.isExists());
    }

    @Test
    void testDingtalkLogin_InvalidState() {
        DingtalkLoginDTO dto = new DingtalkLoginDTO();
        dto.setAuthCode("test_code");
        dto.setState("invalid_state_" + UUID.randomUUID());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            dingtalkLoginService.dingtalkLogin(dto);
        });

        assertEquals("授权已过期，请重新扫码", exception.getMessage());
    }

    @Test
    void testDingtalkLogin_InvalidCode() {
        DingtalkQrCodeResponse qrCode = dingtalkLoginService.getQrCode();

        DingtalkLoginDTO dto = new DingtalkLoginDTO();
        dto.setAuthCode("invalid_test_code");
        dto.setState(qrCode.getState());

        assertThrows(Exception.class, () -> {
            dingtalkLoginService.dingtalkLogin(dto);
        });
    }

    @Test
    void testGetDingtalkStatus() {
        SysUser user = new SysUser();
        user.setUsername("dingtalk_test_user");
        user.setName("Test User");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        sysUserService.save(user);

        Map<String, Object> status = dingtalkLoginService.getDingtalkStatus(user.getId());
        assertNotNull(status);
        assertEquals(false, status.get("binded"));

        user.setDingtalkUserid("test_userid_123");
        sysUserService.updateById(user);

        status = dingtalkLoginService.getDingtalkStatus(user.getId());
        assertEquals(true, status.get("binded"));
    }

    @Test
    void testUnbindDingtalk_WrongPassword() {
        SysUser user = new SysUser();
        user.setUsername("dingtalk_test_user_unbind");
        user.setName("Test Unbind");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        user.setPhone("13900139000");
        user.setDingtalkUserid("test_userid_unbind");
        sysUserService.save(user);

        UnbindDingtalkDTO dto = new UnbindDingtalkDTO();
        dto.setPassword("WrongPassword@123");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            dingtalkLoginService.unbindDingtalk(user.getId(), dto);
        });

        assertEquals("密码错误", exception.getMessage());
    }

    @Test
    void testBindDingtalk_InvalidState() {
        SysUser user = new SysUser();
        user.setUsername("dingtalk_test_user_bind");
        user.setName("Test Bind");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        sysUserService.save(user);

        BindDingtalkDTO dto = new BindDingtalkDTO();
        dto.setCode("test_code");
        dto.setState("invalid_state_" + UUID.randomUUID());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            dingtalkLoginService.bindDingtalk(user.getId(), dto);
        });

        assertEquals("授权已过期，请重新扫码", exception.getMessage());
    }

}
