## Context

现有角色导入实现：
- RoleReadListener 累积所有行到 batchList，达到 50 仅打日志不清空
- importExcel(List<SysRoleExcel>) 接收完整列表，单事务处理
- 大文件会导致 OOM

目标：真正的流式处理，峰值内存 < 2MB。

## Goals / Non-Goals

**Goals:**
- 支持 10,000+ 行文件导入
- 峰值内存 < 2MB
- 每批 100 行，独立事务
- 保持 API 兼容（返回 ImportResultVO）

**Non-Goals:**
- 不支持异步导入
- 不支持断点续传
- 不实现通用 Excel 框架（仅角色导入）

## Decisions

### 1. 回调监听器模式

**决策：** RoleImportListener 接收 Consumer<List<SysRoleExcel>> 回调

**理由：**
- 简单直接，Spring 风格
- 监听器专注解析，服务专注业务逻辑
- 便于测试（Mock Consumer）

**替代方案：**
- 监听器内直接调用 Service：耦合严重，难测试
- PageRead：EasyExcel pageRead 非真正流式，仍读全量到内存

### 2. 事务策略

**决策：** processImportBatch 使用 REQUIRES_NEW 传播级别

**理由：**
- 批次隔离，失败不影响已成功
- 符合用户选项 A（每批次独立事务）
- 避免持有长事务锁

### 3. 去重策略

**决策：** 保持内存 Set<String> processedCodes

**理由：**
- 10,000 个字符串 ~ 1MB，可接受
- 比临时表或 Bloom Filter 简单
- 符合用户选项 A

## Risks / Trade-offs

[风险] 批次中途失败，数据部分导入 → 缓解：记录详细错误，用户可补导入
[风险] 同一代码跨批次重复 → 缓解：全量 processedCodes Set 可检测
[权衡] 独立事务 vs 原子性 → 选择可用性（部分成功 > 全部回滚）

## Implementation Structure

```
controller.SysRoleController
    ↓ (InputStream)
service.SysRoleService.importExcel(InputStream)
    ↓ new RoleImportListener(batch -> processImportBatch(batch))
excel.RoleImportListener  ←  callback  ←┘
    - 100 rows max in memory
    - per-row validation
    - aggregate result
```
