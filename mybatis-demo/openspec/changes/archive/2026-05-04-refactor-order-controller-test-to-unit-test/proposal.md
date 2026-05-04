## Why

当前 `OrderControllerTest` 是基于 `@SpringBootTest` 的集成测试，需要完整的 Spring 上下文和真实数据库连接，测试被标记为 `@Disabled` 无法在常规构建中执行。将其改写为纯单元测试可以实现快速执行、无需基础设施、每次构建都能运行，提升测试覆盖率和开发效率。

## What Changes

- 移除 `@SpringBootTest` 和 `@ExtendWith(SpringExtension.class)` 注解
- 改用 `@ExtendWith(MockitoExtension.class)` 进行纯单元测试
- 使用 `@Mock` 注解模拟 `OrderService` 依赖
- 使用 `@InjectMocks` 注入 `OrderController` 实例
- 移除 `@Disabled` 注解，使测试在常规构建中自动执行
- 重构所有测试方法，使用 Mockito `when/verify` 替代真实数据库交互
- 移除测试中对真实数据的依赖（`@BeforeEach` 中的真实保存操作）

## Capabilities

### New Capabilities
<!-- 无新增功能，仅测试重构 -->

### Modified Capabilities
<!-- 仅测试实现变化，不影响功能规范 -->

## Impact

- 影响代码：`src/test/java/com/bulain/mybatis/demo/ctrl/OrderControllerTest.java`
- 新增测试依赖：已存在（项目已有 Mockito 使用先例）
- 测试执行速度提升（从秒级降至毫秒级）
- 测试从需要数据库变为无需任何基础设施
