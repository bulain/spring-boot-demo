package com.bulain.mybatis.core.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询基础类 
 */
public class Search implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 20;

    // 排序
    private List<OrderBy> orderBys = new ArrayList<OrderBy>();

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

    /**
     * 返回页数，从1开始
     * 
     * @return 页数
     */
    public int getPage() {
        return page;
    }
    /**
     * 设置页数，从1开始
     * 
     * @param page 页数
     */
    public void setPage(final int page) {
        this.page = page;
    }
    /**
     * 返回每页数量
     * 
     * @return 每页数量
     */
    public int getPageSize() {
        return pageSize;
    }
    /**
     * 设置每页数量
     * 
     * @param pageSize 每页数量
     */
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }
    /**
     * 返回排序字段列表
     * @return 排序字段列表
     */
    public List<OrderBy> getOrderBys() {
        return orderBys;
    }

    /**
     * 设置排序字段列表
     * @param orderBy 排序字段列表
     */
    public void setOrderBys(List<OrderBy> orderBys) {
        this.orderBys = orderBys;
    }
    

}
