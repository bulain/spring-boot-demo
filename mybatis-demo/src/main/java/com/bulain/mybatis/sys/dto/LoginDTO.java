package com.bulain.mybatis.sys.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户名密码登录DTO
 */
@Data
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 验证码ID（登录失败达到阈值后需要）
     */
    private String captchaId;

    /**
     * 图片验证码（登录失败达到阈值后需要）
     */
    private String captchaCode;

}
