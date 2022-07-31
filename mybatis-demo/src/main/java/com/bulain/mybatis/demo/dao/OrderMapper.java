package com.bulain.mybatis.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bulain.mybatis.core.dao.PagedMapper;
import com.bulain.mybatis.demo.entity.Order;
import com.bulain.mybatis.demo.pojo.OrderSearch;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order>, PagedMapper<Order, OrderSearch> {

    int deleteAll();

}
