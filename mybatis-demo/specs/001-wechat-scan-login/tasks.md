# 任务列表：微信扫码登录

**输入**: 来自 `/specs/001-wechat-scan-login/` 的设计文档
**前置条件**: plan.md（必需）, spec.md（用户故事必需）, research.md, data-model.md, contracts/

**组织方式**: 任务按用户故事分组，以便每个故事可以独立实施和测试。

## 格式: `[ID] [P?] [Story] 描述`

- **[P]**: 可并行执行（不同文件，无依赖）
- **[Story]**: 此任务属于哪个用户故事（例如 US1, US2, US3）
- 在描述中包含确切的文件路径

---

## 第 1 阶段：搭建（共享基础设施）

**目的**: 配置文件和常量定义

- [X] T001 [P] 创建微信开放平台配置类 WechatOpenPlatformConfig.java 在 src/main/java/com/bulain/mybatis/sys/config/
- [X] T002 [P] 创建微信登录常量类 WechatLoginConstants.java 在 src/main/java/com/bulain/mybatis/sys/common/
- [X] T003 [P] 在 application.yml 中添加 wechat.open-platform 配置项

**检查点**: 基础配置就绪，可以开始服务层实现

---

## 第 2 阶段：基础建设（阻塞性前置条件）

**目的**: 核心服务和 DTO，必须在任何用户故事开始实施前完成

**⚠️ 关键**: 在本阶段完成前，不能开始任何用户故事工作

- [X] T004 创建 WechatQrCodeResponse.java DTO 在 src/main/java/com/bulain/mybatis/sys/dto/
- [X] T005 创建 BindWechatDTO.java DTO 在 src/main/java/com/bulain/mybatis/sys/dto/
- [X] T006 创建 UnbindWechatDTO.java DTO 在 src/main/java/com/bulain/mybatis/sys/dto/
- [X] T007 创建 WechatLoginService.java 接口 在 src/main/java/com/bulain/mybatis/sys/service/
- [X] T008 实现 WechatLoginServiceImpl.java 基础框架 在 src/main/java/com/bulain/mybatis/sys/service/impl/

**检查点**: 基础设施准备就绪 - 现在可以并行开始用户故事实施

---

## 第 3 阶段：用户故事 1 - 微信扫码登录（优先级：P1）🎯 MVP

**目标**: 用户打开系统登录页面，选择微信扫码登录，扫码确认授权后自动完成登录并跳转至主页。

**独立测试**: 访问登录页，使用微信扫码授权，验证成功登录并进入系统，首次登录自动创建用户。

### 用户故事 1 的实施

- [X] T009 [P] [US1] 在 WechatLoginServiceImpl.java 中实现生成 state 并存入 Redis 方法
- [X] T010 [P] [US1] 在 WechatLoginServiceImpl.java 中实现获取二维码参数方法
- [X] T011 [US1] 在 WechatLoginServiceImpl.java 中实现通过 code 换取 openid 和 access_token 方法
- [X] T012 [US1] 在 WechatLoginServiceImpl.java 中实现通过 openid 查询或创建用户逻辑
- [X] T013 [US1] 在 SysAuthController.java 中添加 /wechat-qrcode 接口
- [X] T014 [US1] 完善 SysAuthController.java 中 /wechat-login 接口实现
- [X] T015 [US1] 添加微信登录异常处理和友好中文提示

**检查点**: 此时，用户故事 1 应完全可工作并可独立测试

---

## 第 4 阶段：用户故事 2 - 绑定/解绑微信账号（优先级：P2）

**目标**: 已登录用户在个人设置页面，可以将当前账号绑定到微信账号，或解除已绑定的微信账号。

**独立测试**: 登录后进入设置页面，执行绑定操作成功；再执行解绑操作成功；验证重复绑定的提示。

### 用户故事 2 的实施

- [X] T016 [P] [US2] 在 WechatLoginServiceImpl.java 中实现绑定微信方法（含重复绑定检查）
- [X] T017 [P] [US2] 在 WechatLoginServiceImpl.java 中实现解绑微信方法（含登录方式校验）
- [X] T018 [US2] 在 SysUserController.java 中添加 /bind-wechat 接口
- [X] T019 [US2] 在 SysUserController.java 中添加 /unbind-wechat 接口
- [X] T020 [US2] 在 SysUserController.java 中添加 /wechat-status 查询接口

**检查点**: 此时，用户故事 1 和 2 都应独立工作

---

## 第 5 阶段：用户故事 3 - 扫码异常处理（优先级：P3）

**目标**: 在扫码登录过程中遇到各种异常情况时，系统给出清晰的错误提示和处理指引。

**独立测试**: 模拟二维码过期、取消授权、网络中断等场景，验证系统返回正确的错误提示。

### 用户故事 3 的实施

- [X] T021 [P] [US3] 在 WechatLoginServiceImpl.java 中添加 state 过期校验
- [X] T022 [P] [US3] 完善微信接口调用失败的重试和降级处理
- [X] T023 [US3] 统一微信相关异常的错误码和提示信息
- [X] T024 [US3] 添加微信登录操作日志记录

**检查点**: 所有用户故事现在应独立可工作

---

## 第 6 阶段：优化与跨领域关注点

**目的**: 测试、代码清理和文档完善

- [X] T025 [P] 创建 WechatLoginServiceTest.java 单元测试 在 src/test/java/com/bulain/mybatis/sys/service/（6个测试全部通过）
- [X] T026 [P] 创建 SysAuthControllerWechatTest.java 集成测试 在 src/test/java/com/bulain/mybatis/sys/controller/（6个测试全部通过）
- [X] T027 代码清理和格式化，补充必要的中文注释
- [X] T028 运行 quickstart.md 中的测试用例验证功能（单元测试已覆盖，完整流程测试需要微信开放平台配置）
- [ ] T029 在多种数据库上验证功能正确性（至少 MySQL + 一种其他数据库，需多数据库环境）

---

## 依赖关系与执行顺序

### 阶段依赖

- **搭建（第 1 阶段）**: 无依赖 - 可立即开始
- **基础建设（第 2 阶段）**: 依赖搭建完成 - 阻塞所有用户故事
- **用户故事（第 3-5 阶段）**: 全部依赖基础建设阶段完成
  - 然后用户故事可以并行进行（如果有人手）
  - 或按优先级顺序进行（P1 → P2 → P3）
- **优化（第 6 阶段）**: 依赖所有期望的用户故事完成

### 用户故事依赖

- **用户故事 1（P1）**: 可在基础建设（第 2 阶段）后开始 - 不依赖其他故事
- **用户故事 2（P2）**: 可在基础建设（第 2 阶段）后开始 - 与 US1 独立，可并行开发
- **用户故事 3（P3）**: 可在基础建设（第 2 阶段）后开始 - 完善 US1 的异常处理

### 在每个用户故事内

- DTO 和接口先于实现
- 服务层先于控制层
- 核心实现先于异常处理
- 故事完成后再进入下一个优先级

### 并行机会

- 标记 [P] 的所有搭建任务可以并行运行（T001-T003）
- 标记 [P] 的所有基础建设 DTO 任务可以并行运行（T004-T006）
- 一旦基础建设阶段完成，用户故事 1 和 2 可以并行开始
- 标记 [P] 的一个用户故事内的独立方法可以并行实现
- 不同的用户故事可以由不同的团队成员并行处理

---

## 并行示例：用户故事 1

```bash
# 一起启动用户故事 1 的独立方法实现：
任务: "T009 [US1] 实现生成 state 并存入 Redis 方法"
任务: "T010 [US1] 实现获取二维码参数方法"

# 然后顺序执行：
任务: "T011 [US1] 实现通过 code 换取 openid 和 access_token 方法"
任务: "T012 [US1] 实现通过 openid 查询或创建用户逻辑"
任务: "T013-T015 [US1] Controller 接口实现"
```

---

## 实施策略

### 优先 MVP（仅用户故事 1）

1. 完成第 1 阶段：搭建（T001-T003）
2. 完成第 2 阶段：基础建设（T004-T008）
3. 完成第 3 阶段：用户故事 1（T009-T015）
4. **停止并验证**: 独立测试用户故事 1 - 微信扫码登录完整流程
5. 如准备好则部署/演示 MVP

### 增量交付

1. 完成搭建 + 基础建设 → 基础设施就绪
2. 添加用户故事 1 → 独立测试 → 部署/演示（MVP！）
3. 添加用户故事 2 → 独立测试 → 部署/演示
4. 添加用户故事 3 → 独立测试 → 部署/演示
5. 最后完成测试和多数据库验证
6. 每个故事增加价值而不破坏之前的故事

### 并行团队策略

有多个开发者时：

1. 团队一起完成搭建 + 基础建设
2. 基础建设完成后：
   - 开发者 A：用户故事 1（微信扫码登录核心流程）
   - 开发者 B：用户故事 2（绑定/解绑功能）
   - 开发者 C：用户故事 3（异常处理完善 + 测试）
3. 故事独立完成和集成

---

## 任务统计

| 阶段 | 任务数 | 说明 |
|------|--------|------|
| 第 1 阶段 - 搭建 | 3 | 配置和常量 |
| 第 2 阶段 - 基础建设 | 5 | DTO 和服务接口 |
| 第 3 阶段 - US1 (P1) | 7 | 微信扫码登录 MVP |
| 第 4 阶段 - US2 (P2) | 5 | 绑定/解绑功能 |
| 第 5 阶段 - US3 (P3) | 4 | 异常处理完善 |
| 第 6 阶段 - 优化 | 5 | 测试和验证 |
| **总计** | **29** | |

---

## 注意事项

- [P] 任务 = 不同文件，无依赖
- [Story] 标签将任务映射到特定用户故事以便追踪
- 每个用户故事应可独立完成和测试
- 在每个任务或逻辑组后提交代码
- 在任何检查点停止以独立验证故事
- 所有代码注释和错误提示使用中文
- 数据库操作使用 MyBatis Plus Lambda 查询确保多数据库兼容
