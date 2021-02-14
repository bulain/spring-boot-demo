package com.bulain.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class LockDemo {

    private CuratorFramework client;

    @Before
    public void setUp() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 1);
        client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",
                5000, 1000, retryPolicy);
        client.start();
    }

    @After
    public void tearDown() {
        client.close();
    }

    @Test
    public void testInterProcessMutex() throws Exception {
        InterProcessLock lock = new InterProcessMutex(client, "/lock/a");
        try {
            lock.acquire();
        } finally {
            lock.release();
        }
    }

    @Test
    public void testInterProcessSemaphoreMutex() throws Exception {
        InterProcessLock lock = new InterProcessSemaphoreMutex(client, "/lock/b");
        try {
            lock.acquire();
        } finally {
            lock.release();
        }
    }

    @Test
    public void testInterProcessReadWriteLock() throws Exception {
        InterProcessReadWriteLock lock = new InterProcessReadWriteLock(client, "/lock/c");
        InterProcessMutex readLock = lock.readLock();
        try {
            readLock.acquire();
        } finally {
            readLock.release();
        }
    }

    @Test
    public void testInterProcessMultiLock() throws Exception {
        InterProcessLock lock = new InterProcessMultiLock(client, Arrays.asList("/lock/d", "/lock/e"));
        try {
            lock.acquire();
        } finally {
            lock.release();
        }
    }

}
