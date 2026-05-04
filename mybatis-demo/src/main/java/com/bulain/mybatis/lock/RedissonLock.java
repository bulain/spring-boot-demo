package com.bulain.mybatis.lock;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * Redisson 锁实现
 */
public class RedissonLock implements Lock {

    private final RLock rLock;

    public RedissonLock(RLock rLock) {
        this.rLock = rLock;
    }

    @Override
    public boolean tryLock(long waitTime, long leaseTime) throws InterruptedException {
        return tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
    }

    @Override
    public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        return rLock.tryLock(waitTime, leaseTime, unit);
    }

    @Override
    public void lock() {
        rLock.lock();
    }

    @Override
    public void unlock() {
        if (rLock.isHeldByCurrentThread()) {
            rLock.unlock();
        }
    }

    @Override
    public boolean isLocked() {
        return rLock.isLocked();
    }

    @Override
    public boolean isHeldByCurrentThread() {
        return rLock.isHeldByCurrentThread();
    }

    @Override
    public void forceUnlock() {
        rLock.forceUnlock();
    }

    @Override
    public void close() {
        unlock();
    }

    /**
     * 获取原始 RLock 对象（供实现内部使用）
     */
    RLock getRLock() {
        return rLock;
    }
}
