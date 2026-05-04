package com.bulain.mybatis.sys.dto;

import lombok.Data;

/**
 * 权限查询DTO
 */
@Data
public class PermissionQueryDTO {

    private String name;

    private String code;

    private String resourceType;

    private Integer current = 1;

    private Integer size = 10;

}
