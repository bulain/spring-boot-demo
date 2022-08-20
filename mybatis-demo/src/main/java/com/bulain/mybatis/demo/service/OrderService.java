package com.bulain.mybatis.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.bulain.mybatis.demo.entity.Blog;
import com.bulain.mybatis.demo.entity.Order;

public interface OrderService extends IService<Order> {

    /**直接删除*/
    boolean directRemove(Wrapper<Order> queryWrapper);

}
