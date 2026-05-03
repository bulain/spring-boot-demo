## ADDED Requirements

### Requirement: Service 层保持最小化接口
Service 层 SHALL 只保留必要的业务方法，不提供与父类功能重复的重载方法。

#### Scenario: OrderService 只有父类的 page 方法
- **WHEN** 检查 OrderService 接口定义
- **THEN** 不存在 `page(OrderSearch search)` 方法声明
- **AND** 只能继承 `page(Search search)` 方法

### Requirement: Controller 层处理参数适配
Controller 层 SHALL 负责将 API 参数适配为 Service 层所需的格式。

#### Scenario: Controller 处理分页查询参数
- **WHEN** 客户端调用 `/demo/order/page` 接口
- **THEN** Controller 将 OrderSearch 转换为 Service 层所需的参数格式
- **AND** Controller 构建查询条件
- **AND** Controller 调用 Service 层分页方法

### Requirement: 保持接口兼容性
对外 REST API SHALL 保持参数和返回类型不变。

#### Scenario: 分页接口保持兼容
- **WHEN** 客户端调用 `/demo/order/page` 接口
- **THEN** 接口仍然接受 OrderSearch 类型参数（包含 orderNo、extnRefNo1 等条件字段）
- **AND** 接口仍然返回 Paged<Order> 类型结果
- **AND** 查询功能保持不变
