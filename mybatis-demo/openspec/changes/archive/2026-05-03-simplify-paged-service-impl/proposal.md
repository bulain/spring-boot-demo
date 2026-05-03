## Why

当前继承层级过多：
```
OrderServiceImpl → PagedServiceImpl → ServiceImpl (MyBatis Plus)
                  → PagedService → IService (MyBatis Plus)
```

`PagedService` 和 `PagedServiceImpl` 增加了不必要的抽象层。`find(Search)` 和 `page(Search)` 方法依赖 `PagedMapper`，仅在特定场景下有用。对于大多数场景，直接使用 MyBatis Plus 的标准 `ServiceImpl` 即可满足需求。

## What Changes

- **BREAKING** 让 `OrderServiceImpl` 直接继承 `ServiceImpl<OrderMapper, Order>`
- **BREAKING** 让 `OrderService` 直接继承 `IService<Order>`
- 移除对 `PagedService` 和 `PagedServiceImpl` 的依赖
- 保留 `PagedService` / `PagedServiceImpl`（作为可选扩展，不删除）

## Capabilities

### New Capabilities
- `simplify-inheritance`: 简化继承层级，直接使用 MyBatis Plus 标准基类

### Modified Capabilities
（无现有规范变更）

## Impact

- 影响代码：`OrderService`、`OrderServiceImpl`
- **BREAKING** 若其他代码依赖 `PagedService` 接口会编译失败
- 无外部依赖变更
- `PagedService` / `PagedServiceImpl` 保留在项目中，可继续用于需要 `PagedMapper` 的场景
