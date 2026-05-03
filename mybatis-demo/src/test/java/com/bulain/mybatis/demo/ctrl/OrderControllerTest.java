package com.bulain.mybatis.demo.ctrl;

import com.bulain.mybatis.MybatisPlusApplication;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.demo.entity.Order;
import com.bulain.mybatis.demo.pojo.OrderSearch;
import com.bulain.mybatis.demo.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MybatisPlusApplication.class)
class OrderControllerTest {

    @Autowired
    private OrderController orderController;

    @Autowired
    private OrderService orderService;

    private String id;

    @BeforeEach
    void setup() {
        Order entity = new Order();
        entity.setOrderNo("T00001");
        entity.setExtnRefNo1("E00001");
        orderService.save(entity);
        id = entity.getId();
    }

    @Test
    void testGetById() {
        Order order = orderController.getById(id);
        assertNotNull(order);
        assertEquals("T00001", order.getOrderNo());
    }

    @Test
    void testList() {
        OrderSearch search = new OrderSearch();
        search.setOrderNo("T00001");
        List<Order> list = orderController.list(search);
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    void testPage() {
        OrderSearch search = new OrderSearch();
        search.setPage(1);
        search.setPageSize(10);
        search.setOrderNo("T00001");
        Paged<Order> page = orderController.page(search);
        assertNotNull(page);
        assertTrue(page.getTotalCount() >= 0);
    }

    @Test
    void testListByOrderNo() {
        OrderSearch search = new OrderSearch();
        search.setOrderNo("T00001");
        List<Order> list = orderController.list(search);
        assertNotNull(list);
        for (Order order : list) {
            assertTrue(order.getOrderNo().contains("T00001"));
        }
    }

    @Test
    void testListByExtnRefNo1() {
        OrderSearch search = new OrderSearch();
        search.setExtnRefNo1("E00001");
        List<Order> list = orderController.list(search);
        assertNotNull(list);
        for (Order order : list) {
            assertTrue(order.getExtnRefNo1().contains("E00001"));
        }
    }

    @Test
    void testSave() {
        Order entity = new Order();
        entity.setOrderNo("T00002");
        entity.setExtnRefNo1("E00002");
        boolean result = orderController.save(entity);
        assertTrue(result);
        assertNotNull(entity.getId());
    }

    @Test
    void testUpdate() {
        Order entity = orderService.getById(id);
        entity.setExtnRefNo1("UPDATED");
        boolean result = orderController.update(entity);
        assertTrue(result);
        Order updated = orderService.getById(id);
        assertEquals("UPDATED", updated.getExtnRefNo1());
    }

    @Test
    void testDelete() {
        boolean result = orderController.delete(id);
        assertTrue(result);
        Order deleted = orderService.getById(id);
        assertNull(deleted);
    }
}
