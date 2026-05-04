package com.bulain.mybatis.sys.common;

/**
 * 登录安全相关常量
 */
public class LoginSecurityConstants {

    private LoginSecurityConstants() {
    }

    // ============= Redis Key 前缀 =============

    /**
     * 用户登录失败计数前缀
     */
    public static final String LOGIN_FAIL_USER_PREFIX = "login:fail:user:";

    /**
     * IP 登录失败计数前缀
     */
    public static final String LOGIN_FAIL_IP_PREFIX = "login:fail:ip:";

    /**
     * 用户锁定标记前缀
     */
    public static final String LOGIN_LOCK_USER_PREFIX = "login:lock:user:";

    /**
     * IP 限流标记前缀
     */
    public static final String LOGIN_LIMIT_IP_PREFIX = "login:limit:ip:";

    /**
     * 用户需要验证码标记前缀
     */
    public static final String LOGIN_CAPTCHA_USER_PREFIX = "login:captcha:user:";

    /**
     * IP 需要验证码标记前缀
     */
    public static final String LOGIN_CAPTCHA_IP_PREFIX = "login:captcha:ip:";

    // ============= 阈值配置 =============

    /**
     * 用户登录失败触发验证码阈值
     */
    public static final int USER_FAIL_CAPTCHA_THRESHOLD = 5;

    /**
     * IP 登录失败触发验证码阈值（1分钟内）
     */
    public static final int IP_FAIL_CAPTCHA_THRESHOLD = 10;

    /**
     * 用户登录失败触发锁定阈值
     */
    public static final int USER_FAIL_LOCK_THRESHOLD = 10;

    /**
     * IP 登录失败触发限流阈值（1分钟内）
     */
    public static final int IP_FAIL_LIMIT_THRESHOLD = 20;

    // ============= 过期时间配置（秒） =============

    /**
     * 登录失败计数过期时间 - 15分钟
     */
    public static final int LOGIN_FAIL_EXPIRE_SECONDS = 15 * 60;

    /**
     * 用户锁定过期时间 - 30分钟
     */
    public static final int USER_LOCK_EXPIRE_SECONDS = 30 * 60;

    /**
     * IP 限流过期时间 - 10分钟
     */
    public static final int IP_LIMIT_EXPIRE_SECONDS = 10 * 60;

    /**
     * 验证码触发标记过期时间 - 15分钟
     */
    public static final int CAPTCHA_TRIGGER_EXPIRE_SECONDS = 15 * 60;

    // ============= 密码策略配置 =============

    /**
     * 密码最小长度
     */
    public static final int PASSWORD_MIN_LENGTH = 8;

    /**
     * 密码需要的字符类型数量（大小写字母、数字、特殊字符中的至少 N 种）
     */
    public static final int PASSWORD_REQUIRED_CHAR_TYPES = 3;

}
