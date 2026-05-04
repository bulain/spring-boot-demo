package com.bulain.mybatis.sys.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.sys.dao.SysRoleMapper;
import com.bulain.mybatis.sys.entity.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SysRoleMapperTest {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Test
    void testInsertAndSelectById() {
        SysRole role = new SysRole();
        role.setCode("ROLE_TEST");
        role.setName("Test Role");
        role.setDescription("Test Role Description");

        int result = sysRoleMapper.insert(role);
        assertEquals(1, result);
        assertNotNull(role.getId());

        SysRole selected = sysRoleMapper.selectById(role.getId());
        assertNotNull(selected);
        assertEquals("ROLE_TEST", selected.getCode());
    }

    @Test
    void testUpdateById() {
        SysRole role = new SysRole();
        role.setCode("ROLE_UPDATE");
        role.setName("Update Role");
        sysRoleMapper.insert(role);

        role.setName("Updated Role Name");
        int result = sysRoleMapper.updateById(role);
        assertEquals(1, result);

        SysRole updated = sysRoleMapper.selectById(role.getId());
        assertEquals("Updated Role Name", updated.getName());
    }

    @Test
    void testDeleteById() {
        SysRole role = new SysRole();
        role.setCode("ROLE_DELETE");
        role.setName("Delete Role");
        sysRoleMapper.insert(role);

        int result = sysRoleMapper.deleteById(role.getId());
        assertEquals(1, result);

        SysRole deleted = sysRoleMapper.selectById(role.getId());
        assertNull(deleted);
    }

    @Test
    void testSelectByCode() {
        SysRole role = new SysRole();
        role.setCode("ROLE_FIND");
        role.setName("Find By Code");
        sysRoleMapper.insert(role);

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getCode, "ROLE_FIND");
        SysRole found = sysRoleMapper.selectOne(wrapper);

        assertNotNull(found);
        assertEquals("Find By Code", found.getName());
    }
}
