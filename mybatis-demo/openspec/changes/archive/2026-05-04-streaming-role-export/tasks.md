## 1. Service 层重构

- [x] 1.1 添加必要 import（ExcelWriter、WriteSheet、ResultHandler、ResultContext）
- [x] 1.2 抽取 `exportStreaming()` 公共流式导出方法
- [x] 1.3 重构 `export()` 方法调用流式实现
- [x] 1.4 重构 `exportByIds()` 方法调用流式实现
- [x] 1.5 删除废弃的 `writeExcel()` 全量方法

## 2. 单元测试

- [x] 2.1 空数据导出测试（0行）
- [x] 2.2 小数据量测试（99行，不满一批）
- [x] 2.3 刚好一批测试（100行）
- [x] 2.4 多批次测试（101行 = 2批次）
- [x] 2.5 按条件筛选导出测试
- [x] 2.6 按ID导出测试（含空列表异常）
