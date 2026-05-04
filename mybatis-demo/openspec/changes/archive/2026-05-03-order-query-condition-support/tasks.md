## 1. OrderSearch 字段增强

- [x] 1.1 添加 createdAtFrom（创建日期开始）字段
- [x] 1.2 添加 createdAtTo（创建日期结束）字段
- [x] 1.3 保留原有 orderNo、extnRefNo1 字段并完善注释

## 2. OrderController list 接口修改

- [x] 2.1 修改 list 接口参数从 Order 改为 OrderSearch
- [x] 2.2 实现动态条件构建：orderNo 模糊查询
- [x] 2.3 实现动态条件构建：extnRefNo1 模糊查询
- [x] 2.4 实现动态条件构建：createdAt 范围查询

## 3. OrderController page 接口修改

- [x] 3.1 修改 page 接口参数从 Search 改为 OrderSearch
- [x] 3.2 在 OrderServiceImpl 中实现相同的动态条件构建逻辑

## 4. OrderService 接口调整

- [x] 4.1 修改 page 方法参数从 Search 改为 OrderSearch

## 5. 测试验证

- [x] 5.1 更新 OrderControllerTest 测试用例
- [x] 5.2 编译验证代码正确性
- [x] 5.3 添加中文注释和文档
