package com.bulain.mybatis.sys.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 发送短信验证码请求 DTO
 */
@Data
public class SendCodeRequest {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 图片验证码 ID
     */
    @NotBlank(message = "验证码 ID 不能为空")
    private String captchaId;

    /**
     * 图片验证码
     */
    @NotBlank(message = "图片验证码不能为空")
    private String captchaCode;
}
