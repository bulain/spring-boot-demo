package com.bulain.caffeine.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置
 */
@EnableCaching
@Configuration
public class CaffeineConfig {

    @Value("${spring.cache.caffeine.spec:maximumSize=100000,expireAfterWrite=2m}")
    private String caffeineSpec;

    // Caffeine缓存管理器
    @Bean
    public CacheManager redisCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheSpecification(caffeineSpec);
        return cacheManager;
    }

}
