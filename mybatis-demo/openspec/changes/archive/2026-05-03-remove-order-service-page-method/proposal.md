## Why

OrderService 中的 `Paged<Order> page(OrderSearch search)` 方法：
1. 与父类 `PagedService.page(Search search)` 功能重复，仅参数类型不同
2. 逻辑完全可以在 Controller 层处理（条件构建 + 调用父类方法）
3. 删除后可简化 Service 层，保持接口最小化原则

## What Changes

- **BREAKING** 删除 `OrderService` 中的 `Paged<Order> page(OrderSearch search)` 方法声明
- **BREAKING** 删除 `OrderServiceImpl` 中的对应实现
- 在 `OrderController` 中直接调用父类的分页方法，并处理 OrderSearch 的条件构建

## Capabilities

### New Capabilities
- `simplify-service-layer`: 简化 Service 层，移除不必要的方法重载

### Modified Capabilities
（无现有规范变更）

## Impact

- 影响代码：`OrderService`、`OrderServiceImpl`、`OrderController`
- **BREAKING** 可能影响直接调用 OrderService.page 方法的代码
- 无外部依赖变更
