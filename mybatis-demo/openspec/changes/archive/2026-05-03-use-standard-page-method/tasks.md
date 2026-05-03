## 1. 代码优化

- [x] 1.1 在 Paged 类中添加 from(IPage) 静态工厂方法，统一转换逻辑
- [x] 1.2 OrderServiceImpl 的 page 方法简化为：调用标准 page 方法 + Paged.from() 转换
- [x] 1.3 PagedServiceImpl 的 page 方法也使用同样的简化模式

## 2. 验证和清理

- [x] 2.1 编译验证代码正确性
- [x] 2.2 代码简化验证完成：消除了重复的 Paged 组装逻辑
