package com.bulain.mybatis.sys.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新权限DTO
 */
@Data
public class UpdatePermissionDTO {

    @Size(max = 50, message = "权限名称长度不能超过50")
    private String name;

    @Size(max = 50, message = "资源类型长度不能超过50")
    private String resourceType;

    @Size(max = 255, message = "描述长度不能超过255")
    private String description;

}
