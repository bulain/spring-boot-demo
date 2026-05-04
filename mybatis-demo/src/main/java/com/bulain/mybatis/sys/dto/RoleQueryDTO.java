package com.bulain.mybatis.sys.dto;

import lombok.Data;

/**
 * 角色查询DTO
 */
@Data
public class RoleQueryDTO {

    private String name;

    private String code;

    private Integer current = 1;

    private Integer size = 10;

}
