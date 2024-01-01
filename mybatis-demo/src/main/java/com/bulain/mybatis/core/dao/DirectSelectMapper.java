package com.bulain.mybatis.core.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 公共类（无视逻辑删除字段）
 */
@SuppressWarnings("ALL")
public interface DirectSelectMapper<T> {

    /**
     * 根据 ID 查询- 无视逻辑删除字段
     */
    T directSelectById(Serializable id);

    /**
     * 查询（根据ID 批量查询）- 无视逻辑删除字段
     */
    List<T> directSelectBatchIds(@Param(Constants.COLL) Collection<? extends Serializable> idList);

    /**
     * 根据 entity 条件，查询一条记录- 无视逻辑删除字段
     */
    default T directSelectOne(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper) {
        List<T> ts = this.directSelectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(ts)) {
            if (ts.size() != 1) {
                throw ExceptionUtils.mpe("One record is expected, but the query result is multiple records");
            }
            return ts.get(0);
        }
        return null;
    }

    /**
     * 根据 Wrapper 条件，判断是否存在记录- 无视逻辑删除字段
     */
    default boolean directExists(Wrapper<T> queryWrapper) {
        Long count = this.directSelectCount(queryWrapper);
        return null != count && count > 0;
    }

    /**
     * 根据 Wrapper 条件，查询总记录数- 无视逻辑删除字段
     */
    Long directSelectCount(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 根据 entity 条件 - 无视逻辑删除字段
     */
    List<T> directSelectList(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 根据 entity 条件 - 无视逻辑删除字段
     */
    List<T> directSelectList(IPage<T> page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 根据 Wrapper 条件，查询全部记录 - 无视逻辑删除字段
     */
    List<Map<String, Object>> directSelectMaps(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 根据 Wrapper 条件，查询全部记录 - 无视逻辑删除字段
     */
    List<Map<String, Object>> directSelectMaps(IPage<? extends Map<String, Object>> page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）无视逻辑删除字段分页查询 - 无视逻辑删除字段
     */
    default <P extends IPage<T>> P directSelectPage(P page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper) {
        page.setRecords(directSelectList(page, queryWrapper));
        return page;
    }

    /**
     * 根据 Wrapper 条件，查询全部记录（并翻页） - 无视逻辑删除字段
     */
    default <P extends IPage<Map<String, Object>>> P directSelectMapsPage(P page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper) {
        page.setRecords(directSelectMaps(page, queryWrapper));
        return page;
    }

}
