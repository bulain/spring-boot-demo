package com.bulain.mybatis.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.config.Profiles;
import com.bulain.mybatis.sys.dao.SysUserMapper;
import com.bulain.mybatis.sys.entity.SysUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
class SysUserMapperTest {

    @Autowired
    private SysUserMapper sysUserMapper;

    @BeforeEach
    void setUp() {
        sysUserMapper.directDelete(new LambdaQueryWrapper<>());
    }

    @Test
    void testInsertAndSelectById() {
        SysUser user = new SysUser();
        user.setUsername("testuser");
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPhone("13800138000");
        user.setStatus(1);

        int result = sysUserMapper.insert(user);
        assertEquals(1, result);
        assertNotNull(user.getId());

        SysUser selected = sysUserMapper.selectById(user.getId());
        assertNotNull(selected);
        assertEquals("testuser", selected.getUsername());
    }

    @Test
    void testUpdateById() {
        SysUser user = new SysUser();
        user.setUsername("updateuser");
        user.setName("Update User");
        sysUserMapper.insert(user);

        user.setName("Updated Name");
        int result = sysUserMapper.updateById(user);
        assertEquals(1, result);

        SysUser updated = sysUserMapper.selectById(user.getId());
        assertEquals("Updated Name", updated.getName());
    }

    @Test
    void testDeleteById() {
        SysUser user = new SysUser();
        user.setUsername("deleteuser");
        user.setName("Delete User");
        sysUserMapper.insert(user);

        int result = sysUserMapper.deleteById(user.getId());
        assertEquals(1, result);

        // Logical delete - should still exist but with dr > 0
        SysUser deleted = sysUserMapper.selectById(user.getId());
        assertNull(deleted); // MyBatis Plus @TableLogic filters it out
    }

    @Test
    void testSelectByUsername() {
        SysUser user = new SysUser();
        user.setUsername("findbyusername");
        user.setName("Find By Username");
        sysUserMapper.insert(user);

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, "findbyusername");
        SysUser found = sysUserMapper.selectOne(wrapper);

        assertNotNull(found);
        assertEquals("Find By Username", found.getName());
    }
}
