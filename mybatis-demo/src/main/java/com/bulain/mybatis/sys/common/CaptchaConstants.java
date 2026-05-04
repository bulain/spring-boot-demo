package com.bulain.mybatis.sys.common;

/**
 * 验证码相关常量
 */
public class CaptchaConstants {

    private CaptchaConstants() {
    }

    /**
     * 图片验证码过期时间（秒）- 5 分钟
     */
    public static final int IMAGE_CAPTCHA_EXPIRE_SECONDS = 5 * 60;

    /**
     * 短信验证码过期时间（秒）- 10 分钟
     */
    public static final int SMS_CAPTCHA_EXPIRE_SECONDS = 10 * 60;

    /**
     * 图片验证码长度
     */
    public static final int IMAGE_CAPTCHA_LENGTH = 4;

    /**
     * 短信验证码长度
     */
    public static final int SMS_CAPTCHA_LENGTH = 6;

    /**
     * 图片验证码 IP 限制时间窗口（秒）- 1 分钟
     */
    public static final int IMAGE_CAPTCHA_IP_LIMIT_WINDOW = 60;

    /**
     * 图片验证码 IP 限制最大次数
     */
    public static final int IMAGE_CAPTCHA_IP_LIMIT_MAX = 10;

    /**
     * 短信验证码手机号 1 小时限制次数
     */
    public static final int SMS_CAPTCHA_HOUR_LIMIT_MAX = 5;

    /**
     * 短信验证码手机号 24 小时限制次数
     */
    public static final int SMS_CAPTCHA_DAY_LIMIT_MAX = 10;

    /**
     * Redis 缓存前缀 - 图片验证码
     */
    public static final String CAPTCHA_IMAGE_PREFIX = "captcha:image:";

    /**
     * Redis 缓存前缀 - 短信验证码
     */
    public static final String CAPTCHA_SMS_PREFIX = "captcha:sms:";

    /**
     * Redis 缓存前缀 - 图片验证码 IP 计数
     */
    public static final String CAPTCHA_COUNT_IMAGE_IP_PREFIX = "captcha:count:image:ip:";

    /**
     * Redis 缓存前缀 - 短信验证码手机号小时计数
     */
    public static final String CAPTCHA_COUNT_SMS_HOUR_PREFIX = "captcha:count:sms:hour:";

    /**
     * Redis 缓存前缀 - 短信验证码手机号天计数
     */
    public static final String CAPTCHA_COUNT_SMS_DAY_PREFIX = "captcha:count:sms:day:";
}
