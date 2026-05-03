package com.bulain.mybatis.core.pojo;

import lombok.Data;

/**
 * 提供排序基础设施。 
 */
@Data
public class OrderBy {

    private String column;
    private String order;
    
    public OrderBy() {
    }
    
    public OrderBy(String column, String order) {
        this.column = column;
        this.order = order;
    }

}
