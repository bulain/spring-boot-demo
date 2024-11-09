package com.bulain.mybatis.demo.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bulain.mybatis.core.dao.DirectMapper;
import com.bulain.mybatis.core.dao.PagedMapper;
import com.bulain.mybatis.demo.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends PagedMapper<Order>, DirectMapper<Order> {

    @Select("select * from demo_order ${ew.customSqlSegment}")
    List<Order> selectByCustomSql(@Param(Constants.WRAPPER) Wrapper<Order> wrapper);

}
