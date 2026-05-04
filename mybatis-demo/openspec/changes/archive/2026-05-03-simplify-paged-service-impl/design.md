## Context

**当前状态**:
- `OrderService extends PagedService<Order> extends IService<Order>`
- `OrderServiceImpl extends PagedServiceImpl<OrderMapper, Order> implements OrderService`
- 其中 `find(Search)` 和 `page(Search)` 方法依赖 `PagedMapper.find()`，需要特殊的 Mapper 实现
- 对于 Order 这种使用动态条件构建的场景，这些 `PagedMapper` 方法从未被使用

**约束**:
- 保持 OrderService 的 `directRemove` 方法
- 保持分页查询功能（通过 MyBatis Plus 标准方法实现）

## Goals / Non-Goals

**Goals**:
- 减少不必要的继承层级
- 直接使用 MyBatis Plus 的标准 `IService` / `ServiceImpl`
- 代码更简洁、更易理解

**Non-Goals**:
- 不删除 `PagedService` / `PagedServiceImpl`（其他模块可能需要）
- 不修改 `PagedMapper` 相关代码

## Decisions

**1. OrderService 简化**
- `OrderService extends IService<Order>`（直接继承）
- 只保留必要的业务方法声明（`directRemove`）

**2. OrderServiceImpl 简化**
- `OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService`（直接继承）
- 不需要再继承 `PagedServiceImpl`

**3. 分页功能保持不变**
- Controller 层通过 `orderService.page(IPage, Wrapper)` 实现分页
- 这是 MyBatis Plus 的标准方法，在 `ServiceImpl` 中已提供

## Risks / Trade-offs

**[风险]** 若有代码依赖 `PagedService` 类型会编译失败
→ 缓解：这是清理的目的，这些代码应相应调整

**[权衡]** 通用基类 vs 直接使用标准基类
→ 决策：对于大多数简单场景，直接继承标准基类更清晰；需要特殊功能时才考虑自定义基类
