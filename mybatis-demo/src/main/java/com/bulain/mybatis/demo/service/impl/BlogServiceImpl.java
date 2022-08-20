package com.bulain.mybatis.demo.service.impl;

import com.bulain.mybatis.core.service.PagedServiceImpl;
import com.bulain.mybatis.demo.dao.BlogMapper;
import com.bulain.mybatis.demo.entity.Blog;
import com.bulain.mybatis.demo.service.BlogService;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl extends PagedServiceImpl<BlogMapper, Blog> implements BlogService {

}
