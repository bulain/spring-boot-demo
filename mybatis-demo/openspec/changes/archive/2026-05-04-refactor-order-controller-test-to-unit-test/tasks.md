## 1. 测试类重构

- [x] 1.1 移除 Spring 相关注解（@SpringBootTest、@ExtendWith(SpringExtension.class)、@Disabled）
- [x] 1.2 添加 Mockito 注解（@ExtendWith(MockitoExtension.class)）
- [x] 1.3 将 @Autowired 改为 @Mock（OrderService）和 @InjectMocks（OrderController）
- [x] 1.4 移除依赖真实数据库的 @BeforeEach setup 方法

## 2. 测试方法重构 - 基础 CRUD

- [x] 2.1 重构 testGetById - 使用 when/verify 模拟 service 调用
- [x] 2.2 重构 testSave - 使用 ArgumentCaptor 验证传入参数
- [x] 2.3 重构 testUpdate - 使用 ArgumentCaptor 验证传入参数
- [x] 2.4 重构 testDelete - 使用 verify 验证 removeById 调用

## 3. 测试方法重构 - 查询场景

- [x] 3.1 重构 testList - 使用 ArgumentCaptor 验证查询条件
- [x] 3.2 重构 testPage - 验证分页参数和查询条件传递
- [x] 3.3 重构 testListByOrderNo - 验证 orderNo 模糊查询条件
- [x] 3.4 重构 testListByExtnRefNo1 - 验证 extnRefNo1 模糊查询条件

## 4. 验证

- [x] 4.1 运行测试确保全部通过
- [x] 4.2 验证测试执行速度（毫秒级，无需 Spring 启动）
