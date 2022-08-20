package com.bulain.redis.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存配置
 */
@EnableCaching
@Configuration
public class RedisConfig {

    // Redis客户端
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        //序列化方式
        RedisSerializer<String> keySerializer = RedisSerializer.string();
        RedisSerializer<Object> jsonSerializer = jsonSerializer();

        //Redis客户端
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(jsonSerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setHashValueSerializer(jsonSerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Value("${spring.cache.redis.time-to-live:2m}")
    private Duration redisTimeToLive;

    // Redis缓存管理器
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory factory) {

        //序列化方式
        RedisSerializer<String> keySerializer = RedisSerializer.string();
        RedisSerializer<Object> jsonSerializer = jsonSerializer();

        //缓存配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer))
                .entryTtl(redisTimeToLive);
        Map<String, RedisCacheConfiguration> map = new HashMap<>();

        //生成缓存管理器
        return RedisCacheManager
                .builder(factory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(map)
                .build();
    }

    /**
     * JSON序列化方式
     */
    private RedisSerializer<Object> jsonSerializer() {
        return new GenericFastJsonRedisSerializer();
    }

}
