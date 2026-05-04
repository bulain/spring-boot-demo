## ADDED Requirements

### Requirement: 流式导入角色数据

系统 SHALL 支持流式读取 Excel 文件并批量导入角色数据。

#### Scenario: 导入全部为新增角色
- **WHEN** 用户上传包含角色数据的 Excel 文件，所有角色编码均不存在
- **THEN** 系统使用 EasyExcel 流式逐行读取数据
- **AND** 逐行校验字段格式
- **AND** 批量插入新增的角色数据
- **AND** 返回导入成功数量

#### Scenario: 导入包含已有角色（更新）
- **WHEN** 用户上传的 Excel 包含已存在的角色编码
- **THEN** 系统根据角色编码更新已有角色的信息
- **AND** 返回成功数量和更新数量统计

### Requirement: 导入数据校验

系统 SHALL 在导入时逐行校验数据格式和业务规则。

#### Scenario: 导入数据包含必填字段为空
- **WHEN** Excel 中角色编码或名称字段为空
- **THEN** 记录该行的错误信息
- **AND** 跳过该行继续处理后续数据

#### Scenario: 导入数据包含重复编码
- **WHEN** Excel 内部存在重复的角色编码
- **THEN** 只保留第一条有效数据
- **AND** 记录后续重复行的错误信息

### Requirement: 导入结果反馈

系统 SHALL 返回详细的导入结果统计和错误详情。

#### Scenario: 部分成功部分失败导入
- **WHEN** Excel 中部分行校验失败
- **THEN** 返回成功数量、失败数量
- **AND** 返回每行的错误信息（行号、错误原因）
