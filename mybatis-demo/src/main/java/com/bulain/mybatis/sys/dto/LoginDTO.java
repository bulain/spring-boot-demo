package com.bulain.mybatis.sys.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 用户名密码登录DTO
 */
@Data
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

}
