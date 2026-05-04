## 1. 重构监听器

- [x] 1.1 创建 RoleImportListener，支持 Function 回调
- [x] 1.2 实现批次阈值触发（100行）
- [x] 1.3 实现 doAfterAllAnalysed 处理剩余行

## 2. Service 层

- [x] 2.1 新增 SysRoleService.importExcel(InputStream) 方法
- [x] 2.2 新增 processImportBatch 方法，REQUIRES_NEW 事务
- [x] 2.3 实现结果聚合（多批次结果合并）

## 3. Controller 层

- [x] 3.1 重构 SysRoleController.importRoles 改为调用流式接口
- [x] 3.2 删除旧的 RoleReadListener

## 4. 测试

- [x] 4.1 编写流式导入单元测试（验证分批）
- [x] 4.2 编写批次事务测试（150行分批）
- [x] 4.3 更新现有测试为流式API
