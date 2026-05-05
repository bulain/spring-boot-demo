package com.bulain.mybatis.sys.common;

/**
 * 微信登录相关常量
 */
public class WechatLoginConstants {

    private WechatLoginConstants() {
    }

    // ============= Redis Key 前缀 =============

    /**
     * 微信登录 state 前缀
     */
    public static final String WECHAT_LOGIN_STATE_PREFIX = "wechat:login:state:";

    /**
     * 微信 access token 前缀
     */
    public static final String WECHAT_ACCESS_TOKEN_PREFIX = "wechat:token:";

    // ============= 过期时间配置（秒）=============

    /**
     * state 过期时间 - 5 分钟
     */
    public static final int STATE_EXPIRE_SECONDS = 5 * 60;

    /**
     * access token 过期时间 - 2 小时（微信官方有效期）
     */
    public static final int ACCESS_TOKEN_EXPIRE_SECONDS = 2 * 60 * 60;

    // ============= 微信接口地址 =============

    /**
     * 获取 access token 接口
     */
    public static final String WECHAT_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * 获取用户信息接口
     */
    public static final String WECHAT_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";

    /**
     * 二维码授权地址
     */
    public static final String WECHAT_QRCODE_URL = "https://open.weixin.qq.com/connect/qrconnect";

    // ============= 错误码 =============

    /**
     * 授权码无效或已过期
     */
    public static final int ERROR_CODE_INVALID_CODE = 40029;

    /**
     * 刷新 token 无效
     */
    public static final int ERROR_CODE_INVALID_REFRESH_TOKEN = 40030;

    /**
     * access token 无效
     */
    public static final int ERROR_CODE_INVALID_ACCESS_TOKEN = 40001;

    // ============= 默认用户配置 =============

    /**
     * 默认用户密码前缀（微信登录用户自动生成）
     */
    public static final String DEFAULT_PASSWORD_PREFIX = "wechat_";

    /**
     * 默认用户名前缀
     */
    public static final String DEFAULT_USERNAME_PREFIX = "wx_user_";

}
