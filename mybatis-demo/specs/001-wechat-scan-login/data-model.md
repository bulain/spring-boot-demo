# 微信扫码登录 - 数据模型

**创建日期**: 2026-05-05  
**功能**: 微信扫码登录

## 实体模型

### SysUser（已有实体，部分字段复用）

**说明**: 系统用户表，已有 wechat_openid 字段用于存储微信唯一标识

| 字段名 | 类型 | 说明 | 约束 |
|--------|------|------|------|
| id | varchar(64) | 用户ID | 主键 |
| username | varchar(50) | 用户名 | 唯一，非空 |
| password | varchar(255) | 密码 | 微信登录用户自动生成 |
| name | varchar(50) | 姓名 | 可为空，取自微信昵称 |
| wechat_openid | varchar(100) | 微信OpenID | 唯一索引，可为空 |
| phone | varchar(20) | 手机号 | 可为空 |
| status | int | 状态 | 0-禁用，1-启用 |

**已有索引**:
- `uk_sys_users_wechat`: (wechat_openid, dr) 唯一索引，确保一个微信只能绑定一个账号

### 新增实体：无（复用现有 SysUser）

本功能不需要新增数据库表，仅使用现有 `sys_users` 表的 `wechat_openid` 字段。

## Redis 数据结构

### 1. 微信登录 State 存储

**Key 模式**: `wechat:login:state:{state}`

**类型**: String

**Value**: 生成时间戳或简单标记

**过期时间**: 5 分钟

**用途**: 存储 OAuth2 state 参数，防止 CSRF 攻击

### 2. 微信 Access Token 缓存（可选）

**Key 模式**: `wechat:token:{openid}`

**类型**: Hash

**字段**:
- access_token: 接口调用凭证
- expires_at: 过期时间戳
- refresh_token: 刷新令牌

**用途**: 缓存微信 access_token，避免频繁调用微信接口

## 状态流转

### 微信登录状态流转

```
未登录
  ↓
生成 state + 显示二维码
  ↓
用户扫码 + 授权确认
  ↓
微信回调（携带 code + state）
  ↓
验证 state 有效性
  ↓
通过 code 换取 openid + access_token
  ↓
根据 openid 查询用户
  ├─ 用户已存在 → 登录成功
  └─ 用户不存在 → 自动创建用户 → 登录成功
```

### 绑定/解绑状态流转

```
已登录用户
  ├─ 绑定微信 → 验证微信未被绑定 → 保存 openid → 绑定成功
  └─ 解绑微信 → 验证存在其他登录方式 → 清除 openid → 解绑成功
```

## 数据一致性规则

1. **微信唯一性**: 一个微信 openid 只能绑定到一个系统账号
2. **登录方式校验**: 账号解绑微信前必须确保存在其他登录方式（密码或手机号）
3. **State 一次性**: state 参数使用后立即删除，防止重放攻击
4. **用户创建原子性**: 首次登录创建用户时需使用数据库唯一索引保证并发安全
