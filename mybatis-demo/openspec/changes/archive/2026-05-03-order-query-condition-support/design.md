## Context

**当前状态**:
- OrderController 的分页查询接口使用通用 `Search` 类，仅支持分页参数
- `OrderSearch` 类已存在，但目前只有 `orderNo` 字段
- `list` 接口使用 `Order` 实体作为查询条件，功能有限

**约束**:
- 遵循现有代码架构模式
- 保持接口兼容性
- 使用 MyBatis Plus 的条件构造机制

## Goals / Non-Goals

**Goals**:
- 支持订单特有字段的条件查询
- list 和 page 接口统一使用 OrderSearch
- 支持模糊查询、日期范围查询等常见查询方式

**Non-Goals**:
- 不修改底层 Mapper 查询逻辑
- 不引入新的外部依赖

## Decisions

**1. 接口参数统一**
- `/list` 和 `/page` 接口参数统一使用 `OrderSearch`
- 替代当前的 `Search` 和 `Order` 混合方式

**2. 增强 OrderSearch 字段**
- 补充常用查询字段：status（状态）、createdDateFrom/createdDateTo（创建日期范围）、extnRefNo1（参考号）等
- 字段命名与 Order 实体保持一致

**3. 查询实现方式**
- 使用 MyBatis Plus 的 `Wrappers.query()` 动态构建查询条件
- 对字符串类型字段支持模糊查询（like）
- 对日期字段支持范围查询（between）
- 其他字段使用精确匹配（eq）

## Risks / Trade-offs

**[风险]** 接口参数类型变更可能影响现有调用方
→ 缓解措施：由于是新增功能增强，原有分页参数仍兼容，新增字段为可选

**[权衡]** 动态条件构建 vs 静态 SQL
→ 决策：使用动态条件构建，代码更简洁易维护，满足大多数查询场景需求
