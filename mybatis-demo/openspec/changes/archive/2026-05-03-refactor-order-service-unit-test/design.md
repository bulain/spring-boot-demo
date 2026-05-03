## Context

**当前状态**:
- OrderServiceImplTest 使用 `@SpringBootTest` + `@ExtendWith(SpringExtension.class)`
- 依赖 `@Autowired` 注入真实的 OrderService
- 需要数据库和 Redis 运行环境
- 测试被 `@Disabled` 标记，实际未执行

## Goals / Non-Goals

**Goals**:
- 测试用例可独立运行，无需任何外部依赖
- 测试执行速度：毫秒级完成
- 100% 测试覆盖率：所有 Service 层方法都有对应的单元测试
- 使用 Mockito 标准注解，代码简洁易读

**Non-Goals**:
- 不测试 Mapper 层（这是集成测试的责任）
- 不测试 Controller 层
- 不引入新的测试框架

## Decisions

**1. 测试框架选择**
- 使用 `@ExtendWith(MockitoExtension.class)` 代替 `@SpringBootTest`
- `@Mock OrderMapper orderMapper` - 模拟 Mapper 层
- `@InjectMocks OrderServiceImpl orderService` - 注入被测试对象
- 无需 Spring 上下文

**2. 测试用例设计**
- `save()`: verify `orderMapper.insert()` 被调用，使用 ArgumentCaptor 验证参数
- `removeById()`: verify `update()` 被调用（因为是逻辑删除）
- `directRemove()`: verify `baseMapper.delete()` 被调用
- `getById()`: when `orderMapper.selectById()` thenReturn，验证返回值
- `list()`: when `orderMapper.selectList()` thenReturn，验证结果

**3. 断言策略**
- 不仅 verify 方法调用，还使用 ArgumentCaptor 捕获参数验证
- 验证返回值符合预期
- 验证异常场景（如有）

## Risks / Trade-offs

**[风险]** Service 继承自 `ServiceImpl`，父类方法多，可能遗漏
→ 缓解：覆盖 OrderService 接口中定义的所有方法，加上常用的父类方法

**[权衡]** 纯单元测试 vs 轻量级集成测试
→ 决策：纯单元测试，专注于 Service 层业务逻辑
