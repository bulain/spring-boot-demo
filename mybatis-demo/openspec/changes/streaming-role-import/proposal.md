## Why

当前角色导入实现存在OOM风险：RoleReadListener 将所有有效行累积在内存中，达到批量阈值后仅打日志不清空，Service 接收完整 List 处理。大文件（10k+ 行）会导致内存溢出。需要重构为真正的流式分批处理，保证内存占用稳定。

## What Changes

- 重构 RoleReadListener 为 RoleImportListener，支持服务回调处理每批次
- SysRoleService 新增流式 importExcel(InputStream) 方法
- 新增 processImportBatch 方法，每批次独立事务
- Controller 简化为直接调用流式接口
- 保持原有 API 响应格式不变（ImportResultVO）

## Capabilities

### New Capabilities
- `streaming-batch-import`: 基于回调的流式批量导入框架

### Modified Capabilities
- `role-import`: 角色导入改为真正流式处理，按批次独立事务，OOM 安全

## Impact

- `SysRoleController.java`: importRoles 方法简化
- `SysRoleService.java`: 新增流式导入接口和批次处理方法
- `RoleReadListener.java` → `RoleImportListener.java`: 重构为回调模式
- 无新增依赖，使用现有 EasyExcel 和 Spring 事务能力
