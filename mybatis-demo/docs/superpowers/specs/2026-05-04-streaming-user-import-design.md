# 用户流式导入设计文档

## 1. 背景

### 当前问题

当前 `SysUserController.importUsers()` 实现：
```java
List<SysUser> users = sysExcelService.importUsers(file.getInputStream());
sysUserService.saveBatch(users);
```

**存在问题：**
- 1. 全量加载所有数据到内存，大文件 OOM 风险
- 2. 单事务处理所有批次，无独立事务保障
- 3. 无详细错误统计和行级错误反馈
- 4. 返回值仅为成功数（Integer），无详细信息

### 目标
- 峰值内存 < 2MB
- 支持 100,000+ 行数据无压力
- 每批 100 行，独立事务
- 返回详细导入结果（成功/更新/失败统计 + 错误详情）
- 与角色导入代码模式保持一致

## 2. 技术方案

### 方案选型：复用角色导入模式（方案 A）

**核心架构：**
```
SysUserController.importUsers()
    ↓
SysUserService.importExcel(InputStream)
    ↓
UserImportListener ← Function 回调 ← processImportBatch()
    - 100 行/批
    - 逐行校验
    - 聚合批次结果
    ↓
每批独立事务，成功/失败分别统计
```

## 3. 详细设计

### 3.1 组件变更清单

| 组件 | 变更类型 | 说明 |
|------|----------|------|
| **SysUserController** | 修改 | importUsers() 简化调用，直接传入 InputStream |
| **SysUserService** | 新增方法 | `importExcel(InputStream): ImportResultVO` |
| |  | `processImportBatch(List<SysUserExcel>): ImportResultVO |
| **SysUserServiceImpl** | 实现 | 流式导入逻辑和批次处理（REQUIRES_NEW 事务 |
| **UserImportListener** | 新增 | Function 回调模式，同 RoleImportListener |
| **SysUserReadListener** | 删除 | 废弃旧监听器 |
| **SysExcelService** | 清理 | 删除 importUsers 方法或标记废弃 |

### 3.2 返回值统一

**接口变更：
```java
// 旧：Result<Integer> → Result<ImportResultVO>
```

**ImportResultVO 包含：**
- successCount: 新增成功数
- updateCount: 更新成功数
- failCount: 失败总数
- errors: List<ErrorRecord>（行号 + 错误信息）

### 3.3 数据校验规则

| 字段 | 校验规则 |
|------|----------|
| username | 必填，文件内去重 |
| name | 必填 |
| phone | 可选，格式校验 |
| email | 可选，格式校验 |

### 3.4 业务处理逻辑

**更新策略：**
- 根据 `username` 判断是否存在
- 存在则更新（name、email、phone、status）
- 不存在则新增
  - 默认密码：手机号后6位或 "123456"

**去重策略：**
- 文件内 username 重复检测（内存 Set<String> processedUsernames）
- 每批查询数据库已有 username

**状态转换：**
- "启用" → 1
- "禁用" → 0

### 3.5 事务策略

```java
@Transactional(propagation = Propagation.REQUIRES_NEW
public ImportResultVO processImportBatch(List<SysUserExcel> batch) {
    // 每批独立事务
    // 一批失败不影响其他批次
}
```

## 4. 测试场景设计

| 测试场景 | 验证点 |
|----------|--------|
| 基本导入成功 | 新增用户正确创建 |
| 包含已有用户更新 | username 存在时执行更新 |
| 空值校验失败 | username/name 为空记录错误 |
| 流式分批导入 | 101 行分两批，内存稳定 |
| 空文件导入 | 0 行不报错 |
| 文件内重复 username | 重复行记录错误 |
| 手机号格式错误 | 格式错误行记录错误 |
| 邮箱格式错误 | 格式错误行记录错误 |

## 5. 兼容性说明

- **API 变更：** Controller 返回类型从 `Result<Integer>` 变为 `Result<ImportResultVO>`
- **前端适配：** 前端需要调整解析逻辑
- **代码模式：** 与角色导入完全一致，便于维护
