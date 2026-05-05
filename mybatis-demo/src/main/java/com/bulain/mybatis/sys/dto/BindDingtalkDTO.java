package com.bulain.mybatis.sys.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 绑定钉钉DTO
 */
@Data
public class BindDingtalkDTO {

    /**
     * 钉钉授权码
     */
    @NotBlank(message = "钉钉授权码不能为空")
    private String code;

    /**
     * 防 CSRF state
     */
    private String state;

}
