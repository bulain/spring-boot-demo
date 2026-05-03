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
mvn clean package -Pthin  # Thin Jar 部署优化

# 运行
mvn spring-boot:run
mvn spring-boot:run -Dspring-boot.run.profiles=oracle

# Thin Jar 运行
cd target/thin/root && java -Dthin.root=. -jar mybatis-demo-1.0.0-SNAPSHOT.jar

# 测试
mvn test -Dtest=BlogMapperDemo
```

**注意:** 大部分集成测试标记为 `@Disabled`，需要运行中的数据库和 Redis 基础设施。

## 代码模式

```java
// Mapper 继承层级
interface BlogMapper extends BaseMapper<Blog>           // 标准 MyBatis Plus
interface OrderMapper extends DirectMapper<Order>, ExtensionMapper<Order>  // 直连数据库

// Service 继承 PagedServiceImpl 获取内置分页能力
class OrderServiceImpl extends PagedServiceImpl<Order, OrderSearch>
```

## 核心依赖

- MyBatis Plus 3.5.16、Redisson 3.52.0、Flyway 9.22.3、Hutool 5.8.42

## 配置

- 配置文件: `src/main/resources/application.yml`
- Mapper 扫描: `com.bulain.mybatis.demo.dao`
- Flyway 脚本: `src/main/resources/migration/{db-type}/`
