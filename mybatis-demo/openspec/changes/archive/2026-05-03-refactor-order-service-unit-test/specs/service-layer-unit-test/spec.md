## ADDED Requirements

### Requirement: Service 层单元测试无外部依赖
Service 层单元测试 SHALL 不依赖任何外部基础设施（数据库、Redis、消息队列等）。

#### Scenario: 测试类使用 Mockito 而非 SpringBootTest
- **WHEN** 编写 Service 层单元测试
- **THEN** 使用 `@ExtendWith(MockitoExtension.class)` 注解
- **AND** 不使用 `@SpringBootTest` 注解
- **AND** 测试可在无数据库、无 Redis 的环境下运行

### Requirement: 依赖对象使用 Mock 模拟
Service 的依赖对象 SHALL 使用 Mock 框架模拟，而非真实 Bean。

#### Scenario: Mapper 依赖被 Mock
- **WHEN** 测试 Service 方法
- **THEN** Mapper 层使用 `@Mock` 注解创建 Mock 对象
- **AND** 使用 `@InjectMocks` 将 Mock 注入被测试的 Service

### Requirement: 验证方法调用和参数
单元测试 SHALL 验证方法被正确调用，且参数符合预期。

#### Scenario: 验证 save 方法调用和参数
- **WHEN** 测试 `orderService.save(order)`
- **THEN** verify `orderMapper.insert()` 被调用 1 次
- **AND** 使用 ArgumentCaptor 捕获并验证插入的参数值

#### Scenario: 验证 getById 返回值
- **WHEN** 测试 `orderService.getById(id)`
- **THEN** stub `orderMapper.selectById()` 返回预期对象
- **AND** 验证返回值与 stub 结果一致

### Requirement: 测试可快速执行
单元测试 SHALL 在毫秒级时间内完成执行。

#### Scenario: 测试执行速度
- **WHEN** 运行完整的 Service 层测试类
- **THEN** 所有测试用例应在 1 秒内完成
- **AND** 无需等待 Spring 上下文加载

### Requirement: 无 @Disabled 标记
单元测试 SHALL 不使用 `@Disabled` 标记，可在任何环境下重复执行。

#### Scenario: 测试可重复执行
- **WHEN** 多次运行相同的单元测试
- **THEN** 每次运行结果一致（都是通过或都是失败）
- **AND** 不会因为数据残留导致测试不稳定
