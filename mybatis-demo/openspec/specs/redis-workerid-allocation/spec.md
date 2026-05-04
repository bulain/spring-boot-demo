## Purpose

通过 Redis 原子分配机制确保 MyBatis Plus 分布式 ID 生成器的 workerId 唯一性，解决 Docker 多实例部署场景下的 ID 重复问题。

## Requirements

### Requirement: Redis 原子分配 workerId
系统 SHALL 通过 Redis 的原子自增操作分配 MyBatis Plus 分布式 ID 生成器的 workerId，避免 Docker 多实例部署时产生重复 ID。

#### Scenario: Redis 可用时分配 workerId
- **WHEN** 应用启动且 Redis 连接可用
- **THEN** 系统通过 Redis INCR 原子操作获取唯一 workerId
- **AND** workerId 的范围为 0-31（符合雪花算法要求）

#### Scenario: Redis 不可用时降级
- **WHEN** 应用启动但 Redis 连接不可用
- **THEN** 系统生成 0-31 范围内的随机 workerId
- **AND** 记录 WARN 级别日志提示用户降级为随机 workerId

#### Scenario: workerId 取模循环
- **WHEN** Redis 自增的 workerId 超过 31
- **THEN** 系统自动对 32 取模，确保 workerId 始终在有效范围内

### Requirement: 配置文件读取 datacenterId
系统 SHALL 支持从配置文件读取 datacenterId，统一管理多数据中心部署的 ID 生成配置。

#### Scenario: 配置文件指定 datacenterId
- **WHEN** 配置文件中设置了 `mybatis-plus.global-config.datacenter-id`
- **THEN** 系统使用配置的值作为 datacenterId
- **AND** 取值范围验证为 0-31

#### Scenario: 未配置 datacenterId
- **WHEN** 配置文件未设置 datacenterId
- **THEN** 系统使用默认值 0
