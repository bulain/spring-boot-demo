# 流式用户导出设计文档

## 1. 背景

### 当前问题
当前 `SysUserController.exportUsers()` 方法存在两个严重问题：

1. **Bug：仅导出第 1 页数据**
   ```java
   Paged<SysUser> page = sysUserService.pageUsers(query);  // 默认第 1 页，10 条/页
   List<SysUserExcel> excelList = page.getData().stream()...;  // 仅 10 条数据
   ```

2. **OOM 风险**：即使修复分页问题，全量加载数据也存在内存溢出风险
   - 10 万行用户数据 × 每个对象约 600B = 约 60MB 内存
   - 加上 Excel 序列化临时对象，峰值可能超过 120MB
   - 并发导出场景下内存压力更大

### 目标
- 修复导出 Bug：导出**所有符合查询条件**的用户数据
- 峰值内存 < 2MB（仅保留 100 行批次数据）
- 支持 100,000+ 行数据无压力导出
- 新增按 ID 选择导出功能
- 保持现有 API 完全兼容

## 2. 技术方案

### 方案选型：ResultHandler 流式查询 + EasyExcel 分批写入

**核心原理：**
- MyBatis `ResultHandler` 逐行回调，不累积全量数据
- EasyExcel `ExcelWriter` 分批写入，内存中仅保留当前批次
- 批次大小：100 行/批
- 与角色导出模式完全一致，代码风格统一

## 3. 详细设计

### 3.1 架构流程

```
Controller.exportUsers(query, response)        Controller.exportUsersByIds(ids, response)
    ↓                                            ↓
Service.export(query, response)               Service.exportByIds(ids, response)
    ↓                                            ↓
    ├─ 构建 LambdaQueryWrapper                   ├─ 构建 in(id) 查询 wrapper
    └──────────────────┬─────────────────────────┘
                       ↓
              exportStreaming(wrapper, response)  ← 抽取公共方法
                       ↓
              1. 设置响应头
              2. 创建 ExcelWriter (try-with-resources)
              3. 初始化批次缓存 List<SysUserExcel>(100)
              4. baseMapper.selectList(wrapper, resultHandler)
                   └─ 逐行转换 SysUser → SysUserExcel
                   └─ 批次满 100 行 → write → clear
              5. 处理剩余不满 100 行的数据
              6. ExcelWriter 自动关闭
```

### 3.2 内存控制要点

| 措施 | 说明 |
|------|------|
| `batch.clear()` | 每批写入后立即清空，GC 可及时回收 |
| `new ArrayList<>(100)` | 预分配容量，避免扩容拷贝 |
| 无中间 List | 不收集全量数据，直接回调处理 |
| `try-with-resources` | 确保流正确关闭，资源释放 |

### 3.3 字段转换逻辑

| Excel 列 | 来源字段 | 转换逻辑 |
|----------|----------|----------|
| 用户名 | username | 原样 |
| 姓名 | name | 原样 |
| 邮箱 | email | 原样 |
| 手机号 | phone | 原样 |
| 状态 | status | `status == 1 ? "启用" : "禁用"` |

### 3.4 涉及方法

**Controller 层：**
1. `exportUsers(UserQueryDTO query, HttpServletResponse response)` - 现有方法，流式重写
2. `exportUsersByIds(List<String> ids, HttpServletResponse response)` - 新增方法

**Service 层：**
1. `export(UserQueryDTO query, HttpServletResponse response)` - 新增接口方法
2. `exportByIds(List<String> ids, HttpServletResponse response)` - 新增接口方法
3. `exportStreaming(LambdaQueryWrapper<SysUser> wrapper, HttpServletResponse response)` - 私有实现

## 4. 端点设计

### 现有端点（保持不变）
```
POST /api/sys/users/export
Content-Type: application/x-www-form-urlencoded

参数：UserQueryDTO（username、name 模糊查询）
```

### 新增端点（按 ID 导出）
```
POST /api/sys/users/export/selected
Content-Type: application/json

请求体：["id1", "id2", "id3", ...]
```

## 5. 异常与边界处理

| 场景 | 处理方式 |
|------|----------|
| 空查询结果 | 生成 0 行的正常 Excel 文件 |
| 空 ID 列表 | 抛出异常 `"请选择要导出的用户"` |
| 部分 ID 不存在 | 静默忽略，只导出实际存在的数据 |
| 导出中途连接断开 | try-with-resources 确保资源释放 |
| 99 行数据 | 剩余数据在循环结束后写入 |
| 101 行数据 | 分两批次（100 + 1） |

## 6. 测试要点

| 测试场景 | 验证点 |
|----------|--------|
| 空数据导出 | 0 行数据正常生成 Excel |
| 99 行数据 | 不满一批次，剩余数据正常写入 |
| 100 行数据 | 刚好一批次完成 |
| 101 行数据 | 分两批次，剩余 1 行正常写入 |
| 按条件筛选导出 | 用户名/姓名模糊查询逻辑正确 |
| 按 ID 导出 | 选择导出正确，空列表抛出异常 |
| 导出格式 | 文件头、文件名、列顺序与原实现一致 |
| 状态转换 | status=1→"启用", 其他→"禁用" |

## 7. 兼容性

- **API 兼容性**：现有 `/export` 端点签名 100% 不变
- **行为兼容性**：导出的 Excel 内容、格式完全一致（数据量从 10 条修复为全部）
- **新增功能**：`/export/selected` 端点为纯新增，无兼容性问题
- **性能影响**：大文件导出性能略优（减少 GC 次数）

## 8. 风险与应对

| 风险 | 应对措施 |
|------|----------|
| ResultHandler 行为差异 | 参照已验证的角色导出实现，代码模式完全一致 |
| MySQL 默认全量拉取 | MySQL JDBC 默认行为，可后续通过 `StatementCreator` 自定义 fetchSize 优化 |
| 导出中途连接断开 | 依赖 try-with-resources 确保资源释放 |

## 9. 实现任务清单

1. 在 `SysUserService` 接口添加 `export()` 和 `exportByIds()` 方法
2. 在 `SysUserServiceImpl` 实现 `exportStreaming()` 私有方法
3. 在 `SysUserServiceImpl` 实现两个导出接口方法
4. 修改 `SysUserController.exportUsers()` 调用 Service 层方法
5. 在 `SysUserController` 新增 `exportUsersByIds()` 端点
6. 编写单元测试（边界场景验证）

---

**参考来源：**
- [MyBatis-Plus 流式查询](https://baomidou.com/)
- [EasyExcel 分批写入](https://easyexcel.opensource.alibaba.com/)
- 流式角色导出实现 `SysRoleServiceImpl.exportStreaming()`
