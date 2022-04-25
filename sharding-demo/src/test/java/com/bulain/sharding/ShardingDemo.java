package com.bulain.sharding;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShardingApplication.class)
public class ShardingDemo {

    @Autowired
    private ShardingService shardingService;

    @Test
    public void testSharding() throws SQLException {
        shardingService.sharding();
    }

    @Test
    public void testShardingYear() throws SQLException {
        shardingService.shardingYear();
    }

    @Test
    public void testShardingSearch() throws SQLException{
        shardingService.shardingSearch();
    }

    @Test
    public void testShardingHint() throws SQLException {
        shardingService.shardingHint();
    }

}
