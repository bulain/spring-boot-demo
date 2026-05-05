---
description: "导入模板下载功能实施的任务列表"
---

# 任务列表：导入模板下载

**输入**: 来自 `/specs/002-import-template-download/` 的设计文档
**前置条件**: plan.md（必需）, spec.md（用户故事必需）, research.md, data-model.md, contracts/

**测试**: 本功能包含单元测试和集成测试任务。

**组织方式**: 任务按用户故事分组，以便每个故事可以独立实施和测试。

## 格式: `[ID] [P?] [Story] 描述`

- **[P]**: 可并行执行（不同文件，无依赖）
- **[Story]**: 此任务属于哪个用户故事（例如 US1, US2, US3）
- 在描述中包含确切的文件路径

## 路径约定

- **单项目**: 仓库根目录下的 `src/main/java/`, `src/test/java/`
- 本功能路径: `mybatis-demo/src/main/java/com/bulain/mybatis/sys/`

---

## 第 1 阶段：搭建（共享基础设施）

**目的**: 项目初始化和基本结构

本功能无需搭建任务，项目结构已存在，所有依赖已就绪。

---

## 第 2 阶段：基础建设（阻塞性前置条件）

**目的**: 核心基础设施，必须在任何用户故事开始实施前完成

- [X] T001 [P] 创建 ExcelTemplateService 接口在 `src/main/java/com/bulain/mybatis/sys/service/ExcelTemplateService.java`
- [X] T002 [P] 创建 ExcelTemplateServiceImpl 实现类骨架在 `src/main/java/com/bulain/mybatis/sys/service/impl/ExcelTemplateServiceImpl.java`

**检查点**: 服务接口和实现类骨架已创建 - 现在可以并行开始用户故事实施

---

## 第 3 阶段：用户故事 1 - 用户导入模板下载（优先级：P1）🎯 MVP

**目标**: 管理员可下载用户导入Excel模板，包含填写说明Sheet和用户数据Sheet（含表头和示例数据）

**独立测试**: 调用 GET /api/sys/users/template 接口，验证返回Excel文件可打开，包含两个Sheet，密码字段有明文示例

### 用户故事 1 的测试 ⚠️

> **注意：首先编写这些测试，在实施前确保它们失败**

- [X] T003 [P] [US1] 在 `src/test/java/com/bulain/mybatis/sys/service/ExcelTemplateServiceTest.java` 中为用户模板生成编写单元测试
- [X] T004 [P] [US1] 在 `src/test/java/com/bulain/mybatis/sys/controller/SysUserControllerTemplateTest.java` 中为用户模板下载接口编写集成测试

### 用户故事 1 的实施

- [X] T005 [US1] 在 ExcelTemplateService.java 中添加 downloadUserTemplate 方法签名
- [X] T006 [US1] 在 ExcelTemplateServiceImpl.java 中实现用户模板生成逻辑（双Sheet结构：填写说明 + 用户数据）
- [X] T007 [US1] 在 SysUserController.java 中实现 GET /api/sys/users/template 接口（依赖 T006）
- [X] T008 [US1] 为接口添加 @PreAuthorize("hasRole('ADMIN')") 权限注解（注：项目仅配置 spring-security-crypto，方法级安全需额外配置）
- [X] T009 [US1] 验证中文文件名编码处理正确

**检查点**: 此时，用户故事 1 应完全可工作并可独立测试

---

## 第 4 阶段：用户故事 2 - 角色导入模板下载（优先级：P1）

**目标**: 管理员可下载角色导入Excel模板，包含填写说明Sheet和角色数据Sheet（含表头和示例数据）

**独立测试**: 调用 GET /api/sys/roles/template 接口，验证返回Excel文件可打开，包含两个Sheet，示例数据正确

### 用户故事 2 的测试 ⚠️

- [X] T010 [P] [US2] 在 `src/test/java/com/bulain/mybatis/sys/service/ExcelTemplateServiceTest.java` 中为角色模板生成添加单元测试
- [X] T011 [P] [US2] 在 `src/test/java/com/bulain/mybatis/sys/controller/SysRoleControllerTemplateTest.java` 中为角色模板下载接口编写集成测试

### 用户故事 2 的实施

- [X] T012 [US2] 在 ExcelTemplateService.java 中添加 downloadRoleTemplate 方法签名
- [X] T013 [US2] 在 ExcelTemplateServiceImpl.java 中实现角色模板生成逻辑（双Sheet结构：填写说明 + 角色数据）（依赖 T006 的基础结构）
- [X] T014 [US2] 在 SysRoleController.java 中实现 GET /api/sys/roles/template 接口（依赖 T013）
- [X] T015 [US2] 为接口添加 @PreAuthorize("hasRole('ADMIN')") 权限注解（注：项目仅配置 spring-security-crypto，方法级安全需额外配置）
- [X] T016 [US2] 验证中文文件名编码处理正确

**检查点**: 此时，用户故事 1 和 2 都应独立工作

---

## 第 5 阶段：优化与跨领域关注点

**目的**: 影响多个用户故事的改进

- [X] T017 [P] 运行 quickstart.md 中的手动测试清单验证
- [X] T018 [P] 代码清理和重构（提取公共方法，优化注释）
- [X] T019 运行所有测试确保通过（mvn test -Dtest=*Template*）
- [X] T020 验证所有响应头符合 contracts/api.md 规范

---

## 依赖关系与执行顺序

### 阶段依赖

- **搭建（第 1 阶段）**: 无需执行 - 项目已就绪
- **基础建设（第 2 阶段）**: 无依赖 - 可立即开始 - 阻塞所有用户故事
- **用户故事 1（第 3 阶段）**: 依赖基础建设阶段完成
- **用户故事 2（第 4 阶段）**: 依赖基础建设阶段完成，可与 US1 并行（服务方法独立）
- **优化（第 5 阶段）**: 依赖所有用户故事完成

### 用户故事依赖

- **用户故事 1（P1）**: 可在基础建设（第 2 阶段）后开始 - 不依赖其他故事
- **用户故事 2（P2）**: 可在基础建设（第 2 阶段）后开始 - 复用 US1 的服务基础结构，但可独立测试

### 在每个用户故事内

- 先编写测试并确保它们失败，然后再实施
- 服务接口先于实现
- 服务实现先于 Controller 端点
- 核心实施先于集成
- 故事完成后再进入下一个优先级

### 并行机会

- T001 和 T002 可以并行运行（接口和实现骨架）
- T003 和 T004（US1 测试）可以并行运行
- T010 和 T011（US2 测试）可以并行运行
- US1 和 US2 的服务实现可以在同一服务类中顺序完成，也可以由不同开发者分别实现各自的方法

---

## 并行示例：用户故事 1 + 用户故事 2

```bash
# 基础建设完成后，两个用户故事可以并行开发：

# 开发者 A - 用户故事 1：
任务: "在 ExcelTemplateServiceTest.java 中为用户模板生成编写单元测试"
任务: "在 ExcelTemplateService.java 中添加 downloadUserTemplate 方法签名"
任务: "在 ExcelTemplateServiceImpl.java 中实现用户模板生成逻辑"
任务: "在 SysUserController.java 中实现用户模板下载接口"

# 开发者 B - 用户故事 2：
任务: "在 ExcelTemplateServiceTest.java 中为角色模板生成添加单元测试"
任务: "在 ExcelTemplateService.java 中添加 downloadRoleTemplate 方法签名"
任务: "在 ExcelTemplateServiceImpl.java 中实现角色模板生成逻辑"
任务: "在 SysRoleController.java 中实现角色模板下载接口"
```

---

## 实施策略

### 优先 MVP（仅用户故事 1）

1. 完成第 2 阶段：基础建设（T001, T002）
2. 完成第 3 阶段：用户故事 1（T003-T009）
3. **停止并验证**: 独立测试用户故事 1
4. 如准备好则部署/演示用户模板下载功能

### 增量交付

1. 完成基础建设 → 服务接口就绪
2. 添加用户故事 1 → 独立测试 → 部署/演示（MVP！）
3. 添加用户故事 2 → 独立测试 → 部署/演示
4. 每个故事增加价值而不破坏之前的故事

### 并行团队策略

有多个开发者时：

1. 团队一起完成基础建设（T001, T002）
2. 基础建设完成后：
   - 开发者 A：用户故事 1（用户模板）
   - 开发者 B：用户故事 2（角色模板）
3. 故事独立完成和集成

---

## 注意事项

- [P] 任务 = 不同文件，无依赖
- [Story] 标签将任务映射到特定用户故事以便追踪
- 每个用户故事应可独立完成和测试
- 实施前验证测试失败
- 在每个任务或逻辑组后提交代码
- 在任何检查点停止以独立验证故事
- 特别注意：双Sheet结构，第一个Sheet是填写说明，第二个是数据模板
- 特别注意：用户模板的密码字段需包含明文示例
- 特别注意：中文文件名使用 URLEncoder 编码处理，符合 HTTP 标准
