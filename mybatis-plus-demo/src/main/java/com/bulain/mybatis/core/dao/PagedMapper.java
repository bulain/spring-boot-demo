package com.bulain.mybatis.core.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;

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
    List<T> find(@Param("s") S search);
    /**
     * 返回满足条件的分页记录。
     * 
     * @param search 查询条件
     * @param page 分页信息
     * @return 满足条件的所有记录列表
     */
    List<T> find(@Param("s") S search, @Param("p") IPage<T> page);
}
