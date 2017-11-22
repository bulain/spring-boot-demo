package com.bulain.mybatis.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bulain.mybatis.demo.model.Blog;

@Mapper
public interface BlogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Blog record);

    int insertSelective(Blog record);

    Blog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Blog record);

    int updateByPrimaryKey(Blog record);

    int bulkInsert(List<Blog> list);

}