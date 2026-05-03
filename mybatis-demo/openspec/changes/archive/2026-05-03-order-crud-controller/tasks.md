## 1. 测试准备（TDD 第一步）

- [x] 1.1 创建 OrderController 测试类
- [x] 1.2 编写查询接口测试用例（根据 ID、列表、分页）
- [x] 1.3 编写新增、修改、删除接口测试用例

## 2. 控制器层实现

- [x] 2.1 创建 `com.bulain.mybatis.demo.ctrl` 包路径
- [x] 2.2 创建 `OrderController` 类，添加 `@RestController` 和 `@RequestMapping` 注解
- [x] 2.3 注入 `OrderService` 依赖

## 3. 查询接口实现（TDD 第二步：使测试通过）

- [x] 3.1 实现根据 ID 查询订单接口（GET /demo/order/{id}）
- [x] 3.2 实现查询订单列表接口（GET /demo/order/list）
- [x] 3.3 实现分页查询订单接口（GET /demo/order/page）

## 4. 操作接口实现（TDD 第二步：使测试通过）

- [x] 4.1 实现新增订单接口（POST /demo/order）
- [x] 4.2 实现修改订单接口（PUT /demo/order）
- [x] 4.3 实现删除订单接口（DELETE /demo/order/{id}）

## 5. 验证与重构（TDD 第三步）

- [x] 5.1 运行测试验证所有接口功能
- [x] 5.2 代码重构优化
- [x] 5.3 添加中文注释和文档
