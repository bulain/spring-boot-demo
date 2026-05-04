## 1. Service 层接口

- [x] 1.1 SysUserService 新增 importExcel(InputStream) 方法
- [x] 1.2 SysUserService 新增 processImportBatch(List<SysUserExcel>) 方法

## 2. Service 层实现

- [x] 2.1 SysUserServiceImpl 实现 importExcel()，创建 UserImportListener
- [x] 2.2 SysUserServiceImpl 实现 processImportBatch()，REQUIRES_NEW 事务
- [x] 2.3 实现批量 upsert 逻辑（根据 username 判断新增/更新）

## 3. 监听器

- [x] 3.1 新建 UserImportListener，Function 回调模式
- [x] 3.2 实现 100 行批次阈值触发
- [x] 3.3 实现行级数据校验
- [x] 3.4 实现文件内 username 去重
- [x] 3.5 删除废弃的 SysUserReadListener

## 4. Controller 层

- [x] 4.1 重构 SysUserController.importUsers() 简化调用
- [x] 4.2 返回类型从 Result<Integer> 改为 Result<ImportResultVO>

## 5. 单元测试

- [x] 5.1 基本导入成功测试（新增用户）
- [x] 5.2 包含已有用户更新测试
- [x] 5.3 空值校验失败测试（username/name 为空）
- [x] 5.4 流式分批导入测试（101 行）
- [x] 5.5 空文件导入测试
- [x] 5.6 文件内重复 username 测试
- [x] 5.7 手机号格式错误测试
- [x] 5.8 邮箱格式错误测试
