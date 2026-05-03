## Purpose

提供统一的分布式锁客户端抽象，屏蔽底层 Redisson 实现细节，支持模板模式和细粒度控制两种使用方式。

## Requirements

### Requirement: 模板模式 API

系统 SHALL 提供基于回调的模板模式 API，自动管理锁的获取和释放。

#### Scenario: 基本 execute 调用
- **WHEN** 调用 `execute(lockKey, callback)`
- **THEN** 系统尝试获取锁，成功后执行回调
- **AND** 回调执行完成后自动释放锁
- **AND** 默认使用看门狗自动续期机制

#### Scenario: 带超时的 execute 调用
- **WHEN** 调用 `execute(lockKey, waitTime, leaseTime, callback)`
- **THEN** 系统在 waitTime 秒内尝试获取锁
- **AND** 锁持有超过 leaseTime 秒后自动释放
- **AND** 获取超时时抛出运行时异常

#### Scenario: tryExecute 不抛异常
- **WHEN** 调用 `tryExecute(lockKey, callback)`
- **THEN** 系统尝试获取锁，成功执行回调并返回结果
- **AND** 获取失败返回空 Optional，不抛出异常

### Requirement: 细粒度锁控制 API

系统 SHALL 提供细粒度的锁控制 API，支持手动管理锁生命周期。

#### Scenario: 获取锁对象
- **WHEN** 调用 `getLock(lockKey)`
- **THEN** 返回可重入锁对象，支持 try-with-resources 语法

#### Scenario: 获取公平锁
- **WHEN** 调用 `getFairLock(lockKey)`
- **THEN** 返回公平锁对象，保证先到先得

#### Scenario: 手动 tryLock
- **WHEN** 调用 `tryLock(lockKey, waitTime, leaseTime)`
- **THEN** 获取成功返回 true，失败返回 false

#### Scenario: 手动解锁
- **WHEN** 调用 `unlock(lockKey)`
- **THEN** 释放锁（如当前线程持有）

#### Scenario: 检查锁状态
- **WHEN** 调用 `isLocked(lockKey)`
- **THEN** 返回锁是否被任何线程持有

#### Scenario: 检查当前线程持有
- **WHEN** 调用 `isHeldByCurrentThread(lockKey)`
- **THEN** 返回锁是否被当前线程持有

### Requirement: 锁对象特性

系统 SHALL 提供的锁对象支持以下特性：

#### Scenario: 锁对象可重入
- **WHEN** 同一线程多次调用 lock 或 tryLock
- **THEN** 锁计数递增，需对应次数 unlock 才真正释放

#### Scenario: 锁对象支持强制解锁
- **WHEN** 调用 `forceUnlock()`
- **THEN** 无论持有线程是谁，强制释放锁

#### Scenario: AutoCloseable 自动释放
- **WHEN** 在 try-with-resources 中使用锁对象
- **THEN** 代码块结束时自动调用 unlock

### Requirement: 异常处理

系统 SHALL 采用统一的异常处理策略。

#### Scenario: 获取锁超时
- **WHEN** execute 方法获取锁超时
- **THEN** 抛出运行时异常 `LockTimeoutException`

#### Scenario: 业务代码异常
- **WHEN** 回调内部抛出异常
- **THEN** 异常原样向上传递，锁保证被释放
