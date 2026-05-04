## Context

当前 `SysUserController.exportUsers()` 使用 `pageUsers(query)` 获取数据，只导出第 1 页（默认 10 条）数据，而非全部符合条件的用户。同时，即使修复分页问题，全量加载 List 再转换导出，在大用户量场景下存在 OOM 风险。

角色导出功能已完成流式重构（`SysRoleServiceImpl.exportStreaming()`），验证了 MyBatis ResultHandler + EasyExcel 分批写入模式的可行性和内存安全性。

**约束：**
- 保持现有 Controller API 签名兼容
- 与角色导出代码风格保持一致
- 复用现有 EasyExcel 依赖

## Goals / Non-Goals

**Goals:**
- 修复导出仅 10 条数据的 Bug，导出所有符合查询条件的用户
- 重构为流式导出，峰值内存 < 2MB
- 新增按 ID 选择导出功能
- 代码风格与角色导出保持一致

**Non-Goals:**
- 不修改 EasyExcel 依赖版本
- 不改变导出文件格式和列定义
- 不实现 MySQL fetchSize 优化（保持默认行为）

## Decisions

### 1. 参照角色导出模式实现流式用户导出
**决策：** 完全参照 `SysRoleServiceImpl.exportStreaming()` 的实现模式。

**Rationale:**
- 角色导出已验证该模式的可行性和内存安全性
- 代码风格统一，降低维护成本
- 降低实现风险，减少重复工作

**备选方案：** 在 Controller 内直接实现流式逻辑
- 缺点：逻辑分散，不利于复用，与现有架构不一致

### 2. Service 层封装流式逻辑，Controller 保持薄接入层
**决策：** 在 `SysUserService` 添加导出接口，`SysUserServiceImpl` 实现流式逻辑，Controller 仅做简单调用。

**Rationale:**
- 与角色导出的架构一致（Controller 调用 Service.export()）
- 导出逻辑可复用（如被其他 Service 调用）
- Controller 层保持简洁，专注于 HTTP 协议处理

## Risks / Trade-offs

| 风险 | 影响 | 应对措施 |
|------|------|----------|
| MySQL 默认全量拉取 ResultSet | 数据量极大时 JDBC 层仍可能占用较多内存 | 当前可接受，后续可通过 StatementCreator 自定义 fetchSize 优化 |
| 导出中途网络断开 | 可能产生部分写入的 Excel | 依赖 try-with-resources 确保资源释放，客户端异常由用户重试 |
| 状态字段转换逻辑不一致 | 导出的状态值可能错误 | 严格复用现有 Controller 中的转换逻辑：`status == 1 ? "启用" : "禁用"` |
