package com.bulain.mybatis.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bulain.mybatis.MybatisPlusApplication;
import com.bulain.mybatis.demo.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.Serializable;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MybatisPlusApplication.class)
class OrderServiceImplTest {

	@Autowired
	private OrderService orderService;

	private final Long version = 0L;
	private String id;

	@BeforeEach
	void setup() {
        orderService.directRemove(new QueryWrapper<>());

		Order entity = new Order();
		entity.setOrderNo("X00001");
		entity.setExtnRefNo1("E00001");

		orderService.save(entity);
		id = entity.getId();
	}

	@Test
	void testSave() {
		Order entity = new Order();
		entity.setOrderNo("T00001");
		entity.setExtnRefNo1("T00001");

		boolean bool = orderService.save(entity);
		assertTrue(bool);
	}

	@Test
	void testSaveAllColumn() {
		Order entity = new Order();
		entity.setOrderNo("T00001");
		entity.setExtnRefNo1("T00001");

		boolean bool = orderService.save(entity);
		assertTrue(bool);
	}

	@Test
	void testSaveBatchListOfT() {
		
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.saveBatch(entityList);
		assertTrue(bool);
	}

	@Test
	void testSaveBatchListOfTInt() {
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.saveBatch(entityList, 10);
		assertTrue(bool);
	}

	@Test
	void testSaveOrUpdate() {
	    orderService.getById(id);
	    
		Order entity = new Order();
		entity.setOrderNo("T00001");
		entity.setExtnRefNo1("T00001");
		
		boolean bool = orderService.saveOrUpdate(entity);
		assertTrue(bool);
		
		orderService.getById(id);
	}

	@Test
	void testSaveOrUpdateAllColumn() {
		Order entity = new Order();
		entity.setOrderNo("T00001");
		entity.setExtnRefNo1("T00001");
		
		boolean bool = orderService.saveOrUpdate(entity);
		assertTrue(bool);
	}

	@Test
	void testSaveOrUpdateBatchListOfT() {
	    
	    orderService.getById(id);
	    
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.saveOrUpdateBatch(entityList);
		assertTrue(bool);
	
		orderService.getById(id);
		
	}

	@Test
	void testSaveOrUpdateBatchListOfTInt() {
		
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.saveOrUpdateBatch(entityList, 10);
		assertTrue(bool);
	
	}

	@Test
	void testSaveOrUpdateAllColumnBatchListOfT() {
		
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.saveOrUpdateBatch(entityList);
		assertTrue(bool);
	
	}

	@Test
	void testSaveOrUpdateAllColumnBatchListOfTInt() {
		
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.saveOrUpdateBatch(entityList, 10);
		assertTrue(bool);
	
	}

	@Test
	void testRemoveById() {
		boolean bool = orderService.removeById(id);
		assertTrue(bool);
	}

	@Test
	void testRemoveByMap() {
		Map<String, Object> columnMap = new HashMap<>();
		columnMap.put("ORDER_NO", "X00001");
		boolean bool = orderService.removeByMap(columnMap);
		assertTrue(bool);
	}

	@Test
	void testRemove() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		boolean bool = orderService.remove(new QueryWrapper<>(wrapper));
		assertTrue(bool);
	}

	@Test
	void testRemoveBatchIds() {
		Collection<? extends Serializable> idList = Arrays.asList(id, "2", "3");
		boolean bool = orderService.removeByIds(idList);
		assertTrue(bool);
	}

	@Test
	void testUpdateById() {
		Order entity = new Order();
		entity.setId(id);
		entity.setOrderNo("X00001");
		entity.setExtnRefNo1("E00001");
		entity.setVersion(version);
		
		boolean bool = orderService.updateById(entity);
		assertTrue(bool);
	}

	@Test
	void testUpdateAllColumnById() {
		Order entity = new Order();
		entity.setId(id);
		entity.setOrderNo("X00001");
		entity.setExtnRefNo1("E00001");
		entity.setVersion(version);
		
		boolean bool = orderService.updateById(entity);
		assertTrue(bool);
	}

	@Test
	void testUpdate() {
		Order entity = new Order();
		entity.setExtnRefNo1("E00002");

		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		wrapper.setVersion(version);
		boolean bool = orderService.update(entity, new QueryWrapper<>(wrapper));
		assertTrue(bool);
	}

	@Test
	void testUpdateBatchByIdListOfT() {
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		boolean bool = orderService.updateBatchById(entityList);
		assertTrue(bool);
	}

	@Test
	void testUpdateBatchByIdListOfTInt() {
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		boolean bool = orderService.updateBatchById(entityList, 10);
		assertTrue(bool);
	}

	@Test
	void testUpdateAllColumnBatchByIdListOfT() {
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		boolean bool = orderService.updateBatchById(entityList);
		assertTrue(bool);
	}

	@Test
	void testUpdateAllColumnBatchByIdListOfTInt() {
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		boolean bool = orderService.updateBatchById(entityList, 10);
		assertTrue(bool);
	}

	@Test
	void testSelectById() {
		Order ret = orderService.getById(id);
		assertNotNull(ret);
	}

	@Test
	void testSelectBatchIds() {
		Collection<? extends Serializable> idList = Arrays.asList(id, "2", "3");
		Collection<Order> list = orderService.listByIds(idList);
		assertEquals(1, list.size());
	}

	@Test
	void testSelectByMap() {
		Map<String, Object> columnMap = new HashMap<>();
		columnMap.put("ORDER_NO", "X00001");
		Collection<Order> list = orderService.listByMap(columnMap);
		assertEquals(1, list.size());
	}

	@Test
	void testSelectOne() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		Order ret = orderService.getOne(new QueryWrapper<>(wrapper));
		assertNotNull(ret);
	}

	@Test
	void testSelectMap() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		Map<String, Object> ret = orderService.getMap(new QueryWrapper<>(wrapper));
		assertNotNull(ret);
	}

	@Test
	void testSelectCount() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		long cnt = orderService.count(new QueryWrapper<>(wrapper));
		assertEquals(1, cnt);
	}

	@Test
	void testSelectList() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		List<Order> list = orderService.list(new QueryWrapper<>(wrapper));
		assertEquals(1, list.size());
	}

	@Test
	void testSelectPagePageOfT() {
		IPage<Order> page = new Page<>(0, 2);
		IPage<Order> paged = orderService.page(page);
		assertEquals(1L, paged.getTotal());
	}

	@Test
	void testSelectMaps() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		List<Map<String, Object>> list = orderService.listMaps(new QueryWrapper<>(wrapper));
		assertEquals(1, list.size());
	}

	@Test
	void testSelectObjs() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		List<Object> list = orderService.listObjs(new QueryWrapper<>(wrapper));
		assertEquals(1, list.size());
	}

	@Test
	void testSelectMapsPage() {
		IPage<Map<String, Object>> page = new Page<>(0, 2);
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		IPage<Map<String, Object>> paged = orderService.pageMaps(page, new QueryWrapper<>(wrapper));
		assertEquals(1L, paged.getTotal());
	}

	@Test
	void testSelectPagePageOfTWrapperOfT() {
		IPage<Order> page = new Page<>(0, 2);
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		IPage<Order> paged = orderService.page(page, new QueryWrapper<>(wrapper));
		assertEquals(1L, paged.getTotal());
	}

}
