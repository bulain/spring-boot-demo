## Why

系统目前没有角色数据的批量导入导出功能，管理员需要逐个创建角色，效率低下。当需要迁移数据或批量更新角色权限时，缺乏便捷的操作方式。流式导入导出可以处理大量数据而不占用过多内存。

## What Changes

- 新增角色 Excel 导出功能，支持按条件筛选导出全部角色
- 新增角色 Excel 导出功能，支持选择指定行进行导出
- 新增角色 Excel 导入功能，支持批量创建/更新角色
- 使用流式处理避免大文件导致的内存溢出问题
- 导入支持数据校验（字段格式、重复编码等）
- 导出支持字段映射和格式自定义

## Capabilities

### New Capabilities
- `role-export`: 角色数据导出功能，支持流式 Excel 下载
- `role-import`: 角色数据导入功能，支持流式 Excel 上传和校验

### Modified Capabilities
- `role-management`: 扩展角色管理接口，增加导入导出端点

## Impact

- 新增 EasyExcel 导入导出工具类（流式读取写入）
- 新增 `RoleExcelVO` 数据模型用于 Excel 映射注解
- `SysRoleController` 新增 `import` 和 `export` 接口
- `SysRoleService` 新增导入导出业务方法
- 依赖：EasyExcel (4.0.3)，基于 POI 封装
- 实现：使用 EasyExcel 的 ReadListener 和 WriteHandler 进行流式处理
