## Context

**当前状态:**
- Order 实体已定义，包含完整的字段（订单基本信息、金额、日期、状态等）
- OrderService 和 OrderServiceImpl 已实现，继承 PagedServiceImpl 提供分页能力
- OrderMapper 已实现，继承 DirectMapper 和 ExtensionMapper
- 目前缺少 REST API 层，前端无法通过 HTTP 接口操作 Order 数据

**约束:**
- 使用 Spring Boot 标准 REST 注解
- 遵循项目现有代码规范和分层架构
- 接口路径前缀统一为 `/demo/order`
- 使用中文注释，中文接口文档

## Goals / Non-Goals

**Goals:**
- 提供 Order 实体完整的 CRUD REST 接口
- 支持分页查询、条件查询、单条查询、新增、修改、删除
- 接口返回标准 JSON 格式
- 复用现有 Service 层逻辑，不重复实现业务逻辑

**Non-Goals:**
- 不修改 Order 实体结构
- 不修改现有的 Mapper 和 Service 层实现
- 不引入新的外部依赖
- 不涉及复杂业务逻辑（如审批流程、支付集成等）

## Decisions

**1. 控制器层架构**
- 新建 `com.bulain.mybatis.demo.ctrl` 包路径
- 创建 `OrderController` REST 控制器
- 注入 `OrderService` 进行业务操作
- 遵循 RESTful 设计规范：GET/POST/PUT/DELETE

**2. 接口设计**
- `GET /demo/order/{id}` - 根据 ID 查询单条记录
- `GET /demo/order/list` - 查询列表（条件过滤）
- `GET /demo/order/page` - 分页查询（支持排序、分页参数）
- `POST /demo/order` - 新增订单
- `PUT /demo/order` - 修改订单
- `DELETE /demo/order/{id}` - 删除订单

**3. 参数传递**
- 查询条件使用 `OrderSearch` 实体
- 新增/修改使用 `Order` 实体
- 使用 `@RequestBody`、`@PathVariable`、`@RequestParam` 等注解

**4. 返回格式**
- 使用项目标准的统一返回格式（如存在）
- 查询成功返回数据对象
- 操作成功返回操作结果标识
- 异常情况交由全局异常处理器处理

## Risks / Trade-offs

**[风险]** 控制器包路径 `ctrl` 与常规 `controller` 命名不同
→ 缓解措施：按照用户要求使用 `com.bulain.mybatis.demo.ctrl` 路径，在注释中说明

**[风险]** Order 实体字段较多，前端传参可能遗漏必填项
→ 缓解措施：使用 Bean Validation 注解进行参数校验，在 Controller 层使用 `@Valid`

**[权衡]** 是否需要 DTO 层进行数据转换
→ 决策：当前阶段直接使用实体类，避免过度设计，如后续需要再引入 DTO 层
