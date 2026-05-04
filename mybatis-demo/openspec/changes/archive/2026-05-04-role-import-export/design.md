## Context

当前系统已有角色管理的基础 CRUD 功能，但缺乏批量导入导出能力。用户导出采用 Apache POI 5.2.5 基础实现。本变更将引入 EasyExcel 4.0.3 作为更高效的流式处理框架，实现角色数据的批量导入导出。

## Goals / Non-Goals

**Goals:**
- 使用 EasyExcel 流式读写，支持万级数据量无内存溢出
- 实现按条件筛选导出和按 ID 列表选择导出
- 实现导入数据校验（字段格式、业务规则）
- 返回详细的导入结果统计

**Non-Goals:**
- 不支持复杂格式的 Excel 模板（仅基础字段映射）
- 不支持导入角色权限关联（仅角色基本信息）
- 不支持增量同步（仅全量覆盖更新）

## Decisions

### 1. 框架选择：EasyExcel 4.0.3

**决策**：使用 EasyExcel 替代原生 POI

**理由**：
- 内置流式读写，内存占用稳定在 KB 级别
- 注解驱动的模型映射，代码简洁
- 自动处理类型转换和格式问题

**替代方案**：
- Apache POI SXSSF：也支持流式但代码繁琐
- EasyPOI：功能丰富但性能不如 EasyExcel

### 2. 导出实现方案

**决策**：Controller 层直接返回 Excel 流，Service 层负责数据查询和转换

**设计**：
```
SysRoleController.export()
    ↓
SysRoleService.export() → 流式查询数据
    ↓
EasyExcel.write() → 直接写入 HttpServletResponse
```

**要点**：
- 查询使用 MyBatis-Plus 的流式查询（fetchSize）
- 设置响应头：Content-Type=application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
- 返回值：void，直接写入 response.getOutputStream()

### 3. 导入实现方案

**决策**：使用 ReadListener 逐行处理，分批入库

**设计**：
```
SysRoleController.import()
    ↓
SysRoleService.importExcel()
    ↓
RoleReadListener → 每 50 行批量处理
    ↓
校验数据 → 去重 → 批量保存/更新
    ↓
收集结果 → 返回统计信息
```

**要点**：
- ReadListener 每次读取一批数据（默认 50 条）
- 维护批次状态：成功数、失败数、错误信息列表
- 使用 upsert 逻辑：根据角色编码判断新增或更新

### 4. 数据模型设计

**Excel 模型**：`RoleExcelVO`
- @ExcelProperty("角色编码") String code
- @ExcelProperty("角色名称") String name
- @ExcelProperty("排序") Integer sort
- @ExcelProperty("状态") Integer status
- @ExcelProperty("备注") String remark

**导入结果**：`ImportResultVO`
- Integer successCount
- Integer failCount
- Integer updateCount
- List<ErrorRecord> errors (行号 + 错误信息)

## Risks / Trade-offs

[风险] 大文件导入时间过长 → 前端设置超时，后端异步处理（初期同步，超过 1 万行考虑异步）
[风险] 并发导入冲突 → 数据库唯一索引保护，导入时对角色编码加锁
[风险] EasyExcel 版本兼容 → 确认与现有 POI 5.2.5 兼容（EasyExcel 4.x 内置 POI 5.2.5，直接使用）

## 依赖说明

需在 pom.xml 新增 EasyExcel 依赖：
```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>easyexcel</artifactId>
    <version>4.0.3</version>
</dependency>
```

当前系统已有的 Apache POI 5.2.5 与 EasyExcel 4.x 内置版本一致，无版本冲突。
