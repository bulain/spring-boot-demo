## Context

### 背景
本模块是基于 Spring Boot + MyBatis Plus 的标准 RBAC（基于角色的访问控制）权限管理系统，为系统提供用户、角色、权限的统一管理能力。

### 约束
- 基础包路径：`com.bulain.mybatis.sys`
- 数据库：MySQL（支持多数据库适配）
- 使用 Flyway 进行数据库版本管理
- 使用 EasyExcel 进行 Excel 导入导出
- 密码加密：BCrypt

### 架构分层
```
com.bulain.mybatis.sys/
├── entity/          # 实体类（SysUser, SysRole, SysPermission, SysUserRole, SysRolePermission）
├── mapper/          # MyBatis Plus Mapper 接口
├── service/         # 业务逻辑层
│   └── impl/        # 业务实现
├── controller/      # REST API 控制器
├── dto/             # 数据传输对象
├── annotation/      # 自定义注解（@RequiresPermission, @RequiresRole）
└── aspect/          # AOP 切面（权限校验切面）
```

## Goals / Non-Goals

### Goals
- 实现完整的 RBAC 权限管理模型
- 支持用户、角色、权限的 CRUD 操作
- 支持用户-角色、角色-权限的分配管理
- 支持多渠道登录（手机号、微信、钉钉）
- 支持数据导入导出（Excel 格式）
- 支持基于注解的权限校验

### Non-Goals
- 不实现完整的 OAuth2 授权服务器（仅集成第三方登录）
- 不实现复杂的菜单管理
- 不实现数据权限（行级权限）

## Decisions

### 1. 数据库表设计
**决策**: 使用 5 张表实现 RBAC 模型，表名前缀 `sys_`

**理由**: 标准 RBAC 设计，支持多对多关系，便于扩展

**替代方案**:
- 3 张表（无关联表）：不支持灵活的多对多关系

### 2. 权限校验方式
**决策**: 使用注解 + AOP 方式实现权限校验

**理由**: 对业务代码侵入性小，使用灵活

**替代方案**:
- Spring Security：配置复杂，过度设计
- 拦截器：粒度不够细

### 3. 导入导出方案
**决策**: 使用 EasyExcel

**理由**: 性能好，内存占用低，API 简洁

**替代方案**:
- Apache POI：内存占用高，API 复杂

### 4. 多渠道登录
**决策**: 在用户表增加第三方账号字段，不单独建表

**理由**: 简单直接，满足当前需求

**替代方案**:
- 单独的账号绑定表：更灵活但增加复杂度

## 数据库表结构

**说明**：以下表结构基于 MySQL 设计，后续需要 Oracle 支持时，可参考 `VARCHAR → VARCHAR2`、`DATETIME → TIMESTAMP`、`TINYINT → NUMBER(1)`、`BIGINT → NUMBER(19)` 进行适配。

**逻辑删除规则**：采用时间戳方式实现软删除
- `dr` 字段类型为 `BIGINT`（Delete Record Timestamp）
- 默认值为 `0`，表示未删除
- 删除时赋值当前时间戳（毫秒级），如 `1700000000000`
- 查询时过滤条件：`dr = 0`

**乐观锁规则**：用于用户表和角色表防止并发更新冲突
- `pubts` 字段类型为 `BIGINT`（Publish Timestamp）
- 默认值为 `0`
- 更新时条件：`pubts = 当前版本号`，成功后赋值为当前毫秒时间戳
- 若版本号不匹配则抛出异常，需重试更新

---

### 1. sys_users（用户表）

| 字段名 | 类型 | 长度 | 默认值 | 非空 | 索引 | 说明 |
|--------|------|------|--------|------|------|------|
| id | BIGINT | - | - | 是 | PRIMARY | 主键 |
| username | VARCHAR | 50 | - | 是 | UNIQUE | 用户名 |
| password | VARCHAR | 255 | - | 是 | - | 密码（BCrypt加密） |
| name | VARCHAR | 50 | - | 否 | INDEX | 姓名 |
| email | VARCHAR | 100 | - | 否 | UNIQUE | 邮箱 |
| phone | VARCHAR | 20 | - | 否 | UNIQUE | 手机号 |
| wechat_openid | VARCHAR | 100 | - | 否 | UNIQUE | 微信OpenID |
| dingtalk_userid | VARCHAR | 100 | - | 否 | UNIQUE | 钉钉UserID |
| status | TINYINT | - | 1 | 是 | INDEX | 状态：1-启用，0-禁用 |
| created_by | BIGINT | - | - | 否 | - | 创建人ID |
| created_at | DATETIME | - | CURRENT_TIMESTAMP | 是 | - | 创建时间 |
| updated_by | BIGINT | - | - | 否 | - | 修改人ID |
| updated_at | DATETIME | - | CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 是 | - | 更新时间 |
| pubts | BIGINT | - | 0 | 是 | - | 乐观锁版本号，0表示未更新过，更新时赋值当前毫秒时间戳 |
| dr | BIGINT | - | 0 | 是 | INDEX | 逻辑删除时间戳（毫秒），0表示未删除，删除时赋值当前时间戳 |

**索引**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_username` (`username`, `dr`)
- UNIQUE KEY `uk_email` (`email`, `dr`)
- UNIQUE KEY `uk_phone` (`phone`, `dr`)
- UNIQUE KEY `uk_wechat_openid` (`wechat_openid`, `dr`)
- UNIQUE KEY `uk_dingtalk_userid` (`dingtalk_userid`, `dr`)
- KEY `idx_status` (`status`)
- KEY `idx_name` (`name`)
- KEY `idx_created_by` (`created_by`)
- KEY `idx_updated_by` (`updated_by`)
- KEY `idx_dr` (`dr`)

---

### 2. sys_roles（角色表）

| 字段名 | 类型 | 长度 | 默认值 | 非空 | 索引 | 说明 |
|--------|------|------|--------|------|------|------|
| id | BIGINT | - | - | 是 | PRIMARY | 主键 |
| name | VARCHAR | 50 | - | 是 | INDEX | 角色名称 |
| code | VARCHAR | 50 | - | 是 | UNIQUE | 角色编码 |
| description | VARCHAR | 255 | - | 否 | - | 角色描述 |
| created_by | BIGINT | - | - | 否 | - | 创建人ID |
| created_at | DATETIME | - | CURRENT_TIMESTAMP | 是 | - | 创建时间 |
| updated_by | BIGINT | - | - | 否 | - | 修改人ID |
| updated_at | DATETIME | - | CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 是 | - | 更新时间 |
| pubts | BIGINT | - | 0 | 是 | - | 乐观锁版本号，0表示未更新过，更新时赋值当前毫秒时间戳 |
| dr | BIGINT | - | 0 | 是 | INDEX | 逻辑删除时间戳（毫秒），0表示未删除，删除时赋值当前时间戳 |

**索引**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_code` (`code`, `dr`)
- KEY `idx_name` (`name`)
- KEY `idx_created_by` (`created_by`)
- KEY `idx_updated_by` (`updated_by`)

---

### 3. sys_permissions（权限表）

| 字段名 | 类型 | 长度 | 默认值 | 非空 | 索引 | 说明 |
|--------|------|------|--------|------|------|------|
| id | BIGINT | - | - | 是 | PRIMARY | 主键 |
| name | VARCHAR | 50 | - | 是 | INDEX | 权限名称 |
| code | VARCHAR | 100 | - | 是 | UNIQUE | 权限编码 |
| resource_type | VARCHAR | 50 | - | 否 | INDEX | 资源类型：menu, button, api |
| description | VARCHAR | 255 | - | 否 | - | 权限描述 |
| created_by | BIGINT | - | - | 否 | - | 创建人ID |
| created_at | DATETIME | - | CURRENT_TIMESTAMP | 是 | - | 创建时间 |
| updated_by | BIGINT | - | - | 否 | - | 修改人ID |
| updated_at | DATETIME | - | CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 是 | - | 更新时间 |

**索引**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_code` (`code`)
- KEY `idx_name` (`name`)
- KEY `idx_resource_type` (`resource_type`)
- KEY `idx_created_by` (`created_by`)
- KEY `idx_updated_by` (`updated_by`)

---

### 4. sys_user_roles（用户角色关联表）

| 字段名 | 类型 | 长度 | 默认值 | 非空 | 索引 | 说明 |
|--------|------|------|--------|------|------|------|
| id | BIGINT | - | - | 是 | PRIMARY | 主键 |
| user_id | BIGINT | - | - | 是 | INDEX | 用户ID |
| role_id | BIGINT | - | - | 是 | INDEX | 角色ID |
| created_by | BIGINT | - | - | 否 | - | 创建人ID |
| created_at | DATETIME | - | CURRENT_TIMESTAMP | 是 | - | 创建时间 |
| updated_by | BIGINT | - | - | 否 | - | 修改人ID |
| updated_at | DATETIME | - | CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 是 | - | 更新时间 |

**索引**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
- KEY `idx_user_id` (`user_id`)
- KEY `idx_role_id` (`role_id`)
- KEY `idx_created_by` (`created_by`)
- KEY `idx_updated_by` (`updated_by`)

---

### 5. sys_role_permissions（角色权限关联表）

| 字段名 | 类型 | 长度 | 默认值 | 非空 | 索引 | 说明 |
|--------|------|------|--------|------|------|------|
| id | BIGINT | - | - | 是 | PRIMARY | 主键 |
| role_id | BIGINT | - | - | 是 | INDEX | 角色ID |
| permission_id | BIGINT | - | - | 是 | INDEX | 权限ID |
| created_by | BIGINT | - | - | 否 | - | 创建人ID |
| created_at | DATETIME | - | CURRENT_TIMESTAMP | 是 | - | 创建时间 |
| updated_by | BIGINT | - | - | 否 | - | 修改人ID |
| updated_at | DATETIME | - | CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 是 | - | 更新时间 |

**索引**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`)
- KEY `idx_role_id` (`role_id`)
- KEY `idx_permission_id` (`permission_id`)
- KEY `idx_created_by` (`created_by`)
- KEY `idx_updated_by` (`updated_by`)

**索引**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`)
- KEY `idx_role_id` (`role_id`)
- KEY `idx_permission_id` (`permission_id`)

---

## API 接口设计

### 统一响应结构

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1699999999999
}
```

### 分页查询统一结构

**请求参数**:
```
GET /api/xxx?pageNum=1&pageSize=10&keyword=xxx
```

**响应结构**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 10
  }
}
```

---

### 用户管理接口

#### 1. 创建用户
```
POST /api/sys/users
```

**请求参数**:
```json
{
  "username": "admin",
  "password": "123456",
  "name": "管理员",
  "email": "admin@example.com",
  "phone": "13800138000"
}
```

**响应数据**:
```json
{
  "id": 1,
  "username": "admin",
  "name": "管理员",
  "email": "admin@example.com",
  "phone": "13800138000",
  "status": 1,
  "createdAt": "2023-11-11 12:00:00"
}
```

---

#### 2. 查询用户详情
```
GET /api/sys/users/{id}
```

**请求参数**:
- `id` (path): 用户ID

**响应数据**:
```json
{
  "id": 1,
  "username": "admin",
  "name": "管理员",
  "email": "admin@example.com",
  "phone": "13800138000",
  "wechatOpenid": "wx_123",
  "dingtalkUserid": "ding_456",
  "status": 1,
  "createdAt": "2023-11-11 12:00:00"
}
```

---

#### 3. 分页查询用户列表
```
GET /api/sys/users
```

**请求参数**:
- `pageNum`: 页码（默认1）
- `pageSize`: 每页数量（默认10）
- `username`: 用户名（模糊查询，可选）
- `name`: 姓名（模糊查询，可选）
- `phone`: 手机号（模糊查询，可选）
- `status`: 状态（精确查询，可选）

**响应数据**: 分页结构，list 为用户详情数组

---

#### 4. 更新用户
```
PUT /api/sys/users/{id}
```

**请求参数**:
```json
{
  "name": "超级管理员",
  "email": "super@example.com",
  "phone": "13900139000"
}
```

**响应数据**: 更新后的用户详情

---

#### 5. 删除用户
```
DELETE /api/sys/users/{id}
```

**请求参数**:
- `id` (path): 用户ID

**响应数据**: 无（204 No Content）

---

#### 6. 启用/禁用用户
```
PUT /api/sys/users/{id}/status
```

**请求参数**:
```json
{
  "status": 0
}
```

**响应数据**: 更新后的用户详情

---

#### 7. 重置密码
```
PUT /api/sys/users/{id}/password
```

**请求参数**:
```json
{
  "newPassword": "654321"
}
```

**响应数据**: 无（204 No Content）

---

#### 8. 查询用户角色
```
GET /api/sys/users/{id}/roles
```

**请求参数**:
- `id` (path): 用户ID

**响应数据**:
```json
{
  "userId": 1,
  "roles": [
    {
      "id": 1,
      "name": "超级管理员",
      "code": "SUPER_ADMIN"
    }
  ]
}
```

---

#### 9. 分配用户角色
```
PUT /api/sys/users/{id}/roles
```

**请求参数**:
```json
{
  "roleIds": [1, 2, 3]
}
```

**响应数据**: 无（204 No Content）

---

#### 10. 导出用户数据
```
POST /api/sys/users/export
```

**请求参数**:
```json
{
  "username": "admin",
  "status": 1
}
```

**响应**: Excel 文件下载

---

#### 11. 导入用户数据
```
POST /api/sys/users/import
Content-Type: multipart/form-data
```

**请求参数**:
- `file`: Excel 文件

**响应数据**:
```json
{
  "successCount": 95,
  "failCount": 5,
  "failDetails": [
    {
      "row": 3,
      "username": "user1",
      "reason": "用户名已存在"
    }
  ]
}
```

---

### 角色管理接口

#### 1. 创建角色
```
POST /api/sys/roles
```

**请求参数**:
```json
{
  "name": "超级管理员",
  "code": "SUPER_ADMIN",
  "description": "系统超级管理员"
}
```

**响应数据**:
```json
{
  "id": 1,
  "name": "超级管理员",
  "code": "SUPER_ADMIN",
  "description": "系统超级管理员",
  "status": 1,
  "createdAt": "2023-11-11 12:00:00"
}
```

---

#### 2. 查询角色详情
```
GET /api/sys/roles/{id}
```

**响应数据**: 角色详情

---

#### 3. 查询角色列表
```
GET /api/sys/roles
```

**请求参数**:
- `pageNum`: 页码
- `pageSize`: 每页数量
- `name`: 角色名称（模糊查询，可选）
- `code`: 角色编码（精确查询，可选）
- `status`: 状态（可选）

**响应数据**: 分页角色列表

---

#### 4. 更新角色
```
PUT /api/sys/roles/{id}
```

**请求参数**:
```json
{
  "name": "系统管理员",
  "description": "更新后的描述"
}
```

**响应数据**: 更新后的角色详情

---

#### 5. 删除角色
```
DELETE /api/sys/roles/{id}
```

**响应**: 204 No Content

---

#### 6. 查询角色权限
```
GET /api/sys/roles/{id}/permissions
```

**响应数据**:
```json
{
  "roleId": 1,
  "permissions": [
    {
      "id": 1,
      "name": "用户管理",
      "code": "user:manage"
    }
  ]
}
```

---

#### 7. 分配角色权限
```
PUT /api/sys/roles/{id}/permissions
```

**请求参数**:
```json
{
  "permissionIds": [1, 2, 3]
}
```

**响应**: 204 No Content

---

#### 8. 导出/导入角色
与用户模块结构一致，字段为角色相关字段

---

### 权限管理接口

#### 1. 创建权限
```
POST /api/sys/permissions
```

**请求参数**:
```json
{
  "name": "用户新增",
  "code": "user:add",
  "resourceType": "button",
  "description": "用户新增权限"
}
```

**响应数据**:
```json
{
  "id": 1,
  "name": "用户新增",
  "code": "user:add",
  "resourceType": "button",
  "description": "用户新增权限",
  "status": 1,
  "createdAt": "2023-11-11 12:00:00"
}
```

---

#### 2. 查询权限详情
```
GET /api/sys/permissions/{id}
```

**响应数据**: 权限详情

---

#### 3. 查询权限列表
```
GET /api/sys/permissions
```

**请求参数**:
- `pageNum`: 页码
- `pageSize`: 每页数量
- `name`: 权限名称（模糊查询，可选）
- `code`: 权限编码（精确查询，可选）
- `resourceType`: 资源类型（精确查询，可选）
- `status`: 状态（可选）

**响应数据**: 分页权限列表

---

#### 4. 更新权限
```
PUT /api/sys/permissions/{id}
```

**请求参数**: 权限更新字段
**响应数据**: 更新后的权限详情

---

#### 5. 删除权限
```
DELETE /api/sys/permissions/{id}
```

**响应**: 204 No Content

---

#### 6. 导出/导入权限
与用户模块结构一致，字段为权限相关字段

---

### 用户权限查询接口

#### 1. 查询用户权限列表
```
GET /api/sys/users/{id}/permissions
```

**响应数据**:
```json
{
  "userId": 1,
  "permissions": [
    {
      "id": 1,
      "name": "用户新增",
      "code": "user:add",
      "resourceType": "button"
    }
  ]
}
```

---

#### 2. 查询用户权限树
```
GET /api/sys/users/{id}/permission-tree
```

**响应数据**:
```json
{
  "userId": 1,
  "roles": [
    {
      "id": 1,
      "name": "超级管理员",
      "code": "SUPER_ADMIN",
      "permissions": [
        {
          "id": 1,
          "name": "用户管理",
          "code": "user:manage"
        }
      ]
    }
  ]
}
```

---

#### 3. 查询用户权限编码列表
```
GET /api/sys/users/{id}/permission-codes
```

**响应数据**:
```json
{
  "userId": 1,
  "permissionCodes": ["user:add", "user:edit", "user:delete", "role:manage"]
}
```

---

### 登录接口

#### 1. 用户名密码登录
```
POST /api/sys/auth/login
```

**请求参数**:
```json
{
  "username": "admin",
  "password": "123456"
}
```

**响应数据**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "userInfo": {
    "id": 1,
    "username": "admin",
    "name": "管理员"
  },
  "permissionCodes": ["user:manage", "role:manage"]
}
```

---

#### 2. 手机号验证码登录
```
POST /api/sys/auth/phone-login
```

**请求参数**:
```json
{
  "phone": "13800138000",
  "code": "123456"
}
```

**响应数据**: 同用户名密码登录

---

#### 3. 微信登录
```
POST /api/sys/auth/wechat-login
```

**请求参数**:
```json
{
  "code": "wx_auth_code",
  "state": "random_state"
}
```

**响应数据**: 同用户名密码登录

---

#### 4. 钉钉登录
```
POST /api/sys/auth/dingtalk-login
```

**请求参数**:
```json
{
  "authCode": "ding_auth_code"
}
```

**响应数据**: 同用户名密码登录

## Risks / Trade-offs

### 风险与缓解
| 风险 | 影响 | 缓解措施 |
|------|------|----------|
| 批量导入数据量大导致性能问题 | 高 | 分批导入，限制单次导入数量 |
| 权限查询频繁导致性能瓶颈 | 中 | 使用Redis缓存用户权限编码列表 |
| 第三方登录接口变更 | 低 | 使用适配器模式，便于切换实现 |
| 密码重置安全风险 | 高 | 管理员操作需记录日志，临时密码需强制修改 |

### 权衡
- **简化 vs 灵活**: 第三方账号直接存入用户表而非单独建表 → 简化设计，满足当前需求
- **性能 vs 功能**: 权限查询使用聚合查询而非单独缓存 → 先实现功能，后续按需优化

## Migration Plan

### Flyway 迁移脚本（双数据库支持）

**命名规则**：`VYYYYMMDDHHmmss__{描述}.sql`（基于时间戳）

**MySQL 脚本**：
- 位置：`src/main/resources/migration/mysql/V20241111120000__init_rbac_tables.sql`
- 数据类型：BIGINT, VARCHAR, DATETIME, TINYINT
- 自增主键：`BIGINT NOT NULL AUTO_INCREMENT`
- 索引：标准 MySQL 索引语法

**Oracle 脚本**：
- 位置：`src/main/resources/migration/oracle/V20241111120000__init_rbac_tables.sql`
- 数据类型：NUMBER(19), VARCHAR2, TIMESTAMP, NUMBER(1)
- 自增主键：使用 SEQUENCE + TRIGGER 或 `GENERATED AS IDENTITY`
- 索引：标准 Oracle 索引语法

**脚本包含内容**：
- 5 张表的创建语句
- 主键、唯一约束、普通索引
- 逻辑删除字段默认值设置

### 回滚策略
- 开发环境：直接 drop 表后重新创建
- 生产环境：创建 V2 脚本进行回滚调整

## Open Questions

暂无
