package com.bulain.mybatis.demo.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bulain.mybatis.demo.dao.OrderMapper;
import com.bulain.mybatis.demo.model.Order;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
