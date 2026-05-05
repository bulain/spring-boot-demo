## Context

项目中已实现完整的微信扫码登录功能，包含：
- 登录二维码生成（带 state 防 CSRF）
- OAuth 认证流程（code → access_token → openid）
- 自动创建用户
- 绑定/解绑微信
- 绑定状态查询

钉钉扫码登录目前仅在 `SysAuthController` 中存在 `/dingtalk-login` 占位端点，核心逻辑未实现。需要参考微信登录的实现模式来完善钉钉扫码登录。

## Goals / Non-Goals

**Goals:**
- 实现完整的钉钉扫码登录 OAuth 流程
- 实现钉钉绑定/解绑功能
- 实现钉钉绑定状态查询
- 保持与微信扫码登录一致的架构和代码风格

**Non-Goals:**
- 不实现钉钉 JSAPI 相关功能（仅支持扫码登录）
- 不实现钉钉小程序登录
- 不修改现有的 SysUser 数据模型（dingtalk_userid 字段已存在）

## Decisions

**1. 架构模式：复用微信登录的 Service 分层模式**

- 创建 `DingtalkLoginService` 接口和 `DingtalkLoginServiceImpl` 实现类
- Controller 层仅做参数校验和结果封装，核心逻辑在 Service 层
- 配置类 `DingtalkOpenPlatformConfig` 统一管理 appId、appSecret

**Rationale:** 保持代码库一致性，便于维护和理解

**2. OAuth 流程：使用钉钉 OAuth2 授权码模式**

- 钉钉扫码登录使用 `https://login.dingtalk.com/qrconnect` 生成二维码
- 通过 authCode 换取 userInfo 获取 userId（钉钉的 unionid 或 userId）
- 使用钉钉开放平台的 `/v1.0/oauth2/userAccessToken` 接口

**Rationale:** 钉钉官方推荐的 OAuth2 认证方式

**3. 状态管理：使用 Redis 存储 state，防止 CSRF 和重放攻击**

- 生成二维码时生成随机 state 存入 Redis（5 分钟过期）
- 登录/绑定时验证 state 有效性
- 验证后立即删除 state

**Rationale:** 与微信登录相同的安全机制，防止 CSRF 攻击

## Risks / Trade-offs

**[Risk] 钉钉 API 接口变更**
→ Mitigation: 使用钉钉官方最新 v1.0 接口，封装在 Service 实现中，便于后续升级

**[Risk] 钉钉与微信用户数据模型差异**
→ Mitigation: 复用现有 SysUser 表的 dingtalk_userid 字段，用户昵称等通用字段保持一致

**[Trade-off] 自动创建用户 vs 仅支持绑定后登录**
→ 决策：与微信登录一致，支持未绑定用户扫码时自动创建账号
→ 原因：降低用户使用门槛，提高登录体验
