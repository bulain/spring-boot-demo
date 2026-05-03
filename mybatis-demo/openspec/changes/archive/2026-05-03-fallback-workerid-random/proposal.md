## Why

当前 Redis 不可用时的降级机制使用基于 MAC 地址的默认 workerId 生成方式。在 Docker 等容器化环境中，同一主机上的多个实例通常共享相同的网络命名空间和 MAC 地址，导致降级场景下仍可能产生重复的 workerId，进而引发 ID 冲突。使用随机数生成 workerId 可以显著降低多实例场景下的碰撞概率。

## What Changes

- 修改 `RedisWorkerIdGenerator.getWorkerId()`：Redis 不可用时，返回 0-31 范围内的随机 workerId 而非 null
- 移除 `MybatisPlusIdConfig` 中基于 `InetAddress` 的降级逻辑
- 新增随机 workerId 分配的日志记录
- 更新相应的单元测试用例

## Capabilities

### New Capabilities
（无）

### Modified Capabilities
- `redis-workerid-allocation`: 修改 Redis 不可用时的降级机制，从 MAC 地址默认方式改为随机 workerId 生成

## Impact

- **影响代码**: `com.bulain.mybatis.config.RedisWorkerIdGenerator`、`com.bulain.mybatis.config.MybatisPlusIdConfig`
- **影响测试**: `RedisWorkerIdGeneratorTest` 需更新降级场景的测试用例
- **破坏性变更**: 无（不影响公共 API 行为，仅改善降级场景下的唯一性）
