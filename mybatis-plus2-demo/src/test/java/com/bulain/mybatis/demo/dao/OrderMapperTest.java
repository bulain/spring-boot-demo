package com.bulain.mybatis.demo.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bulain.mybatis.MybatisPlusApplication;
import com.bulain.mybatis.demo.model.Order;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MybatisPlusApplication.class)
public class OrderMapperTest {

	@Autowired
	private OrderMapper orderMapper;
	private Long id;
	private Long version = 1L;

	@Before
	public void setup() {
		orderMapper.delete(new EntityWrapper<Order>());

		Order entity = new Order();
		entity.setOrderNo("X00001");
		entity.setExtnRefNo1("E00001");
		entity.setVersion(version);

		orderMapper.insert(entity);
		id = entity.getId();
	}

	@Test
	public void testInsert() {
		Order entity = new Order();
		entity.setOrderNo("T00001");
		entity.setExtnRefNo1("T00001");
		entity.setVersion(version);

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

		Integer dcnt = orderMapper.delete(new EntityWrapper<Order>(wrapper));
		assertEquals(Integer.valueOf(1), dcnt);
	}

	@Test
	public void testDeleteBatchIds() {
		Collection<? extends Serializable> idList = Arrays.asList(new Long[]{id, 2L, 3L});
		Integer dcnt = orderMapper.deleteBatchIds(idList);
		assertEquals(Integer.valueOf(1), dcnt);
	}

	@Test
	public void testUpdateById() {
		Order entity = new Order();
		entity.setId(id);
		entity.setOrderNo("X00001");
		entity.setExtnRefNo1("E00001");
		entity.setVersion(version);

		Integer ucnt = orderMapper.updateById(entity);
		assertEquals(Integer.valueOf(1), ucnt);
	}

	@Test
	public void testUpdateAllColumnById() {
		Order entity = new Order();
		entity.setId(id);
		entity.setOrderNo("X00001");
		entity.setExtnRefNo1("E00001");
		entity.setVersion(version);

		Integer ucnt = orderMapper.updateAllColumnById(entity);
		assertEquals(Integer.valueOf(1), ucnt);
	}

	@Test
	public void testUpdate() {

		Order entity = new Order();
		entity.setExtnRefNo1("E00002");

		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		wrapper.setVersion(version);
		Integer ucnt = orderMapper.update(entity, new EntityWrapper<Order>(wrapper));
		assertEquals(Integer.valueOf(1), ucnt);
	}

	@Test
	public void testUpdateForSet() {
		String setStr = "CREATED_VIA='MANUAL'";

		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		Integer ucnt = orderMapper.updateForSet(setStr, new EntityWrapper<Order>(wrapper));
		assertEquals(Integer.valueOf(1), ucnt);
	}

	@Test
	public void testSelectById() {
		Order ret = orderMapper.selectById(id);
		assertNotNull(ret);
	}

	@Test
	public void testSelectBatchIds() {
		Collection<? extends Serializable> idList = Arrays.asList(new Long[]{id, 2L, 3L});
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
		Order ret = orderMapper.selectOne(entity);
		assertNotNull(ret);
	}

	@Test
	public void testSelectCount() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		Integer cnt = orderMapper.selectCount(new EntityWrapper<Order>(wrapper));
		assertEquals(Integer.valueOf(1), cnt);
	}

	@Test
	public void testSelectList() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		List<Order> list = orderMapper.selectList(new EntityWrapper<Order>(wrapper));
		assertEquals(1, list.size());
	}

	@Test
	public void testSelectMaps() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		List<Map<String, Object>> list = orderMapper.selectMaps(new EntityWrapper<Order>(wrapper));
		assertEquals(1, list.size());
	}

	@Test
	public void testSelectObjs() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		List<Object> list = orderMapper.selectObjs(new EntityWrapper<Order>(wrapper));
		assertEquals(1, list.size());
	}

	@Test
	public void testSelectPage() {
		RowBounds rowBounds = new RowBounds(0, 2);
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		List<Order> list = orderMapper.selectPage(rowBounds, new EntityWrapper<Order>(wrapper));
		assertEquals(1, list.size());
	}

	@Test
	public void testSelectMapsPage() {
		RowBounds rowBounds = new RowBounds(0, 2);
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		List<Map<String, Object>> list = orderMapper.selectMapsPage(rowBounds, new EntityWrapper<Order>(wrapper));
		assertEquals(1, list.size());
	}

}
