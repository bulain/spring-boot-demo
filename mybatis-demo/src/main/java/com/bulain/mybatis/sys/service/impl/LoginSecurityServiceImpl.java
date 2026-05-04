package com.bulain.mybatis.sys.service.impl;

import com.bulain.mybatis.sys.common.LoginSecurityConstants;
import com.bulain.mybatis.sys.service.LoginSecurityService;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 登录安全服务实现
 */
@Service
public class LoginSecurityServiceImpl implements LoginSecurityService {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void recordLoginFailure(String userId, String ip) {
        // 记录用户维度失败计数
        if (userId != null) {
            String userKey = LoginSecurityConstants.LOGIN_FAIL_USER_PREFIX + userId;
            RAtomicLong userCount = redissonClient.getAtomicLong(userKey);
            long count = userCount.incrementAndGet();
            if (count == 1) {
                userCount.expire(LoginSecurityConstants.LOGIN_FAIL_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }

            // 检查是否达到锁定阈值
            if (count >= LoginSecurityConstants.USER_FAIL_LOCK_THRESHOLD) {
                String lockKey = LoginSecurityConstants.LOGIN_LOCK_USER_PREFIX + userId;
                RBucket<Boolean> lockBucket = redissonClient.getBucket(lockKey);
                lockBucket.set(true, LoginSecurityConstants.USER_LOCK_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }

            // 检查是否达到验证码阈值
            if (count >= LoginSecurityConstants.USER_FAIL_CAPTCHA_THRESHOLD) {
                String captchaKey = LoginSecurityConstants.LOGIN_CAPTCHA_USER_PREFIX + userId;
                RBucket<Boolean> captchaBucket = redissonClient.getBucket(captchaKey);
                captchaBucket.set(true, LoginSecurityConstants.CAPTCHA_TRIGGER_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }
        }

        // 记录 IP 维度失败计数
        if (ip != null) {
            String ipKey = LoginSecurityConstants.LOGIN_FAIL_IP_PREFIX + ip;
            RAtomicLong ipCount = redissonClient.getAtomicLong(ipKey);
            long count = ipCount.incrementAndGet();
            if (count == 1) {
                ipCount.expire(LoginSecurityConstants.LOGIN_FAIL_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }

            // 检查是否达到限流阈值
            if (count >= LoginSecurityConstants.IP_FAIL_LIMIT_THRESHOLD) {
                String limitKey = LoginSecurityConstants.LOGIN_LIMIT_IP_PREFIX + ip;
                RBucket<Boolean> limitBucket = redissonClient.getBucket(limitKey);
                limitBucket.set(true, LoginSecurityConstants.IP_LIMIT_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }

            // 检查是否达到验证码阈值
            if (count >= LoginSecurityConstants.IP_FAIL_CAPTCHA_THRESHOLD) {
                String captchaKey = LoginSecurityConstants.LOGIN_CAPTCHA_IP_PREFIX + ip;
                RBucket<Boolean> captchaBucket = redissonClient.getBucket(captchaKey);
                captchaBucket.set(true, LoginSecurityConstants.CAPTCHA_TRIGGER_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public void recordLoginSuccess(String userId, String ip) {
        // 重置用户维度的所有计数和标记
        if (userId != null) {
            String userKey = LoginSecurityConstants.LOGIN_FAIL_USER_PREFIX + userId;
            redissonClient.getAtomicLong(userKey).delete();

            String userLockKey = LoginSecurityConstants.LOGIN_LOCK_USER_PREFIX + userId;
            redissonClient.getBucket(userLockKey).delete();

            String userCaptchaKey = LoginSecurityConstants.LOGIN_CAPTCHA_USER_PREFIX + userId;
            redissonClient.getBucket(userCaptchaKey).delete();
        }

        // 重置 IP 维度的所有计数和标记
        if (ip != null) {
            String ipKey = LoginSecurityConstants.LOGIN_FAIL_IP_PREFIX + ip;
            redissonClient.getAtomicLong(ipKey).delete();

            String ipLimitKey = LoginSecurityConstants.LOGIN_LIMIT_IP_PREFIX + ip;
            redissonClient.getBucket(ipLimitKey).delete();

            String ipCaptchaKey = LoginSecurityConstants.LOGIN_CAPTCHA_IP_PREFIX + ip;
            redissonClient.getBucket(ipCaptchaKey).delete();
        }
    }

    @Override
    public boolean isUserLocked(String userId) {
        if (userId == null) {
            return false;
        }
        String key = LoginSecurityConstants.LOGIN_LOCK_USER_PREFIX + userId;
        RBucket<Boolean> bucket = redissonClient.getBucket(key);
        return bucket.isExists() && Boolean.TRUE.equals(bucket.get());
    }

    @Override
    public boolean isIpLimited(String ip) {
        if (ip == null) {
            return false;
        }
        String key = LoginSecurityConstants.LOGIN_LIMIT_IP_PREFIX + ip;
        RBucket<Boolean> bucket = redissonClient.getBucket(key);
        return bucket.isExists() && Boolean.TRUE.equals(bucket.get());
    }

    @Override
    public boolean isCaptchaRequired(String userId, String ip) {
        boolean userRequires = false;
        boolean ipRequires = false;

        if (userId != null) {
            String userKey = LoginSecurityConstants.LOGIN_CAPTCHA_USER_PREFIX + userId;
            RBucket<Boolean> userBucket = redissonClient.getBucket(userKey);
            userRequires = userBucket.isExists() && Boolean.TRUE.equals(userBucket.get());
        }

        if (ip != null) {
            String ipKey = LoginSecurityConstants.LOGIN_CAPTCHA_IP_PREFIX + ip;
            RBucket<Boolean> ipBucket = redissonClient.getBucket(ipKey);
            ipRequires = ipBucket.isExists() && Boolean.TRUE.equals(ipBucket.get());
        }

        return userRequires || ipRequires;
    }

    @Override
    public void unlockUser(String userId) {
        if (userId == null) {
            return;
        }
        // 删除锁定标记和失败计数
        String userLockKey = LoginSecurityConstants.LOGIN_LOCK_USER_PREFIX + userId;
        redissonClient.getBucket(userLockKey).delete();

        String userFailKey = LoginSecurityConstants.LOGIN_FAIL_USER_PREFIX + userId;
        redissonClient.getAtomicLong(userFailKey).delete();

        String userCaptchaKey = LoginSecurityConstants.LOGIN_CAPTCHA_USER_PREFIX + userId;
        redissonClient.getBucket(userCaptchaKey).delete();
    }

    @Override
    public boolean isUserCaptchaThresholdReached(String userId) {
        if (userId == null) {
            return false;
        }
        String key = LoginSecurityConstants.LOGIN_FAIL_USER_PREFIX + userId;
        RAtomicLong count = redissonClient.getAtomicLong(key);
        return count.isExists() && count.get() >= LoginSecurityConstants.USER_FAIL_CAPTCHA_THRESHOLD;
    }

    @Override
    public boolean isIpCaptchaThresholdReached(String ip) {
        if (ip == null) {
            return false;
        }
        String key = LoginSecurityConstants.LOGIN_FAIL_IP_PREFIX + ip;
        RAtomicLong count = redissonClient.getAtomicLong(key);
        return count.isExists() && count.get() >= LoginSecurityConstants.IP_FAIL_CAPTCHA_THRESHOLD;
    }

}
