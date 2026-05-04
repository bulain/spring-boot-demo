package com.bulain.mybatis.sys.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 钉钉登录DTO
 */
@Data
public class DingtalkLoginDTO {

    @NotBlank(message = "钉钉授权码不能为空")
    private String authCode;

}
