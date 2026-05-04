package com.bulain.mybatis.lock;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁客户端接口
 * 提供模板模式（推荐）和细粒度控制两种使用方式
 */
public interface LockClient {

    // ========== 模板模式（推荐使用） ==========

    /**
     * 在锁保护下执行操作（默认：无限等待，看门狗自动续期）
     *
     * @param lockKey 锁键
     * @param action  回调操作
     * @param <T>     返回值类型
     * @return 操作结果
     * @throws LockTimeoutException 获取锁超时
     */
    <T> T execute(String lockKey, LockCallback<T> action);

    /**
     * 在锁保护下执行操作（带超时）
     *
     * @param lockKey   锁键
     * @param waitTime  最大等待时间（秒）
     * @param leaseTime 锁持有时间（秒），超过后自动释放
     * @param action    回调操作
     * @param <T>       返回值类型
     * @return 操作结果
     * @throws LockTimeoutException 获取锁超时
     */
    <T> T execute(String lockKey, long waitTime, long leaseTime, LockCallback<T> action);

    /**
     * 在锁保护下执行操作（带超时和时间单位）
     *
     * @param lockKey   锁键
     * @param waitTime  最大等待时间
     * @param leaseTime 锁持有时间，超过后自动释放
     * @param unit      时间单位
     * @param action    回调操作
     * @param <T>       返回值类型
     * @return 操作结果
     * @throws LockTimeoutException 获取锁超时
     */
    <T> T execute(String lockKey, long waitTime, long leaseTime, TimeUnit unit, LockCallback<T> action);

    /**
     * 尝试在锁保护下执行操作，获取失败不抛异常
     *
     * @param lockKey 锁键
     * @param action  回调操作
     * @param <T>     返回值类型
     * @return 操作结果（获取锁失败返回空 Optional）
     */
    <T> Optional<T> tryExecute(String lockKey, LockCallback<T> action);

    /**
     * 尝试在锁保护下执行操作，获取失败不抛异常
     *
     * @param lockKey   锁键
     * @param waitTime  最大等待时间
     * @param leaseTime 锁持有时间
     * @param unit      时间单位
     * @param action    回调操作
     * @param <T>       返回值类型
     * @return 操作结果（获取锁失败返回空 Optional）
     */
    <T> Optional<T> tryExecute(String lockKey, long waitTime, long leaseTime, TimeUnit unit, LockCallback<T> action);

    // ========== 细粒度控制（高级使用） ==========

    /**
     * 获取可重入锁对象
     *
     * @param lockKey 锁键
     * @return 锁对象
     */
    Lock getLock(String lockKey);

    /**
     * 获取公平锁对象（先到先得）
     *
     * @param lockKey 锁键
     * @return 公平锁对象
     */
    Lock getFairLock(String lockKey);

    /**
     * 尝试获取锁
     *
     * @param lockKey   锁键
     * @param waitTime  最大等待时间（秒）
     * @param leaseTime 锁持有时间（秒）
     * @return 是否获取成功
     */
    boolean tryLock(String lockKey, long waitTime, long leaseTime);

    /**
     * 释放锁
     *
     * @param lockKey 锁键
     */
    void unlock(String lockKey);

    /**
     * 检查锁是否被任何线程持有
     *
     * @param lockKey 锁键
     * @return 是否被锁定
     */
    boolean isLocked(String lockKey);

    /**
     * 检查当前线程是否持有锁
     *
     * @param lockKey 锁键
     * @return 当前线程是否持有锁
     */
    boolean isHeldByCurrentThread(String lockKey);
}
