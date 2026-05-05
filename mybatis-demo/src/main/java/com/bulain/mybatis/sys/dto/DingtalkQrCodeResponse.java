package com.bulain.mybatis.sys.dto;

import lombok.Data;

/**
 * 钉钉二维码响应DTO
 */
@Data
public class DingtalkQrCodeResponse {

    /**
     * 钉钉二维码授权完整URL
     */
    private String qrCodeUrl;

    /**
     * 防 CSRF 随机字符串
     */
    private String state;

    /**
     * 二维码有效时间（秒）
     */
    private int expireSeconds;

}
