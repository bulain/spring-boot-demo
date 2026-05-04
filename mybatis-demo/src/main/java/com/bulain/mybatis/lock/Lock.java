package com.bulain.mybatis.lock;

import java.util.concurrent.TimeUnit;

/**
 * 锁接口
 * 支持 try-with-resources 语法自动释放
 */
public interface Lock extends AutoCloseable {

    /**
     * 尝试获取锁
     *
     * @param waitTime  等待时间（秒）
     * @param leaseTime 租赁时间（秒），超过后自动释放
     * @return 是否获取成功
     * @throws InterruptedException 被中断时抛出
     */
    boolean tryLock(long waitTime, long leaseTime) throws InterruptedException;

    /**
     * 尝试获取锁
     *
     * @param waitTime  等待时间
     * @param leaseTime 租赁时间，超过后自动释放
     * @param unit      时间单位
     * @return 是否获取成功
     * @throws InterruptedException 被中断时抛出
     */
    boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException;

    /**
     * 阻塞获取锁（使用看门狗自动续期）
     */
    void lock();

    /**
     * 释放锁
     */
    void unlock();

    /**
     * 检查锁是否被任何线程持有
     *
     * @return 是否被锁定
     */
    boolean isLocked();

    /**
     * 检查当前线程是否持有锁
     *
     * @return 当前线程是否持有锁
     */
    boolean isHeldByCurrentThread();

    /**
     * 强制释放锁（无论持有线程是谁）
     */
    void forceUnlock();

    /**
     * try-with-resources 自动释放
     */
    @Override
    void close();
}
