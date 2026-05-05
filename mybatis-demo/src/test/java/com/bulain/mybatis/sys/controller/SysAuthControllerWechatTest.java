package com.bulain.mybatis.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.config.Profiles;
import com.bulain.mybatis.sys.common.WechatLoginConstants;
import com.bulain.mybatis.sys.dao.SysUserMapper;
import com.bulain.mybatis.sys.dto.BindWechatDTO;
import com.bulain.mybatis.sys.dto.UnbindWechatDTO;
import com.bulain.mybatis.sys.dto.WechatLoginDTO;
import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.service.SysJwtService;
import com.bulain.mybatis.sys.service.SysUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
@AutoConfigureMockMvc
class SysAuthControllerWechatTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private SysJwtService sysJwtService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        // 物理删除所有测试用户（避免唯一键冲突）
        sysUserMapper.directDelete(new LambdaQueryWrapper<>());
    }

    @Test
    void testGetWechatQrCode() throws Exception {
        mockMvc.perform(get("/api/sys/auth/wechat-qrcode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.qrCodeUrl").exists())
                .andExpect(jsonPath("$.data.state").exists())
                .andExpect(jsonPath("$.data.expireSeconds").exists());
    }

    @Test
    void testWechatLogin_InvalidState() throws Exception {
        WechatLoginDTO dto = new WechatLoginDTO();
        dto.setCode("test_code");
        dto.setState("invalid_state_" + UUID.randomUUID());

        mockMvc.perform(post("/api/sys/auth/wechat-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("授权已过期，请重新扫码"));
    }

    @Test
    void testWechatLogin_ValidStateButInvalidCode() throws Exception {
        // 1. 获取二维码（生成有效 state）
        String qrResponse = mockMvc.perform(get("/api/sys/auth/wechat-qrcode"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String state = objectMapper.readTree(qrResponse).path("data").path("state").asText();

        // 2. 使用无效的 code 进行登录
        WechatLoginDTO dto = new WechatLoginDTO();
        dto.setCode("invalid_wx_code");
        dto.setState(state);

        // 应该返回微信接口错误（因为 code 无效）
        mockMvc.perform(post("/api/sys/auth/wechat-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    void testBindWechat_InvalidState() throws Exception {
        // 创建测试用户
        SysUser user = new SysUser();
        user.setUsername("wechat_integration_user");
        user.setName("Integration Test");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        sysUserService.save(user);

        String token = sysJwtService.generateToken(user.getId(), user.getUsername());

        BindWechatDTO dto = new BindWechatDTO();
        dto.setCode("test_code");
        dto.setState("invalid_state_" + UUID.randomUUID());

        mockMvc.perform(post("/api/sys/users/" + user.getId() + "/bind-wechat")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("授权已过期，请重新扫码"));
    }

    @Test
    void testUnbindWechat_WrongPassword() throws Exception {
        // 创建测试用户（已绑定微信）
        SysUser user = new SysUser();
        user.setUsername("wechat_integration_user_unbind");
        user.setName("Integration Test Unbind");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        user.setPhone("13800138999"); // 有手机号，解绑后仍可登录
        user.setWechatOpenid("test_openid_12345");
        sysUserService.save(user);

        String token = sysJwtService.generateToken(user.getId(), user.getUsername());

        UnbindWechatDTO dto = new UnbindWechatDTO();
        dto.setPassword("WrongPassword");

        mockMvc.perform(post("/api/sys/users/" + user.getId() + "/unbind-wechat")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("密码错误"));
    }

    @Test
    void testGetWechatStatus() throws Exception {
        // 创建测试用户
        SysUser user = new SysUser();
        user.setUsername("wechat_integration_user_status");
        user.setName("Integration Test Status");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        sysUserService.save(user);

        String token = sysJwtService.generateToken(user.getId(), user.getUsername());

        // 初始状态：未绑定
        mockMvc.perform(get("/api/sys/users/" + user.getId() + "/wechat-status")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.binded").value(false));

        // 更新用户为已绑定微信
        user.setWechatOpenid("test_openid_status");
        sysUserService.updateById(user);

        // 验证已绑定状态
        mockMvc.perform(get("/api/sys/users/" + user.getId() + "/wechat-status")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.binded").value(true));
    }

}
