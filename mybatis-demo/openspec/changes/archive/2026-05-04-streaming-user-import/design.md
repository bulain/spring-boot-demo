## Context

当前用户导入实现问题：
- 全量加载所有数据到内存，OOM 风险
- 单事务处理所有批次，无独立事务保障
- 无详细错误统计和行级错误反馈
- 返回类型 Integer 仅返回成功数
- 与角色导入代码模式不一致

## Goals / Non-Goals

**Goals:**
- 峰值内存 < 2MB（仅 100 行数据）
- 支持 100,000+ 行数据无压力
- 每批 100 行，独立事务（REQUIRES_NEW）
- 返回详细 ImportResultVO，与角色导入一致
- 代码模式与角色导入保持统一

**Non-Goals:**
- 不实现异步导入
- 不实现断点续传
- 不修改 Excel 字段映射
- 不新增依赖

## Decisions

### 1. 架构复用：与角色导入完全一致

**决策：** 复用 RoleImportListener 的 Function 回调模式

**理由：**
- 代码模式统一，团队熟悉，便于维护
- 经过验证的可靠实现
- 最小学习成本

**实现结构：**
```
SysUserController.importUsers()
    ↓
SysUserService.importExcel(InputStream)
    ↓
UserImportListener ← Function 回调 ← processImportBatch()
    - 100 行/批
    - 逐行校验
    - 聚合批次结果
```

### 2. 事务策略：REQUIRES_NEW

**决策：** 每批独立事务

**理由：**
- 部分失败不影响整体
- 大文件场景下避免长事务
- 与角色导入保持一致

### 3. 返回值：统一 ImportResultVO

**决策：** 从 `Result<Integer>` 改为 `Result<ImportResultVO>`

**理由：**
- 与角色导入 API 一致
- 提供详细错误信息（行号、原因）
- 支持新增/更新区分统计

### 4. 校验策略：监听器内逐行校验

**决策：** UserImportListener 内完成行级校验

**校验规则：**
- username：必填 + 文件内去重
- name：必填
- phone：可选，格式校验
- email：可选，格式校验

### 5. 业务处理逻辑

**更新策略：**
- 根据 username 判断是否存在
- 存在则更新（name、email、phone、status）
- 不存在则新增
  - 默认密码：手机号后6位或 "123456"

**状态转换：**
- "启用" → 1
- "禁用" → 0

## Risks / Trade-offs

| 风险 | 应对措施 |
|------|----------|
| API 返回值变更影响前端 | 前端需适配 ImportResultVO |
| 手机号格式校验规则可能与现有不一致 | 参考项目现有校验规则 |
| 默认密码生成逻辑 | 参考 SysExcelServiceImpl 现有逻辑 |

## Implementation Structure

### 核心文件变更

1. **SysUserController.java**
   - importUsers() 简化：直接调用 sysUserService.importExcel(inputStream)

2. **SysUserService.java**
   - 新增：ImportResultVO importExcel(java.io.InputStream inputStream)
   - 新增：ImportResultVO processImportBatch(List<SysUserExcel> batch)

3. **SysUserServiceImpl.java**
   - 实现 importExcel()：创建 UserImportListener，调用 EasyExcel.read()
   - 实现 processImportBatch()：@Transactional(REQUIRES_NEW)，处理每批

4. **UserImportListener.java**（新建）
   - 同 RoleImportListener，Function 回调模式
   - 100 行/批阈值
   - 逐行校验，文件内 username 去重

5. **SysUserReadListener.java**（删除）
   - 废弃旧实现

6. **SysUserServiceTest.java**
   - 新增 10 个流式导入测试用例
