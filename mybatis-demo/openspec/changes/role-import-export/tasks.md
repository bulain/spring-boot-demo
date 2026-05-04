## 1. 依赖配置

- [x] 1.1 在 pom.xml 中添加 EasyExcel 4.0.3 依赖

## 2. 数据模型

- [x] 2.1 创建 RoleExcelVO 类，添加 ExcelProperty 注解（复用 SysRoleExcel）
- [x] 2.2 创建 ImportResultVO 类（成功数、失败数、错误列表）
- [x] 2.3 创建 ErrorRecord 类（行号、错误信息）

## 3. 导入监听器

- [x] 3.1 创建 RoleReadListener 继承 ReadListener
- [x] 3.2 实现批量处理逻辑（每 50 条一批）
- [x] 3.3 实现数据校验逻辑（必填字段、格式校验）
- [x] 3.4 实现结果统计收集（成功、更新、失败数）

## 4. 业务服务层

- [x] 4.1 在 SysRoleService 接口添加 export 方法
- [x] 4.2 在 SysRoleService 接口添加 importExcel 方法
- [x] 4.3 实现按条件筛选导出逻辑
- [x] 4.4 实现按 ID 列表选择导出逻辑
- [x] 4.5 实现导入数据的 upsert 逻辑（根据角色编码）

## 5. 控制器层

- [x] 5.1 在 SysRoleController 添加 export 接口（POST /export）
- [x] 5.2 在 SysRoleController 添加 import 接口（POST /import）
- [x] 5.3 正确设置响应头，支持文件下载

## 6. 单元测试

- [x] 6.1 编写导出功能单元测试
- [x] 6.2 编写导入功能单元测试
- [x] 6.3 编写导入数据校验测试
