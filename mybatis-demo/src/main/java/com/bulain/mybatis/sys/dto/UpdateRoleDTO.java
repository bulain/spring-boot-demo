package com.bulain.mybatis.sys.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新角色DTO
 */
@Data
public class UpdateRoleDTO {

    @Size(max = 50, message = "角色名称长度不能超过50")
    private String name;

    @Size(max = 255, message = "描述长度不能超过255")
    private String description;

}
