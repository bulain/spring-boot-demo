## Context

当前 `SysRoleServiceImpl.export()` 实现：
```java
List<SysRole> list = baseMapper.selectList(wrapper);  // 全量加载
List<SysRoleExcel> excelList = list.stream().map(...).collect(toList());  // 二次拷贝
writeExcel(response, excelList);  // 全量写入
```

问题：10万行数据 ≈ 50MB+ 内存，并发导出极易 OOM。

## Goals / Non-Goals

**Goals:**
- 峰值内存 < 2MB（仅 100 行数据）
- 支持 100,000+ 行数据无压力导出
- 保持 API 完全兼容（方法签名不变）
- export 和 exportByIds 两个方法均支持流式

**Non-Goals:**
- 不引入新依赖
- 不修改 Controller 层
- 不改变导出 Excel 的字段和格式

## Decisions

### 1. 流式查询方案：MyBatis ResultHandler

**决策：** 使用 `baseMapper.selectList(wrapper, ResultHandler)` 逐行回调

**理由：**
- MyBatis 原生支持，代码改动最小
- 真正流式，无额外 COUNT 查询
- 比分页查询性能更优

**替代方案：**
- 分页查询：需要额外 COUNT，10万行 = 1000次查询
- Cursor 游标：需要自定义 Mapper 方法

### 2. 写入方案：EasyExcel ExcelWriter 分批

**决策：** `ExcelWriter.write(batch, sheet)` + `batch.clear()`

**理由：**
- EasyExcel 原生支持分批写入
- 每批 clear() 后 GC 可立即回收内存
- try-with-resources 自动关闭资源

### 3. 批次大小：100 行/批

**决策：** 100 行/批次

**理由：**
- 与流式导入保持一致（100 行/批）
- 内存占用极低（约 10KB）
- 平衡 Excel 写入性能

## Risks / Trade-offs

| 风险 | 应对措施 |
|------|----------|
| MySQL 默认 fetchSize=0 全量拉取 | 当前角色数据量不大可接受；后续可自定义 Mapper 优化 |
| ResultHandler 方法签名兼容性 | 运行时验证，单元测试覆盖 |
| 导出中途连接断开 | 依赖 try-with-resources 确保资源释放 |

## Implementation Structure

```
SysRoleServiceImpl.export()
    ↓
exportStreaming(wrapper, response)  // 公共抽取
    ↓
1. 设置响应头
2. 创建 ExcelWriter (try-with-resources)
3. 初始化 batch = new ArrayList<>(100)
4. baseMapper.selectList(wrapper, result -> {
       转换 -> batch.add -> 满 100 行 write + clear
   })
5. 处理剩余数据（不满 100 行）
6. ExcelWriter 自动关闭
```

### 核心代码片段

```java
private void exportStreaming(LambdaQueryWrapper<SysRole> wrapper, HttpServletResponse response) {
    // 设置响应头...
    
    try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), SysRoleExcel.class).build()) {
        WriteSheet sheet = EasyExcel.writerSheet("角色").build();
        List<SysRoleExcel> batch = new ArrayList<>(100);
        
        baseMapper.selectList(wrapper, (ResultHandler<SysRole>) resultContext -> {
            SysRole role = resultContext.getResultObject();
            SysRoleExcel excel = new SysRoleExcel();
            BeanUtils.copyProperties(role, excel);
            batch.add(excel);
            
            if (batch.size() >= 100) {
                excelWriter.write(batch, sheet);
                batch.clear();
            }
        });
        
        // 处理剩余数据
        if (!batch.isEmpty()) {
            excelWriter.write(batch, sheet);
        }
    } catch (IOException e) {
        throw new RuntimeException("导出Excel失败", e);
    }
}
```
