## 1. 配置类实现

- [x] 1.1 创建 `RedisWorkerIdGenerator` 类，实现 workerId 分配逻辑
- [x] 1.2 创建 `MybatisPlusIdConfig` 配置类，注入自定义 IdentifierGenerator

## 2. Redis 原子分配实现

- [x] 2.1 使用 `StringRedisTemplate` 实现 INCR 原子操作
- [x] 2.2 实现 workerId 取模 32 的逻辑
- [x] 2.3 实现 Redis 异常捕获和降级逻辑
- [x] 2.4 添加 WARN 级别日志提示

## 3. 配置属性支持

- [x] 3.1 支持从 `application.yml` 读取 datacenterId 配置
- [x] 3.2 实现 datacenterId 范围验证 (0-31)
- [x] 3.3 默认值处理（datacenterId 默认为 0）

## 4. 单元测试

- [x] 4.1 编写 Redis 可用场景的单元测试
- [x] 4.2 编写 Redis 不可用场景的降级测试
- [x] 4.3 编写配置读取和范围验证测试
