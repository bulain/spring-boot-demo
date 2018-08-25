package com.bulain.core.pojo;

import java.io.Serializable;

/**
 * 请求值基类 
 * @author Bulain
 */
public class Req implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private int pageSize; //每页条数
    private int page; //当前页

    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }

}
