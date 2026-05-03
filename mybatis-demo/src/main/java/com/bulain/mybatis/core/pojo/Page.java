package com.bulain.mybatis.core.pojo;

import lombok.Data;

/**
 * 分页信息 
 */
@Data
public class Page {
    // 分页
    private long low;//最低值
    private long high;//最高值
    private long count;//总记录数
    private int pageSize;//每页数量
    private int page;//设置页数，从1开始
    private int totalPage;//总页数

    public void setCount(int psize, long cnt, int page) {
        pageSize = Math.max(psize, 0);
        count = cnt < 0 ? 0 : cnt;
        totalPage = (int) (((count - 1) / pageSize) + 1);
        page = Math.max(page, 1);
        low = (long) (page - 1) * pageSize;
        high = (long) page * pageSize;
    }

}
