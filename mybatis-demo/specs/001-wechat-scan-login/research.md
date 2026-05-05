# 微信扫码登录 - 调研文档

**创建日期**: 2026-05-05  
**功能**: 微信扫码登录

## 调研总结

本功能基于 Spring Boot 3.5 框架和微信开放平台 OAuth2.0 协议实现网页端扫码登录。项目已有基础代码框架（Controller、DTO、Entity、Service 接口），需要完善微信 OAuth2 调用逻辑、状态验证、用户自动创建、绑定/解绑功能。

## 技术决策

### 1. 微信 OAuth2 接入方案

**决策**: 使用微信开放平台网页授权流程（扫码登录）

**Rationale**:
- 项目已有 `wechat-login` 接口和 WechatLoginDTO 基础结构
- 微信开放平台提供标准 OAuth2 流程，支持生成二维码并获取用户信息
- 通过 code 换取 access_token 和 openid，再获取用户基本信息

**实现要点**:
- 前端通过 `https://open.weixin.qq.com/connect/qrconnect` 生成二维码
- 后端通过 code 调用 `sns/oauth2/access_token` 获取 access_token 和 openid
- 如需用户昵称头像，调用 `sns/userinfo` 获取用户信息

**备选方案**:
- 微信公众平台网页授权（需在微信内打开，不适合网页端扫码）
- 使用第三方 SDK（增加依赖，灵活性低）

### 2. State 参数防 CSRF 攻击

**决策**: 使用 Redis 存储 state 并设置过期时间

**Rationale**:
- OAuth2 协议标准要求使用 state 参数防止 CSRF 攻击
- 项目已集成 Redisson，可直接使用 Redis 存储
- state 需要在用户扫码前生成，验证后立即失效

**实现要点**:
- 生成 UUID 作为 state，存入 Redis 并设置 5 分钟过期
- 微信回调时验证 state 是否存在且有效
- 验证通过后立即删除 state，防止重放攻击

### 3. 首次登录自动创建用户

**决策**: 首次微信登录自动创建用户账号并分配默认角色

**Rationale**:
- 提升用户体验，无需用户先注册再绑定
- 使用微信 openid 作为唯一标识关联用户
- 需要设置默认用户名（如"微信用户_随机串"）和默认密码

**实现要点**:
- openid 不存在时，自动创建 SysUser 记录
- 用户名默认使用微信昵称，如为空则生成随机名称
- 密码使用随机生成并加密存储（用户后续可通过绑定手机号修改）
- 自动分配默认角色（如"普通用户"）

### 4. 微信绑定/解绑功能

**决策**: 在用户设置中提供微信绑定和解绑功能

**Rationale**:
- 用户可能需要更换绑定的微信账号
- 一个微信只能绑定一个系统账号
- 解绑后用户仍可通过账号密码或其他方式登录

**实现要点**:
- 绑定时检查微信是否已被其他账号绑定
- 解绑时需要用户验证（如输入密码或短信验证码）
- 账号不能同时没有密码、没有手机号且没有微信绑定

### 5. 异常处理与用户提示

**决策**: 对微信登录各环节进行异常捕获并返回友好中文提示

**Rationale**:
- 微信接口可能因网络、频率限制、参数错误等原因调用失败
- 用户可能取消授权、二维码过期等操作
- 需要给用户清晰的错误指引

**异常场景**:
- code 无效或已使用：提示"授权已过期，请重新扫码"
- 网络异常：提示"网络繁忙，请稍后重试"
- 用户取消授权：提示"您已取消授权"
- 微信接口限流：提示"系统繁忙，请稍后重试"

## 依赖说明

### 现有依赖（无需新增）
- Spring Boot 3.5.14 - Web 框架
- MyBatis Plus 3.5.16 - ORM 框架
- Redisson 3.52.0 - Redis 客户端（用于 state 存储）
- Lombok - 简化代码
- Jakarta Validation - 参数校验

### 新增配置项
```yaml
wechat:
  open-platform:
    app-id: ${WECHAT_APP_ID:}
    app-secret: ${WECHAT_APP_SECRET:}
    redirect-uri: ${WECHAT_REDIRECT_URI:http://localhost:8080/api/sys/auth/wechat-callback}
```

## 参考资料
- 微信开放平台文档：https://developers.weixin.qq.com/doc/oplatform/Website_App/WeChat_Login/Wechat_Login.html
- OAuth 2.0 安全最佳实践
