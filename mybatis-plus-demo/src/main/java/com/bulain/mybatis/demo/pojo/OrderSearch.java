package com.bulain.mybatis.demo.pojo;

import com.bulain.mybatis.core.pojo.Search;
import com.bulain.mybatis.demo.model.Order;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderSearch extends Search<Order> {
    private static final long serialVersionUID = 1L;

    private String orderNo;
    
}
