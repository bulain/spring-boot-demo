## Context

**当前状态：**
- MyBatis Plus 默认使用 `IdentifierGenerator` 生成分布式 ID（雪花算法）
- 默认 `DefaultIdentifierGenerator` 基于网卡 MAC 地址生成 workerId
- Docker 环境下多实例共享相同的网络命名空间，导致 MAC 地址相同，产生重复 workerId

**约束条件：**
- workerId 和 datacenterId 的取值范围均为 0-31（雪花算法要求）
- 必须兼容现有单机部署（无 Redis 环境）
- 不能影响应用启动流程（Redis 故障时需降级）

## Goals / Non-Goals

**Goals:**
- 通过 Redis 原子分配确保多实例 workerId 唯一性
- 支持配置文件统一管理 datacenterId
- Redis 不可用时优雅降级

**Non-Goals:**
- 不修改 MyBatis Plus 核心 ID 生成逻辑
- 不实现 Redis 集群节点感知的动态调整
- 不持久化 workerId 到本地文件

## Decisions

### Decision 1: 使用 Redis INCR 原子操作分配 workerId

**Rationale:**
- INCR 是 Redis 原子操作，保证并发安全
- 简单高效，不需要分布式锁
- 自动取模 32 确保 workerId 在有效范围

**Alternatives considered:**
- **Redis Hash + Lua 脚本**: 更复杂，收益有限
- **ZooKeeper 临时节点**: 引入额外依赖，过重
- **随机数 + 重试**: 无法保证绝对唯一

### Decision 2: Redis 不可用时降级为默认机制

**Rationale:**
- 保证应用在任何环境下都能启动
- 单机部署场景通常不会有 ID 冲突问题
- 通过 WARN 日志提示用户潜在风险

**Alternatives considered:**
- **启动失败**: 过于严格，影响开发体验
- **随机生成 workerId**: 仍有小概率冲突，不如使用官方默认实现

### Decision 3: datacenterId 仅从配置文件读取

**Rationale:**
- datacenterId 属于部署层面的静态配置，不应动态分配
- 通常一个数据中心的所有实例共享相同的 datacenterId
- 配置文件管理更符合 Spring Boot 生态习惯

## Risks / Trade-offs

| 风险 | 缓解措施 |
|------|----------|
| Redis INCR 值无限增长 | 对 32 取模，理论上不会溢出（Long.MAX_VALUE = 9e18） |
| Redis 重启后计数器重置 | 取模机制确保在 0-31 循环，重启后只是重新开始分配，不影响唯一性 |
| 实例数 > 32 时 workerId 重复 | 32 个 workerId × 32 个 datacenterId = 1024 个唯一组合，通常足够；超过时建议拆分 datacenter |
