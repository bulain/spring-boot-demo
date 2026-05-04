# mybatis-demo/CLAUDE.md

MyBatis Plus 3.5.16 多数据库演示模块。

## 架构

**核心扩展:**
- DirectMapper 系列：绕过 MyBatis Plus 缓存直连数据库
- ExtensionMapper：alwaysUpdateSomeColumnById、upsert、insertBatchSomeColumn
- PagedService：通用分页服务基类

**数据库支持:** MySQL、Oracle、SQL Server、PostgreSQL、Kingbase、DM、OpenGauss

**功能特性:** Redisson Redis 集成、数据审计、MFA TOTP、Javers 差异对比

## 常用命令

```bash
# 构建
mvn clean install

# 运行
mvn spring-boot:run
mvn spring-boot:run -Dspring-boot.run.profiles=oracle

# 测试
mvn test -Dtest=BlogMapperDemo
```

**注意:** 大部分集成测试标记为 `@Disabled`，需要运行中的数据库和 Redis 基础设施。

## 核心依赖

- MyBatis Plus 3.5.16、Redisson 3.52.0、Flyway 9.22.3、Hutool 5.8.42

## 配置

- 配置文件: `src/main/resources/application.yml`
- Mapper 扫描: `com.bulain.mybatis.demo.dao`
- Flyway 脚本: `src/main/resources/migration/{db-type}/`

## 开发规范

- 所有文档使用中文编写
- 代码注释统一使用中文
- 接口、类、方法的说明文档使用中文
