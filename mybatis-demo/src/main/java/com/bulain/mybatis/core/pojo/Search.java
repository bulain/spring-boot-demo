package com.bulain.mybatis.core.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询基础类 
 */
@Data
public class Search {
    private static final int PAGE_SIZE = 20;

    // 排序
    private List<OrderBy> orderBys = new ArrayList<>();

    // 分页
    private int pageSize = PAGE_SIZE;
    private int page = 1;
    
    public Search() {
    }
    
    /**
     * 设置排序字段列表
     * @param orderBy 排序字段列表
     */
    public void addOrderBy(OrderBy orderBy) {
        this.orderBys.add(orderBy);
    }

    /**
     * 设置排序字段列表
     * @param column 排序字段
     * @param order 排序顺序
     */
    public void addOrderBy(String column, String order) {
        addOrderBy(new OrderBy(column, order));
    }

    /**
     * 清空排序
     */
    public void clearOrderBy() {
        this.orderBys.clear();
    }

}
