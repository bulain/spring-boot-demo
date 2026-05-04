package com.bulain.mybatis.demo.pojo;

import com.bulain.mybatis.core.pojo.Search;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 订单查询条件
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderSearch extends Search {

    /**
     * 订单号（模糊查询）
     */
    private String orderNo;

    /**
     * 参考号（模糊查询）
     */
    private String extnRefNo1;

    /**
     * 创建日期开始
     */
    private LocalDateTime createdAtFrom;

    /**
     * 创建日期结束
     */
    private LocalDateTime createdAtTo;

}
