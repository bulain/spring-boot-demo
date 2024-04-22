package com.bulain.mybatis.demo.service;

import com.bulain.mybatis.MybatisPlusApplication;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.demo.entity.Blog;
import com.bulain.mybatis.demo.pojo.BlogSearch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MybatisPlusApplication.class)
class BlogServiceImplTest {

    @Autowired
    private BlogService blogService;

    private BlogSearch search;

    @BeforeEach
    void setUp() {
        search = new BlogSearch();
        search.setTitle("abd");
        search.setDescr("descr");
        search.setActiveFlag("Y");
        search.setCreatedVia("Thread");
    }

    @Test
    void testInsert() {
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
    void testUpdates() {
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
    void testFind() {
        search.addOrderBy("id", "asc");
        List<Blog> list = blogService.find(search);
        Assertions.assertNotNull(list);
    }

    @Test
    void testPage() {
        search.setPage(3);
        search.setPageSize(2);
        search.addOrderBy("id", "asc");
        Paged<Blog> paged = blogService.page(search);
        Assertions.assertNotNull(paged);
    }

    @Test
    void testSaveBatch() {
        //初始化环境
        testInsert();

        //准备数据
        Collection<Blog> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            Blog data = new Blog();
            data.setTitle("abd-" + i);
            data.setDescr("descr-" + i);
            data.setCreatedVia("Thread-" + i);
            data.setActiveFlag("Y");
            data.setCreatedAt(LocalDateTime.now());
            data.setCreatedBy("PT");
            list.add(data);
        }

        StopWatch stopWatch = StopWatch.createStarted();

        //执行保存
        blogService.saveBatch(list);

        stopWatch.stop();
        log.info("execute time: {}ms", stopWatch.getTime());

    }

}
