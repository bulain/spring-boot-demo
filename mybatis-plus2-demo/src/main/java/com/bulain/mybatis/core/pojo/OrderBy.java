package com.bulain.mybatis.core.pojo;

import java.io.Serializable;

/**
 * 提供排序基础设施。 
 */
public class OrderBy implements Serializable {
    private static final long serialVersionUID = 1L;

    private String column;
    private String order;
    
    public OrderBy() {
    }
    
    public OrderBy(String column, String order) {
        this.column = column;
        this.order = order;
    }
    public String getColumn() {
        return column != null ? column.replace("\\s", "_") : null;
    }
    public void setColumn(String column) {
        this.column = column;
    }
    public String getOrder() {
        return order != null ? order.replace("\\s", "_") : null;
    }
    public void setOrder(String order) {
        this.order = order;
    }

}
