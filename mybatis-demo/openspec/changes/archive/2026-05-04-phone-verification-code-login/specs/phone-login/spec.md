## ADDED Requirements

### Requirement: SMS Verification Code Validation

手机登录接口 SHALL 验证短信验证码的有效性。

#### Scenario: 短信验证码正确
- **WHEN** 用户提供有效的手机号和正确的短信验证码
- **THEN** 从 Redis 读取该手机号对应的验证码
- **AND** 验证验证码匹配
- **AND** 登录成功，返回 token、用户信息和权限列表

#### Scenario: 短信验证码错误
- **WHEN** 用户提供的短信验证码不正确
- **THEN** 返回错误提示："验证码错误"
- **AND** 不返回 token

#### Scenario: 短信验证码已过期
- **WHEN** 用户提供的短信验证码已超过 10 分钟有效期
- **THEN** 返回错误提示："验证码已过期"

#### Scenario: 手机号不存在
- **WHEN** 用户提供的手机号未在系统中注册
- **THEN** 返回错误提示："手机号未注册"
