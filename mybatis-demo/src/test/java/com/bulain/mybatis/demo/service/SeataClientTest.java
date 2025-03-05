//package com.bulain.mybatis.demo.service;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.bulain.mybatis.MybatisPlusApplication;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import javax.sql.DataSource;
//
//@Slf4j
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = MybatisPlusApplication.class)
//class SeataClientTest {
//
//    @Autowired
//    private DataSource dataSource;
//    @Autowired
//    private SeataClient seataClient;
//    @Autowired
//    private BlogService blogService;
//    @Autowired
//    private OrderService orderService;
//
//    @BeforeEach
//    void setup() {
//        blogService.directRemove(new QueryWrapper<>());
//        orderService.directRemove(new QueryWrapper<>());
//    }
//
//    @SneakyThrows
//    @AfterAll
//    static void afterAll() {
//        Thread.sleep(5000L);
//    }
//
//    @Test
//    void testDataSource() {
//        log.debug("{}", dataSource.getClass().getName());
//    }
//
//    @Test
//    void testTranfer() {
//        seataClient.tranfer();
//    }
//
//    @Test
//    void testManual() {
//        seataClient.manual();
//    }
//
//
//}
