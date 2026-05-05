## ADDED Requirements

### Requirement: Get DingTalk QR Code
系统 SHALL 提供获取钉钉登录二维码的接口，返回二维码URL、state参数和过期时间。

#### Scenario: Get QR Code successfully
- **WHEN** 用户调用获取钉钉二维码接口
- **THEN** 系统返回二维码URL、随机state、过期时间（300秒）
- **AND** state 被存入 Redis，设置 5 分钟过期

### Requirement: DingTalk OAuth Login
系统 SHALL 实现钉钉 OAuth 登录功能，通过 authCode 和 state 完成用户认证。

#### Scenario: Login with valid code - user already bound
- **WHEN** 用户使用有效的 authCode 和 state 调用钉钉登录接口
- **THEN** 系统验证 state 有效性
- **AND** 通过 authCode 换取钉钉 userId
- **AND** 根据 userId 查询已绑定的用户
- **AND** 返回 JWT token、用户信息和权限列表

#### Scenario: Login with valid code - user not bound (auto create)
- **WHEN** 用户使用有效的 authCode 和 state 调用钉钉登录接口
- **THEN** 系统验证 state 有效性
- **AND** 通过 authCode 换取钉钉 userId
- **AND** 查询发现该钉钉未绑定任何用户
- **AND** 自动创建新用户，设置钉钉 userId、昵称等信息
- **AND** 返回 JWT token、用户信息和权限列表

#### Scenario: Login with expired state
- **WHEN** 用户使用已过期的 state 调用登录接口
- **THEN** 系统返回 "授权已过期，请重新扫码" 错误

#### Scenario: Login with invalid authCode
- **WHEN** 用户使用无效的 authCode 调用登录接口
- **THEN** 系统返回 "授权码无效或已过期，请重新扫码" 错误

#### Scenario: Login with disabled user account
- **WHEN** 用户使用有效的 authCode 登录，但账号已被禁用
- **THEN** 系统返回 "账号已被禁用" 错误

### Requirement: Bind DingTalk Account
系统 SHALL 提供钉钉账号绑定功能，允许已登录用户绑定钉钉账号。

#### Scenario: Bind DingTalk successfully
- **WHEN** 已登录用户使用有效的 authCode 和 state 调用绑定接口
- **THEN** 系统验证 state 有效性
- **AND** 通过 authCode 换取钉钉 userId
- **AND** 检查该钉钉未被其他账号绑定
- **AND** 检查当前账号未绑定钉钉
- **AND** 将钉钉 userId 保存到用户记录

#### Scenario: Bind DingTalk already bound to another account
- **WHEN** 用户绑定的钉钉已被其他账号绑定
- **THEN** 系统返回 "该钉钉已被其他账号绑定" 错误

#### Scenario: Bind DingTalk when already bound
- **WHEN** 当前账号已绑定钉钉，用户再次调用绑定接口
- **THEN** 系统返回 "当前账号已绑定钉钉，请先解绑" 错误

### Requirement: Unbind DingTalk Account
系统 SHALL 提供钉钉账号解绑功能，允许已登录用户解绑钉钉账号。

#### Scenario: Unbind DingTalk successfully
- **WHEN** 已登录用户提供正确的密码调用解绑接口
- **THEN** 系统验证密码正确性
- **AND** 检查解绑后仍有其他登录方式（手机号或密码）
- **AND** 清除用户的钉钉 userId 字段

#### Scenario: Unbind with wrong password
- **WHEN** 用户提供错误密码调用解绑接口
- **THEN** 系统返回 "密码错误"

#### Scenario: Unbind when no other login method exists
- **WHEN** 用户解绑后将无法登录（无手机号且无密码）
- **THEN** 系统返回 "解绑后无法登录，请先设置密码或绑定手机号" 错误

### Requirement: Get DingTalk Bind Status
系统 SHALL 提供钉钉绑定状态查询接口。

#### Scenario: Get bind status for bound user
- **WHEN** 用户查询钉钉绑定状态
- **THEN** 系统返回 `{"binded": true}`

#### Scenario: Get bind status for unbound user
- **WHEN** 用户查询钉钉绑定状态
- **THEN** 系统返回 `{"binded": false}`
