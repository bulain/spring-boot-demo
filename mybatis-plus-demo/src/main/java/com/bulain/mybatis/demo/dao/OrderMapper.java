package com.bulain.mybatis.demo.dao;

import org.apache.ibatis.annotations.CacheNamespace;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bulain.mybatis.core.dao.PagedMapper;
import com.bulain.mybatis.demo.model.Order;
import com.bulain.mybatis.demo.pojo.OrderSearch;
import com.bulain.mybatis.redis.RedisCache;

@CacheNamespace(eviction = RedisCache.class, implementation = RedisCache.class)
public interface OrderMapper extends BaseMapper<Order>, PagedMapper<Order, OrderSearch> {

    int deleteAll();

}
