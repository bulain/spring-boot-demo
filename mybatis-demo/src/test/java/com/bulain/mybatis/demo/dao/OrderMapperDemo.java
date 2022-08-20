package com.bulain.mybatis.demo.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bulain.mybatis.MybatisPlusApplication;
import com.bulain.mybatis.demo.entity.Order;
import com.bulain.mybatis.demo.pojo.OrderSearch;
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
public class OrderMapperDemo {

    @Autowired
    private OrderMapper orderMapper;

    private String id;

    @BeforeEach
    public void setup() {
        orderMapper.deleteAll();

        Order entity = new Order();
        entity.setOrderNo("X00001");
        entity.setExtnRefNo1("E00001");

        orderMapper.insert(entity);
        id = entity.getId();
    }

    @Test
    public void testInsert() {
        Order entity = new Order();
        entity.setOrderNo("T00001");
        entity.setExtnRefNo1("T00001");

        Integer icnt = orderMapper.insert(entity);
        assertEquals(Integer.valueOf(1), icnt);
    }

    @Test
    public void testDeleteById() {
        Integer dcnt = orderMapper.deleteById(id);
        assertEquals(Integer.valueOf(1), dcnt);
    }

    @Test
    public void testDeleteByMap() {
        Map<String, Object> columnMap = new HashMap<String, Object>();
        columnMap.put("ORDER_NO", "X00001");
        Integer dcnt = orderMapper.deleteByMap(columnMap);
        assertEquals(Integer.valueOf(1), dcnt);
    }

    @Test
    public void testDelete() {
        Order wrapper = new Order();
        wrapper.setOrderNo("X00001");

        Integer dcnt = orderMapper.delete(new QueryWrapper<Order>(wrapper));
        assertEquals(Integer.valueOf(1), dcnt);
    }

    @Test
    public void testDeleteBatchIds() {
        Collection<? extends Serializable> idList = Arrays.asList(new String[]{id, "2", "3"});
        Integer dcnt = orderMapper.deleteBatchIds(idList);
        assertEquals(Integer.valueOf(1), dcnt);
    }

    @Test
    public void testUpdateById() {
        Order entity = new Order();
        entity.setId(id);
        entity.setOrderNo("X00001");
        entity.setExtnRefNo1("E00001");
        entity.setVersion(0L);

        Integer ucnt = orderMapper.updateById(entity);
        assertEquals(Integer.valueOf(1), ucnt);
    }

    @Test
    public void testUpdate() {

        Order entity = new Order();
        entity.setExtnRefNo1("E00002");

        Order wrapper = new Order();
        wrapper.setOrderNo("X00001");
        //wrapper.setVersion(version);
        Integer ucnt = orderMapper.update(entity, new QueryWrapper<Order>(wrapper));
        assertEquals(Integer.valueOf(1), ucnt);
    }

    @Test
    public void testSelectById() {
        Order ret = orderMapper.selectById(id);
        assertNotNull(ret);
    }

    @Test
    public void testSelectBatchIds() {
        Collection<? extends Serializable> idList = Arrays.asList(new String[]{id, "2", "3"});
        List<Order> list = orderMapper.selectBatchIds(idList);
        assertEquals(1, list.size());
    }

    @Test
    public void testSelectByMap() {
        Map<String, Object> columnMap = new HashMap<String, Object>();
        columnMap.put("ORDER_NO", "X00001");
        List<Order> list = orderMapper.selectByMap(columnMap);
        assertEquals(1, list.size());
    }

    @Test
    public void testSelectOne() {
        Order entity = new Order();
        entity.setOrderNo("X00001");
        Order ret = orderMapper.selectOne(new QueryWrapper<Order>(entity));
        assertNotNull(ret);
    }

    @Test
    public void testSelectCount() {
        Order entity = new Order();
        entity.setOrderNo("X00001");
        Long cnt = orderMapper.selectCount(new QueryWrapper<Order>(entity));
        assertEquals(Long.valueOf(1), cnt);
    }

    @Test
    public void testSelectList() {
        Order entity = new Order();
        entity.setOrderNo("X00001");
        List<Order> list = orderMapper.selectList(new QueryWrapper<Order>(entity));
        assertEquals(1, list.size());
    }

    @Test
    public void testSelectMaps() {
        Order entity = new Order();
        entity.setOrderNo("X00001");
        List<Map<String, Object>> list = orderMapper.selectMaps(new QueryWrapper<Order>(entity));
        assertEquals(1, list.size());
    }

    @Test
    public void testSelectObjs() {
        Order entity = new Order();
        entity.setOrderNo("X00001");
        List<Object> list = orderMapper.selectObjs(new QueryWrapper<Order>(entity));
        assertEquals(1, list.size());
    }

    @Test
    public void testSelectPage() {
        IPage<Order> page = new Page<Order>(1, 2);
        Order entity = new Order();
        entity.setOrderNo("X00001");
        IPage<Order> paged = orderMapper.selectPage(page, new QueryWrapper<Order>(entity));
        assertEquals(1, paged.getTotal());
    }

    @Test
    public void testSelectMapsPage() {
        IPage<Map<String, Object>> page = new Page<>(1, 2);
        Order entity = new Order();
        entity.setOrderNo("X00001");
        IPage<Map<String, Object>> paged = orderMapper.selectMapsPage(page, new QueryWrapper<Order>(entity));
        assertEquals(1, paged.getTotal());

        page = new Page<>(1, 2);
        paged = orderMapper.selectMapsPage(page, new QueryWrapper<Order>(entity));
        assertEquals(1, paged.getTotal());
    }

    @Test
    public void testFind() {
        OrderSearch search = new OrderSearch();
        search.setOrderNo("X00001");
        List<Order> list = orderMapper.find(search);
        assertEquals(1, list.size());

        search = new OrderSearch();
        search.setOrderNo("X00001");
        list = orderMapper.find(search);
        assertEquals(1, list.size());
    }

    @Test
    public void testFindWithPage() {
        IPage<Order> page = new Page<Order>(1, 2);
        OrderSearch search = new OrderSearch();
        search.setOrderNo("X00001");
        IPage<Order> paged = orderMapper.find(page, search);
        assertEquals(1, paged.getTotal());

        page = new Page<Order>(1, 2);
        search = new OrderSearch();
        search.setOrderNo("X00001");
        paged = orderMapper.find(page, search);
        assertEquals(1, paged.getTotal());
    }

    @Test
    public void testUpdateToNull() {
        Order entity = new Order();
        entity.setExtnRefNo1("E00002");

        Order wrapper = new Order();
        wrapper.setOrderNo("X00001");
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<Order>(wrapper);
        updateWrapper.lambda().set(Order::getExtnRefNo2, null);
        orderMapper.update(entity, updateWrapper);
    }

    @Test
    public void testUpdateToLogic() {
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<Order>()
                .set("deleted", "1")
                .set("version", SystemClock.now())
                .eq("id", id);
        orderMapper.update(null, updateWrapper);
    }

}
