## ADDED Requirements

### Requirement: Send Verification Code with Image Captcha Validation

系统 SHALL 在验证图片验证码通过后发送短信验证码。

#### Scenario: 图片验证码验证成功，发送短信验证码
- **WHEN** 用户提供手机号和正确的图片验证码
- **THEN** 验证图片验证码正确
- **AND** 生成 6 位随机短信验证码
- **AND** 将短信验证码存储到 Redis（过期时间 10 分钟）
- **AND** 调用短信服务发送验证码
- **AND** 返回发送成功提示

#### Scenario: 图片验证码错误
- **WHEN** 用户提供的图片验证码不正确
- **THEN** 返回错误提示："图片验证码错误"

#### Scenario: 图片验证码已过期
- **WHEN** 用户提供的图片验证码已超过 5 分钟有效期
- **THEN** 返回错误提示："图片验证码已过期"

### Requirement: SMS Verification Code Rate Limiting

系统 SHALL 对短信验证码发送进行次数限制。

#### Scenario: 未超过次数限制
- **WHEN** 同一手机号 1 小时内发送次数小于 5 次，且 24 小时内小于 10 次
- **THEN** 正常发送短信验证码

#### Scenario: 超过 1 小时次数限制
- **WHEN** 同一手机号 1 小时内发送次数大于等于 5 次
- **THEN** 返回错误提示："1 小时内发送次数过多，请稍后再试"

#### Scenario: 超过 24 小时次数限制
- **WHEN** 同一手机号 24 小时内发送次数大于等于 10 次
- **THEN** 返回错误提示："24 小时内发送次数过多，请明天再试"
