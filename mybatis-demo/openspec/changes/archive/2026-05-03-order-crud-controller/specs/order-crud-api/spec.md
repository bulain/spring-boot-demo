## ADDED Requirements

### Requirement: 根据 ID 查询订单
系统 SHALL 提供根据订单 ID 查询单个订单详情的接口。

#### Scenario: 查询存在的订单
- **WHEN** 客户端发送 GET 请求到 `/demo/order/{id}`，且该 ID 对应的订单存在
- **THEN** 系统返回 200 状态码和订单详情数据

#### Scenario: 查询不存在的订单
- **WHEN** 客户端发送 GET 请求到 `/demo/order/{id}`，且该 ID 对应的订单不存在
- **THEN** 系统返回 404 状态码或空数据

### Requirement: 查询订单列表
系统 SHALL 提供根据条件查询订单列表的接口。

#### Scenario: 无条件查询列表
- **WHEN** 客户端发送 GET 请求到 `/demo/order/list`，不带查询参数
- **THEN** 系统返回 200 状态码和所有订单列表

#### Scenario: 带条件查询列表
- **WHEN** 客户端发送 GET 请求到 `/demo/order/list`，携带查询条件（如订单号、状态等）
- **THEN** 系统返回 200 状态码和符合条件的订单列表

### Requirement: 分页查询订单
系统 SHALL 提供分页查询订单的接口，支持分页参数和排序。

#### Scenario: 分页查询
- **WHEN** 客户端发送 GET 请求到 `/demo/order/page`，携带分页参数（pageNum, pageSize）
- **THEN** 系统返回 200 状态码和分页结果数据（包含总记录数、当前页数据等）

### Requirement: 新增订单
系统 SHALL 提供新增订单的接口。

#### Scenario: 正常新增订单
- **WHEN** 客户端发送 POST 请求到 `/demo/order`，携带完整的订单数据
- **THEN** 系统创建新订单，返回 200 状态码和新增结果

#### Scenario: 新增订单参数校验失败
- **WHEN** 客户端发送 POST 请求到 `/demo/order`，参数不完整或格式错误
- **THEN** 系统返回 400 状态码和错误信息

### Requirement: 修改订单
系统 SHALL 提供修改订单的接口。

#### Scenario: 正常修改订单
- **WHEN** 客户端发送 PUT 请求到 `/demo/order`，携带订单 ID 和修改数据
- **THEN** 系统更新订单数据，返回 200 状态码和修改结果

#### Scenario: 修改不存在的订单
- **WHEN** 客户端发送 PUT 请求到 `/demo/order`，但订单 ID 不存在
- **THEN** 系统返回操作失败标识

### Requirement: 删除订单
系统 SHALL 提供根据 ID 删除订单的接口。

#### Scenario: 正常删除订单
- **WHEN** 客户端发送 DELETE 请求到 `/demo/order/{id}`，且该订单存在
- **THEN** 系统删除该订单，返回 200 状态码和删除成功标识

#### Scenario: 删除不存在的订单
- **WHEN** 客户端发送 DELETE 请求到 `/demo/order/{id}`，且该订单不存在
- **THEN** 系统返回操作失败标识
