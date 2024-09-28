package com.bulain.mybatis.demo.dao;

import com.bulain.mybatis.core.dao.PagedMapper;
import com.bulain.mybatis.demo.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper extends PagedMapper<Blog> {

}
