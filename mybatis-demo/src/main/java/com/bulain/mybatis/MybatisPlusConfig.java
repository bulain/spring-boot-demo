package com.bulain.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.bulain.mybatis.redis.RedisContextHolder;
import com.bulain.mybatis.redis.RedisService;
import com.bulain.mybatis.redis.RedisServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.bulain.mybatis.demo.dao")
public class MybatisPlusConfig {


    @Bean
    public RedisService redisService(RedisTemplate<Object, Object> redisTemplate) {
        RedisServiceImpl redisService = new RedisServiceImpl(redisTemplate);
        RedisContextHolder.setRedisService(redisService);
        return redisService;
    }

    private DbType dbType(MybatisPlusProperties properties, DataSource dataSource) {

        // 数据库类型
        try (Connection conn = dataSource.getConnection()) {
            String url = conn.getMetaData().getURL();
            DbType dbType = JdbcUtils.getDbType(url);
            MybatisPlusContext.setDbType(dbType);
        } catch (Exception e) {
            // NOP
        }

        // 查询方言
        DbType dbTypeDialect = null;
        MybatisPlusProperties.CoreConfiguration configuration = properties.getConfiguration();
        if (configuration != null && configuration.getVariables() != null) {
            String dbType = configuration.getVariables().getProperty("db-type");
            if (StringUtils.isNotBlank(dbType)) {
                dbTypeDialect = DbType.getDbType(dbType);
            }
        }

        return dbTypeDialect;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(MybatisPlusProperties properties, DataSource dataSource) {
        DbType dbType = dbType(properties, dataSource);
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(dbType));
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    @Bean
    public MybatisPlusSqlInjector mbatisPlusSqlInjector() {
        return new MybatisPlusSqlInjector();
    }

}
