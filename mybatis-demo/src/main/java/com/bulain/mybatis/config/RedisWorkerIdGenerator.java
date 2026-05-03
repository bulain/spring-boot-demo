package com.bulain.mybatis.config;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis 原子分配 workerId 的分布式 ID 生成器
 * <p>
 * 通过 Redis INCR 原子操作分配 workerId，避免 Docker 多实例部署时产生重复 ID
 */
public class RedisWorkerIdGenerator {

    private static final Logger log = LoggerFactory.getLogger(RedisWorkerIdGenerator.class);

    private static final String WORKER_ID_KEY = "mybatis:id:worker-id";
    private static final long MAX_WORKER_ID = 31L;

    private final StringRedisTemplate stringRedisTemplate;

    public RedisWorkerIdGenerator(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 获取 workerId
     * <p>
     * 通过 Redis INCR 原子操作获取，然后对 32 取模确保在有效范围
     * Redis 不可用时返回 null，使用默认机制
     *
     * @return workerId (0-31)，Redis 不可用时返回 null
     */
    public Long getWorkerId() {
        try {
            Long increment = stringRedisTemplate.opsForValue().increment(WORKER_ID_KEY);
            if (increment == null) {
                log.warn("Redis INCR 返回 null，降级使用默认 workerId 生成机制");
                return null;
            }
            long workerId = increment & MAX_WORKER_ID; // 等价于 % 32
            log.info("通过 Redis 原子分配获得 workerId: {}", workerId);
            return workerId;
        } catch (Exception e) {
            log.warn("Redis 不可用，降级使用默认 workerId 生成机制: {}", e.getMessage());
            return null;
        }
    }

}
