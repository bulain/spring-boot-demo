package com.bulain.mybatis.sys.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建权限DTO
 */
@Data
public class CreatePermissionDTO {

    @NotBlank(message = "权限名称不能为空")
    @Size(max = 50, message = "权限名称长度不能超过50")
    private String name;

    @NotBlank(message = "权限编码不能为空")
    @Size(max = 100, message = "权限编码长度不能超过100")
    private String code;

    @Size(max = 50, message = "资源类型长度不能超过50")
    private String resourceType;

    @Size(max = 255, message = "描述长度不能超过255")
    private String description;

}
