package com.bulain.mybatis.core.service;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.bulain.mybatis.core.dao.BasicMapper;

/**
 *  基本服务实现，主要提供CRUD功能。
 *  @author Bulain
 */
public abstract class BasicServiceImpl<T> implements BasicService<T> {
    protected static final String ID = "id";

    /**
     * 返回BasicMapper,子类应该实现这个方法
     * @return BasicMapper
     */
    protected abstract BasicMapper<T> getBasicMapper();


    @Override
    public T get(Long id) {
        return getBasicMapper().selectByPrimaryKey(id);
    }

    @Override
    public Long insert(T t, boolean forced) {
        if (forced) {
            getBasicMapper().insert(t);
        } else {
            getBasicMapper().insertSelective(t);
        }

        BeanWrapper wrapper = new BeanWrapperImpl(t);
        return (Long) wrapper.getPropertyValue(ID);

    }

    @Override
    public Long update(T t, boolean forced) {
        if (forced) {
            getBasicMapper().updateByPrimaryKey(t);
        } else {
            getBasicMapper().updateByPrimaryKeySelective(t);
        }

        BeanWrapper wrapper = new BeanWrapperImpl(t);
        return (Long) wrapper.getPropertyValue(ID);
    }

    @Override
    public void delete(Long id) {
        getBasicMapper().deleteByPrimaryKey(id);
    }

    @Override
    public Long save(T record, boolean forced) {
        BeanWrapper wrapper = new BeanWrapperImpl(record);
        Long id = (Long) wrapper.getPropertyValue(ID);

        if (id == null) {
            return insert(record, forced);
        } else {
            return update(record, forced);
        }
    }

}
