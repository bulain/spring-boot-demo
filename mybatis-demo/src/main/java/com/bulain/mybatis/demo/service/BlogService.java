package com.bulain.mybatis.demo.service;

import com.bulain.mybatis.core.service.BasicService;
import com.bulain.mybatis.core.service.PagedService;
import com.bulain.mybatis.demo.entity.Blog;
import com.bulain.mybatis.demo.pojo.BlogSearch;

public interface BlogService extends PagedService<Blog, BlogSearch>,
		BasicService<Blog> {

}
