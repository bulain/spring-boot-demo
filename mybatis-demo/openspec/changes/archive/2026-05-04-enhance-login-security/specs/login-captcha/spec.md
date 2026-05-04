## ADDED Requirements

### Requirement: Login Captcha Trigger

系统 SHALL 在登录失败达到阈值后触发图片验证码要求。

#### Scenario: 用户维度触发验证码
- **WHEN** 同一用户连续登录失败达到 5 次
- **THEN** 后续该用户的登录需要提供图片验证码
- **AND** 返回需要验证码的标记

#### Scenario: IP 维度触发验证码
- **WHEN** 同一 IP 1 分钟内登录失败达到 10 次
- **THEN** 后续该 IP 的所有登录需要提供图片验证码
- **AND** 返回需要验证码的标记

#### Scenario: 登录成功关闭验证码要求
- **WHEN** 用户登录成功
- **THEN** 清除该用户和 IP 的验证码触发标记
- **AND** 后续登录暂不需要验证码

#### Scenario: 提供正确验证码登录
- **WHEN** 触发验证码后用户提供了正确的验证码和正确的密码
- **THEN** 登录成功
- **AND** 清除所有失败计数和验证码标记

#### Scenario: 验证码正确但密码错误
- **WHEN** 验证码正确但密码错误
- **THEN** 返回错误提示："用户名或密码错误"
- **AND** 返回新的 captchaId 供下次尝试
- **AND** 失败计数继续增加
