## Purpose

OrderController 单元测试规范，确保控制器层的逻辑验证不依赖 Spring 容器或外部基础设施，实现快速执行和每次构建自动运行。

## Requirements

### Requirement: OrderController 单元测试
OrderController 测试类 SHALL 是纯单元测试，不依赖 Spring 容器或外部基础设施。

#### Scenario: 根据 ID 查询订单
- **WHEN** 调用 `getById(id)` 方法
- **THEN** 正确调用 `orderService.getById(id)` 并返回结果
- **AND** 传入的 ID 参数必须正确传递

#### Scenario: 查询订单列表
- **WHEN** 调用 `list(search)` 方法
- **THEN** 正确调用 `orderService.list(wrapper)`
- **AND** 查询条件包装器正确构建

#### Scenario: 分页查询订单
- **WHEN** 调用 `page(search)` 方法
- **THEN** 正确调用 `orderService.page(pagination, wrapper)`
- **AND** 分页参数和查询条件正确传递

#### Scenario: 新增订单
- **WHEN** 调用 `save(entity)` 方法
- **THEN** 正确调用 `orderService.save(entity)`
- **AND** 订单实体正确传递

#### Scenario: 修改订单
- **WHEN** 调用 `update(entity)` 方法
- **THEN** 正确调用 `orderService.updateById(entity)`
- **AND** 订单实体正确传递

#### Scenario: 删除订单
- **WHEN** 调用 `delete(id)` 方法
- **THEN** 正确调用 `orderService.removeById(id)`
- **AND** 传入的 ID 参数必须正确传递

#### Scenario: 按订单号模糊查询
- **WHEN** search 对象包含 orderNo
- **THEN** 构建的查询条件包含 `like(orderNo)` 条件

#### Scenario: 按参考号模糊查询
- **WHEN** search 对象包含 extnRefNo1
- **THEN** 构建的查询条件包含 `like(extnRefNo1)` 条件
