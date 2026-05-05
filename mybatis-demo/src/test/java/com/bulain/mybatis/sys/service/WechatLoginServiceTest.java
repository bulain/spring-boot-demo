package com.bulain.mybatis.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.config.Profiles;
import com.bulain.mybatis.sys.common.WechatLoginConstants;
import com.bulain.mybatis.sys.config.WechatOpenPlatformConfig;
import com.bulain.mybatis.sys.dao.SysUserMapper;
import com.bulain.mybatis.sys.dto.BindWechatDTO;
import com.bulain.mybatis.sys.dto.UnbindWechatDTO;
import com.bulain.mybatis.sys.dto.WechatLoginDTO;
import com.bulain.mybatis.sys.dto.WechatQrCodeResponse;
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
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
class WechatLoginServiceTest {

    @Autowired
    private WechatLoginService wechatLoginService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private WechatOpenPlatformConfig wechatConfig;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        // 物理删除所有测试用户（避免唯一键冲突）
        sysUserMapper.directDelete(new LambdaQueryWrapper<>());
    }

    @Test
    void testGetQrCode() {
        WechatQrCodeResponse qrCode = wechatLoginService.getQrCode();

        assertNotNull(qrCode);
        assertNotNull(qrCode.getQrCodeUrl());
        assertNotNull(qrCode.getState());
        assertTrue(qrCode.getExpireSeconds() > 0);

        // 验证 state 已存入 Redis
        String stateKey = WechatLoginConstants.WECHAT_LOGIN_STATE_PREFIX + qrCode.getState();
        RBucket<String> bucket = redissonClient.getBucket(stateKey);
        assertTrue(bucket.isExists());
    }

    @Test
    void testWechatLogin_InvalidState() {
        WechatLoginDTO dto = new WechatLoginDTO();
        dto.setCode("test_code");
        dto.setState("invalid_state_" + UUID.randomUUID());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            wechatLoginService.wechatLogin(dto);
        });

        assertEquals("授权已过期，请重新扫码", exception.getMessage());
    }

    @Test
    void testWechatLogin_InvalidCode() {
        // 生成有效的 state
        WechatQrCodeResponse qrCode = wechatLoginService.getQrCode();

        WechatLoginDTO dto = new WechatLoginDTO();
        dto.setCode("invalid_test_code");
        dto.setState(qrCode.getState());

        // 微信接口会返回错误，但测试环境下至少应该抛出异常
        assertThrows(Exception.class, () -> {
            wechatLoginService.wechatLogin(dto);
        });
    }

    @Test
    void testGetWechatStatus() {
        // 创建一个测试用户
        SysUser user = new SysUser();
        user.setUsername("wechat_test_user");
        user.setName("Test User");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        sysUserService.save(user);

        // 初始状态：未绑定
        Map<String, Object> status = wechatLoginService.getWechatStatus(user.getId());
        assertNotNull(status);
        assertEquals(false, status.get("binded"));

        // 更新用户为已绑定微信
        user.setWechatOpenid("test_openid_123");
        sysUserService.updateById(user);

        // 验证已绑定状态
        status = wechatLoginService.getWechatStatus(user.getId());
        assertEquals(true, status.get("binded"));
    }

    @Test
    void testUnbindWechat_WrongPassword() {
        // 创建一个测试用户（已绑定微信）
        SysUser user = new SysUser();
        user.setUsername("wechat_test_user_unbind");
        user.setName("Test Unbind");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        user.setPhone("13900139000"); // 有手机号，解绑后仍可登录
        user.setWechatOpenid("test_openid_unbind");
        sysUserService.save(user);

        UnbindWechatDTO dto = new UnbindWechatDTO();
        dto.setPassword("WrongPassword@123");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            wechatLoginService.unbindWechat(user.getId(), dto);
        });

        assertEquals("密码错误", exception.getMessage());
    }

    @Test
    void testBindWechat_InvalidState() {
        // 创建一个测试用户
        SysUser user = new SysUser();
        user.setUsername("wechat_test_user_bind");
        user.setName("Test Bind");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        sysUserService.save(user);

        BindWechatDTO dto = new BindWechatDTO();
        dto.setCode("test_code");
        dto.setState("invalid_state_" + UUID.randomUUID());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            wechatLoginService.bindWechat(user.getId(), dto);
        });

        assertEquals("授权已过期，请重新扫码", exception.getMessage());
    }

}
