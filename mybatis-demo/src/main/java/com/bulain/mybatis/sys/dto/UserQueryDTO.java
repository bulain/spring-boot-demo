package com.bulain.mybatis.sys.dto;

import lombok.Data;

/**
 * 用户查询DTO
 */
@Data
public class UserQueryDTO {

    private String username;

    private String name;

    private String phone;

    private Integer status;

    private Integer current = 1;

    private Integer size = 10;

}
