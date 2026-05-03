## Context

**当前状态：**
- 项目已集成 Redisson 依赖
- 已有 RedisService 封装 RedisTemplate 操作
- 缺少统一的分布式锁抽象，业务代码可能直接使用 Redisson API

**约束条件：**
- 必须基于现有 Redisson 依赖，不引入新依赖
- API 风格需与现有 RedisService 保持一致
- 需支持 Spring 环境下的自动配置

## Goals / Non-Goals

**Goals:**
- 提供模板模式 API，自动管理锁的获取和释放
- 提供细粒度锁控制 API，满足复杂场景需求
- 支持可重入锁、公平锁、超时获取、看门狗自动续期
- 完全屏蔽 Redisson 实现细节，接口层不暴露 Redisson 类型

**Non-Goals:**
- 不实现锁算法（复用 Redisson）
- 不支持多锁（MultiLock）、红锁（RedLock）等高级特性（首次交付）
- 不实现锁监控指标（后续可扩展）

## Decisions

### Decision 1: 混合模式设计

**Rationale:**
- 模板模式满足 90% 常规业务场景，简洁安全
- 细粒度 API 满足复杂场景需求（如多级锁、条件重试）
- 两种模式共享同一套底层实现，保证一致性

**Alternatives considered:**
- **纯模板模式**: 简单但不够灵活
- **纯细粒度 API**: 灵活但业务代码需要写大量重复逻辑

### Decision 2: 锁对象实现 AutoCloseable

**Rationale:**
- 支持 try-with-resources 语法，避免锁泄漏
- 与 Java 7+ 语言特性保持一致
- 业务代码更简洁

### Decision 3: 异常策略 - 运行时异常

**Rationale:**
- 获取锁超时抛出运行时异常，业务代码可选择捕获或不处理
- 不强制检查异常，简化业务代码
- 与 Spring 数据访问异常体系保持一致

### Decision 4: 默认时间单位为秒

**Rationale:**
- 秒级精度足够应对绝大多数业务场景
- API 签名更简洁，减少 TimeUnit 参数传递

## Risks / Trade-offs

| 风险 | 缓解措施 |
|------|----------|
| 业务代码误用细粒度 API 导致锁泄漏 | 文档和示例优先推荐模板模式 |
| 看门狗续期导致死锁 | 提供 leaseTime 参数，建议业务显式设置合理超时 |
| Redisson 版本升级导致 API 变化 | 实现层做适配，接口层保持稳定 |
