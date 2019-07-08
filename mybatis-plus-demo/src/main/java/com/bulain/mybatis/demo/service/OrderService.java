package com.bulain.mybatis.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bulain.mybatis.demo.model.Order;

public interface OrderService extends IService<Order> {

    int deleteAll();
    
}
