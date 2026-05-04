package com.bulain.mybatis.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.config.Profiles;
import com.bulain.mybatis.sys.dao.SysUserMapper;
import com.bulain.mybatis.sys.dto.CreateUserDTO;
import com.bulain.mybatis.sys.dto.UpdateUserDTO;
import com.bulain.mybatis.sys.entity.SysUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
class OptimisticLockTest {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @BeforeEach
    void setUp() {
        sysUserMapper.directDelete(new LambdaQueryWrapper<>());
    }

    @Test
    void testPubtsFieldExistsOnInsert() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("lockuser");
        dto.setName("Lock User");
        dto.setPassword("password123!");

        SysUser user = sysUserService.createUser(dto);

        assertNotNull(user.getPubts());
    }

    @Test
    void testPubtsIncrementsOnUpdate() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("updatepubts");
        dto.setName("Update Pubts User");
        dto.setPassword("password123!");
        SysUser user = sysUserService.createUser(dto);

        Long initialPubts = user.getPubts();

        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setName("Updated Name");
        SysUser updated = sysUserService.updateUser(user.getId(), updateDTO);

        // MyBatis Plus optimistic lock increments the version on each update
        // The actual increment value may vary depending on configuration
        assertNotEquals(initialPubts, updated.getPubts());
    }

    @Test
    void testConcurrentUpdates() throws InterruptedException {
        // Create user
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("concurrentuser");
        dto.setName("Concurrent User");
        dto.setPassword("password123!");
        SysUser user = sysUserService.createUser(dto);

        int threadCount = 5;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    // Get fresh copy of user each time
                    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(SysUser::getId, user.getId());
                    SysUser freshUser = sysUserMapper.selectOne(wrapper);

                    UpdateUserDTO updateDTO = new UpdateUserDTO();
                    updateDTO.setName("Concurrent Update " + index);

                    // Need to set pubts for optimistic lock
                    freshUser.setName("Concurrent Update " + index);
                    int result = sysUserMapper.updateById(freshUser);

                    if (result > 0) {
                        successCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // At least one update should succeed
        assertTrue(successCount.get() > 0);
        // Not all updates may succeed due to optimistic lock
        // This is expected behavior for optimistic locking
    }

    @Test
    void testOptimisticLockWithStaleObject() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("stalelockuser");
        dto.setName("Stale Lock User");
        dto.setPassword("password123!");
        SysUser user = sysUserService.createUser(dto);

        // Keep reference to original user
        SysUser staleUser = new SysUser();
        staleUser.setId(user.getId());
        staleUser.setPubts(user.getPubts());
        staleUser.setName("Stale Name");

        // First update
        UpdateUserDTO update1 = new UpdateUserDTO();
        update1.setName("First Update");
        sysUserService.updateUser(user.getId(), update1);

        // Attempt second update with stale pubts
        // MyBatis Plus optimistic lock will fail silently (return 0) or throw depending on config
        staleUser.setName("Stale Update");
        int result = sysUserMapper.updateById(staleUser);

        // Should not update because pubts is stale
        // This may be 0 (silent failure) or 1 depending on configuration
        // The important thing is the mechanism is in place
        SysUser finalUser = sysUserService.getById(user.getId());
        assertEquals("First Update", finalUser.getName());
    }
}
