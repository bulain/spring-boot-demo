package com.bulain.mybatis.sys.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.bulain.mybatis.sys.common.CaptchaConstants;
import com.bulain.mybatis.sys.dto.ImageCaptchaResponse;
import com.bulain.mybatis.sys.service.SmsService;
import com.bulain.mybatis.sys.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 验证码服务实现 - 使用 Redis 存储
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final RedissonClient redissonClient;
    private final SmsService smsService;

    @Override
    public ImageCaptchaResponse generateImageCaptcha(String ip) {
        // 检查 IP 次数限制
        String ipCountKey = CaptchaConstants.CAPTCHA_COUNT_IMAGE_IP_PREFIX + ip;
        RAtomicLong ipCount = redissonClient.getAtomicLong(ipCountKey);
        if (ipCount.isExists()) {
            if (ipCount.get() >= CaptchaConstants.IMAGE_CAPTCHA_IP_LIMIT_MAX) {
                throw new RuntimeException("请求过于频繁，请稍后再试");
            }
        }

        // 生成验证码 ID 和验证码
        String captchaId = IdUtil.fastSimpleUUID();
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 100, CaptchaConstants.IMAGE_CAPTCHA_LENGTH, 5);
        String code = captcha.getCode();

        // 存储到 Redis
        String captchaKey = CaptchaConstants.CAPTCHA_IMAGE_PREFIX + captchaId;
        RBucket<String> bucket = redissonClient.getBucket(captchaKey);
        bucket.set(code, CaptchaConstants.IMAGE_CAPTCHA_EXPIRE_SECONDS, TimeUnit.SECONDS);

        // 更新计数
        if (!ipCount.isExists()) {
            ipCount.set(1);
            ipCount.expire(CaptchaConstants.IMAGE_CAPTCHA_IP_LIMIT_WINDOW, TimeUnit.SECONDS);
        } else {
            ipCount.incrementAndGet();
        }

        log.info("生成图片验证码, captchaId: {}, code: {}, ip: {}", captchaId, code, ip);

        return new ImageCaptchaResponse(captchaId, captcha.getImageBase64Data());
    }

    @Override
    public boolean validateImageCaptcha(String captchaId, String captchaCode) {
        String captchaKey = CaptchaConstants.CAPTCHA_IMAGE_PREFIX + captchaId;
        RBucket<String> bucket = redissonClient.getBucket(captchaKey);

        if (!bucket.isExists()) {
            return false;
        }

        String storedCode = bucket.get();
        // 验证后立即删除，防止重复使用
        bucket.delete();

        return storedCode != null && storedCode.equalsIgnoreCase(captchaCode);
    }

    @Override
    public void sendSmsCode(String phone, String ip) {
        // 检查小时次数限制
        String hourCountKey = CaptchaConstants.CAPTCHA_COUNT_SMS_HOUR_PREFIX + phone;
        RAtomicLong hourCount = redissonClient.getAtomicLong(hourCountKey);
        if (hourCount.isExists() && hourCount.get() >= CaptchaConstants.SMS_CAPTCHA_HOUR_LIMIT_MAX) {
            throw new RuntimeException("1 小时内发送次数过多，请稍后再试");
        }

        // 检查 24 小时次数限制
        String dayCountKey = CaptchaConstants.CAPTCHA_COUNT_SMS_DAY_PREFIX + phone;
        RAtomicLong dayCount = redissonClient.getAtomicLong(dayCountKey);
        if (dayCount.isExists() && dayCount.get() >= CaptchaConstants.SMS_CAPTCHA_DAY_LIMIT_MAX) {
            throw new RuntimeException("24 小时内发送次数过多，请明天再试");
        }

        // 生成短信验证码
        String code = RandomUtil.randomNumbers(CaptchaConstants.SMS_CAPTCHA_LENGTH);

        // 存储到 Redis
        String smsKey = CaptchaConstants.CAPTCHA_SMS_PREFIX + phone;
        RBucket<String> bucket = redissonClient.getBucket(smsKey);
        bucket.set(code, CaptchaConstants.SMS_CAPTCHA_EXPIRE_SECONDS, TimeUnit.SECONDS);

        // 更新计数
        if (!hourCount.isExists()) {
            hourCount.set(1);
            hourCount.expire(1, TimeUnit.HOURS);
        } else {
            hourCount.incrementAndGet();
        }

        if (!dayCount.isExists()) {
            dayCount.set(1);
            dayCount.expire(24, TimeUnit.HOURS);
        } else {
            dayCount.incrementAndGet();
        }

        // 发送短信
        smsService.sendVerificationCode(phone, code);

        log.info("发送短信验证码, phone: {}, code: {}", phone, code);
    }

    @Override
    public boolean validateSmsCode(String phone, String code) {
        String smsKey = CaptchaConstants.CAPTCHA_SMS_PREFIX + phone;
        RBucket<String> bucket = redissonClient.getBucket(smsKey);

        if (!bucket.isExists()) {
            return false;
        }

        String storedCode = bucket.get();
        // 验证后立即删除，防止重复使用
        bucket.delete();

        return storedCode != null && storedCode.equals(code);
    }
}
