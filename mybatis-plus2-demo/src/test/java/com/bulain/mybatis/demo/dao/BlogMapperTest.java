package com.bulain.mybatis.demo.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.plugins.Page;
import com.bulain.mybatis.MybatisPlusApplication;
import com.bulain.mybatis.demo.model.Blog;
import com.bulain.mybatis.demo.pojo.BlogSearch;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MybatisPlusApplication.class)
public class BlogMapperTest {
    @Autowired
    private BlogMapper blogMapper;

    private BlogSearch search;

    @Before
    public void setUp() {
        search = new BlogSearch();
        search.setTitle("abd");
        search.setDescr("descr");
        search.setActiveFlag("Y");
        search.setCreatedVia("Thread");
    }

    @Test
    public void testFind() {
        blogMapper.find(search);
    }

    @Test
    public void testFindPage() {
        Page<Blog> page = new Page<Blog>(1, 2);
        blogMapper.find(search, page);
    }

}
