## Why

Order 实体目前缺少可供前端调用的 REST API 接口，虽然已有 Mapper 和 Service 层基础实现，但无法通过 HTTP 接口进行数据操作。需要为前端提供标准的 CRUD 接口支持。

## What Changes

- 新增 `com.bulain.mybatis.demo.ctrl` 包路径
- 新增 `OrderController` REST 控制器，提供完整的 CRUD 接口
- 包含查询（列表/分页/单条）、新增、修改、删除接口
- 遵循 Spring Boot RESTful 接口规范

## Capabilities

### New Capabilities
- `order-crud-api`: Order 实体的 CRUD REST API 接口，包含 GET/POST/PUT/DELETE 标准操作

### Modified Capabilities
（无现有规范变更）

## Impact

- 新增控制器类：`com.bulain.mybatis.demo.ctrl.OrderController`
- 依赖现有 Order 相关 Mapper 和 Service 层
- 不影响现有业务逻辑，仅新增接口层
- 无破坏性变更
