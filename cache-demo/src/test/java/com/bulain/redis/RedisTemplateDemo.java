package com.bulain.redis;

import com.bulain.redis.pojo.RedisUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RedisApplication.class)
public class RedisTemplateDemo {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
        opsForValue.set("itemForValue1", new RedisUser().setUserName("value1"));

        RedisUser obj = (RedisUser) opsForValue.get("itemForValue1");
        assertEquals("value1", obj.getUserName());
    }

}
