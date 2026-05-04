## MODIFIED Requirements

### Requirement: 角色管理接口扩展

系统 SHALL 在角色管理接口中增加导入导出端点。

#### Scenario: 角色导出接口
- **WHEN** 用户访问角色导出接口
- **THEN** 返回 Excel 文件流
- **AND** 支持按条件筛选或按 ID 列表导出

#### Scenario: 角色导入接口
- **WHEN** 用户上传 Excel 文件到导入接口
- **THEN** 处理导入并返回导入结果
