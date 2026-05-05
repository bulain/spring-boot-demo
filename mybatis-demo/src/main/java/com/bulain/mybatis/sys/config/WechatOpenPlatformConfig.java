package com.bulain.mybatis.sys.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信开放平台配置
 */
@Component
@ConfigurationProperties(prefix = "wechat.open-platform")
public class WechatOpenPlatformConfig {

    /**
     * 微信开放平台 AppID
     */
    private String appId;

    /**
     * 微信开放平台 AppSecret
     */
    private String appSecret;

    /**
     * 授权回调地址
     */
    private String redirectUri;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
