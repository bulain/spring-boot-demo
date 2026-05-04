package com.bulain.mybatis.sys.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 微信登录DTO
 */
@Data
public class WechatLoginDTO {

    @NotBlank(message = "微信授权码不能为空")
    private String code;

    private String state;

}
