package com.bulain.mybatis.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.config.Profiles;
import com.bulain.mybatis.sys.dao.SysUserMapper;
import com.bulain.mybatis.sys.dto.BindDingtalkDTO;
import com.bulain.mybatis.sys.dto.DingtalkLoginDTO;
import com.bulain.mybatis.sys.dto.UnbindDingtalkDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
@AutoConfigureMockMvc
class SysAuthControllerDingtalkTest {

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
        sysUserMapper.directDelete(new LambdaQueryWrapper<>());
    }

    @Test
    void testGetDingtalkQrCode() throws Exception {
        mockMvc.perform(get("/api/sys/auth/dingtalk-qrcode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.qrCodeUrl").exists())
                .andExpect(jsonPath("$.data.state").exists())
                .andExpect(jsonPath("$.data.expireSeconds").exists());
    }

    @Test
    void testDingtalkLogin_InvalidState() throws Exception {
        DingtalkLoginDTO dto = new DingtalkLoginDTO();
        dto.setAuthCode("test_code");
        dto.setState("invalid_state_" + UUID.randomUUID());

        mockMvc.perform(post("/api/sys/auth/dingtalk-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("授权已过期，请重新扫码"));
    }

    @Test
    void testDingtalkLogin_ValidStateButInvalidCode() throws Exception {
        String qrResponse = mockMvc.perform(get("/api/sys/auth/dingtalk-qrcode"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String state = objectMapper.readTree(qrResponse).path("data").path("state").asText();

        DingtalkLoginDTO dto = new DingtalkLoginDTO();
        dto.setAuthCode("invalid_dingtalk_code");
        dto.setState(state);

        mockMvc.perform(post("/api/sys/auth/dingtalk-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    void testBindDingtalk_InvalidState() throws Exception {
        SysUser user = new SysUser();
        user.setUsername("dingtalk_integration_user");
        user.setName("Integration Test");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        sysUserService.save(user);

        String token = sysJwtService.generateToken(user.getId(), user.getUsername());

        BindDingtalkDTO dto = new BindDingtalkDTO();
        dto.setCode("test_code");
        dto.setState("invalid_state_" + UUID.randomUUID());

        mockMvc.perform(post("/api/sys/users/" + user.getId() + "/bind-dingtalk")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("授权已过期，请重新扫码"));
    }

    @Test
    void testUnbindDingtalk_WrongPassword() throws Exception {
        SysUser user = new SysUser();
        user.setUsername("dingtalk_integration_user_unbind");
        user.setName("Integration Test Unbind");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        user.setPhone("13800138999");
        user.setDingtalkUserid("test_userid_12345");
        sysUserService.save(user);

        String token = sysJwtService.generateToken(user.getId(), user.getUsername());

        UnbindDingtalkDTO dto = new UnbindDingtalkDTO();
        dto.setPassword("WrongPassword");

        mockMvc.perform(post("/api/sys/users/" + user.getId() + "/unbind-dingtalk")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("密码错误"));
    }

    @Test
    void testGetDingtalkStatus() throws Exception {
        SysUser user = new SysUser();
        user.setUsername("dingtalk_integration_user_status");
        user.setName("Integration Test Status");
        user.setPassword(passwordEncoder.encode("Test@1234"));
        sysUserService.save(user);

        String token = sysJwtService.generateToken(user.getId(), user.getUsername());

        mockMvc.perform(get("/api/sys/users/" + user.getId() + "/dingtalk-status")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.binded").value(false));

        user.setDingtalkUserid("test_userid_status");
        sysUserService.updateById(user);

        mockMvc.perform(get("/api/sys/users/" + user.getId() + "/dingtalk-status")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.binded").value(true));
    }

}
