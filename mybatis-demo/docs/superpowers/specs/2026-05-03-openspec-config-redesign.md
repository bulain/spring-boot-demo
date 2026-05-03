---
name: OpenSpec 配置文件结构重设计
description: 将 openspec/config.yaml 从扁平结构重构为模块化分层结构，提升机器可读性和可扩展性
type: design
---

# OpenSpec 配置文件重设计

## 设计目标

1. **机器友好**：结构化数据便于工具解析、校验和自动化处理
2. **可扩展**：新增 artifact 类型或规则无需重构整个结构
3. **语义清晰**：每个字段有明确含义，减少歧义
4. **版本化**：支持配置格式的演进

## 设计方案

采用**模块化分层结构**，按功能分层，每个 artifact 有独立的命名空间和子配置。

### 完整配置结构

```yaml
# OpenSpec 配置文件
version: 1.2.0
schema: spec-driven

# 项目元数据
project:
  type: Java/Spring Boot/Mybatis Plus
  build_tool: Maven
  jdk_version: 17
  primary_language: zh

# 约定规范
conventions:
  code_style: 阿里巴巴 Java 开发手册
  commit_style: Conventional Commits (feat/fix/docs/chore...)
  test_framework: JUnit 5 + Mockito
  db_migration: Flyway VYYYYMMDDHHmmss__description.sql

# 代码结构元数据 - AI 自动生成代码时使用
code_structure:
  controller_package: com.bulain.mybatis.controller
  service_package: com.bulain.mybatis.service
  mapper_package: com.bulain.mybatis.mapper
  entity_package: com.bulain.mybatis.entity
  config_pattern: src/main/resources/application-*.yml

# 工件定义 - 每个 artifact 自包含配置
artifacts:
  # 提案文档
  proposal:
    enabled: true
    description: 变更可行性分析文档
    required_sections:
      - risk_assessment
      - goals_non_goals
      - impact_analysis
      - test_impact_analysis
    validation_rules:
      - SHALL 包含风险评估章节
      - SHALL 明确列出目标、非目标、成功指标
      - SHALL 包含变更影响范围分析
      - SHALL 评估对现有测试的影响

  # 需求规格
  specs:
    enabled: true
    description: 需求规格定义
    required_sections:
      - normative_requirements
      - test_scenarios
    validation_rules:
      - SHALL 使用 SHALL/MUST 等规范性词汇描述需求
      - SHALL 每个需求包含至少一个测试场景
      - SHALL 场景描述遵循 WHEN/THEN 格式
      - SHALL 使用中文编写需求描述
    on_change:
      - SHALL 完整列出变更前后的对比

  # 任务分解
  tasks:
    enabled: true
    description: 实施任务分解
    required_sections:
      - unit_tests
      - integration_tests
      - code_review
    constraints:
      max_hours_per_task: 2
    validation_rules:
      - SHALL 包含单元测试任务
      - SHALL 包含集成测试任务
      - SHALL 包含代码审查任务

  # 设计文档
  design:
    enabled: true
    description: 技术方案设计
    required_sections:
      - architecture_diagram
      - api_definition
      - data_flow
      - error_handling
      - transaction_boundary
    validation_rules:
      - SHALL 使用中文编写
      - SHALL 包含架构图（ASCII 或 Mermaid 格式）
      - SHALL 包含接口定义（REST API 的 URL、HTTP 方法、请求/响应格式）
      - SHALL 包含数据流转说明
      - SHALL 明确异常处理策略
      - SHALL 考虑事务边界
```

## 设计要点

| 改进项 | 说明 |
|--------|------|
| 版本号 | 新增 `version` 字段，支持配置格式演进 |
| 四层结构 | 项目元数据 → 约定规范 → 代码结构 → 工件定义 |
| 自描述 artifact | 每个 artifact 有 `description` 说明其用途 |
| 可校验结构 | `required_sections` 和 `validation_rules` 分离 |
| 显式约束 | `constraints` 字段明确列出数值型约束 |

## 向后兼容性

这是一个破坏性变更（原 `context` 和 `rules` 顶级字段被重构）。如果需要兼容旧工具，可在过渡期保留旧字段的别名或转换层。

## 未来扩展方向

1. 配置校验（JSON Schema）
2. 自动化代码生成
3. 规则引擎集成
4. 新增 artifact 类型
