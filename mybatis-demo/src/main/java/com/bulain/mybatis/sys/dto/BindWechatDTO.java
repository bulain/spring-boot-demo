package com.bulain.mybatis.sys.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 绑定微信DTO
 */
@Data
public class BindWechatDTO {

    /**
     * 微信授权码
     */
    @NotBlank(message = "微信授权码不能为空")
    private String code;

    /**
     * 防 CSRF state
     */
    private String state;

}
