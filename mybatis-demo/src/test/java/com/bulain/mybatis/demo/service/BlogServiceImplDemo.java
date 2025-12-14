package com.bulain.mybatis.demo.service;

import com.bulain.mybatis.MybatisPlusApplication;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.demo.entity.Blog;
import com.bulain.mybatis.demo.pojo.BlogSearch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MybatisPlusApplication.class)
public class BlogServiceImplDemo {

    @Autowired
    private BlogService blogService;

    private BlogSearch search;

    @BeforeEach
    public void setUp() {
        search = new BlogSearch();
        search.setTitle("abd");
        search.setDescr("descr");
        search.setActiveFlag("Y");
        search.setCreatedVia("Thread");
    }

    @Test
    public void testInsert() {
        Blog data = new Blog();
        data.setTitle("abd");
        data.setDescr("descr");
        data.setCreatedVia("Thread");
        data.setActiveFlag("Y");
        data.setCreatedAt(LocalDateTime.now());
        data.setCreatedBy("PT");
        blogService.save(data);
    }

    @Test
    public void testUpdates() {
        Blog data = new Blog();
        data.setId("1");
        data.setTitle("abd");
        data.setDescr("descr");
        data.setCreatedVia("Thread");
        data.setActiveFlag("Y");
        data.setCreatedAt(LocalDateTime.now());
        data.setCreatedBy("PT");
        blogService.updateById(data);
    }

    @Test
    public void testFind() {
        search.addOrderBy("id", "asc");
        List<Blog> list = blogService.find(search);
        Assertions.assertNotNull(list);
    }

    @Test
    public void testPage() {
        search.setPage(3);
        search.setPageSize(2);
        search.addOrderBy("id", "asc");
        Paged<Blog> paged = blogService.page(search);
        Assertions.assertNotNull(paged);
    }

}
