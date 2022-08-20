package com.bulain.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RedisApplication.class)
public class RedissonLockDemo {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testLock() {
        String lockKey = "LockKey";
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.lock();
            Thread.sleep(10000L);
        } catch (Exception e) {
            log.error("lock()", e);
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void testFairLock() {
        String lockKey = "FairLockKey";
        RLock lock = redissonClient.getFairLock(lockKey);
        try {
            lock.lock();
            Thread.sleep(10000L);
        } catch (Exception e) {
            log.error("lock()", e);
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void testReadLock() {
        String lockKey = "ReadLockKey";
        RReadWriteLock rwLock = redissonClient.getReadWriteLock(lockKey);
        RLock lock = rwLock.readLock();
        try {
            lock.lock();
            Thread.sleep(10000L);
        } catch (Exception e) {
            log.error("lock()", e);
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void testWriteLock() {
        String lockKey = "WriteLockKey";
        RReadWriteLock rwLock = redissonClient.getReadWriteLock(lockKey);
        RLock lock = rwLock.writeLock();
        try {
            lock.lock();
            Thread.sleep(10000L);
        } catch (Exception e) {
            log.error("lock()", e);
        } finally {
            lock.unlock();
        }
    }

}
