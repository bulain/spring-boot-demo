package com.bulain.mybatis.lock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedissonLockTest {

    @Mock
    private RLock rLock;

    private RedissonLock lock;

    @BeforeEach
    void setUp() {
        lock = new RedissonLock(rLock);
    }

    @Test
    void tryLock_shouldDelegateToRLock() throws InterruptedException {
        when(rLock.tryLock(5, 30, TimeUnit.SECONDS)).thenReturn(true);

        boolean result = lock.tryLock(5, 30);

        assertTrue(result);
        verify(rLock).tryLock(5, 30, TimeUnit.SECONDS);
    }

    @Test
    void tryLockWithUnit_shouldUseSpecifiedUnit() throws InterruptedException {
        when(rLock.tryLock(5000, 30000, TimeUnit.MILLISECONDS)).thenReturn(true);

        boolean result = lock.tryLock(5000, 30000, TimeUnit.MILLISECONDS);

        assertTrue(result);
        verify(rLock).tryLock(5000, 30000, TimeUnit.MILLISECONDS);
    }

    @Test
    void lock_shouldDelegateToRLock() {
        lock.lock();
        verify(rLock).lock();
    }

    @Test
    void unlock_shouldUnlockWhenHeldByCurrentThread() {
        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        lock.unlock();

        verify(rLock).unlock();
    }

    @Test
    void unlock_shouldNotUnlockWhenNotHeldByCurrentThread() {
        when(rLock.isHeldByCurrentThread()).thenReturn(false);

        lock.unlock();

        verify(rLock, never()).unlock();
    }

    @Test
    void isLocked_shouldReturnLockStatus() {
        when(rLock.isLocked()).thenReturn(true);
        assertTrue(lock.isLocked());

        when(rLock.isLocked()).thenReturn(false);
        assertFalse(lock.isLocked());
    }

    @Test
    void isHeldByCurrentThread_shouldReturnHeldStatus() {
        when(rLock.isHeldByCurrentThread()).thenReturn(true);
        assertTrue(lock.isHeldByCurrentThread());

        when(rLock.isHeldByCurrentThread()).thenReturn(false);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    void forceUnlock_shouldDelegateToRLock() {
        lock.forceUnlock();
        verify(rLock).forceUnlock();
    }

    @Test
    void close_shouldUnlockWhenHeldByCurrentThread() {
        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        lock.close();

        verify(rLock).unlock();
    }

    @Test
    void close_shouldNotUnlockWhenNotHeldByCurrentThread() {
        when(rLock.isHeldByCurrentThread()).thenReturn(false);

        lock.close();

        verify(rLock, never()).unlock();
    }
}
