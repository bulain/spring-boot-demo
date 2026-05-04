## ADDED Requirements

### Requirement: 继承层级最小化
Service 类 SHALL 尽量直接继承 MyBatis Plus 的标准基类，避免不必要的中间抽象层。

#### Scenario: OrderService 直接继承 IService
- **WHEN** 检查 OrderService 接口定义
- **THEN** OrderService 直接继承 IService<Order>
- **AND** 不通过 PagedService 间接继承

#### Scenario: OrderServiceImpl 直接继承 ServiceImpl
- **WHEN** 检查 OrderServiceImpl 类定义
- **THEN** OrderServiceImpl 直接继承 ServiceImpl<OrderMapper, Order>
- **AND** 不通过 PagedServiceImpl 间接继承

### Requirement: 保留核心业务方法
Service 层 SHALL 保留所有已有的核心业务方法。

#### Scenario: directRemove 方法保留
- **WHEN** OrderService 继承层级简化后
- **THEN** `directRemove(Wrapper<Order>)` 方法仍然存在
- **AND** 功能保持不变

### Requirement: 分页功能保持不变
分页查询功能 SHALL 保持完全可用。

#### Scenario: 分页查询功能不变
- **WHEN** 调用 OrderService 的 `page(IPage, Wrapper)` 方法
- **THEN** 返回正确的分页结果
- **AND** Controller 层调用方式不变
