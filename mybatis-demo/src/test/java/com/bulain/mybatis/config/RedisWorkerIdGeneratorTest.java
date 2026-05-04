package com.bulain.mybatis.config;

import com.bulain.mybatis.core.config.RedisWorkerIdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisWorkerIdGeneratorTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisWorkerIdGenerator redisWorkerIdGenerator;

    @Test
    void getWorkerId_whenRedisAvailable_shouldReturnWorkerIdInRange() {
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("mybatis:id:worker-id")).thenReturn(5L);

        long workerId = redisWorkerIdGenerator.getWorkerId();

        assertTrue(workerId >= 0 && workerId <= 31);
        assertEquals(5L, workerId);
    }

    @Test
    void getWorkerId_whenIdExceedsMax_shouldModBy32() {
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("mybatis:id:worker-id")).thenReturn(35L);

        long workerId = redisWorkerIdGenerator.getWorkerId();

        assertEquals(3L, workerId); // 35 % 32 = 3
    }

    @Test
    void getWorkerId_whenRedisException_shouldReturnRandomId() {
        when(stringRedisTemplate.opsForValue()).thenThrow(new RuntimeException("Redis connection failed"));

        long workerId = redisWorkerIdGenerator.getWorkerId();

        assertTrue(workerId >= 0 && workerId <= 31);
    }

    @Test
    void getWorkerId_whenIncrReturnsNull_shouldReturnRandomId() {
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("mybatis:id:worker-id")).thenReturn(null);

        long workerId = redisWorkerIdGenerator.getWorkerId();

        assertTrue(workerId >= 0 && workerId <= 31);
    }

}
