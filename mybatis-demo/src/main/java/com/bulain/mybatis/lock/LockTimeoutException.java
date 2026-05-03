package com.bulain.mybatis.lock;

/**
 * 获取锁超时异常
 */
public class LockTimeoutException extends RuntimeException {

    private final String lockKey;
    private final long waitTime;

    public LockTimeoutException(String lockKey, long waitTime) {
        super(String.format("获取锁超时 [%s]，等待时间: %d秒", lockKey, waitTime));
        this.lockKey = lockKey;
        this.waitTime = waitTime;
    }

    public LockTimeoutException(String lockKey, long waitTime, Throwable cause) {
        super(String.format("获取锁超时 [%s]，等待时间: %d秒", lockKey, waitTime), cause);
        this.lockKey = lockKey;
        this.waitTime = waitTime;
    }

    public String getLockKey() {
        return lockKey;
    }

    public long getWaitTime() {
        return waitTime;
    }
}
