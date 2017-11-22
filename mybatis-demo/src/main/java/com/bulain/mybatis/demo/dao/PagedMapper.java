package com.bulain.mybatis.demo.dao;

import java.util.List;

/**
 * 分页DAO，对分页查询提供支持。
 * @author Bulain
 */
public interface PagedMapper<T, S> {
    /**
     * 返回满足条件的所有记录。
     * 
     * @param search 查询条件
     * @return 满足条件的所有记录列表
     */
    List<T> find(S search);
}
