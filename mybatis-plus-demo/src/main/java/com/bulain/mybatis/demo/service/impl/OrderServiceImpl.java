package com.bulain.mybatis.demo.service.impl;

import java.util.Collection;

import com.bulain.mybatis.demo.service.OrderService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bulain.mybatis.demo.dao.OrderMapper;
import com.bulain.mybatis.demo.entity.Order;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private static final String FAKE_ID = "*-_-*_flush_cache_*-_-*";

    @Override
    public boolean saveBatch(Collection<Order> entityList, int batchSize) {
        baseMapper.deleteById(FAKE_ID);
        return super.saveBatch(entityList, batchSize);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<Order> entityList, int batchSize) {
        baseMapper.deleteById(FAKE_ID);
        return super.saveOrUpdateBatch(entityList, batchSize);
    }

    @Override
    public boolean updateBatchById(Collection<Order> entityList, int batchSize) {
        baseMapper.deleteById(FAKE_ID);
        return super.updateBatchById(entityList, batchSize);
    }

    @Override
    public int deleteAll() {
        return baseMapper.deleteAll();
    }

}
