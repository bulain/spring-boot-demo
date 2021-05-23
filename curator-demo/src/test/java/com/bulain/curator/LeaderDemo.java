package com.bulain.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class LeaderDemo {

    private CuratorFramework client;

    @BeforeEach
    public void setUp() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 1);
        client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",
                5000, 1000, retryPolicy);
        client.start();
    }

    @AfterEach
    public void tearDown() {
        client.close();
    }
    
    @Test
    public void testLeaderLatch() throws Exception {

        LeaderLatch latch = new LeaderLatch(client, "/leader/a");
        latch.addListener(new LeaderLatchListener() {
            @Override
            public void isLeader() {
                log.info("isLeader()");
            }
            @Override
            public void notLeader() {
                log.info("notLeader()");
            }
        });
        latch.start();
        
    }

    @Test
    public void testLeaderSelector() {
        LeaderSelector latch = new LeaderSelector(client, "/leader/b", new LeaderSelectorListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                log.info("stateChanged()");
            }
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                log.info("takeLeadership()");
            }
        });
        latch.start();
    }
    
}
