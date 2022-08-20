package com.bulain.mybatis.demo.service.impl;

import java.io.Serializable;
import java.util.Collection;

import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.bulain.mybatis.demo.service.OrderService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bulain.mybatis.demo.dao.OrderMapper;
import com.bulain.mybatis.demo.entity.Order;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> list) {
        UpdateChainWrapper<Order> updateWrapper = super.update()
                .in("id", list)
                .set("deleted", 1)
                .set("version", SystemClock.now());
        return updateWrapper.update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        UpdateChainWrapper<Order> updateWrapper = super.update()
                .eq("id", id)
                .set("deleted", 1)
                .set("version", SystemClock.now());
        return updateWrapper.update();
    }

}
