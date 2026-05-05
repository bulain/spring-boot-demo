# 实施计划：导入模板下载

**分支**: `002-import-template-download` | **日期**: 2026-05-05 | **规格**: [spec.md](spec.md)
**输入**: 来自 `/specs/002-import-template-download/spec.md` 的功能规格说明

**注意**: 此模板由 `/speckit-plan` 命令填充。请参阅 `.specify/templates/plan-template.md` 了解执行工作流。

## 摘要

为系统提供用户和角色的导入模板下载功能，管理员可下载包含字段说明和示例数据的标准Excel模板。使用 EasyExcel 动态生成双Sheet结构的Excel文件（填写说明 + 数据模板），通过 Spring Security 进行管理员权限控制，支持中文文件名编码处理。

## 技术上下文

**语言/版本**: Java 17  
**主要依赖**: Spring Boot 3.5, MyBatis Plus, EasyExcel  
**存储**: 不适用（动态生成，无需数据库）  
**测试**: JUnit 5  
**目标平台**: Linux 服务器  
**项目类型**: Web 服务  
**性能目标**: 模板下载响应时间 < 2 秒  
**约束条件**: 支持中文文件名，兼容主流浏览器和Excel查看软件  
**规模/范围**: 2个API端点，无数据库变更

## 章程检查

*关口：必须在第 0 阶段调研前通过。在第 1 阶段设计后重新检查。*

- ✅ 所有文档使用简体中文编写
- ✅ 代码命名使用英文
- ✅ 符合项目架构规范（Controller + Service 分层）
- ✅ 不引入不必要的第三方依赖（复用项目已有的 EasyExcel
- ✅ 遵循项目测试规范（JUnit 5）

## 项目结构

### 文档（本功能）

```text
specs/002-import-template-download/
├── plan.md              # 本文件（/speckit-plan 命令输出）
├── research.md          # 第 0 阶段输出（/speckit-plan 命令）
├── data-model.md        # 第 1 阶段输出（/speckit-plan 命令）
├── quickstart.md        # 第 1 阶段输出（/speckit-plan 命令）
├── contracts/           # 第 1 阶段输出（/speckit-plan 命令）
└── tasks.md             # 第 2 阶段输出（/speckit-tasks 命令 - 不由 /speckit-plan 创建）
```

### 源代码（仓库根目录）

```text
mybatis-demo/src/main/java/com/bulain/mybatis/sys/
├── controller/
│   ├── SysUserController.java    # 修改：添加用户模板下载接口
│   └── SysRoleController.java    # 修改：添加角色模板下载接口
├── service/
│   ├── ExcelTemplateService.java    # 新增：Excel模板生成服务接口
│   └── impl/
│       └── ExcelTemplateServiceImpl.java    # 新增：Excel模板生成服务实现
└── excel/
    ├── SysUserExcel.java    # 复用：用户Excel模型
    └── SysRoleExcel.java    # 复用：角色Excel模型

mybatis-demo/src/test/java/com/bulain/mybatis/sys/
├── service/
│   └── ExcelTemplateServiceTest.java    # 新增：服务单元测试
└── controller/
    ├── SysUserControllerTemplateTest.java    # 新增：用户模板接口测试
    └── SysRoleControllerTemplateTest.java    # 新增：角色模板接口测试
```

**结构决策**: 采用单项目结构，复用现有 Controller，新增 ExcelTemplateService 专门处理模板生成逻辑，保持与现有导入导出功能分离。

## 复杂度追踪

> **仅当章程检查存在必须说明的违规情况时填写**

本功能实现简单，无章程检查全部通过，无复杂度违规。

## 第 0 阶段：调研与技术选择

*输出：`research.md`（已生成）

### 已完成的调研

1. **Excel生成库选择**：继续使用 EasyExcel（项目已有依赖），支持流式读写、多Sheet生成，内存占用低。

2. **模板生成方式**：动态生成模板（无需静态文件），便于字段变更时统一修改代码，支持生成说明页Sheet。

3. **中文文件名处理**：使用 URLEncoder 编码 + Content-Disposition 响应头，符合 HTTP 标准规范。

4. **模板Sheet结构**：双Sheet结构（填写说明 + 数据模板），符合用户澄清的需求。

5. **权限控制**：使用现有 Spring Security 机制，验证管理员权限，与其他管理接口保持一致。

### 依赖说明（无需新增）
- EasyExcel - Excel文件生成
- Spring Web - HTTP响应处理
- Spring Security - 权限控制

## 第 1 阶段：设计与接口契约

*先决条件：`research.md` 完成

### 1.1 数据模型设计

输出：`data-model.md`（已生成）

**用户导入模板**（双Sheet）**：
- Sheet 1: 填写说明（字段名称、是否必填、字段说明、示例值）
- Sheet 2: 用户数据（用户名、姓名、邮箱、手机号、密码、状态）

**角色导入模板**（双Sheet）：
- Sheet 1: 填写说明（字段名称、是否必填、字段说明、示例值）
- Sheet 2: 角色数据（角色编码、角色名称、描述、状态）

### 1.2 接口契约设计

输出：`contracts/api.md`（已生成）

| 接口 | 方法 | 说明 |
|------|------|------|
| /api/sys/users/template | GET | 下载用户导入模板（需管理员权限） |
| /api/sys/roles/template | GET | 下载角色导入模板（需管理员权限） |

**响应头规范**：
- Content-Type: `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- Content-Disposition: `attachment; filename*=UTF-8''%E7%94%A8%E6%88%B7%E5%AF%BC%E5%85%A5%E6%A8%A1%E6%9D%BF.xlsx`

### 1.3 快速开始指南

输出：`quickstart.md`（已生成）

包含功能说明、使用指南、字段说明、常见问题、技术栈说明、关键实现点、测试验证清单。

### 1.4 重新检查章程

✅ 所有设计文档使用简体中文
✅ 接口命名符合 RESTful 规范
✅ 代码结构符合项目分层架构
✅ 无不必要的依赖引入

## 第 2 阶段：任务生成（待执行）

*先决条件：第 1 阶段完成，章程重新检查通过

**下一步**：运行 `/speckit-tasks` 生成详细任务列表，然后运行 `/speckit-implement` 执行实现。

### 实现任务预览

1. **创建 ExcelTemplateService 接口和实现类
2. **实现用户模板生成方法（双Sheet结构）
3. **实现角色模板生成方法（双Sheet结构）
4. **在 SysUserController 添加模板下载接口
5. **在 SysRoleController 添加模板下载接口
6. **添加单元测试和集成测试
