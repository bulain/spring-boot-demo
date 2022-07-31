package com.bulain.mybatis.core.pojo;

/**
 * 分页信息 
 */
public class Page {
    // 分页
    private long low;//最低值
    private long high;//最高值
    private long count;//总记录数
    private int pageSize;//每页数量
    private int page;//设置页数，从1开始
    private int totalPage;//总页数

    public void setCount(int psize, long cnt, int page) {
        pageSize = psize < 0 ? 0 : psize;
        count = cnt < 0 ? 0 : cnt;
        totalPage = (int) (((count - 1) / pageSize) + 1);
        page = page < 1 ? 1 : page;
        low = (page - 1) * pageSize;
        high = page * pageSize;
    }

    public long getCount() {
        return count;
    }
    public long getHigh() {
        return high;
    }
    public void setHigh(long high) {
        this.high = high;
    }
    public long getLow() {
        return low;
    }
    public void setLow(long low) {
        this.low = low;
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
    public void setCount(long count) {
        this.count = count;
    }

}
