package com.bulain.mybatis.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bulain.mybatis.demo.dao.BlogMapper;
import com.bulain.mybatis.demo.entity.Blog;
import com.bulain.mybatis.demo.service.BlogService;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {


}
