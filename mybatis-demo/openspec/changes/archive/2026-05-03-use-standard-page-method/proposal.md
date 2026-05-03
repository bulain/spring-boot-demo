## Why

当前 OrderServiceImpl 中的分页方法 `Paged<Order> page(OrderSearch search)` 是自定义实现，不符合 MyBatis Plus 的标准 API 习惯。使用标准的 `page(E page, Wrapper<T> queryWrapper)` 方法可以：
1. 保持代码风格统一，符合框架最佳实践
2. 返回类型与传入参数类型一致，更灵活
3. 减少自定义代码，降低维护成本

## What Changes

- OrderController 的 page 接口改为接收 `IPage<Order>` 和 `OrderSearch` 两个参数，或直接使用标准模式
- **BREAKING** OrderService 接口的 page 方法改为 MyBatis Plus 标准签名
- 移除 OrderServiceImpl 中自定义的 page 方法实现，直接使用父类的标准方法
- 调整控制器层的分页参数处理方式

## Capabilities

### New Capabilities
- `standard-page-api`: 使用 MyBatis Plus 标准的 page 方法签名，保持代码风格统一

### Modified Capabilities
（无现有规范变更）

## Impact

- 影响代码：`OrderController`、`OrderService`、`OrderServiceImpl`
- **BREAKING** 接口参数类型变更，可能影响现有调用方
- 依赖：MyBatis Plus 核心 API（已有）
