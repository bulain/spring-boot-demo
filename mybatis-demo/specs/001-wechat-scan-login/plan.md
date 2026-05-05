# 实施计划：微信扫码登录

**分支**: `001-wechat-scan-login` | **日期**: 2026-05-05 | **规格**: [spec.md](./spec.md)
**输入**: 来自 `/specs/001-wechat-scan-login/spec.md` 的功能规格说明

## 摘要

本功能完善 Spring Boot 项目的微信扫码登录能力，主要包括：
1. 微信 OAuth2.0 授权流程实现（生成二维码、回调处理、code 换 openid）
2. 首次微信登录自动创建用户账号并关联微信标识
3. 已登录用户的微信账号绑定/解绑功能
4. 登录过程的安全防护（state 防 CSRF、限流、异常处理）

技术方案：使用微信开放平台标准 OAuth2 接口 + Redis 存储 state + MyBatis Plus 持久化，复用现有 SysUser 实体和 JWT 认证体系。

## 技术上下文

**语言/版本**: Java 17  
**主要依赖**: Spring Boot 3.5.14, MyBatis Plus 3.5.16, Redisson 3.52.0, Lombok  
**存储**: MySQL（用户数据）、Redis（state 存储、限流计数）  
**测试**: JUnit 5, Spring Boot Test  
**目标平台**: Linux 服务器、Docker 容器  
**项目类型**: Web 服务（RESTful API）  
**性能目标**: 登录接口响应时间 < 500ms，支持并发登录  
**约束条件**: 必须支持 7 种数据库（MySQL、Oracle、SQL Server、PostgreSQL、达梦、人大金仓、OpenGauss），SQL 需使用 MyBatis Plus 方言机制  
**规模/范围**: 单模块实现，影响 sys 模块下的认证和用户相关代码

## 章程检查

*关口：必须在第 0 阶段调研前通过。在第 1 阶段设计后重新检查。*

### 预检查（第 0 阶段前）

| 检查项 | 状态 | 说明 |
|--------|------|------|
| 文档使用简体中文 | ✅ 合规 | 所有规格、计划、文档均使用简体中文编写 |
| 代码标识符使用英文 | ✅ 合规 | 类名、方法名、变量名等均遵循英文命名规范 |
| 多数据库兼容性 | ✅ 合规 | 数据模型已考虑，将使用 MyBatis Plus Lambda 查询，无硬编码 SQL |
| 测试要求 | ✅ 合规 | 核心功能需编写单元测试和集成测试 |

### 设计后重检（第 1 阶段后）

| 检查项 | 状态 | 说明 |
|--------|------|------|
| 面向用户输出使用中文 | ✅ 合规 | 接口错误提示、日志业务说明均使用中文 |
| 命名规范 | ✅ 合规 | 类名 PascalCase，方法名 camelCase，数据库字段 snake_case |
| SQL 兼容性 | ✅ 合规 | 无原生 SQL，全部通过 MyBatis Plus 实现，兼容多数据库 |

**结论**: 章程检查通过，无违规项。

## 项目结构

### 文档（本功能）

```text
specs/001-wechat-scan-login/
├── plan.md              # 本文件（/speckit-plan 命令输出）
├── research.md          # 第 0 阶段输出 - 技术调研与决策
├── data-model.md        # 第 1 阶段输出 - 数据模型设计
├── quickstart.md        # 第 1 阶段输出 - 快速开始指南
├── contracts/           # 第 1 阶段输出 - API 接口契约
│   └── api.md
└── tasks.md             # 第 2 阶段输出（/speckit-tasks 命令）
```

### 源代码（仓库根目录）

```text
mybatis-demo/
├── src/main/java/com/bulain/mybatis/
│   ├── sys/
│   │   ├── common/
│   │   │   └── WechatLoginConstants.java （新增）
│   │   ├── config/
│   │   │   └── WechatOpenPlatformConfig.java （新增）
│   │   ├── controller/
│   │   │   ├── SysAuthController.java （修改：完善 wechat-login，新增 wechat-qrcode）
│   │   │   └── SysUserController.java （修改：新增绑定/解绑接口）
│   │   ├── dto/
│   │   │   ├── WechatLoginDTO.java （已有）
│   │   │   ├── WechatQrCodeResponse.java （新增）
│   │   │   ├── BindWechatDTO.java （新增）
│   │   │   └── UnbindWechatDTO.java （新增）
│   │   ├── service/
│   │   │   ├── WechatLoginService.java （新增接口）
│   │   │   └── impl/
│   │   │       └── WechatLoginServiceImpl.java （新增实现）
│   │   └── entity/
│   │       └── SysUser.java （已有，复用 wechat_openid 字段）
│   └── core/
│       └── http/
│           └── RestTemplateConfig.java （如需要新增 HTTP 客户端配置）
└── src/test/java/com/bulain/mybatis/sys/
    ├── service/
    │   └── WechatLoginServiceTest.java （新增单元测试）
    └── controller/
        └── SysAuthControllerWechatTest.java （新增集成测试）
```

**结构决策**: 遵循现有项目结构，在 sys 模块下新增微信登录相关的 service、config、dto，修改现有 controller。不新增独立模块，保持代码内聚。

## 复杂度追踪

> **仅当章程检查存在必须说明的违规情况时填写**

本功能无章程违规项，无需填写。
