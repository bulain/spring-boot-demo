package com.bulain.mybatis.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.config.Profiles;
import com.bulain.mybatis.sys.dao.SysPermissionMapper;
import com.bulain.mybatis.sys.entity.SysPermission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
class SysPermissionMapperTest {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @BeforeEach
    void setUp() {
        sysPermissionMapper.directDelete(new LambdaQueryWrapper<>());
    }

    @Test
    void testInsertAndSelectById() {
        SysPermission permission = new SysPermission();
        permission.setCode("user:create");
        permission.setName("Create User");
        permission.setResourceType("API");
        permission.setDescription("Permission to create user");

        int result = sysPermissionMapper.insert(permission);
        assertEquals(1, result);
        assertNotNull(permission.getId());

        SysPermission selected = sysPermissionMapper.selectById(permission.getId());
        assertNotNull(selected);
        assertEquals("user:create", selected.getCode());
    }

    @Test
    void testUpdateById() {
        SysPermission permission = new SysPermission();
        permission.setCode("user:update");
        permission.setName("Update User");
        sysPermissionMapper.insert(permission);

        permission.setName("Update User V2");
        int result = sysPermissionMapper.updateById(permission);
        assertEquals(1, result);

        SysPermission updated = sysPermissionMapper.selectById(permission.getId());
        assertEquals("Update User V2", updated.getName());
    }

    @Test
    void testDeleteById() {
        SysPermission permission = new SysPermission();
        permission.setCode("user:delete");
        permission.setName("Delete User");
        sysPermissionMapper.insert(permission);

        int result = sysPermissionMapper.deleteById(permission.getId());
        assertEquals(1, result);

        SysPermission deleted = sysPermissionMapper.selectById(permission.getId());
        assertNull(deleted);
    }

    @Test
    void testSelectByCode() {
        SysPermission permission = new SysPermission();
        permission.setCode("user:view");
        permission.setName("View User");
        sysPermissionMapper.insert(permission);

        LambdaQueryWrapper<SysPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysPermission::getCode, "user:view");
        SysPermission found = sysPermissionMapper.selectOne(wrapper);

        assertNotNull(found);
        assertEquals("View User", found.getName());
    }
}
