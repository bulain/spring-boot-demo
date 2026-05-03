package com.bulain.mybatis.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bulain.mybatis.demo.dao.OrderMapper;
import com.bulain.mybatis.demo.entity.Order;
import com.bulain.mybatis.demo.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * OrderServiceImpl 单元测试
 * 使用 Mockito 模拟依赖，无需 Spring 上下文和数据库
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderMapper orderMapper;

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl();
        // 使用反射注入 baseMapper（ServiceImpl 基类中的字段）
        ReflectionTestUtils.setField(orderService, "baseMapper", orderMapper);
    }

    @Test
    void testSave() {
        // 准备测试数据
        Order order = new Order();
        order.setOrderNo("TEST001");
        order.setExtnRefNo1("REF001");

        // stub: 当调用 insert 时返回 1（成功插入 1 条记录）
        when(orderMapper.insert(any(Order.class))).thenReturn(1);

        // 执行测试
        boolean result = orderService.save(order);

        // 验证结果
        assertTrue(result);
        // 验证 insert 被调用 1 次
        verify(orderMapper, times(1)).insert(any(Order.class));

        // 使用 ArgumentCaptor 捕获实际传入的参数
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderMapper).insert(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertEquals("TEST001", capturedOrder.getOrderNo());
        assertEquals("REF001", capturedOrder.getExtnRefNo1());
    }

    @Test
    void testRemoveById() {
        // 准备测试数据
        String orderId = "test-id-001";

        // stub: 逻辑删除通过 update 实现，返回 1 表示成功
        // 注意：ServiceImpl.removeById 内部使用的是 UpdateWrapper 而非 LambdaQueryWrapper
        when(orderMapper.update(any(), any())).thenReturn(1);

        // 执行测试
        boolean result = orderService.removeById(orderId);

        // 验证结果
        assertTrue(result);
        // 验证 update 被调用 1 次
        verify(orderMapper, times(1)).update(any(), any());
    }

    @Test
    void testSelectById() {
        // 准备测试数据
        String orderId = "test-id-001";
        Order expectedOrder = new Order();
        expectedOrder.setId(orderId);
        expectedOrder.setOrderNo("TEST001");
        expectedOrder.setExtnRefNo1("REF001");

        // stub: 当调用 selectById 时返回预设对象
        when(orderMapper.selectById(orderId)).thenReturn(expectedOrder);

        // 执行测试
        Order actualOrder = orderService.getById(orderId);

        // 验证结果
        assertNotNull(actualOrder);
        assertEquals(orderId, actualOrder.getId());
        assertEquals("TEST001", actualOrder.getOrderNo());
        assertEquals("REF001", actualOrder.getExtnRefNo1());
        // 验证 selectById 被调用 1 次
        verify(orderMapper, times(1)).selectById(orderId);
    }

    @Test
    void testSelectList() {
        // 准备测试数据
        Order order1 = new Order();
        order1.setId("id1");
        order1.setOrderNo("TEST001");

        Order order2 = new Order();
        order2.setId("id2");
        order2.setOrderNo("TEST002");

        List<Order> expectedList = Arrays.asList(order1, order2);

        // stub: 当调用 selectList 时返回预设列表
        when(orderMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(expectedList);

        // 执行测试
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        List<Order> actualList = orderService.list(wrapper);

        // 验证结果
        assertNotNull(actualList);
        assertEquals(2, actualList.size());
        assertEquals("TEST001", actualList.get(0).getOrderNo());
        assertEquals("TEST002", actualList.get(1).getOrderNo());
        // 验证 selectList 被调用 1 次
        verify(orderMapper, times(1)).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    void testDirectRemove() {
        // 准备测试数据
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getOrderNo, "TEST001");

        // stub: 当调用 directDelete 时返回 1（成功删除 1 条记录）
        when(orderMapper.directDelete(any(LambdaQueryWrapper.class))).thenReturn(1);

        // 执行测试
        boolean result = orderService.directRemove(wrapper);

        // 验证结果
        assertTrue(result);
        // 验证 directDelete 被调用 1 次
        verify(orderMapper, times(1)).directDelete(any(LambdaQueryWrapper.class));
    }

}
