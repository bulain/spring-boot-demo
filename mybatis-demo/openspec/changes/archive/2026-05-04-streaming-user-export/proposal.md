## Why

当前用户导出存在两个严重问题：1) Bug：仅导出第 1 页（默认 10 条）数据，而非全部符合条件的用户；2) OOM 风险：全量加载数据在大用户量场景下会导致内存溢出。需要参照已实现的流式角色导出模式，重构为流式导出，修复 Bug 并保障内存安全。

## What Changes

- 修复 `SysUserController.exportUsers()` 仅导出 10 条数据的 Bug，改为导出所有符合查询条件的用户
- 将用户导出重构为流式实现（MyBatis ResultHandler + EasyExcel 分批写入），峰值内存 < 2MB
- 新增按 ID 选择导出功能，支持导出选中的特定用户
- 保持现有导出 API 签名完全兼容

## Capabilities

### New Capabilities
- `user-export`: 用户导出功能，支持按条件导出所有用户和按 ID 导出选中用户

### Modified Capabilities
- `streaming-batch-export`: 新增用户导出作为流式批量导出的具体实现

## Impact

- **代码**：`SysUserService` 接口新增方法，`SysUserServiceImpl` 新增流式实现，`SysUserController` 简化导出逻辑
- **API**：现有 `/api/sys/users/export` 行为修复（10 条 → 全部），新增 `/api/sys/users/export/selected` 端点
- **依赖**：复用现有 EasyExcel 依赖，无新增依赖
