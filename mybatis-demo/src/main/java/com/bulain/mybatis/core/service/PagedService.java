package com.bulain.mybatis.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.core.pojo.Search;

import java.util.List;

/**
 * 分页服务，对分页查询提供支持。
 */
public interface PagedService<T> extends IService<T> {
    /**
     * 返回满足条件的所有记录。
     * 
     * @param search 查询条件
     * @return 满足条件的所有记录列表
     */
    List<T> find(Search search);
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
    Paged<T> page(Search search);
}
