## 1. 核心接口定义

- [x] 1.1 创建 `LockCallback<T>` 函数式回调接口
- [x] 1.2 创建 `Lock` 接口，继承 `AutoCloseable`
- [x] 1.3 创建 `LockClient` 主接口，定义全部 API 方法
- [x] 1.4 创建 `LockTimeoutException` 运行时异常类

## 2. Redisson 实现

- [x] 2.1 创建 `RedissonLock` 实现类，封装 `RLock`
- [x] 2.2 创建 `RedissonLockClient` 实现类，注入 `RedissonClient`
- [x] 2.3 实现模板模式的 `execute()` 和 `tryExecute()` 方法
- [x] 2.4 实现细粒度控制的全部方法

## 3. Spring 配置

- [x] 3.1 创建 `LockClientAutoConfiguration` 自动配置类
- [x] 3.2 条件注解：存在 `RedissonClient` Bean 时生效
- [x] 3.3 添加 Spring Boot 3.x 自动配置注册文件

## 4. 单元测试

- [x] 4.1 编写 LockCallback 测试用例
- [x] 4.2 编写模板模式 execute 测试（含超时场景）
- [x] 4.3 编写细粒度 API 测试（tryLock、unlock、状态检查）
- [x] 4.4 编写异常场景测试
