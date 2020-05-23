package com.bulain.mybatis.core.service;

import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.core.pojo.Search;

/**
 * 分页服务，对分页查询提供支持。
 */
public interface PagedService<T, S extends Search> {
    /**
     * 返回满足条件的所有记录。
     * 
     * @param search 查询条件
     * @return 满足条件的所有记录列表
     */
    Paged<T> find(S search);
    /**
     * 返回满足条件的记录数。
     * 
     * @param search 查询条件
     * @return 满足条件的记录数
     */
    //long count(S search);
    /**
     * 返回满足条件的分页记录。
     * 
     * @param search 查询条件
     * @return 满足条件的分页记录列表
     */
    Paged<T> page(S search);
}
