## 1. Service 层清理

- [x] 1.1 从 OrderService 接口中删除 `Paged<Order> page(OrderSearch search)` 声明
- [x] 1.2 从 OrderServiceImpl 中删除对应的实现方法和条件构建逻辑（及 buildQueryWrapper）

## 2. Controller 层调整

- [x] 2.1 OrderController 的 page 方法不再直接调用 OrderService.page(OrderSearch)
- [x] 2.2 在 Controller 中复用 buildQueryWrapper 逻辑，调用 MyBatis Plus 标准分页方法

## 3. 验证和清理

- [x] 3.1 编译验证代码正确性
- [x] 3.2 测试用例无需修改（Controller 接口保持不变）
