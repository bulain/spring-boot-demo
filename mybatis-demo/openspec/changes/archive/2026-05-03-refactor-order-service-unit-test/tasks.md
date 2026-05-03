## 1. 测试类重构

- [x] 1.1 将 `@SpringBootTest` 改为 `@ExtendWith(MockitoExtension.class)`
- [x] 1.2 添加 `@Mock OrderMapper orderMapper`
- [x] 1.3 添加 `@InjectMocks OrderServiceImpl orderService`
- [x] 1.4 移除 `@Autowired` 和 `@Disabled` 注解
- [x] 1.5 清理不再需要的 import

## 2. 测试用例重构

- [x] 2.1 重构 `testSave()`：使用 Mockito when/thenReturn，verify insert 调用
- [x] 2.2 重构 `testRemoveById()`：verify update 调用（逻辑删除）
- [x] 2.3 重构 `testSelectById()`：stub selectById，验证返回值
- [x] 2.4 重构 `testSelectList()`：stub selectList，验证结果列表
- [x] 2.5 重构 `testDirectRemove()`：verify directDelete 调用

## 3. 验证和清理

- [x] 3.1 编译验证代码正确性 ✓
- [x] 3.2 运行测试，确保全部通过 ✓（5 个测试全部通过）
- [x] 3.3 添加中文注释说明测试意图 ✓
