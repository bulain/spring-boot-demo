package com.bulain.mybatis.lock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedissonLockClientTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock rLock;

    private RedissonLockClient lockClient;

    @BeforeEach
    void setUp() {
        lockClient = new RedissonLockClient(redissonClient);
        lenient().when(redissonClient.getLock("test-lock")).thenReturn(rLock);
    }

    @Test
    void execute_shouldExecuteCallbackWhenLockAcquired() throws Exception {
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);

        String result = lockClient.execute("test-lock", () -> "success");

        assertEquals("success", result);
        verify(rLock).tryLock(-1, -1, TimeUnit.SECONDS);
        verify(rLock).unlock();
    }

    @Test
    void execute_shouldThrowLockTimeoutWhenLockNotAcquired() throws Exception {
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(false);

        assertThrows(LockTimeoutException.class, () ->
                lockClient.execute("test-lock", 5, 30, () -> "success")
        );

        verify(rLock).tryLock(5, 30, TimeUnit.SECONDS);
        verify(rLock, never()).unlock();
    }

    @Test
    void execute_shouldUnlockEvenWhenCallbackThrowsException() throws Exception {
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                lockClient.execute("test-lock", () -> {
                    throw new RuntimeException("business error");
                })
        );

        verify(rLock).unlock();
    }

    @Test
    void tryExecute_shouldReturnResultWhenLockAcquired() throws Exception {
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);

        Optional<String> result = lockClient.tryExecute("test-lock", () -> "success");

        assertTrue(result.isPresent());
        assertEquals("success", result.get());
        verify(rLock).unlock();
    }

    @Test
    void tryExecute_shouldReturnEmptyWhenLockNotAcquired() throws Exception {
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(false);

        Optional<String> result = lockClient.tryExecute("test-lock", () -> "success");

        assertFalse(result.isPresent());
        verify(rLock, never()).unlock();
    }

    @Test
    void getLock_shouldReturnLockInstance() {
        Lock lock = lockClient.getLock("test-lock");

        assertNotNull(lock);
        assertInstanceOf(RedissonLock.class, lock);
    }

    @Test
    void getFairLock_shouldReturnFairLockInstance() {
        when(redissonClient.getFairLock("fair-lock")).thenReturn(rLock);

        Lock lock = lockClient.getFairLock("fair-lock");

        assertNotNull(lock);
        verify(redissonClient).getFairLock("fair-lock");
    }

    @Test
    void tryLock_shouldReturnTrueWhenAcquired() throws Exception {
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);

        boolean result = lockClient.tryLock("test-lock", 5, 30);

        assertTrue(result);
    }

    @Test
    void tryLock_shouldReturnFalseWhenNotAcquired() throws Exception {
        when(rLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(false);

        boolean result = lockClient.tryLock("test-lock", 5, 30);

        assertFalse(result);
    }

    @Test
    void unlock_shouldUnlockWhenHeldByCurrentThread() {
        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        lockClient.unlock("test-lock");

        verify(rLock).unlock();
    }

    @Test
    void unlock_shouldNotUnlockWhenNotHeldByCurrentThread() {
        when(rLock.isHeldByCurrentThread()).thenReturn(false);

        lockClient.unlock("test-lock");

        verify(rLock, never()).unlock();
    }

    @Test
    void isLocked_shouldReturnLockStatus() {
        when(rLock.isLocked()).thenReturn(true);

        boolean locked = lockClient.isLocked("test-lock");

        assertTrue(locked);
    }

    @Test
    void isHeldByCurrentThread_shouldReturnHeldStatus() {
        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        boolean held = lockClient.isHeldByCurrentThread("test-lock");

        assertTrue(held);
    }
}
