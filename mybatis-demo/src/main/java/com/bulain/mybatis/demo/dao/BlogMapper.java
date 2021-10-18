package com.bulain.mybatis.demo.dao;

import com.bulain.mybatis.core.dao.BasicMapper;
import com.bulain.mybatis.core.dao.PagedMapper;
import com.bulain.mybatis.demo.entity.Blog;
import com.bulain.mybatis.demo.pojo.BlogSearch;

public interface BlogMapper extends PagedMapper<Blog, BlogSearch>, BasicMapper<Blog> {

}
