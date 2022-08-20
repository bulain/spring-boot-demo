package com.bulain.mybatis.core.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;

/**
 * 公共类（无视逻辑删除字段）
 */
@SuppressWarnings("ALL")
public interface DirectDeleteMapper<T> {
    /**
     * 根据ID删除 - 无视逻辑删除字段
     */
    int directDeleteById(Serializable id);

    /**
     * 根据实体(ID)删除 - 无视逻辑删除字段
     */
    int directDeleteById(T entity);

    /**
     * 根据entity条件，删除记录 - 无视逻辑删除字段
     */
    int directDelete(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 删除（根据ID或实体 批量删除）- 无视逻辑删除字段
     */
    int directDeleteBatchIds(@Param(Constants.COLL) Collection<?> idList);

}
