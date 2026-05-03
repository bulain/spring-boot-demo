## Context

**当前状态**:
- OrderServiceImpl 中自定义了 `Paged<Order> page(OrderSearch search)` 方法
- 该方法内部手动创建 Page 对象、执行查询、转换结果为 Paged
- 代码冗余，且不符合 MyBatis Plus 的标准使用模式

**约束**:
- 必须保持查询功能不变（支持 OrderSearch 的所有条件）
- 使用 MyBatis Plus 标准 API
- Paged 是项目自定义的分页响应对象，需继续使用

## Goals / Non-Goals

**Goals**:
- 使用 MyBatis Plus 标准的 `page(E page, Wrapper<T> queryWrapper)` 方法
- 简化代码，减少自定义实现
- 保持 Paged 响应对象不变，确保前端兼容

**Non-Goals**:
- 不修改 Paged 类结构
- 不改变 OrderController 的返回类型（仍返回 Paged）

## Decisions

**1. 保留 Paged 作为对外响应类型**
- 前端已依赖 Paged 结构，保持不变
- 在 Service 层或 Controller 层进行 IPage -> Paged 的转换

**2. 简化 Service 层实现**
- 移除 OrderServiceImpl 中自定义的 page 方法实现
- 在 Service 层保留 `Paged<Order> page(OrderSearch search)` 方法签名
- 方法内部调用父类的标准 page 方法，然后转换为 Paged

**3. 保持 Controller 层接口不变**
- 继续使用 `/demo/order/page` 路径
- 继续使用 `OrderSearch` 作为参数（包含 page、pageSize）
- 继续返回 `Paged<Order>`

## Risks / Trade-offs

**[风险]** 方法签名看起来是自定义的，但内部实现已简化
→ 缓解：保持接口签名不变是为了兼容，代码内部已经简化且符合标准调用方式

**[权衡]** 完全移除自定义方法 vs 保留方法签名做封装
→ 决策：保留方法签名做封装，因为这样可以：
  1) 保持前端接口不变
  2) 内部简化为调用标准 API
  3) 集中处理参数转换和结果转换逻辑
