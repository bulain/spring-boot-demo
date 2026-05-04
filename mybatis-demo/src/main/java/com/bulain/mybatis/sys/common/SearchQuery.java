package com.bulain.mybatis.sys.common;

import lombok.Data;

/**
 * 基础查询参数
 */
@Data
public class SearchQuery {

    private Integer pageNum = 1;
    private Integer pageSize = 10;

}
