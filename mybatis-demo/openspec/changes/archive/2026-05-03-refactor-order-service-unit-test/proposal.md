## Why

当前 `OrderServiceImplTest` 是基于 `@SpringBootTest` 的集成测试，存在以下问题：
1. 测试启动慢（需要加载完整 Spring 上下文）
2. 依赖数据库和 Redis 基础设施
3. 测试用例被标记为 `@Disabled`，实际未执行
4. 集成测试失败时难以定位是业务逻辑还是环境问题

重构为纯单元测试后：执行速度快（毫秒级）、无环境依赖、可重复运行、问题定位精准。

## What Changes

- 重构 `OrderServiceImplTest`：从 `@SpringBootTest` 改为 `@ExtendWith(MockitoExtension.class)`
- 使用 `@Mock` 模拟 `OrderMapper` 依赖
- 使用 `@InjectMocks` 注入 `OrderServiceImpl`
- 移除 `@Disabled` 标记，使测试可正常执行
- 保留原有测试用例逻辑，改为 Mockito `verify()` / `thenReturn()` 验证
- 使用 ArgumentCaptor 捕获参数进行断言

## Capabilities

### New Capabilities
- `service-layer-unit-test`: Service 层纯单元测试最佳实践，使用 Mockito 无需 Spring 上下文

### Modified Capabilities
（无现有规范变更）

## Impact

- 影响代码：`OrderServiceImplTest.java`
- 测试执行速度提升：秒级 → 毫秒级
- 不再依赖数据库和 Redis 基础设施
- 无破坏性变更，仅影响测试代码
