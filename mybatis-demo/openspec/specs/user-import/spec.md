## ADDED Requirements

### Requirement: 流式导入用户数据

系统 SHALL 支持流式读取 Excel 文件并按批次导入用户数据，避免 OOM。

#### Scenario: 流式导入大文件成功
- **WHEN** 用户上传包含 10,000+ 行用户数据的 Excel 文件
- **THEN** 系统按每 100 行分批处理
- **AND** 每批次独立事务提交
- **AND** 内存占用保持稳定（< 2MB）

#### Scenario: 部分批次失败
- **WHEN** 导入过程中某批次处理失败
- **THEN** 已成功的批次保持提交
- **AND** 继续处理后续批次
- **AND** 返回结果中包含失败详情

#### Scenario: 导入结果聚合正确
- **WHEN** 流式导入完成
- **THEN** 返回结果包含所有批次的成功数、更新数、失败数总和
- **AND** 所有错误明细按行号排序

#### Scenario: 基本导入成功
- **WHEN** 用户上传包含有效用户数据的 Excel 文件
- **THEN** 系统成功创建新用户
- **AND** 默认密码正确设置

#### Scenario: 包含已有用户更新
- **WHEN** Excel 中 username 已存在
- **THEN** 系统更新该用户信息（name、email、phone、status）
- **AND** 更新数统计正确

#### Scenario: 空值校验失败
- **WHEN** Excel 中 username 或 name 为空
- **THEN** 记录该行错误信息
- **AND** 跳过该行继续处理

#### Scenario: 文件内重复 username
- **WHEN** Excel 中存在重复的 username
- **THEN** 第一条成功处理
- **AND** 后续重复行记录错误信息

#### Scenario: 手机号格式错误
- **WHEN** Excel 中 phone 字段格式不合法
- **THEN** 记录该行错误信息

#### Scenario: 邮箱格式错误
- **WHEN** Excel 中 email 字段格式不合法
- **THEN** 记录该行错误信息

#### Scenario: 空文件导入
- **WHEN** Excel 文件中无有效数据行
- **THEN** 返回成功数为 0
- **AND** 不抛出异常
