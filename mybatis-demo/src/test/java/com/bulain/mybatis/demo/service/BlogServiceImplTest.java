package com.bulain.mybatis.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import java.util.Random;

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
    void testQuery() {
        LambdaQueryWrapper<Blog> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.and(qw -> qw.and(ew -> ew.eq(Blog::getActiveFlag, "Y").eq(Blog::getDescr, "descr"))
                        .or(ew -> ew.eq(Blog::getTitle, "abd").eq(Blog::getDescr, "descr")))
                .exists("select 1 from demo_order x where x.order_no={0}", new Object[]{"X00001"});

        log.info("{}",queryWrapper.getParamAlias());

        List<Blog> list = blogService.list(queryWrapper);
        Assertions.assertNotNull(list);
    }

    @Test
    void testFind() {
        search.addOrderBy("id", "asc");
        List<Blog> list = blogService.find(search);
        Assertions.assertNotNull(list);
    }

    @Test
    void testPage() {
        search.setPage(1);
        search.setPageSize(2);
        search.addOrderBy("id", "asc");
        Paged<Blog> paged = blogService.page(search);
        Assertions.assertNotNull(paged);
    }

    @Test
    void testSaveBatch() {
        //初始化环境
        testInsert();

        LocalDateTime now = LocalDateTime.now();

        Random random = new Random();
        int idx = 0;
        int size = 100000;
        for (int i = 0; i < 50; i++) {
            //准备数据
            Collection<Blog> list = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                idx = i * size + j;

                LocalDateTime createdAt = now.plusSeconds(idx);
                LocalDateTime updatedAt = createdAt;
                int rint = random.nextInt() % 1000;
                boolean b = rint == 5;
                if (b) {
                    updatedAt = createdAt.plusMinutes(rint);
                }

                Blog data = new Blog();
                data.setTitle("abd-" + idx);
                data.setDescr("descr-" + idx);
                data.setCreatedVia("Thread-" + idx);
                data.setActiveFlag("Y");
                data.setCreatedAt(createdAt);
                data.setCreatedBy("PT");
                data.setUpdatedAt(updatedAt);
                data.setUpdatedBy("PT");
                list.add(data);
            }

            StopWatch stopWatch = StopWatch.createStarted();

            //执行保存
            blogService.saveBatch(list);

            stopWatch.stop();
            log.info("execute time: {}ms @{}", stopWatch.getTime(), i);
        }
    }

}
