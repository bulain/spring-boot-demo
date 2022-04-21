package com.bulain.sharding;

import com.zaxxer.hikari.HikariDataSource;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.InlineShardingStrategyConfiguration;
import io.shardingsphere.core.constant.properties.ShardingPropertiesConstant;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardingApplication.class)
public class ShardingDemo {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testSharding() throws SQLException {

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            long orderId = new Date().getTime() % (365 * 24 * 3600);
            for (long i = 0; i < 16; i++) {
                for (long userId = 1; userId < 16; userId++) {
                    String orderSql = "insert into t_order(order_id, user_id, status) values (?, ?, ?)";
                    try (PreparedStatement ps1 = conn.prepareStatement(orderSql)) {
                        ps1.setLong(1, orderId);
                        ps1.setLong(2, userId);
                        ps1.setString(3, "Y");
                        ps1.executeUpdate();

                        for (long j = 0; j < 4; j++) {
                            String itemSql = "insert into t_order_item(order_id, user_id) values (?, ?)";
                            try (PreparedStatement ps2 = conn.prepareStatement(itemSql)) {
                                ps2.setLong(1, orderId);
                                ps2.setLong(2, userId);
                                ps2.executeUpdate();
                            }
                        }
                    }
                    conn.commit();
                    orderId++;
                }
            }
        }
    }

    @Test
    public void testShardingYear() throws SQLException {

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            //long dt = LocalDateTime.now().minusYears(1).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
            long dt = LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
            long hu = new Date().getTime() % (365 * 24 * 3600);
            for (int i = 0; i < 10; i++) {
                long dn = new Date().getTime() % (365 * 24 * 3600);
                for (int j = 0; j < 5; j++) {
                    String husql = "insert into t_hu (hu, dn, pgi) values (?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(husql)) {
                        ps.setString(1, Long.toString(hu));
                        ps.setString(2, Long.toString(dn));
                        ps.setTimestamp(3, new Timestamp(dt));
                        ps.executeUpdate();
                    }
                    hu++;
                }
                conn.commit();
                dn++;
            }
        }

    }

    @Test
    public void testShardingSearch() throws SQLException {

        String sql = "SELECT * from t_hu WHERE dn = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "26716058");

            try (ResultSet rs = pstmt.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count ++;
                }
                log.info("count: {}", count);
            }
        }

//        sql = "SELECT * from t_hu WHERE pgi > ? and pgi < ?";
        sql = "SELECT * from t_hu WHERE pgi between ? and ?";
        long prev = LocalDateTime.now().minusMonths(2).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        long curr = LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(prev));
            ps.setTimestamp(2, new Timestamp(curr));

            try (ResultSet rs = ps.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    count ++;
                }
                log.info("count: {}", count);
            }
        }

    }

    @Test
    public void testShardingCoding() throws SQLException {

        //数据源
        Map<String, DataSource> dataSourceMap = dataSourceMap();

        // 配置 Order 表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("t_order");
        orderTableRuleConfig.setActualDataNodes("ds$->{0..1}.t_order$->{0..1}");

        // 配置分库 + 分表策略
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "ds${user_id % 2}"));
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "t_order${order_id % 2}"));

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.setDefaultDataSourceName("ds0");
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);

        // 获取数据源对象
        Map<String, Object> configMap = new HashMap<>();
        Properties props = new Properties();
        props.put(ShardingPropertiesConstant.SQL_SHOW.getKey(), true);
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, configMap, props);

        String sql = "SELECT * from t_order WHERE order_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, 7086829L);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // %2 结果，路由到 shard1.order1
                    log.info("orderId---------" + rs.getInt(1));
                    log.info("userId---------" + rs.getInt(2));
                    log.info("status---------" + rs.getString(3));
                }
            }
        }

        sql = "SELECT * from t_order WHERE order_id = ? and user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, 7086829L);
            pstmt.setLong(2, 9L);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // %2 结果，路由到 shard1.order1
                    log.info("orderId---------" + rs.getInt(1));
                    log.info("userId---------" + rs.getInt(2));
                    log.info("status---------" + rs.getString(3));
                }
            }
        }

    }

    private Map<String, DataSource> dataSourceMap() {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // 配置第一个数据源
        HikariDataSource dataSource1 = new HikariDataSource();
        dataSource1.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource1.setJdbcUrl("jdbc:mysql://localhost:3306/db1?useSSL=false&serverTimezone=PRC&characterEncoding=utf-8");
        dataSource1.setUsername("sharding");
        dataSource1.setPassword("dev");
        dataSourceMap.put("ds0", dataSource1);

        // 配置第二个数据源
        HikariDataSource dataSource2 = new HikariDataSource();
        dataSource2.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource2.setJdbcUrl("jdbc:mysql://localhost:3306/db2?useSSL=false&serverTimezone=PRC&characterEncoding=utf-8");
        dataSource2.setUsername("sharding");
        dataSource2.setPassword("dev");
        dataSourceMap.put("ds1", dataSource2);
        return dataSourceMap;
    }

}
