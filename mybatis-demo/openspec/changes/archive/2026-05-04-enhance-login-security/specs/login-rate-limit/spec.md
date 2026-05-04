## ADDED Requirements

### Requirement: Login Failure Counting

系统 SHALL 统计每个用户和每个 IP 的登录失败次数。

#### Scenario: 登录失败增加计数（用户维度）
- **WHEN** 用户登录失败（密码错误）
- **THEN** 该用户的失败计数 +1
- **AND** 计数有效期为 15 分钟

#### Scenario: 登录失败增加计数（IP 维度）
- **WHEN** 任意用户从同一 IP 登录失败
- **THEN** 该 IP 的失败计数 +1
- **AND** 计数有效期为 15 分钟

#### Scenario: 登录成功重置计数
- **WHEN** 用户登录成功
- **THEN** 该用户的失败计数清零
- **AND** 该 IP 的失败计数清零

### Requirement: Account Locking

系统 SHALL 在用户连续登录失败达到阈值时自动锁定账户。

#### Scenario: 连续失败达到锁定阈值
- **WHEN** 用户连续登录失败达到 10 次
- **THEN** 账户被自动锁定 30 分钟
- **AND** 返回错误提示："登录失败次数过多，账户已被锁定 30 分钟"

#### Scenario: 锁定期间尝试登录
- **WHEN** 账户已锁定期间用户尝试登录
- **THEN** 返回错误提示："账户已被锁定，请稍后再试或联系管理员"
- **AND** 不延长锁定时间

#### Scenario: 锁定自动过期
- **WHEN** 锁定时间超过 30 分钟
- **THEN** 账户自动解锁
- **AND** 失败计数清零

### Requirement: IP Rate Limiting

系统 SHALL 对同一 IP 的登录失败进行限流。

#### Scenario: IP 失败达到限流阈值
- **WHEN** 同一 IP 1 分钟内登录失败达到 20 次
- **THEN** 该 IP 被限流 10 分钟
- **AND** 返回错误提示："登录过于频繁，请 10 分钟后再试"

#### Scenario: 限流期间尝试登录
- **WHEN** IP 被限流期间尝试登录
- **THEN** 返回错误提示："登录过于频繁，请稍后再试"
- **AND** 不延长限流时间
