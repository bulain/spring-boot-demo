package com.bulain.core.pojo;

import java.util.List;

/**
 * 列表返回值基类 
 * @author Bulain
 */
public class ListResp extends BaseResp {
    private static final long serialVersionUID = 1L;

    private int pageSize; //每页条数
    private int page; //当前页
    private int totalPage; //总共页数
    private long totalCount; //总共记录数
    private List<?> data; //列表数据

    public static <T> ListResp ok(List<T> data) {
        ListResp res = new ListResp();
        res.setSuccess(true);
        res.setPage(1);
        res.setPageSize(data.size());
        res.setTotalCount(data.size());
        res.setTotalPage(1);
        res.setData(data);
        return res;
    }

    public static ListResp fail(String err, String msg) {
        ListResp res = new ListResp();
        res.setSuccess(false);
        res.setErr(err);
        res.setMsg(msg);
        return res;
    }

    public static ListResp fail(Exception e) {
        String msg = e.getMessage();
        if (msg == null) {
            msg = e.getClass().getName();
        }

        ListResp res = new ListResp();
        res.setSuccess(false);
        res.setErr("E40010");
        res.setMsg(msg);
        return res;
    }

    public long getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(long count) {
        this.totalCount = count;
    }
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
    public int getTotalPage() {
        return totalPage;
    }
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    @SuppressWarnings("unchecked")
    public <T> List<T> getData() {
        return (List<T>) data;
    }
    public <T> void setData(List<T> data) {
        this.data = data;
    }

}
