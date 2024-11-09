package com.bulain.mybatis.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.bulain.mybatis.core.service.PagedServiceImpl;
import com.bulain.mybatis.demo.dao.BlogMapper;
import com.bulain.mybatis.demo.entity.Blog;
import com.bulain.mybatis.demo.service.BlogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlogServiceImpl extends PagedServiceImpl<BlogMapper, Blog> implements BlogService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean directRemove(Wrapper<Blog> queryWrapper) {
        return SqlHelper.retBool(baseMapper.directDelete(queryWrapper));
    }

}
