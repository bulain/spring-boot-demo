package com.bulain.mybatis.sys.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户角色分配DTO
 */
@Data
public class UserRoleAssignDTO {

    private List<Long> roleIds;

}
