package com.bulain.mybatis.sys.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 钉钉登录DTO
 */
@Data
public class DingtalkLoginDTO {

    @NotBlank(message = "钉钉授权码不能为空")
    private String authCode;

}
