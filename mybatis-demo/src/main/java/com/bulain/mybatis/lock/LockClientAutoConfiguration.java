package com.bulain.mybatis.lock;

import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * LockClient 自动配置
 * 当 RedissonClient Bean 存在时自动装配
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedissonClient.class)
public class LockClientAutoConfiguration {

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnMissingBean(LockClient.class)
    public LockClient lockClient(RedissonClient redissonClient) {
        return new RedissonLockClient(redissonClient);
    }
}
