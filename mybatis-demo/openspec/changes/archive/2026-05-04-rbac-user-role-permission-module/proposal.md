## Why

系统目前缺少标准的用户角色权限管理模型，无法实现细粒度的访问控制。构建 RBAC（Role-Based Access Control）标准模型为系统提供基础的权限管理能力，这是绝大多数业务系统必备的基础功能。

## What Changes

- 新增用户管理模块（User CRUD 接口）
- 新增角色管理模块（Role CRUD 接口）
- 新增权限管理模块（Permission CRUD 接口）
- 新增用户-角色分配接口（为用户分配/移除角色）
- 新增角色-权限分配接口（为角色分配/移除权限）
- 新增权限校验中间件/注解（基于用户角色权限进行访问控制）
- 新增用户权限查询接口（查询用户具有的所有角色和权限列表）
- 新增用户密码重置功能（管理员可重置用户密码）
- 新增多渠道登录支持：手机号、微信、钉钉
- 新增用户、角色、权限的导入导出功能（Excel格式）
- 新增用户角色分配、角色权限分配的导入导出功能（Excel格式）

## Capabilities

### New Capabilities
- `user-management`: 用户管理能力，包括用户的增删改查、启用/禁用状态管理、密码重置及多渠道登录（手机号、微信、钉钉）
- `role-management`: 角色管理能力，包括角色的增删改查及角色编码管理
- `permission-management`: 权限管理能力，包括权限的增删改查及权限资源定义
- `user-role-assignment`: 用户角色分配能力，支持为用户分配多个角色
- `role-permission-assignment`: 角色权限分配能力，支持为角色分配多个权限
- `permission-check`: 权限校验能力，基于用户角色权限链进行访问控制
- `user-permission-query`: 用户权限查询能力，获取用户的完整权限树（包含角色和权限列表）

### Modified Capabilities
（无）

## Impact

- 新增实体类：SysUser、SysRole、SysPermission、SysUserRole、SysRolePermission
- 新增 Mapper 层：各实体对应的 Mapper 接口
- 新增 Service 层：业务逻辑处理
- 新增 Controller 层：REST API 接口
- 新增数据库表：5 张新表（sys_users, sys_roles, sys_permissions, sys_user_roles, sys_role_permissions）
- 新增 Flyway 迁移脚本
- 新增权限校验注解及切面
- 新增权限聚合查询逻辑（递归获取用户所有角色的所有权限）
- 新增密码加密存储（使用 BCrypt 或类似算法）
- 新增用户表字段：手机号、微信OpenID、钉钉UserID
- 新增短信验证码服务集成
- 新增微信OAuth登录集成
- 新增钉钉OAuth登录集成
- 新增Excel导入导出支持（EasyExcel）
