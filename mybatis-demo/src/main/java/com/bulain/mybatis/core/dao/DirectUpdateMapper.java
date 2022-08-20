package com.bulain.mybatis.core.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

/**
 * 公共类（无视逻辑删除字段）
 */
@SuppressWarnings("ALL")
public interface DirectUpdateMapper<T> {

    /**
     * 根据 ID 修改- 无视逻辑删除字段
     */
    int directUpdateById(@Param(Constants.ENTITY) T entity);

    /**
     * 根据 whereEntity 条件，更新记录- 无视逻辑删除字段
     */
    int directUpdate(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> updateWrapper);

}
