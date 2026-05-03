package com.bulain.mybatis.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bulain.mybatis.demo.entity.Order;

/**
 * 订单服务接口
 * 直接继承 MyBatis Plus 的 IService，保持最小化接口
 */
public interface OrderService extends IService<Order> {

    /**
     * 直接删除（绕过逻辑删除）
     */
    boolean directRemove(Wrapper<Order> queryWrapper);

}
