## Why

Docker 环境下多实例部署时，MyBatis Plus 的分布式 ID 生成器默认基于机器 MAC 地址生成 workerId，导致多个容器实例生成相同的 workerId，最终产生重复 ID。需要通过 Redis 原子分配机制解决这个问题。

## What Changes

- 新增 Redis 原子分配 workerId 的初始化机制
- 支持从配置文件读取 datacenterId
- 新增分布式 ID 生成器的配置类
- 兼容现有单机部署场景（无 Redis 时降级为默认机制）

## Capabilities

### New Capabilities
- `redis-workerid-allocation`: 通过 Redis 原子分配 MyBatis Plus 分布式 ID 生成器的 workerId

### Modified Capabilities
(无)

## Impact

- **新增依赖**: spring-boot-starter-data-redis（可选依赖，已存在）
- **新增配置**: `mybatis-plus.global-config.worker-id`、`mybatis-plus.global-config.datacenter-id` 相关配置
- **新增代码**: `com.bulain.mybatis.config` 包下的配置类
- **影响范围**: 所有使用 MyBatis Plus 分布式 ID 生成的实体类
