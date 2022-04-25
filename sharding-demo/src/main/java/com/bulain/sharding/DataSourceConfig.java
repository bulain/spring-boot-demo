package com.bulain.sharding;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AutoConfigureAfter({SpringBootConfiguration.class})
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.shardingsphere.datasource.ds0")
    public DataSource masterDataSource() {
        return new HikariDataSource();
    }

    @Qualifier("shardingDataSource")
    @Autowired
    private DataSource shardingDataSource;

    @Primary
    @Bean
    public DynamicDataSource dynamicDataSource() {

        //主数据源
        DataSource masterDataSource = masterDataSource();

        //动态数据源
        DynamicDataSource dynamicDataSource = new DynamicDataSource();

        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource);

        // 配置多数据源
        Map<Object, Object> dsMap = new HashMap();
        dsMap.put("master", masterDataSource);
        dsMap.put("sharding", shardingDataSource);
        dynamicDataSource.setTargetDataSources(dsMap);

        return dynamicDataSource;

    }

    @Bean
    public PlatformTransactionManager transactionManager(DynamicDataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }

}
