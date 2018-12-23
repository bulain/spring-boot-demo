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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
		orderService.remove(new QueryWrapper<Order>());

		Order entity = new Order();
		entity.setOrderNo("X00001");
		entity.setExtnRefNo1("E00001");
		entity.setVersion(version);

		orderService.save(entity);
		id = entity.getId();
	}

	@Test
	public void testsave() {
		Order entity = new Order();
		entity.setOrderNo("T00001");
		entity.setExtnRefNo1("T00001");
		entity.setVersion(version);

		boolean bool = orderService.save(entity);
		assertTrue(bool);
	}

	@Test
	public void testsaveAllColumn() {
		Order entity = new Order();
		entity.setOrderNo("T00001");
		entity.setExtnRefNo1("T00001");
		entity.setVersion(version);

		boolean bool = orderService.save(entity);
		assertTrue(bool);
	}

	@Test
	public void testsaveBatchListOfT() {
		
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.saveBatch(entityList);
		assertTrue(bool);
	}

	@Test
	public void testsaveBatchListOfTInt() {
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.saveBatch(entityList, 10);
		assertTrue(bool);
	}

	@Test
	public void testsaveOrUpdate() {
		Order entity = new Order();
		entity.setOrderNo("T00001");
		entity.setExtnRefNo1("T00001");
		entity.setVersion(version);
		
		boolean bool = orderService.saveOrUpdate(entity);
		assertTrue(bool);
	}

	@Test
	public void testsaveOrUpdateAllColumn() {
		Order entity = new Order();
		entity.setOrderNo("T00001");
		entity.setExtnRefNo1("T00001");
		entity.setVersion(version);
		
		boolean bool = orderService.saveOrUpdate(entity);
		assertTrue(bool);
	}

	@Test
	public void testsaveOrUpdateBatchListOfT() {
		
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.saveOrUpdateBatch(entityList);
		assertTrue(bool);
	
	}

	@Test
	public void testsaveOrUpdateBatchListOfTInt() {
		
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.saveOrUpdateBatch(entityList, 10);
		assertTrue(bool);
	
	}

	@Test
	public void testsaveOrUpdateAllColumnBatchListOfT() {
		
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.saveOrUpdateBatch(entityList);
		assertTrue(bool);
	
	}

	@Test
	public void testsaveOrUpdateAllColumnBatchListOfTInt() {
		
		Order entity1 = new Order();
		entity1.setOrderNo("T00001");
		entity1.setExtnRefNo1("T00001");
		entity1.setVersion(version);
		
		Order entity2 = new Order();
		entity2.setOrderNo("T00002");
		entity2.setExtnRefNo1("T00002");
		entity2.setVersion(version);
		
		List<Order> entityList = Arrays.asList(entity1, entity2);
		
		boolean bool = orderService.saveOrUpdateBatch(entityList, 10);
		assertTrue(bool);
	
	}

	@Test
	public void testremoveById() {
		boolean bool = orderService.removeById(id);
		assertTrue(bool);
	}

	@Test
	public void testremoveByMap() {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("ORDER_NO", "X00001");
		boolean bool = orderService.removeByMap(columnMap);
		assertTrue(bool);
	}

	@Test
	public void testremove() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		boolean bool = orderService.remove(new QueryWrapper<Order>(wrapper));
		assertTrue(bool);
	}

	@Test
	public void testremoveBatchIds() {
		Collection<? extends Serializable> idList = Arrays.asList(new Long[]{id, 2L, 3L});
		boolean bool = orderService.removeByIds(idList);
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
		
		boolean bool = orderService.updateById(entity);
		assertTrue(bool);
	}

	@Test
	public void testUpdate() {
		Order entity = new Order();
		entity.setExtnRefNo1("E00002");

		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		wrapper.setVersion(version);
		boolean bool = orderService.update(entity, new QueryWrapper<Order>(wrapper));
		assertTrue(bool);
	}

	/*@Test
	public void testUpdateForSet() {
		String setStr = "CREATED_VIA='MANUAL'";

		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		boolean bool = orderService.updateForSet(setStr, new QueryWrapper<Order>(wrapper));
		assertTrue(bool);
	}*/

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
		boolean bool = orderService.updateBatchById(entityList);
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
		boolean bool = orderService.updateBatchById(entityList, 10);
		assertTrue(bool);
	}

	@Test
	public void testSelectById() {
		Order ret = orderService.getById(id);
		assertNotNull(ret);
	}

	@Test
	public void testSelectBatchIds() {
		Collection<? extends Serializable> idList = Arrays.asList(new Long[]{id, 2L, 3L});
		Collection<Order> list = orderService.listByIds(idList);
		assertEquals(1, list.size());
	}

	@Test
	public void testSelectByMap() {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("ORDER_NO", "X00001");
		Collection<Order> list = orderService.listByMap(columnMap);
		assertEquals(1, list.size());
	}

	@Test
	public void testSelectOne() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		Order ret = orderService.getOne(new QueryWrapper<Order>(wrapper));
		assertNotNull(ret);
	}

	@Test
	public void testSelectMap() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		Map<String, Object> ret = orderService.getMap(new QueryWrapper<Order>(wrapper));
		assertNotNull(ret);
	}

	@Test
	public void testSelectObj() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		Object ret = orderService.getObj(new QueryWrapper<Order>(wrapper));
		assertNotNull(ret);
	}

	@Test
	public void testSelectCount() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		int cnt = orderService.count(new QueryWrapper<Order>(wrapper));
		assertEquals(1, cnt);
	}

	@Test
	public void testSelectList() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		List<Order> list = orderService.list(new QueryWrapper<Order>(wrapper));
		assertEquals(1, list.size());
	}

	@Test
	public void testSelectPagePageOfT() {
		IPage<Order> page = new Page<Order>(0, 2);
		IPage<Order> paged = orderService.page(page);
		assertEquals(1L, paged.getTotal());
	}

	@Test
	public void testSelectMaps() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		List<Map<String, Object>> list = orderService.listMaps(new QueryWrapper<Order>(wrapper));
		assertEquals(1, list.size());
	}

	@Test
	public void testSelectObjs() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		List<Object> list = orderService.listObjs(new QueryWrapper<Order>(wrapper));
		assertEquals(1, list.size());
	}

	@Test
	public void testSelectMapsPage() {
		IPage<Order> page = new Page<Order>(0, 2);
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		IPage<Map<String, Object>> paged = orderService.pageMaps(page, new QueryWrapper<Order>(wrapper));
		assertEquals(1L, paged.getTotal());
	}

	@Test
	public void testSelectPagePageOfTWrapperOfT() {
		IPage<Order> page = new Page<Order>(0, 2);
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		IPage<Order> paged = orderService.page(page, new QueryWrapper<Order>(wrapper));
		assertEquals(1L, paged.getTotal());
	}

}
