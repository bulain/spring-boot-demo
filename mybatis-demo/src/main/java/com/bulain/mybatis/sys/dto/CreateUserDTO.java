package com.bulain.mybatis.sys.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 创建用户DTO
 */
@Data
public class CreateUserDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(max = 50, message = "用户名长度不能超过50")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(max = 255, message = "密码长度不能超过255")
    private String password;

    @Size(max = 50, message = "姓名长度不能超过50")
    private String name;

    @Size(max = 100, message = "邮箱长度不能超过100")
    private String email;

    @Size(max = 20, message = "手机号长度不能超过20")
    private String phone;

}
