package com.bulain.mybatis.demo.service;

import com.bulain.mybatis.demo.entity.Blog;
import com.bulain.mybatis.demo.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.apache.seata.tm.api.GlobalTransaction;
import org.apache.seata.tm.api.GlobalTransactionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SeataClient {
    private final BlogService blogService;
    private final OrderService orderService;

    @GlobalTransactional
    public void tranfer() {

        Blog data = new Blog();
        data.setTitle("abd");
        data.setDescr("descr");
        data.setCreatedVia("Thread");
        data.setActiveFlag("Y");
        data.setCreatedAt(LocalDateTime.now());
        data.setCreatedBy("PT");
        blogService.save(data);

        Order entity1 = new Order();
        entity1.setOrderNo("T00001");
        entity1.setExtnRefNo1("T00001");
        entity1.setVersion(1L);
        orderService.save(entity1);

    }

    @SneakyThrows
    public void manual() {
        GlobalTransaction current = GlobalTransactionContext.getCurrentOrCreate();
        current.begin();
        try {
            Blog data = new Blog();
            data.setTitle("abd");
            data.setDescr("descr");
            data.setCreatedVia("Thread");
            data.setActiveFlag("Y");
            data.setCreatedAt(LocalDateTime.now());
            data.setCreatedBy("PT");
            blogService.save(data);

            Order entity1 = new Order();
            entity1.setOrderNo("T00001");
            entity1.setExtnRefNo1("T00001");
            entity1.setVersion(1L);
            orderService.save(entity1);
            current.commit();
        } catch (Exception e) {
            current.rollback();
            throw e;
        }
    }

}
