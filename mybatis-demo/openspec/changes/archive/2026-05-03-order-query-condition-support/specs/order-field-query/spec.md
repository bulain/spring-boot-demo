## ADDED Requirements

### Requirement: 支持订单号查询
系统 SHALL 支持根据订单号（orderNo）进行模糊查询。

#### Scenario: 根据订单号模糊查询
- **WHEN** 客户端调用 `/demo/order/list` 或 `/demo/order/page`，传入 orderNo 参数
- **THEN** 系统返回所有订单号包含该字符串的订单记录

### Requirement: 支持订单状态查询
系统 SHALL 支持根据订单状态（status）进行精确查询。

#### Scenario: 根据订单状态查询
- **WHEN** 客户端调用 `/demo/order/list` 或 `/demo/order/page`，传入 status 参数
- **THEN** 系统返回所有状态等于该值的订单记录

### Requirement: 支持参考号查询
系统 SHALL 支持根据参考号（extnRefNo1）进行模糊查询。

#### Scenario: 根据参考号模糊查询
- **WHEN** 客户端调用 `/demo/order/list` 或 `/demo/order/page`，传入 extnRefNo1 参数
- **THEN** 系统返回所有参考号包含该字符串的订单记录

### Requirement: 支持创建日期范围查询
系统 SHALL 支持根据创建日期范围进行查询。

#### Scenario: 根据创建日期开始时间查询
- **WHEN** 客户端调用 `/demo/order/list` 或 `/demo/order/page`，仅传入 createdDateFrom 参数
- **THEN** 系统返回所有创建日期大于等于该时间的订单记录

#### Scenario: 根据创建日期结束时间查询
- **WHEN** 客户端调用 `/demo/order/list` 或 `/demo/order/page`，仅传入 createdDateTo 参数
- **THEN** 系统返回所有创建日期小于等于该时间的订单记录

#### Scenario: 根据创建日期范围查询
- **WHEN** 客户端调用 `/demo/order/list` 或 `/demo/order/page`，同时传入 createdDateFrom 和 createdDateTo 参数
- **THEN** 系统返回所有创建日期在该时间范围内的订单记录
