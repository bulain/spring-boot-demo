package com.bulain.sharding;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ShardingApplication.class)
class ShardingTest {

    @Autowired
    private ShardingService shardingService;

    @Test
    void testSharding() {
        shardingService.sharding();
        Assertions.assertTrue(true);
    }

    @Test
    void testShardingYear() {
        shardingService.shardingYear();
        Assertions.assertTrue(true);
    }

    @Test
    void testShardingSearch() {
        shardingService.shardingSearch();
        Assertions.assertTrue(true);
    }

    @Test
    void testShardingPage() {
        shardingService.shardingPage();
        Assertions.assertTrue(true);
    }

    @Test
    void testShardingHint() {
        shardingService.shardingHint();
        Assertions.assertTrue(true);
    }

}
