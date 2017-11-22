package com.bulain.mybatis.demo.dao;

import com.bulain.mybatis.demo.model.Blog;
import com.bulain.mybatis.demo.pojo.BlogSearch;

public interface BlogMapper extends PagedMapper<Blog, BlogSearch>, BasicMapper<Blog> {

}