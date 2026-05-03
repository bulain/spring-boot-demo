package com.bulain.mybatis.core.pojo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 * 分页响应对象
 */
@Data
public class Paged<T> {

    /**
     * 每页数量
     */
    private int pageSize;

    /**
     * 页号，从1开始
     */
    private int page;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 总记录数
     */
    private long totalCount;

    /**
     * 数据列表
     */
    private List<T> data;

    /**
     * 从 IPage 转换为 Paged
     * @param pageResult MyBatis Plus 分页结果
     * @param <T> 数据类型
     * @return Paged 分页对象
     */
    public static <T> Paged<T> from(IPage<T> pageResult) {
        Paged<T> paged = new Paged<>();
        paged.setPageSize((int) pageResult.getSize());
        paged.setTotalPage((int) pageResult.getPages());
        paged.setTotalCount(pageResult.getTotal());
        paged.setPage((int) pageResult.getCurrent());
        paged.setData(pageResult.getRecords());
        return paged;
    }

}
