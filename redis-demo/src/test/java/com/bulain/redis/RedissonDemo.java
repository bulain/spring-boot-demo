package com.bulain.redis;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RedisApplication.class)
public class RedissonDemo {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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

    @Test
    public void testOpsForValue() {
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        opsForValue.set("opsForValue1", "value1");
        opsForValue.set("opsForValue2", "value2");

        assertEquals("value1", opsForValue.get("opsForValue1"));
        assertEquals("value2", opsForValue.get("opsForValue2"));
    }

    @Test
    public void testOpsForItem() {
        ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
        opsForValue.set("itemForValue1", new Item().setStr("value1"));

        Item obj = (Item) opsForValue.get("itemForValue1");
        assertEquals("value1", obj.getStr());
    }

    @Data
    @Accessors(chain = true)
    static class Item {
        private String str;
        private Date dt;
    }

}
