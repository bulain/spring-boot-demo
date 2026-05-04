package com.bulain.mybatis.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.sys.dao.SysRoleMapper;
import com.bulain.mybatis.sys.dao.SysUserMapper;
import com.bulain.mybatis.sys.dao.SysUserRoleMapper;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.entity.SysUserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SysUserRoleMapperTest {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Test
    void testInsertAndSelectByUserId() {
        SysUser user = new SysUser();
        user.setUsername("roleuser");
        user.setName("Role User");
        sysUserMapper.insert(user);

        SysRole role = new SysRole();
        role.setCode("ROLE_USER_TEST");
        role.setName("User Test Role");
        sysRoleMapper.insert(role);

        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());
        int result = sysUserRoleMapper.insert(userRole);
        assertEquals(1, result);

        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, user.getId());
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(wrapper);

        assertFalse(userRoles.isEmpty());
        assertEquals(role.getId(), userRoles.get(0).getRoleId());
    }

    @Test
    void testDeleteByUserId() {
        SysUser user = new SysUser();
        user.setUsername("deleteroleuser");
        user.setName("Delete Role User");
        sysUserMapper.insert(user);

        SysRole role = new SysRole();
        role.setCode("ROLE_DELETE");
        role.setName("Delete Test Role");
        sysRoleMapper.insert(role);

        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());
        sysUserRoleMapper.insert(userRole);

        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, user.getId());
        int deleted = sysUserRoleMapper.delete(wrapper);

        assertEquals(1, deleted);
    }
}
