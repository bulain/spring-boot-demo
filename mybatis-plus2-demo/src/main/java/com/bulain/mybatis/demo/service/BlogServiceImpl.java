package com.bulain.mybatis.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bulain.mybatis.core.dao.BasicMapper;
import com.bulain.mybatis.core.dao.PagedMapper;
import com.bulain.mybatis.core.service.PagedServiceImpl;
import com.bulain.mybatis.demo.dao.BlogMapper;
import com.bulain.mybatis.demo.model.Blog;
import com.bulain.mybatis.demo.pojo.BlogSearch;

@Service
public class BlogServiceImpl extends PagedServiceImpl<Blog, BlogSearch> implements BlogService {

	@Autowired
    private BlogMapper blogMapper;

	@Override
	protected PagedMapper<Blog, BlogSearch> getPagedMapper() {
		return blogMapper;
	}

	@Override
	protected BasicMapper<Blog> getBasicMapper() {
		return blogMapper;
	}
    

}
