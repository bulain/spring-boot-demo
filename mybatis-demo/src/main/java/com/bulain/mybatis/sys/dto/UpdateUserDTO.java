package com.bulain.mybatis.sys.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新用户DTO
 */
@Data
public class UpdateUserDTO {

    @Size(max = 50, message = "姓名长度不能超过50")
    private String name;

    @Size(max = 100, message = "邮箱长度不能超过100")
    private String email;

    @Size(max = 20, message = "手机号长度不能超过20")
    private String phone;

}
