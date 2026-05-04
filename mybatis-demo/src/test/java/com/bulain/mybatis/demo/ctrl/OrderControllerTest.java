package com.bulain.mybatis.demo.ctrl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.demo.entity.Order;
import com.bulain.mybatis.demo.pojo.OrderSearch;
import com.bulain.mybatis.demo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * OrderController 单元测试
 * 使用 Mockito 模拟依赖，无需 Spring 上下文和数据库
 */
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    void testGetById() {
        // 准备测试数据
        String orderId = "test-id-001";
        Order expectedOrder = new Order();
        expectedOrder.setId(orderId);
        expectedOrder.setOrderNo("TEST001");

        // stub
        when(orderService.getById(orderId)).thenReturn(expectedOrder);

        // 执行测试
        Order result = orderController.getById(orderId);

        // 验证结果
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals("TEST001", result.getOrderNo());
        verify(orderService, times(1)).getById(orderId);
    }

    @Test
    void testList() {
        // 准备测试数据
        Order order1 = new Order();
        order1.setId("id1");
        order1.setOrderNo("TEST001");

        Order order2 = new Order();
        order2.setId("id2");
        order2.setOrderNo("TEST002");

        List<Order> expectedList = Arrays.asList(order1, order2);

        // stub
        when(orderService.list(any(LambdaQueryWrapper.class))).thenReturn(expectedList);

        // 执行测试
        OrderSearch search = new OrderSearch();
        List<Order> result = orderController.list(search);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("TEST001", result.get(0).getOrderNo());
        verify(orderService, times(1)).list(any(LambdaQueryWrapper.class));
    }

    @Test
    void testPage() {
        // 准备测试数据
        Order order1 = new Order();
        order1.setOrderNo("TEST001");

        List<Order> expectedList = Arrays.asList(order1);

        // stub
        when(orderService.page(any(IPage.class), any(LambdaQueryWrapper.class))).thenAnswer(invocation -> {
            IPage<Order> page = invocation.getArgument(0);
            page.setRecords(expectedList);
            page.setTotal(1);
            return page;
        });

        // 执行测试
        OrderSearch search = new OrderSearch();
        search.setPage(1);
        search.setPageSize(10);
        Paged<Order> result = orderController.page(search);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotalCount());
        assertEquals(1, result.getData().size());
        assertEquals("TEST001", result.getData().get(0).getOrderNo());
        verify(orderService, times(1)).page(any(IPage.class), any(LambdaQueryWrapper.class));
    }

    @Test
    void testListByOrderNo() {
        // 准备测试数据
        Order order = new Order();
        order.setOrderNo("TEST001");
        List<Order> expectedList = Arrays.asList(order);

        // stub
        when(orderService.list(any(LambdaQueryWrapper.class))).thenReturn(expectedList);

        // 执行测试
        OrderSearch search = new OrderSearch();
        search.setOrderNo("TEST001");
        List<Order> result = orderController.list(search);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
        for (Order o : result) {
            assertTrue(o.getOrderNo().contains("TEST001"));
        }

        // 使用 ArgumentCaptor 验证查询条件
        ArgumentCaptor<LambdaQueryWrapper<Order>> captor = ArgumentCaptor.forClass(LambdaQueryWrapper.class);
        verify(orderService).list(captor.capture());
        LambdaQueryWrapper<Order> capturedWrapper = captor.getValue();
        assertNotNull(capturedWrapper);
    }

    @Test
    void testListByExtnRefNo1() {
        // 准备测试数据
        Order order = new Order();
        order.setExtnRefNo1("REF001");
        List<Order> expectedList = Arrays.asList(order);

        // stub
        when(orderService.list(any(LambdaQueryWrapper.class))).thenReturn(expectedList);

        // 执行测试
        OrderSearch search = new OrderSearch();
        search.setExtnRefNo1("REF001");
        List<Order> result = orderController.list(search);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
        for (Order o : result) {
            assertTrue(o.getExtnRefNo1().contains("REF001"));
        }

        // 使用 ArgumentCaptor 验证查询条件
        ArgumentCaptor<LambdaQueryWrapper<Order>> captor = ArgumentCaptor.forClass(LambdaQueryWrapper.class);
        verify(orderService).list(captor.capture());
        LambdaQueryWrapper<Order> capturedWrapper = captor.getValue();
        assertNotNull(capturedWrapper);
    }

    @Test
    void testSave() {
        // 准备测试数据
        Order entity = new Order();
        entity.setOrderNo("TEST001");
        entity.setExtnRefNo1("REF001");

        // stub
        when(orderService.save(any(Order.class))).thenReturn(true);

        // 执行测试
        boolean result = orderController.save(entity);

        // 验证结果
        assertTrue(result);

        // 使用 ArgumentCaptor 验证传入参数
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderService, times(1)).save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertEquals("TEST001", capturedOrder.getOrderNo());
        assertEquals("REF001", capturedOrder.getExtnRefNo1());
    }

    @Test
    void testUpdate() {
        // 准备测试数据
        Order entity = new Order();
        entity.setId("test-id-001");
        entity.setOrderNo("TEST001");
        entity.setExtnRefNo1("UPDATED");

        // stub
        when(orderService.updateById(any(Order.class))).thenReturn(true);

        // 执行测试
        boolean result = orderController.update(entity);

        // 验证结果
        assertTrue(result);

        // 使用 ArgumentCaptor 验证传入参数
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderService, times(1)).updateById(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertEquals("test-id-001", capturedOrder.getId());
        assertEquals("UPDATED", capturedOrder.getExtnRefNo1());
    }

    @Test
    void testDelete() {
        // 准备测试数据
        String orderId = "test-id-001";

        // stub
        when(orderService.removeById(orderId)).thenReturn(true);

        // 执行测试
        boolean result = orderController.delete(orderId);

        // 验证结果
        assertTrue(result);
        verify(orderService, times(1)).removeById(orderId);
    }
}
