package com.bulain.mybatis.demo.dao;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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

	@Before
	public void setup() {
		orderMapper.delete(new EntityWrapper<Order>());

		Order entity = new Order();
		entity.setOrderNo("X00001");
		entity.setExtnRefNo1("E00001");
		entity.setVersion(id);

		orderMapper.insert(entity);
		id = entity.getId();
	}

	@Test
	public void testInsert() {
		Order entity = new Order();
		entity.setOrderNo("T00001");
		entity.setExtnRefNo1("T00001");
		entity.setVersion(id);

		orderMapper.insert(entity);
	}

	@Test
	public void testDeleteById() {
		orderMapper.deleteById(id);
	}

	@Test
	public void testDeleteByMap() {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("ORDER_NO", "X00001");
		orderMapper.deleteByMap(columnMap);
	}

	@Test
	public void testDelete() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");

		orderMapper.delete(new EntityWrapper<Order>(wrapper));
	}

	@Test
	public void testDeleteBatchIds() {
		Collection<? extends Serializable> idList = Arrays.asList(new Long[]{id, 2L, 3L});
		orderMapper.deleteBatchIds(idList);
	}

	@Test
	public void testUpdateById() {
		Order entity = new Order();
		entity.setId(id);
		entity.setOrderNo("X00001");
		entity.setExtnRefNo1("E00001");

		orderMapper.updateById(entity);
	}

	@Test
	public void testUpdateAllColumnById() {
		Order entity = new Order();
		entity.setId(id);
		entity.setOrderNo("X00001");
		entity.setExtnRefNo1("E00001");

		orderMapper.updateAllColumnById(entity);
	}

	@Test
	public void testUpdate() {
		Order entity = new Order();
		entity.setOrderNo("X00001");
		entity.setExtnRefNo1("E00002");

		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		orderMapper.update(entity, new EntityWrapper<Order>(wrapper));
	}

	@Test
	public void testUpdateForSet() {
		String setStr = "CREATED_VIA='MANUAL'";

		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		orderMapper.updateForSet(setStr, new EntityWrapper<Order>(wrapper));
	}

	@Test
	public void testSelectById() {
		orderMapper.selectById(id);
	}

	@Test
	public void testSelectBatchIds() {
		Collection<? extends Serializable> idList = Arrays.asList(new Long[]{id, 2L, 3L});
		orderMapper.selectBatchIds(idList);
	}

	@Test
	public void testSelectByMap() {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("ORDER_NO", "X00001");
		orderMapper.selectByMap(columnMap);
	}

	@Test
	public void testSelectOne() {
		Order entity = new Order();
		entity.setOrderNo("X00001");
		orderMapper.selectOne(entity);
	}

	@Test
	public void testSelectCount() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		orderMapper.selectCount(new EntityWrapper<Order>(wrapper));
	}

	@Test
	public void testSelectList() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		orderMapper.selectList(new EntityWrapper<Order>(wrapper));
	}

	@Test
	public void testSelectMaps() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		orderMapper.selectMaps(new EntityWrapper<Order>(wrapper));
	}

	@Test
	public void testSelectObjs() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		orderMapper.selectObjs(new EntityWrapper<Order>(wrapper));
	}

	@Test
	public void testSelectPage() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		RowBounds rowBounds = new RowBounds(0, 2);
		orderMapper.selectPage(rowBounds, new EntityWrapper<Order>(wrapper));
	}

	@Test
	public void testSelectMapsPage() {
		Order wrapper = new Order();
		wrapper.setOrderNo("X00001");
		RowBounds rowBounds = new RowBounds(0, 2);
		orderMapper.selectMapsPage(rowBounds, new EntityWrapper<Order>(wrapper));
	}

}
