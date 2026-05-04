## ADDED Requirements

### Requirement: 流式导出全部角色

系统 SHALL 支持流式导出全部角色数据为 Excel 文件，避免内存溢出。

#### Scenario: 导出全部角色成功
- **WHEN** 用户不指定筛选条件，请求导出全部角色
- **THEN** 系统流式查询数据库中的所有角色
- **AND** 系统使用 EasyExcel 流式写入 Excel 文件
- **AND** 返回 Excel 文件供下载

#### Scenario: 按条件筛选导出角色
- **WHEN** 用户指定角色名称或状态等筛选条件
- **THEN** 系统根据条件查询匹配的角色
- **AND** 将匹配结果流式导出为 Excel 文件

### Requirement: 选择指定行导出

系统 SHALL 支持用户选择特定角色 ID 列表进行导出。

#### Scenario: 选择多个角色导出成功
- **WHEN** 用户提供角色 ID 列表，请求导出选中的角色
- **THEN** 系统根据 ID 列表查询对应角色
- **AND** 将选中的角色流式导出为 Excel 文件

#### Scenario: 选择空列表导出
- **WHEN** 用户提供空的 ID 列表
- **THEN** 系统返回错误提示："请选择要导出的角色"
