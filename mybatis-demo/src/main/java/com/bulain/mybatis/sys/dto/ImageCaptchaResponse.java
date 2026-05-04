package com.bulain.mybatis.sys.dto;

import lombok.Data;

/**
 * 图片验证码响应 DTO
 */
@Data
public class ImageCaptchaResponse {

    /**
     * 验证码唯一标识
     */
    private String captchaId;

    /**
     * Base64 编码的验证码图片
     */
    private String imageBase64;

    public ImageCaptchaResponse(String captchaId, String imageBase64) {
        this.captchaId = captchaId;
        this.imageBase64 = imageBase64;
    }
}
