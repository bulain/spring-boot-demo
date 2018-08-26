package com.bulain.mybatis.core.service;


/**
 * 基本服务，主要提供CRUD操作。
 */
public interface BasicService<T> {
    /**
     * 通过主键查询数据库中表记录。
     * 
     * @param id 主键
     * @return 数据库记录
     */
    T get(Long id);
    /**
     * 插入记录到数据库中。
     * 
     * @param data 要插入数据库的记录
     */
    Long insert(T data, boolean forced);
    /**
     * 更新记录到数据库中。
     * 
     * @param data 要更新数据库的记录
     * @param forced 是否更新所有字段
     */
    Long update(T data, boolean forced);
    /**
     * 通过主键删除数据库记录。
     * 
     * @param id 主键
     */
    void delete(Long id);
    /**
     * 保存记录到数据库，可能新建或更新
     * 
     * @param data 要保存的记录
     */
    Long save(T data, boolean forced);

}
