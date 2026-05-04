package com.bulain.mybatis.sys.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 手机号验证码登录DTO
 */
@Data
public class PhoneLoginDTO {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    private String code;

}
