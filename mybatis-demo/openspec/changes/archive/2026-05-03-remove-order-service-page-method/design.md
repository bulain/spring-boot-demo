## Context

**当前状态**:
- `OrderService` 继承 `PagedService`，已有 `Paged<T> page(Search search)` 方法
- `OrderService` 额外声明了 `Paged<Order> page(OrderSearch search)`，功能重复
- `OrderController` 调用 `OrderService.page(OrderSearch)`

**约束**:
- OrderSearch 继承 Search，支持更多字段
- 需要保留所有现有查询功能
- 保持 Controller 对外接口不变

## Goals / Non-Goals

**Goals**:
- 移除重复的 Service 层方法
- 将条件构建逻辑集中到 Controller 层
- 保持对外 API 完全兼容

**Non-Goals**:
- 不改变 PagedService 接口
- 不改变 OrderSearch 结构

## Decisions

**1. Service 层简化**
- 移除 OrderService 中 `page(OrderSearch search)` 方法声明
- 移除 OrderServiceImpl 中对应的实现
- 继承使用父类 `PagedService.page(Search search)`（基于 baseMapper.find）

**2. Controller 层调整**
- 对于 `/demo/order/page`，继续使用 `OrderSearch` 接收参数
- 在 Controller 中处理条件构建
- 保留 `/demo/order/list` 已有的条件构建逻辑

## Risks / Trade-offs

**[风险]** 如果有其他代码直接调用 `OrderService.page(OrderSearch)` 会编译失败
→ 缓解：这是预期的清理效果，调用方应调整到正确的方式

**[权衡]** 条件构建逻辑在 Controller 层 vs Service 层
→ 决策：Controller 层更合适，因为这是 API 参数适配逻辑
