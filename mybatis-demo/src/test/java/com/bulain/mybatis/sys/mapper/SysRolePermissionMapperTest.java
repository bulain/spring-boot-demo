package com.bulain.mybatis.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.config.Profiles;
import com.bulain.mybatis.sys.dao.SysPermissionMapper;
import com.bulain.mybatis.sys.dao.SysRoleMapper;
import com.bulain.mybatis.sys.dao.SysRolePermissionMapper;
import com.bulain.mybatis.sys.entity.SysPermission;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.entity.SysRolePermission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
class SysRolePermissionMapperTest {

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @BeforeEach
    void setUp() {
        sysRolePermissionMapper.directDelete(new LambdaQueryWrapper<>());
        sysRoleMapper.directDelete(new LambdaQueryWrapper<>());
        sysPermissionMapper.directDelete(new LambdaQueryWrapper<>());
    }

    @Test
    void testInsertAndSelectByRoleId() {
        SysRole role = new SysRole();
        role.setCode("ROLE_PERM_TEST");
        role.setName("Permission Test Role");
        sysRoleMapper.insert(role);

        SysPermission permission = new SysPermission();
        permission.setCode("test:permission");
        permission.setName("Test Permission");
        sysPermissionMapper.insert(permission);

        SysRolePermission rolePermission = new SysRolePermission();
        rolePermission.setRoleId(role.getId());
        rolePermission.setPermissionId(permission.getId());
        int result = sysRolePermissionMapper.insert(rolePermission);
        assertEquals(1, result);

        LambdaQueryWrapper<SysRolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRolePermission::getRoleId, role.getId());
        List<SysRolePermission> rolePermissions = sysRolePermissionMapper.selectList(wrapper);

        assertFalse(rolePermissions.isEmpty());
        assertEquals(permission.getId(), rolePermissions.get(0).getPermissionId());
    }

    @Test
    void testDeleteByRoleId() {
        SysRole role = new SysRole();
        role.setCode("ROLE_DEL_PERM");
        role.setName("Delete Permission Role");
        sysRoleMapper.insert(role);

        SysPermission permission = new SysPermission();
        permission.setCode("delete:permission");
        permission.setName("Delete Permission");
        sysPermissionMapper.insert(permission);

        SysRolePermission rolePermission = new SysRolePermission();
        rolePermission.setRoleId(role.getId());
        rolePermission.setPermissionId(permission.getId());
        sysRolePermissionMapper.insert(rolePermission);

        LambdaQueryWrapper<SysRolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRolePermission::getRoleId, role.getId());
        int deleted = sysRolePermissionMapper.delete(wrapper);

        assertEquals(1, deleted);
    }
}
