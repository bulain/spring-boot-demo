package com.bulain.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class RedissonDemo {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testRLock() {
        String lockKey = "RLockKey";
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

}
