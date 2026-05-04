## Why

当前 `SysRoleServiceImpl.export()` 方法存在 OOM 风险：全量加载所有角色数据到内存后转换并写入 Excel。当角色数据量达到 10 万行级别时，内存占用可达 100MB 以上，并发导出场景下极易触发内存溢出。需要重构为真正的流式处理，保证内存占用稳定与数据量无关。

## What Changes

- 重构 `export(RoleQueryDTO, HttpServletResponse)` 为流式导出
- 重构 `exportByIds(List<String>, HttpServletResponse)` 为流式导出
- 采用 MyBatis `ResultHandler` 逐行回调 + EasyExcel 分批写入方案
- 批次大小：100 行/批，峰值内存 < 2MB
- 删除废弃的全量写入 `writeExcel()` 方法
- 保持 API 签名 100% 兼容

## Capabilities

### New Capabilities
- `streaming-role-export`: 基于 ResultHandler 的流式角色导出，OOM 安全

### Modified Capabilities
- `role-export`: 角色导出功能升级为流式处理，支持 10 万行以上大数据量

## Impact

- `SysRoleServiceImpl.java`: export 和 exportByIds 方法重构
- 无新增依赖，复用现有 MyBatis 和 EasyExcel 能力
- 方法签名不变，Controller 层无需修改
