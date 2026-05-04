package com.bulain.mybatis.sys.dto;

import lombok.Data;

import java.util.List;

/**
 * 角色权限分配DTO
 */
@Data
public class RolePermissionAssignDTO {

    private List<String> permissionIds;

}
