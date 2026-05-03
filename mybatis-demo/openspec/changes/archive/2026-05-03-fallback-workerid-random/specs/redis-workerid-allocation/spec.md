## MODIFIED Requirements

### Requirement: Redis 原子分配 workerId

#### Scenario: Redis 不可用时降级
- **WHEN** 应用启动但 Redis 连接不可用
- **THEN** 系统生成 0-31 范围内的随机 workerId
- **AND** 记录 WARN 级别日志提示用户降级为随机 workerId
