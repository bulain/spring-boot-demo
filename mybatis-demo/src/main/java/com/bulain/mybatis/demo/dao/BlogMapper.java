package com.bulain.mybatis.demo.dao;

import java.util.List;

import com.bulain.mybatis.demo.model.Blog;

public interface BlogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Blog record);

    int insertSelective(Blog record);

    Blog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Blog record);

    int updateByPrimaryKey(Blog record);

    int bulkInsert(List<Blog> list);

}