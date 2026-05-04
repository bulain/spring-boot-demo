package com.bulain.mybatis.core.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * MyBatis Plus 分布式 ID 生成器配置
 * <p>
 * 通过 Redis 原子分配 workerId，支持配置 datacenterId
 */
@Configuration
public class MybatisPlusIdConfig {

    private static final Logger log = LoggerFactory.getLogger(MybatisPlusIdConfig.class);

    private static final long MAX_DATACENTER_ID = 31L;

    @Value("${mybatis-plus.global-config.datacenter-id:0}")
    private long datacenterId;

    private long getValidDatacenterId() {
        if (datacenterId < 0 || datacenterId > MAX_DATACENTER_ID) {
            log.warn("datacenterId {} 超出范围 (0-31)，使用默认值 0", datacenterId);
            return 0;
        }
        log.info("MyBatis Plus 分布式 ID 生成器配置，datacenterId: {}", datacenterId);
        return datacenterId;
    }

    @Bean
    public RedisWorkerIdGenerator redisWorkerIdGenerator(StringRedisTemplate stringRedisTemplate) {
        return new RedisWorkerIdGenerator(stringRedisTemplate);
    }

    @Bean
    public IdentifierGenerator identifierGenerator(RedisWorkerIdGenerator redisWorkerIdGenerator) {
        long validDatacenterId = getValidDatacenterId();
        long workerId = redisWorkerIdGenerator.getWorkerId();
        Sequence sequence = new Sequence(workerId, validDatacenterId);
        return new DefaultIdentifierGenerator(sequence);
    }
}
