package com.bulain.mybatis.sys.service.impl;

import com.bulain.mybatis.sys.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Mock 短信服务实现 - 仅打印日志，不实际发送短信
 */
@Slf4j
@Service
public class MockSmsServiceImpl implements SmsService {

    @Override
    public void sendVerificationCode(String phone, String code) {
        log.info("【模拟短信】向手机号 {} 发送验证码: {}", phone, code);
    }
}
