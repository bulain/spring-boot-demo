package com.bulain.mybatis.core.dao;

/**
 * 公共类（无视逻辑删除字段）
 */
public interface DirectMapper<T> extends DirectDeleteMapper<T>, DirectUpdateMapper<T>, DirectSelectMapper<T> {

}
