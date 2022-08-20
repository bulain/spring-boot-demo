package com.bulain.mybatis.demo.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bulain.mybatis.MybatisPlusApplication;
import com.bulain.mybatis.demo.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.Serializable;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MybatisPlusApplication.class)
public class BasicMapperDemo {

    @Autowired
    private OrderMapper orderMapper;

    private String id;

    @BeforeEach
    public void setup() {
        orderMapper.directDelete(new QueryWrapper<>());

        Order entity = new Order();
        entity.setOrderNo("X00001");
        entity.setExtnRefNo1("E00001");

        orderMapper.insert(entity);
        id = entity.getId();
    }

    @Test
    public void testDirectDeleteById() {
        Integer dcnt = orderMapper.directDeleteById(id);
        assertEquals(Integer.valueOf(1), dcnt);
    }

    @Test
    public void testDirectDeleteByIdEntity() {
        Order wrapper = new Order();
        wrapper.setId(id);
        Integer dcnt = orderMapper.directDeleteById(wrapper);
        assertEquals(Integer.valueOf(1), dcnt);
    }

    @Test
    public void testDirectDelete() {
        Order wrapper = new Order();
        wrapper.setOrderNo("X00001");

        Integer dcnt = orderMapper.directDelete(new QueryWrapper<Order>(wrapper));
        assertEquals(Integer.valueOf(1), dcnt);
    }

    @Test
    public void testDirectDeleteBatchIds() {
        Collection<? extends Serializable> idList = Arrays.asList(new String[]{id, "2", "3"});
        Integer dcnt = orderMapper.directDeleteBatchIds(idList);
        assertEquals(Integer.valueOf(1), dcnt);
    }

    @Test
    public void testDirectUpdateById() {
        Order entity = new Order();
        entity.setId(id);
        entity.setOrderNo("X00001");
        entity.setExtnRefNo1("E00001");
        entity.setVersion(0L);

        Integer ucnt = orderMapper.directUpdateById(entity);
        assertEquals(Integer.valueOf(1), ucnt);
    }

    @Test
    public void testDirectUpdate() {
        Order entity = new Order();
        entity.setExtnRefNo1("E00002");

        Order wrapper = new Order();
        wrapper.setOrderNo("X00001");
        //wrapper.setVersion(version);
        Integer ucnt = orderMapper.directUpdate(entity, new QueryWrapper<Order>(wrapper));
        assertEquals(Integer.valueOf(1), ucnt);
    }

    @Test
    public void testDirectSelectById() {
        Order ret = orderMapper.directSelectById(id);
        assertNotNull(ret);
    }

    @Test
    public void testDirectSelectBatchIds() {
        Collection<? extends Serializable> idList = Arrays.asList(new String[]{id, "2", "3"});
        List<Order> list = orderMapper.directSelectBatchIds(idList);
        assertEquals(1, list.size());
    }

    @Test
    public void testDirectSelectOne() {
        Order entity = new Order();
        entity.setOrderNo("X00001");
        Order ret = orderMapper.directSelectOne(new QueryWrapper<Order>(entity));
        assertNotNull(ret);
    }

    @Test
    public void testDirectSelectCount() {
        Order entity = new Order();
        entity.setOrderNo("X00001");
        Long cnt = orderMapper.directSelectCount(new QueryWrapper<Order>(entity));
        assertEquals(Long.valueOf(1), cnt);
    }

    @Test
    public void testDirectSelectList() {
        Order entity = new Order();
        entity.setOrderNo("X00001");
        List<Order> list = orderMapper.directSelectList(new QueryWrapper<Order>(entity));
        assertEquals(1, list.size());
    }

    @Test
    public void testDirectSelectMaps() {
        Order entity = new Order();
        entity.setOrderNo("X00001");
        List<Map<String, Object>> list = orderMapper.directSelectMaps(new QueryWrapper<Order>(entity));
        assertEquals(1, list.size());
    }

    @Test
    public void testDirectSelectPage() {
        IPage<Order> page = new Page<Order>(1, 2);
        Order entity = new Order();
        entity.setOrderNo("X00001");
        IPage<Order> paged = orderMapper.directSelectPage(page, new QueryWrapper<Order>(entity));
        assertEquals(1, paged.getTotal());
    }

    @Test
    public void testDirectSelectMapsPage() {
        IPage<Map<String, Object>> page = new Page<>(1, 2);
        Order entity = new Order();
        entity.setOrderNo("X00001");
        IPage<Map<String, Object>> paged = orderMapper.directSelectMapsPage(page, new QueryWrapper<Order>(entity));
        assertEquals(1, paged.getTotal());
    }

}
