## 1. Service 层接口与实现

- [x] 1.1 在 `SysUserService` 接口添加 `export(UserQueryDTO query, HttpServletResponse response)` 方法
- [x] 1.2 在 `SysUserService` 接口添加 `exportByIds(List<String> ids, HttpServletResponse response)` 方法
- [x] 1.3 在 `SysUserServiceImpl` 实现 `exportStreaming()` 私有方法
- [x] 1.4 在 `SysUserServiceImpl` 实现 `export()` 方法
- [x] 1.5 在 `SysUserServiceImpl` 实现 `exportByIds()` 方法

## 2. Controller 层修改

- [x] 2.1 修改 `SysUserController.exportUsers()` 调用 Service 层导出方法
- [x] 2.2 在 `SysUserController` 新增 `exportUsersByIds()` 端点
- [x] 2.3 移除 Controller 中不再需要的 `convertToExcel()` 方法和导出逻辑

## 3. 测试验证

- [x] 3.1 编写 `SysUserService.export()` 单元测试（空数据、边界场景）
- [x] 3.2 编写 `SysUserService.exportByIds()` 单元测试（空列表抛异常、部分 ID 不存在）
- [x] 3.3 验证导出文件格式与原实现一致（列名、顺序、状态转换）
