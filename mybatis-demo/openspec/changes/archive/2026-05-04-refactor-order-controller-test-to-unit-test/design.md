## Context

当前 `OrderControllerTest` 是集成测试，使用 `@SpringBootTest` 启动完整 Spring 上下文，依赖真实数据库和 Redis 基础设施。由于基础设施要求，测试被标记为 `@Disabled`，在 `mvn test` 时不执行，导致控制器层没有有效的测试覆盖。

项目中已有纯单元测试先例（如 `OrderServiceImplTest`），使用 Mockito 模拟依赖，无需任何基础设施即可运行。

## Goals / Non-Goals

**Goals:**
- 将 `OrderControllerTest` 改造为纯单元测试，每次构建自动执行
- 使用 Mockito 模拟 `OrderService` 依赖，验证 Controller 层的调用正确性
- 保持所有原测试场景的覆盖（getById、list、page、save、update、delete）
- 测试执行速度从秒级降至毫秒级

**Non-Goals:**
- 不修改 `OrderController` 生产代码
- 不改变 Controller 层的业务逻辑
- 不添加新的测试场景（保持原有测试范围）

## Decisions

**决策 1：使用 MockitoExtension 替代 SpringExtension**
- Rationale：纯单元测试不需要启动 Spring 容器，`MockitoExtension` 提供足够的依赖注入能力
- Alternatives：继续使用 Spring Boot Test + MockBean（仍需部分 Spring 上下文，速度较慢）

**决策 2：使用 @InjectMocks + @Mock 进行依赖注入**
- Rationale：Controller 只有一个 `OrderService` 依赖，Mockito 的注解驱动注入足够简单清晰
- Alternatives：手动通过构造函数/setter 注入（代码冗余）

**决策 3：验证方法调用参数而非仅返回值**
- Rationale：Controller 的核心职责是参数处理和委托调用，重点验证调用是否正确传递
- 使用 `ArgumentCaptor` 捕获查询条件和实体参数，确保 Controller 正确构建和传递

**决策 4：不创建 specs 文件**
- Rationale：此变更仅涉及测试实现，不影响任何用户可见的功能或 API 契约，无需新增或修改规范文档

## Risks / Trade-offs

**风险：单元测试无法覆盖 Spring Web 注解（如 @GetMapping、@RequestParam）**
→ Mitigation：保持集成测试的价值定位，Controller 的 HTTP 绑定由专门的 `@WebMvcTest` 或端到端测试覆盖，本单元测试专注于业务逻辑委托

**权衡：单元测试 vs 集成测试的分工**
→ 单元测试：快速验证 Controller 内部逻辑（查询构建、参数传递），每次构建运行
→ 集成测试：验证完整 HTTP 链路和数据库交互，在 CI 环境按需运行
