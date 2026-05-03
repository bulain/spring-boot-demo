package com.bulain.mybatis.demo.ctrl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.demo.entity.Order;
import com.bulain.mybatis.demo.pojo.OrderSearch;
import com.bulain.mybatis.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器
 * 提供 Order 实体的 CRUD REST API 接口
 */
@RestController
@RequestMapping("/demo/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 根据 ID 查询订单
     * @param id 订单 ID
     * @return 订单详情
     */
    @GetMapping("/{id}")
    public Order getById(@PathVariable String id) {
        return orderService.getById(id);
    }

    /**
     * 查询订单列表
     * @param search 查询条件
     * @return 订单列表
     */
    @GetMapping("/list")
    public List<Order> list(OrderSearch search) {
        return orderService.list(buildQueryWrapper(search));
    }

    /**
     * 分页查询订单
     * @param search 查询条件（包含 page、pageSize）
     * @return 分页结果
     */
    @GetMapping("/page")
    public Paged<Order> page(OrderSearch search) {
        LambdaQueryWrapper<Order> wrapper = buildQueryWrapper(search);
        IPage<Order> pagination = new Page<>(search.getPage(), search.getPageSize());
        IPage<Order> pageResult = orderService.page(pagination, wrapper);
        return Paged.from(pageResult);
    }

    /**
     * 构建动态查询条件
     * @param search 查询参数
     * @return 查询条件包装器
     */
    private LambdaQueryWrapper<Order> buildQueryWrapper(OrderSearch search) {
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();

        // 订单号模糊查询
        if (StringUtils.hasText(search.getOrderNo())) {
            wrapper.like(Order::getOrderNo, search.getOrderNo());
        }

        // 参考号模糊查询
        if (StringUtils.hasText(search.getExtnRefNo1())) {
            wrapper.like(Order::getExtnRefNo1, search.getExtnRefNo1());
        }

        // 创建日期范围查询
        if (search.getCreatedAtFrom() != null) {
            wrapper.ge(Order::getCreatedAt, search.getCreatedAtFrom());
        }
        if (search.getCreatedAtTo() != null) {
            wrapper.le(Order::getCreatedAt, search.getCreatedAtTo());
        }

        return wrapper;
    }

    /**
     * 新增订单
     * @param entity 订单实体
     * @return 是否成功
     */
    @PostMapping
    public boolean save(@RequestBody Order entity) {
        return orderService.save(entity);
    }

    /**
     * 修改订单
     * @param entity 订单实体
     * @return 是否成功
     */
    @PutMapping
    public boolean update(@RequestBody Order entity) {
        return orderService.updateById(entity);
    }

    /**
     * 删除订单
     * @param id 订单 ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable String id) {
        return orderService.removeById(id);
    }
}
