package com.bulain.mybatis.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.bulain.mybatis.MybatisPlusApplication;
import com.bulain.mybatis.demo.model.Order;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MybatisPlusApplication.class)
public class OrderServiceImplTest {

	@Autowired
	private OrderService orderService;

	private Long id;
	private Long version = 1L;

	@Before
	public void setup() {
		orderService.delete(new EntityWrapper<Order>());

		Order entity = new Order();
		entity.setOrderNo("X00001");
		entity.setExtnRefNo1("E00001");
		entity.setVersion(version);

		orderService.insert(entity);
		id = entity.getId();
	}

	@Test
	public void testInsert() {
		Order entity = new Order();
		entity.setOrderNo("T00001");
		entity.setExtnRefNo1("T00001");
		entity.setVersion(version);

		boolean bool = orderService.insert(entity);
		assertTrue(bool);
	}

	@Test
	public void testInsertAllColumn() {
		Order entity = new Order();
		entity.setOrderNo("T00001");
		entity.setExtnRefNo1("T00001");
		entity.setVersion(version);

		boolean bool = orderService.insertAllColumn(entity);
		assertTrue(bool);
	}

	@Test
	public void testInsertBatchListOfT() {
		
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.insertBatch(entityList);
		assertTrue(bool);
	}

	@Test
	public void testInsertBatchListOfTInt() {
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.insertBatch(entityList, 10);
		assertTrue(bool);
	}

	@Test
	public void testInsertOrUpdate() {
		Order entity = new Order();
		entity.setOrderNo("T00001");
		entity.setExtnRefNo1("T00001");
		entity.setVersion(version);
		
		boolean bool = orderService.insertOrUpdate(entity);
		assertTrue(bool);
	}

	@Test
	public void testInsertOrUpdateAllColumn() {
		Order entity = new Order();
		entity.setOrderNo("T00001");
		entity.setExtnRefNo1("T00001");
		entity.setVersion(version);
		
		boolean bool = orderService.insertOrUpdateAllColumn(entity);
		assertTrue(bool);
	}

	@Test
	public void testInsertOrUpdateBatchListOfT() {
		
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.insertOrUpdateBatch(entityList);
		assertTrue(bool);
	
	}

	@Test
	public void testInsertOrUpdateBatchListOfTInt() {
		
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.insertOrUpdateBatch(entityList, 10);
		assertTrue(bool);
	
	}

	@Test
	public void testInsertOrUpdateAllColumnBatchListOfT() {
		
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.insertOrUpdateAllColumnBatch(entityList);
		assertTrue(bool);
	
	}

	@Test
	public void testInsertOrUpdateAllColumnBatchListOfTInt() {
		
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.insertOrUpdateAllColumnBatch(entityList, 10);
		assertTrue(bool);
	
	}

	@Test
	public void testDeleteById() {
		boolean bool = orderService.deleteById(id);
		assertTrue(bool);
	}

	@Test
	public void testDeleteByMap() {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("ORDER_NO", "X00001");
		boolean bool = orderService.deleteByMap(columnMap);
		assertTrue(bool);
	}

	@Test
	public void testDelete() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		boolean bool = orderService.delete(new EntityWrapper<Order>(wrapper));
		assertTrue(bool);
	}

	@Test
	public void testDeleteBatchIds() {
		Collection<? extends Serializable> idList = Arrays.asList(new Long[]{id, 2L, 3L});
		boolean bool = orderService.deleteBatchIds(idList);
		assertTrue(bool);
	}

	@Test
	public void testUpdateById() {
		Order entity = new Order();
		entity.setId(id);
		entity.setOrderNo("X00001");
		entity.setExtnRefNo1("E00001");
		entity.setVersion(version);
		
		boolean bool = orderService.updateById(entity);
		assertTrue(bool);
	}

	@Test
	public void testUpdateAllColumnById() {
		Order entity = new Order();
		entity.setId(id);
		entity.setOrderNo("X00001");
		entity.setExtnRefNo1("E00001");
		entity.setVersion(version);
		
		boolean bool = orderService.updateAllColumnById(entity);
		assertTrue(bool);
	}

	@Test
	public void testUpdate() {
		Order entity = new Order();
		entity.setExtnRefNo1("E00002");

		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		wrapper.setVersion(version);
		boolean bool = orderService.update(entity, new EntityWrapper<Order>(wrapper));
		assertTrue(bool);
	}

	@Test
	public void testUpdateForSet() {
		String setStr = "CREATED_VIA='MANUAL'";

		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		boolean bool = orderService.updateForSet(setStr, new EntityWrapper<Order>(wrapper));
		assertTrue(bool);
	}

	@Test
	public void testUpdateBatchByIdListOfT() {
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
	public void testUpdateBatchByIdListOfTInt() {
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
	public void testUpdateAllColumnBatchByIdListOfT() {
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		boolean bool = orderService.updateAllColumnBatchById(entityList);
		assertTrue(bool);
	}

	@Test
	public void testUpdateAllColumnBatchByIdListOfTInt() {
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		boolean bool = orderService.updateAllColumnBatchById(entityList, 10);
		assertTrue(bool);
	}

	@Test
	public void testSelectById() {
		Order ret = orderService.selectById(id);
		assertNotNull(ret);
	}

	@Test
	public void testSelectBatchIds() {
		Collection<? extends Serializable> idList = Arrays.asList(new Long[]{id, 2L, 3L});
		List<Order> list = orderService.selectBatchIds(idList);
		assertEquals(1, list.size());
	}

	@Test
	public void testSelectByMap() {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("ORDER_NO", "X00001");
		List<Order> list = orderService.selectByMap(columnMap);
		assertEquals(1, list.size());
	}

	@Test
	public void testSelectOne() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		Order ret = orderService.selectOne(new EntityWrapper<Order>(wrapper));
		assertNotNull(ret);
	}

	@Test
	public void testSelectMap() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		Map<String, Object> ret = orderService.selectMap(new EntityWrapper<Order>(wrapper));
		assertNotNull(ret);
	}

	@Test
	public void testSelectObj() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		Object ret = orderService.selectObj(new EntityWrapper<Order>(wrapper));
		assertNotNull(ret);
	}

	@Test
	public void testSelectCount() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		int cnt = orderService.selectCount(new EntityWrapper<Order>(wrapper));
		assertEquals(1, cnt);
	}

	@Test
	public void testSelectList() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		List<Order> list = orderService.selectList(new EntityWrapper<Order>(wrapper));
		assertEquals(1, list.size());
	}

	@Test
	public void testSelectPagePageOfT() {
		Page<Order> page = new Page<Order>(0, 2);
		Page<Order> paged = orderService.selectPage(page);
		assertEquals(1L, paged.getTotal());
	}

	@Test
	public void testSelectMaps() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		List<Map<String, Object>> list = orderService.selectMaps(new EntityWrapper<Order>(wrapper));
		assertEquals(1, list.size());
	}

	@Test
	public void testSelectObjs() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		List<Object> list = orderService.selectObjs(new EntityWrapper<Order>(wrapper));
		assertEquals(1, list.size());
	}

	@Test
	public void testSelectMapsPage() {
		Page<Order> page = new Page<Order>(0, 2);
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		Page<Map<String, Object>> paged = orderService.selectMapsPage(page, new EntityWrapper<Order>(wrapper));
		assertEquals(1L, paged.getTotal());
	}

	@Test
	public void testSelectPagePageOfTWrapperOfT() {
		Page<Order> page = new Page<Order>(0, 2);
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		Page<Order> paged = orderService.selectPage(page, new EntityWrapper<Order>(wrapper));
		assertEquals(1L, paged.getTotal());
	}

}
