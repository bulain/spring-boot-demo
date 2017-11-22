package com.bulain.mybatis.demo.pojo;

import java.io.Serializable;
import java.util.List;

public class Paged<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    // 分页
    private int pageSize;//每页数量
    private int page;//页号，从1开始
    private int totalPage;//总页数
    private long totalCount;//总个数

    //数据列表
    private List<T> data;

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return (List<T>) data;
    }

    public int getPage() {
        return page;
    }
    public void setPage(final int page) {
        this.page = page;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }
    public int getTotalPage() {
        return totalPage;
    }
    public void setTotalPage(final int totalPage) {
        this.totalPage = totalPage;
    }
    public long getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

}
