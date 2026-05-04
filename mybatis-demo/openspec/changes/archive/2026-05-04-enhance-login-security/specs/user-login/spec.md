## MODIFIED Requirements

### Requirement: Password Verification

用户名密码登录 SHALL 验证密码正确性，并进行安全检查。

#### Scenario: 正确密码
- **WHEN** 用户提交有效的用户名和正确的密码
- **THEN** 验证通过
- **AND** 登录成功，返回 token、用户信息和权限列表
- **AND** 重置该用户和 IP 的登录失败计数

#### Scenario: 错误密码
- **WHEN** 用户提供的密码不正确
- **THEN** 返回错误提示："用户名或密码错误"
- **AND** 增加该用户和 IP 的登录失败计数

#### Scenario: 账户已锁定
- **WHEN** 用户账户已被锁定（连续失败次数过多）
- **THEN** 返回错误提示："账户已被锁定，请稍后再试或联系管理员"
- **AND** 拒绝登录

#### Scenario: IP 已被限流
- **WHEN** 当前 IP 登录失败次数过多已被限流
- **THEN** 返回错误提示："登录过于频繁，请稍后再试"
- **AND** 拒绝登录

#### Scenario: 需要验证码但未提供
- **WHEN** 需要验证码但用户未提供或验证码为空
- **THEN** 返回错误提示："请输入验证码"
- **AND** 返回需要验证码的标记和 captchaId

#### Scenario: 验证码错误
- **WHEN** 提供的验证码不正确或已过期
- **THEN** 返回错误提示："验证码错误或已过期"
- **AND** 返回新的 captchaId
