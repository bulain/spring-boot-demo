package com.bulain.mybatis.demo.dao;

/**
 * 基本DAO，主要提供CRUD操作。
 * @author Bulain
 */
public interface BasicMapper<T> {
    /**
     * 通过主键删除数据库记录。
     * 
     * @param id 主键
     * @return 影响的记录数
     */
    int deleteByPrimaryKey(Long id);
    /**
     * 插入记录到数据库中。
     * 
     * @param record 要插入数据库的记录
     * @return 影响的记录数
     */
    int insert(T record);
    /**
     * 插入记录到数据库中，只插入非空值。
     * 
     * @param record 要插入数据库的记录
     * @return 影响的记录数
     */
    int insertSelective(T record);
    /**
     * 通过主键查询数据库中表记录。
     * 
     * @param id 主键
     * @return 数据库记录
     */
    T selectByPrimaryKey(Long id);
    /**
     * 更新记录到数据库中，只更新非空值。
     * 
     * @param record 要更新数据库的记录
     * @return 影响的记录数
     */
    int updateByPrimaryKeySelective(T record);
    /**
     * 更新记录到数据库中。
     * 
     * @param record 要更新数据库的记录
     * @return 影响的记录数
     */
    int updateByPrimaryKey(T record);
}
