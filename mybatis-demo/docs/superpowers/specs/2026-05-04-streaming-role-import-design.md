# 流式角色导入重构设计文档

**Date:** 2026-05-04
**Status:** Draft
**Scope:** `SysRoleController.importRoles()` 方法重构

## 1. 背景与目标

### 问题
当前实现存在OOM风险：
1. `RoleReadListener` 将所有有效行累积在 `batchList` 中（内存持有全部数据）
2. 达到阈值仅打日志，不清空
3. Service 接收完整 List 处理，大文件会导致内存溢出

### 目标
- 支持 10,000+ 行文件导入，内存占用稳定
- 批量大小 100 行/批次
- 每批次独立事务，失败不影响已成功批次
- 保持原有API不变，返回完整导入结果

## 2. 架构设计

```
┌─────────────────┐
│  Controller     │  - 接收 MultipartFile
│                 │  - 调用 service.importExcel(inputStream)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  SysRoleService │  - 创建监听器（带回调）
│  (streaming)    │  - 启动 EasyExcel 读取
│                 │  - 每批次 REQUIRES_NEW 事务
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  ImportListener │  - 持有 100 rows max
│  (with callback)│  - 行级别校验 + 去重
│                 │  - 触发服务回调处理批次
└─────────────────┘
```

## 3. 核心组件设计

### 3.1 `RoleImportListener` （替换 `RoleReadListener`）

**字段：**
```java
- int BATCH_SIZE = 100
- List<SysRoleExcel> currentBatch   // 只持有当前批次
- Set<String> processedCodes        // 去重（可接受内存占用：10k strings ~1MB）
- ImportResultVO result             // 聚合结果
- int rowNum
- Consumer<List<SysRoleExcel>> batchProcessor  // 服务回调
```

**关键方法：**
- `invoke()`: 校验 → 去重 → 加入批次 → 达到阈值触发处理
- `doAfterAllAnalysed()`: 处理剩余的部分批次
- `processBatch()`: 调用 callback → 清空批次列表 → 累加结果

### 3.2 `SysRoleService` 新增方法

```java
/**
 * 流式导入角色（InputStream 版本）
 * @param inputStream Excel文件流
 * @return 导入结果
 */
ImportResultVO importExcel(InputStream inputStream);

/**
 * 处理单个批次（REQUIRES_NEW 事务）
 */
@Transactional(propagation = Propagation.REQUIRES_NEW)
ImportResultVO processImportBatch(List<SysRoleExcel> batch);
```

### 3.3 Controller 变化

**Before:**
```java
EasyExcel.read(file.getInputStream(), ...).doRead();
return service.importExcel(listener.getDataList());  // full list
```

**After:**
```java
return service.importExcel(file.getInputStream());  // streaming
```

## 4. 数据流程

```
Excel (10k rows)
    ↓
EasyExcel SAX Parser
    ↓
invoke(row1) → validate → add to batch
invoke(row2) → validate → add to batch
...
invoke(row100) → validate → add to batch
    ↓
batchProcessor.accept(batch)  ← service.processImportBatch()
    ↓
    clear batch list          ← memory freed
    ↓
continue...
    ↓
doAfterAllAnalysed() → process remaining rows
```

## 5. 事务与错误处理

### 事务策略
- `processImportBatch()`: `REQUIRES_NEW` - 每批次独立事务
- `importExcel()`: 无事务 - 作为协调者
- 失败的批次不影响已提交的批次

### 错误聚合
- 行级错误（校验失败）：立即记录到 `result`，跳过该行
- 批次级错误（DB异常）：
  - 捕获异常，记录到 result
  - 继续处理后续批次
  - 返回 `{success: N, fail: M, errors: [...]}`

## 6. 内存占用估算

| 组件 | 大小 |
|------|------|
| 100 行 SysRoleExcel | ~10KB |
| processedCodes (10k) | ~1MB |
| ImportResultVO errors | ~tens of KB |
| **Total peak** | **< 2MB** |

对比旧实现：10k 行全部在内存 → ~1MB+，但如果是 100k 行 → 10MB+，流式版本峰值恒定。

## 7. 测试

### 单元测试
- 大文件导入：构造 10k 行 Excel，验证内存稳定
- 分批提交验证：mock service，verify N 次 processImportBatch 调用
- 错误场景：中间批次失败，后续批次继续

### 集成测试
- 真实文件导入，验证数据完整性
- 失败批次验证（主键冲突时的行为）
