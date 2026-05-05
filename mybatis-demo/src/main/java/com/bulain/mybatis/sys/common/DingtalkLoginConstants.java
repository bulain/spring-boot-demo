package com.bulain.mybatis.sys.common;

/**
 * 钉钉登录相关常量
 */
public class DingtalkLoginConstants {

    private DingtalkLoginConstants() {
    }

    // ============= Redis Key 前缀 =============

    /**
     * 钉钉登录 state 前缀
     */
    public static final String DINGTALK_LOGIN_STATE_PREFIX = "dingtalk:login:state:";

    // ============= 过期时间配置（秒）=============

    /**
     * state 过期时间 - 5 分钟
     */
    public static final int STATE_EXPIRE_SECONDS = 5 * 60;

    // ============= 钉钉接口地址 =============

    /**
     * 获取用户 access_token 接口
     */
    public static final String DINGTALK_USER_ACCESS_TOKEN_URL = "https://api.dingtalk.com/v1.0/oauth2/userAccessToken";

    /**
     * 获取用户信息接口
     */
    public static final String DINGTALK_USER_INFO_URL = "https://api.dingtalk.com/v1.0/contact/users/me";

    /**
     * 二维码授权地址
     */
    public static final String DINGTALK_QRCODE_URL = "https://login.dingtalk.com/oauth2/auth";

    // ============= 默认用户配置 =============

    /**
     * 默认用户密码前缀（钉钉登录用户自动生成）
     */
    public static final String DEFAULT_PASSWORD_PREFIX = "dingtalk_";

    /**
     * 默认用户名前缀
     */
    public static final String DEFAULT_USERNAME_PREFIX = "dd_user_";

}
