## Context

**当前状态：**
- `RedisWorkerIdGenerator.getWorkerId()` 在 Redis 不可用时返回 `null`
- `MybatisPlusIdConfig` 检测到 `null` 时，降级使用基于 `InetAddress.getLocalHost()` 的默认 ID 生成器
- 在 Docker 多实例场景下，MAC 地址相同导致多实例 workerId 冲突概率高

**约束条件：**
- workerId 范围必须为 0-31（雪花算法要求）
- 不得引入新的外部依赖
- 启动速度不受显著影响

## Goals / Non-Goals

**Goals:**
- Redis 不可用时，使用随机数生成 0-31 范围内的 workerId
- 降低 Docker 多实例场景下的 workerId 冲突概率
- 简化配置类中的降级逻辑

**Non-Goals:**
- 不保证 100% 唯一性（概率可接受即可）
- 不引入持久化或分布式协调机制
- 不修改正常 Redis 可用场景的逻辑

## Decisions

### Decision 1: 随机数在 `RedisWorkerIdGenerator` 内生成

**Rationale:**
- 职责更清晰：所有 workerId 分配逻辑集中在一处
- 简化配置类：只需处理 `Sequence` 创建，无需分支判断
- 便于单元测试

**Alternatives considered:**
- **在 `MybatisPlusIdConfig` 中生成随机数**：职责分散，不利于复用

### Decision 2: 使用 `ThreadLocalRandom` 而非 `Random`

**Rationale:**
- `ThreadLocalRandom` 性能更好，无锁竞争
- 适合高并发场景下的应用启动
- Java 7+ 标准库，无需额外依赖

**Alternatives considered:**
- **`SecureRandom`**: 安全性更好但性能开销大，本场景不需要加密级随机
- **`UUID` 哈希**: 复杂且无必要

### Decision 3: 移除 `InetAddress` 依赖

**Rationale:**
- 随机数方案已能满足降级场景需求
- 消除 `InetAddress.getLocalHost()` 可能抛出的 `UnknownHostException`
- 简化代码，减少依赖

## Risks / Trade-offs

| 风险 | 缓解措施 |
|------|----------|
| 随机数碰撞（多实例选择相同 workerId） | 32 个可能值，概率可接受；配合 datacenterId 配置进一步降低冲突概率 |
| 重启后 workerId 变化 | 雪花算法允许 workerId 变化，只要同一时刻不重复即可；重启期间时间窗口极短 |
| 随机数分布不均 | 使用 `ThreadLocalRandom.nextLong(32)` 保证均匀分布 |
