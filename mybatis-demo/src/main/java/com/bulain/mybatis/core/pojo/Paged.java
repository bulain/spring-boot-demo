package com.bulain.mybatis.core.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Paged<T> {

    // 分页
    private int pageSize;//每页数量
    private int page;//页号，从1开始
    private int totalPage;//总页数
    private long totalCount;//总个数

    //数据列表
    private List<T> data;

}
