## ADDED Requirements

### Requirement: 基于 ResultHandler 的流式批量导出框架

系统 SHALL 提供基于 MyBatis ResultHandler 的流式批量导出能力，可复用于其他实体。

#### Scenario: ResultHandler 逐行回调处理
- **WHEN** 执行流式导出查询
- **THEN** 每读取一行数据立即触发 ResultHandler 回调
- **AND** 数据累积到批次阈值（100行）立即写入 Excel
- **AND** 写入后清空当前批次，释放内存

#### Scenario: 最终批次处理
- **WHEN** 数据库所有数据读取完成
- **THEN** 处理剩余的不足 100 行数据（部分批次）
- **AND** 确保所有数据都写入 Excel

#### Scenario: 导出资源正确释放
- **WHEN** 导出完成或发生异常
- **THEN** ExcelWriter 自动关闭
- **AND** HttpServletResponse 输出流正确关闭
- **AND** 无内存泄漏或资源泄漏

### Requirement: 支持用户实体的流式批量导出
系统 SHALL 支持用户实体的流式批量导出，复用基于 ResultHandler 的流式导出框架。

#### Scenario: 用户实体流式导出
- **WHEN** 执行用户数据导出
- **THEN** 使用与角色导出相同的 ResultHandler + EasyExcel 流式框架
- **AND** 每 100 行写入 Excel 并清空批次
- **AND** 峰值内存 < 2MB
