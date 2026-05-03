## Why

当前 OrderController 的分页查询接口使用通用的 Search 类，不支持订单特有的字段（如订单号、状态、创建日期范围、客户信息等）进行条件过滤，无法满足前端根据订单业务字段进行高级查询的需求。

## What Changes

- 修改 OrderController 的 `/list` 和 `/page` 接口，使用 OrderSearch 替代 Search
- 支持订单相关字段的条件查询（如 orderNo、status、createdDate 范围等）
- 不破坏现有接口的兼容性，仅增强查询能力

## Capabilities

### New Capabilities
- `order-field-query`: 支持订单特有字段的条件查询，包括订单号、状态、日期范围等

### Modified Capabilities
（无现有规范变更）

## Impact

- 影响代码：`OrderController`（修改接口参数类型）
- 依赖：`OrderSearch` 类（已存在，需确保字段正确映射）
- 无破坏性变更，仅增强接口功能
