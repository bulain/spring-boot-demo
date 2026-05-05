package com.bulain.mybatis.sys.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 解绑微信DTO
 */
@Data
public class UnbindWechatDTO {

    /**
     * 当前账号密码，用于身份确认
     */
    @NotBlank(message = "密码不能为空")
    private String password;

}
