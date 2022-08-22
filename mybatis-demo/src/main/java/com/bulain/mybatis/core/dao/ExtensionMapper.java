package com.bulain.mybatis.core.dao;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 扩展工具包
 */
public interface ExtensionMapper<T> {

    /**
     * 根据ID强制修改 - 任何值都写入数据库(含NULL)
     */
    int alwaysUpdateSomeColumnById(@Param(Constants.ENTITY) T entity);

    /**
     * 插入或更新一条数据（选择字段插入）
     */
    int upsert(@Param(Constants.ENTITY) T entity);

    /**
     * 批量新增数据,自选字段 insert
     */
    int insertBatchSomeColumn(List<T> list);

}
