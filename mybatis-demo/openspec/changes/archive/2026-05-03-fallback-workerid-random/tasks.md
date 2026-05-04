## 1. 核心实现

- [x] 1.1 修改 `RedisWorkerIdGenerator.getWorkerId()`：Redis 不可用时返回随机 workerId (0-31)
- [x] 1.2 更新降级场景的日志消息内容

## 2. 配置类简化

- [x] 2.1 移除 `MybatisPlusIdConfig` 中基于 `InetAddress` 的降级逻辑分支
- [x] 2.2 简化 identifierGenerator 方法，统一使用 Sequence(workerId, datacenterId)

## 3. 单元测试更新

- [x] 3.1 更新 Redis 不可用时的降级测试断言
- [x] 3.2 添加随机 workerId 范围测试 (0-31)
