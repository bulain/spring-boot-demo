package com.bulain.mybatis.sys.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.config.Profiles;
import com.bulain.mybatis.sys.common.CaptchaConstants;
import com.bulain.mybatis.sys.dao.SysUserMapper;
import com.bulain.mybatis.sys.dto.CreateUserDTO;
import com.bulain.mybatis.sys.dto.LoginDTO;
import com.bulain.mybatis.sys.dto.PhoneLoginDTO;
import com.bulain.mybatis.sys.dto.SendCodeRequest;
import com.bulain.mybatis.sys.service.SysUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
@AutoConfigureMockMvc
class SysAuthControllerTest {

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

    @BeforeEach
    void setUp() {
        // 清理测试数据
        sysUserMapper.directDelete(new LambdaQueryWrapper<>());

        // 创建测试用户
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("loginuser");
        dto.setName("Login User");
        dto.setPassword("password123");
        dto.setPhone("13800138000");
        sysUserService.createUser(dto);
    }

    @Test
    void testLogin() throws Exception {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("loginuser");
        dto.setPassword("password123");

        mockMvc.perform(post("/api/sys/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").exists());
    }

    @Test
    void testGetImageCaptcha() throws Exception {
        mockMvc.perform(get("/api/sys/auth/image-captcha"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.captchaId").exists())
                .andExpect(jsonPath("$.data.imageBase64").exists());
    }

    @Test
    void testSendSmsCode_Success() throws Exception {
        // 1. 先获取图片验证码
        String captchaResponse = mockMvc.perform(get("/api/sys/auth/image-captcha"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String captchaId = objectMapper.readTree(captchaResponse).path("data").path("captchaId").asText();

        // 2. 直接在 Redis 中设置一个已知的验证码用于测试
        String testCaptchaCode = "1234";
        String captchaKey = CaptchaConstants.CAPTCHA_IMAGE_PREFIX + captchaId;
        RBucket<String> bucket = redissonClient.getBucket(captchaKey);
        bucket.set(testCaptchaCode, 5, TimeUnit.MINUTES);

        // 3. 发送短信验证码
        SendCodeRequest request = new SendCodeRequest();
        request.setPhone("13800138000");
        request.setCaptchaId(captchaId);
        request.setCaptchaCode(testCaptchaCode);

        mockMvc.perform(post("/api/sys/auth/send-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testSendSmsCode_InvalidCaptcha() throws Exception {
        SendCodeRequest request = new SendCodeRequest();
        request.setPhone("13800138000");
        request.setCaptchaId(IdUtil.fastSimpleUUID()); // 无效的 captchaId
        request.setCaptchaCode("wrong");

        mockMvc.perform(post("/api/sys/auth/send-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    void testPhoneLogin_Success() throws Exception {
        // 1. 直接在 Redis 中设置一个已知的短信验证码
        String testSmsCode = "123456";
        String phone = "13800138000";
        String smsKey = CaptchaConstants.CAPTCHA_SMS_PREFIX + phone;
        RBucket<String> bucket = redissonClient.getBucket(smsKey);
        bucket.set(testSmsCode, 10, TimeUnit.MINUTES);

        // 2. 执行手机号登录
        PhoneLoginDTO dto = new PhoneLoginDTO();
        dto.setPhone(phone);
        dto.setCode(testSmsCode);

        mockMvc.perform(post("/api/sys/auth/phone-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").exists());
    }

    @Test
    void testPhoneLogin_WrongCode() throws Exception {
        // 1. 设置正确的验证码
        String testSmsCode = "123456";
        String phone = "13800138000";
        String smsKey = CaptchaConstants.CAPTCHA_SMS_PREFIX + phone;
        RBucket<String> bucket = redissonClient.getBucket(smsKey);
        bucket.set(testSmsCode, 10, TimeUnit.MINUTES);

        // 2. 使用错误的验证码登录
        PhoneLoginDTO dto = new PhoneLoginDTO();
        dto.setPhone(phone);
        dto.setCode("654321");

        mockMvc.perform(post("/api/sys/auth/phone-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }

    @Test
    void testPhoneLogin_ExpiredCode() throws Exception {
        PhoneLoginDTO dto = new PhoneLoginDTO();
        dto.setPhone("13800138000");
        dto.setCode("123456");

        mockMvc.perform(post("/api/sys/auth/phone-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
    }
}
