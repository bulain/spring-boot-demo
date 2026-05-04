## ADDED Requirements

### Requirement: 支持用户实体的流式批量导出
系统 SHALL 支持用户实体的流式批量导出，复用基于 ResultHandler 的流式导出框架。

#### Scenario: 用户实体流式导出
- **WHEN** 执行用户数据导出
- **THEN** 使用与角色导出相同的 ResultHandler + EasyExcel 流式框架
- **AND** 每 100 行写入 Excel 并清空批次
- **AND** 峰值内存 < 2MB
