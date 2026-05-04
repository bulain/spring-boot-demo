## ADDED Requirements

### Requirement: 使用 MyBatis Plus 标准 page 方法
系统 SHALL 使用 MyBatis Plus 提供的标准 `page(E page, Wrapper<T> queryWrapper)` 方法进行分页查询。

#### Scenario: 分页查询使用标准方法
- **WHEN** 系统执行分页查询操作
- **THEN** 内部实现调用 MyBatis Plus 的标准 page 方法
- **AND** 不使用自定义的分页实现逻辑

### Requirement: 保持接口兼容性
系统 SHALL 保持 OrderController 分页接口的参数和返回类型不变。

#### Scenario: 分页接口保持原有签名
- **WHEN** 客户端调用 `/demo/order/page` 接口
- **THEN** 接口仍然接受 `OrderSearch` 类型参数
- **AND** 接口仍然返回 `Paged<Order>` 类型结果

### Requirement: 减少自定义代码
系统 SHALL 减少冗余的自定义分页实现代码。

#### Scenario: 移除重复的分页逻辑
- **WHEN** 检查 OrderServiceImpl 代码
- **THEN** 不再包含手动创建 IPage、执行查询、组装 Paged 的重复逻辑
- **AND** 核心分页逻辑通过调用父类标准方法实现
