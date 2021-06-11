package com.bulain.mybatis;

import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.bulain.mybatis.plugin.PageNoCacheInterceptor;
import com.bulain.mybatis.redis.RedisContextHolder;
import com.bulain.mybatis.redis.RedisService;
import com.bulain.mybatis.redis.RedisServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.bulain.mybatis.demo.dao")
public class MybatisPlusConfig {

    @Bean
    public RedisService redisService(RedisTemplate<Object, Object> redisTemplate) {
        return new RedisServiceImpl(redisTemplate);
    }

    @Bean
    public RedisContextHolder redisContextHolder(RedisService redisService) {
        return new RedisContextHolder(redisService);
    }
    
    @Bean
    public PaginationInnerInterceptor paginationInterceptor() {
        return new PaginationInnerInterceptor();
    }

    @Bean
    public PageNoCacheInterceptor cachePaginationInterceptor() {
        return new PageNoCacheInterceptor();
    }

    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

}
