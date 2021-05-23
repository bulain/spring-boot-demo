package com.bulain.mybatis.demo.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bulain.mybatis.MybatisPlusApplication;
import com.bulain.mybatis.demo.model.Blog;
import com.bulain.mybatis.demo.pojo.BlogSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MybatisPlusApplication.class)
public class BlogMapperDemo {
    @Autowired
    private BlogMapper blogMapper;

    private BlogSearch search;

    @BeforeEach
    public void setUp() {
        search = new BlogSearch();
        search.setTitle("abd");
        search.setDescr("descr");
        search.setActiveFlag("Y");
        search.setCreatedVia("Thread");
        search.addOrderBy("id", "desc");
    }

    @Test
    public void testFind() {
        blogMapper.find(search);
    }

    @Test
    public void testFindPage() {
        Page<Blog> page = new Page<Blog>(1, 2);
        blogMapper.find(page, search);
    }

}
