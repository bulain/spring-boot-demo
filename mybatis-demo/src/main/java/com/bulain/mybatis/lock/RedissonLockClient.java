package com.bulain.mybatis.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 基于 Redisson 的分布式锁客户端实现
 */
public class RedissonLockClient implements LockClient {

    private static final Logger log = LoggerFactory.getLogger(RedissonLockClient.class);

    private final RedissonClient redissonClient;

    public RedissonLockClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public <T> T execute(String lockKey, LockCallback<T> action) {
        // 使用看门狗模式：-1 表示启用自动续期，默认 30 秒
        return execute(lockKey, -1, -1, TimeUnit.SECONDS, action);
    }

    @Override
    public <T> T execute(String lockKey, long waitTime, long leaseTime, LockCallback<T> action) {
        return execute(lockKey, waitTime, leaseTime, TimeUnit.SECONDS, action);
    }

    @Override
    public <T> T execute(String lockKey, long waitTime, long leaseTime, TimeUnit unit, LockCallback<T> action) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            locked = lock.tryLock(waitTime, leaseTime, unit);
            if (!locked) {
                throw new LockTimeoutException(lockKey, unit.toSeconds(waitTime));
            }
            log.debug("成功获取锁 [{}]", lockKey);
            return action.doInLock();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LockTimeoutException(lockKey, unit.toSeconds(waitTime), e);
        } catch (LockTimeoutException e) {
            throw e;
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException("锁保护下的操作异常", e);
        } finally {
            if (locked) {
                lock.unlock();
                log.debug("释放锁 [{}]", lockKey);
            }
        }
    }

    @Override
    public <T> Optional<T> tryExecute(String lockKey, LockCallback<T> action) {
        return tryExecute(lockKey, 0, -1, TimeUnit.SECONDS, action);
    }

    @Override
    public <T> Optional<T> tryExecute(String lockKey, long waitTime, long leaseTime, TimeUnit unit, LockCallback<T> action) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            locked = lock.tryLock(waitTime, leaseTime, unit);
            if (!locked) {
                log.debug("获取锁失败 [{}]", lockKey);
                return Optional.empty();
            }
            log.debug("成功获取锁 [{}]", lockKey);
            T result = action.doInLock();
            return Optional.ofNullable(result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException("锁保护下的操作异常", e);
        } finally {
            if (locked) {
                lock.unlock();
                log.debug("释放锁 [{}]", lockKey);
            }
        }
    }

    @Override
    public Lock getLock(String lockKey) {
        return new RedissonLock(redissonClient.getLock(lockKey));
    }

    @Override
    public Lock getFairLock(String lockKey) {
        return new RedissonLock(redissonClient.getFairLock(lockKey));
    }

    @Override
    public boolean tryLock(String lockKey, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    @Override
    public boolean isLocked(String lockKey) {
        return redissonClient.getLock(lockKey).isLocked();
    }

    @Override
    public boolean isHeldByCurrentThread(String lockKey) {
        return redissonClient.getLock(lockKey).isHeldByCurrentThread();
    }
}
