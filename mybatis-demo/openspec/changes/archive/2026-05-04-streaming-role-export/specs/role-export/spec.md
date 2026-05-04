## MODIFIED Requirements

### Requirement: 流式导出角色数据

系统 SHALL 支持流式读取数据库并分批写入 Excel，避免 OOM。

#### Scenario: 流式导出大文件成功
- **WHEN** 用户请求导出包含 100,000+ 行角色数据
- **THEN** 系统使用 ResultHandler 逐行读取数据库
- **AND** 每 100 行写入 Excel 后立即清空内存
- **AND** 内存占用保持稳定 (< 2MB)

#### Scenario: 导出 99 行数据（不满一批）
- **WHEN** 用户导出 99 行角色数据
- **THEN** 所有数据在最后一次写入中完整导出
- **AND** Excel 文件内容正确完整

#### Scenario: 导出 100 行数据（刚好一批）
- **WHEN** 用户导出 100 行角色数据
- **THEN** 一批次完成写入
- **AND** 无剩余数据未处理

#### Scenario: 导出 101 行数据（两批次）
- **WHEN** 用户导出 101 行角色数据
- **THEN** 第一批写入 100 行
- **AND** 剩余 1 行在读取完成后写入
- **AND** 所有 101 行数据完整

#### Scenario: 按条件筛选导出
- **WHEN** 用户指定角色编码或名称进行筛选导出
- **THEN** 筛选条件正确生效
- **AND** 结果按创建时间倒序排列

#### Scenario: 按 ID 列表导出
- **WHEN** 用户指定角色 ID 列表导出
- **THEN** 仅导出指定 ID 的角色
- **AND** 空 ID 列表抛出异常（原有行为保持）

#### Scenario: 空数据导出
- **WHEN** 导出条件匹配 0 条数据
- **THEN** 生成空的 Excel 文件（仅表头）
- **AND** 不抛出异常
