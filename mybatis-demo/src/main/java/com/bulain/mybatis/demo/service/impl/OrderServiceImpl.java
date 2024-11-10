package com.bulain.mybatis.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.bulain.mybatis.core.service.PagedServiceImpl;
import com.bulain.mybatis.demo.dao.OrderMapper;
import com.bulain.mybatis.demo.entity.Order;
import com.bulain.mybatis.demo.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

@Service
public class OrderServiceImpl extends PagedServiceImpl<OrderMapper, Order> implements OrderService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Order entity) {
        //throw new RuntimeException("rollback");
        return super.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean directRemove(Wrapper<Order> queryWrapper) {
        return SqlHelper.retBool(baseMapper.directDelete(queryWrapper));
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
