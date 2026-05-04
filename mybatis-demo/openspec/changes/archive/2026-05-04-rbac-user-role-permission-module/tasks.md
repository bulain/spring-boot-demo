## 1. 数据库迁移

- [x] 1.1 创建 MySQL Flyway 迁移脚本 V20241111120000__init_rbac_tables.sql
- [x] 1.2 创建 5 张表：sys_users, sys_roles, sys_permissions, sys_user_roles, sys_role_permissions
- [x] 1.3 添加主键、唯一约束和普通索引
- [x] 1.4 验证数据库表结构正确

## 2. 实体类和 Mapper 层

- [x] 2.1 创建 SysUser 实体类（含审计字段、乐观锁 pubts、逻辑删除 dr）
- [x] 2.2 创建 SysRole 实体类（含审计字段、乐观锁 pubts、逻辑删除 dr）
- [x] 2.3 创建 SysPermission 实体类（含审计字段、无逻辑删除）
- [x] 2.4 创建 SysUserRole 关联实体类（含审计字段、无逻辑删除）
- [x] 2.5 创建 SysRolePermission 关联实体类（含审计字段、无逻辑删除）
- [x] 2.6 创建 SysUserMapper 接口（继承 BaseMapper）
- [x] 2.7 创建 SysRoleMapper 接口（继承 BaseMapper）
- [x] 2.8 创建 SysPermissionMapper 接口（继承 BaseMapper）
- [x] 2.9 创建 SysUserRoleMapper 接口（继承 BaseMapper）
- [x] 2.10 创建 SysRolePermissionMapper 接口（继承 BaseMapper）

## 3. DTO 和 VO 类

- [x] 3.1 创建用户相关 DTO（CreateUserDTO, UpdateUserDTO, UserQueryDTO）
- [x] 3.2 创建角色相关 DTO（CreateRoleDTO, UpdateRoleDTO, RoleQueryDTO）
- [x] 3.3 创建权限相关 DTO（CreatePermissionDTO, UpdatePermissionDTO, PermissionQueryDTO）
- [x] 3.4 创建用户角色分配 DTO（UserRoleAssignDTO）
- [x] 3.5 创建角色权限分配 DTO（RolePermissionAssignDTO）
- [x] 3.6 创建登录 DTO（LoginDTO, PhoneLoginDTO, WechatLoginDTO, DingtalkLoginDTO）
- [x] 3.7 创建导出导入 Excel DTO（使用 EasyExcel 注解）

## 4. Service 层实现

- [x] 4.1 创建 SysUserService 接口和实现类
- [x] 4.2 实现用户 CRUD 操作（含分页查询、按姓名/用户名模糊查询）
- [x] 4.3 实现用户启用/禁用功能
- [x] 4.4 实现密码重置功能
- [x] 4.5 实现用户权限树查询
- [x] 4.6 实现用户权限编码列表查询
- [x] 4.7 创建 SysRoleService 接口和实现类
- [x] 4.8 实现角色 CRUD 操作
- [x] 4.9 创建 SysPermissionService 接口和实现类
- [x] 4.10 实现权限 CRUD 操作
- [x] 4.11 创建 SysUserRoleService 接口和实现类（用户角色分配）
- [x] 4.12 创建 SysRolePermissionService 接口和实现类（角色权限分配）
- [x] 4.13 实现乐观锁更新处理（pubts 字段）
- [x] 4.14 实现逻辑删除处理（dr 字段）

## 5. 权限校验注解和 AOP

- [x] 5.1 创建 @RequiresPermission 自定义注解
- [x] 5.2 创建 @RequiresRole 自定义注解
- [x] 5.3 实现 PermissionCheckAspect AOP 切面
- [x] 5.4 实现基于权限编码的访问控制
- [x] 5.5 实现基于角色的访问控制

## 6. 导入导出功能

- [x] 6.1 集成 EasyExcel 依赖（需要添加到 pom.xml）
- [x] 6.2 实现用户数据 Excel 导出
- [x] 6.3 实现用户数据 Excel 导入（含数据验证）
- [x] 6.4 实现角色数据 Excel 导出
- [x] 6.5 实现角色数据 Excel 导入
- [x] 6.6 实现权限数据 Excel 导出
- [x] 6.7 实现权限数据 Excel 导入
- [x] 6.8 实现用户角色分配 Excel 导出导入
- [x] 6.9 实现角色权限分配 Excel 导出导入
- [x] 6.10 提供导入模板下载

## 7. Controller 层实现

- [x] 7.1 创建 SysUserController REST API
- [x] 7.2 创建 SysRoleController REST API
- [x] 7.3 创建 SysPermissionController REST API
- [x] 7.4 创建用户权限查询接口
- [x] 7.5 统一响应结构封装（统一返回格式）
- [x] 7.6 全局异常处理

## 8. 登录认证

- [x] 8.1 实现用户名密码登录接口
- [x] 8.2 实现手机号验证码登录接口
- [x] 8.3 集成微信 OAuth 登录
- [x] 8.4 集成钉钉 OAuth 登录
- [x] 8.5 实现第三方账号绑定/解绑接口
- [x] 8.6 实现 JWT Token 生成和验证

## 9. 测试

- [x] 9.1 编写 Mapper 层单元测试
- [x] 9.2 编写 Service 层单元测试
- [x] 9.3 编写 Controller 层单元测试
- [x] 9.4 测试导入导出功能
- [x] 9.5 测试权限校验功能
- [x] 9.6 测试乐观锁并发场景
- [x] 9.7 测试逻辑删除功能
