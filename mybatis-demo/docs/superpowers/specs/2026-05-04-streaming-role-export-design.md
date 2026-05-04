# 流式角色导出设计文档

## 1. 背景

### 当前问题
当前 `SysRoleServiceImpl.export()` 方法存在 OOM 风险：
```java
List<SysRole> list = baseMapper.selectList(wrapper);  // 全量加载到内存
List<SysRoleExcel> excelList = list.stream().map(...).collect(toList());  // 二次拷贝
writeExcel(response, excelList);  // 全量写入
```

**风险分析：**
- 10万行角色数据 × 每个对象约 500B = 约 50MB 内存
- 加上 Excel 序列化时的临时对象，峰值可能超过 100MB
- 并发导出场景下内存压力更大

### 目标
- 峰值内存 < 2MB（仅 100 行数据）
- 支持 100,000+ 行数据无压力导出
- 保持 API 完全兼容（方法签名不变）

## 2. 技术方案

### 方案选型：ResultHandler 流式查询 + EasyExcel 分批写入

**核心原理：**
- MyBatis `ResultHandler` 逐行回调，不累积全量数据
- EasyExcel `ExcelWriter` 分批写入，内存中仅保留当前批次
- 批次大小：100 行/批

## 3. 详细设计

### 3.1 架构流程

```
export(query, response)
    ↓
1. 设置响应头（Content-Type、文件名等）
    ↓
2. 创建 ExcelWriter (try-with-resources)
    ↓
3. 初始化批次缓存 List<SysRoleExcel> batch = new ArrayList<>(100)
    ↓
4. baseMapper.selectList(wrapper, resultContext -> {
       SysRole role = resultContext.getResultObject();
       // 转换为 Excel 模型
       SysRoleExcel excel = convert(role);
       batch.add(excel);
       // 达到批次阈值立即写入
       if (batch.size() >= 100) {
           excelWriter.write(batch, sheet);
           batch.clear();  // 立即释放内存
       }
   })
    ↓
5. 处理剩余数据（不满 100 行的部分）
   if (!batch.isEmpty()) {
       excelWriter.write(batch, sheet);
   }
    ↓
6. ExcelWriter 自动关闭（try-with-resources）
```

### 3.2 内存控制要点

| 措施 | 说明 |
|------|------|
| `batch.clear()` | 每批写入后立即清空，GC 可及时回收 |
| `new ArrayList<>(100)` | 预分配容量，避免扩容拷贝 |
| 无中间 List | 不收集全量数据，直接回调处理 |
| `try-with-resources` | 确保流正确关闭，资源释放 |

### 3.3 涉及方法修改

**方法 1：`export(RoleQueryDTO query, HttpServletResponse response)`**
- 条件导出，支持按编码、名称模糊查询
- 按创建时间倒序导出

**方法 2：`exportByIds(List<String> ids, HttpServletResponse response)`**
- 按指定 ID 列表导出
- 空列表抛出异常（保持现有行为）

### 3.4 公共抽取

抽取通用流式导出方法，避免代码重复：

```java
private void exportStreaming(LambdaQueryWrapper<SysRole> wrapper, HttpServletResponse response)
```

## 4. 数据模型

### 4.1 导出字段（保持不变）

```java
public class SysRoleExcel {
    @ExcelProperty("角色编码")
    private String code;
    
    @ExcelProperty("角色名称")
    private String name;
    
    @ExcelProperty("描述")
    private String description;
    
    // ... 其他字段
}
```

### 4.2 响应头（保持不变）

```
Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
Content-Disposition: attachment;filename*=utf-8''角色数据.xlsx
```

## 5. 测试要点

| 测试场景 | 验证点 |
|----------|--------|
| 空数据导出 | 0行数据正常生成 Excel |
| 99 行数据 | 不满一批次，剩余数据正常写入 |
| 100 行数据 | 刚好一批次完成 |
| 101 行数据 | 分两批次，剩余 1 行正常写入 |
| 1000 行数据 | 验证流式行为（可选性能测试） |
| 按条件筛选导出 | 筛选逻辑正确 |
| 按 ID 导出 | 选择导出正确 |
| 异常场景 | 响应流关闭、内存泄漏 |

## 6. 兼容性

- **API 兼容性**：方法签名 100% 不变
- **行为兼容性**：导出的 Excel 内容、格式完全一致
- **性能影响**：大文件导出性能略优（减少 GC 次数）

## 7. 风险与应对

| 风险 | 应对措施 |
|------|----------|
| ResultHandler 行为差异 | 查阅 MyBatis-Plus 源码确认方法签名 |
| fetchSize 导致 MySQL 全量拉取 | MySQL 默认全量拉取，可通过 `StatementCreator` 自定义（后续优化） |
| 导出中途连接断开 | 依赖 try-with-resources 确保资源释放 |

## 8. 实现任务清单

1. 抽取 `exportStreaming()` 公共方法
2. 重构 `export()` 方法为流式实现
3. 重构 `exportByIds()` 方法为流式实现
4. 删除废弃的 `writeExcel()` 全量方法
5. 编写单元测试（边界场景验证）

---

**来源参考：**
- [MyBatis-Plus 流式查询](https://baomidou.com/)
- [EasyExcel 分批写入](https://easyexcel.opensource.alibaba.com/)
