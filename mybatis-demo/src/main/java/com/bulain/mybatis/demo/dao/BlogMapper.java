package com.bulain.mybatis.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bulain.mybatis.core.dao.DirectDeleteMapper;
import com.bulain.mybatis.demo.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper extends BaseMapper<Blog>, DirectDeleteMapper<Blog> {

}
