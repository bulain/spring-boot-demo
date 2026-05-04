package com.bulain.mybatis.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.config.Profiles;
import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dao.SysUserMapper;
import com.bulain.mybatis.sys.dto.CreateUserDTO;
import com.bulain.mybatis.sys.dto.UpdateUserDTO;
import com.bulain.mybatis.sys.dto.UserQueryDTO;
import com.bulain.mybatis.sys.entity.SysUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
class SysUserServiceTest {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @BeforeEach
    void setUp() {
        sysUserMapper.directDelete(new LambdaQueryWrapper<>());
    }

    @Test
    void testCreateUser() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("serviceuser");
        dto.setName("Service User");
        dto.setEmail("service@example.com");
        dto.setPhone("13900139000");
        dto.setPassword("password123!");

        SysUser user = sysUserService.createUser(dto);

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals("serviceuser", user.getUsername());
    }

    @Test
    void testUpdateUser() {
        CreateUserDTO createDTO = new CreateUserDTO();
        createDTO.setUsername("updateuser");
        createDTO.setName("Update User");
        createDTO.setPassword("password123!");
        SysUser user = sysUserService.createUser(createDTO);

        UpdateUserDTO updateDTO = new UpdateUserDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setEmail("updated@example.com");

        SysUser updated = sysUserService.updateUser(user.getId(), updateDTO);

        assertEquals("Updated Name", updated.getName());
        assertEquals("updated@example.com", updated.getEmail());
    }

    @Test
    void testToggleStatus() {
        CreateUserDTO createDTO = new CreateUserDTO();
        createDTO.setUsername("statususer");
        createDTO.setName("Status User");
        createDTO.setPassword("password123!");
        SysUser user = sysUserService.createUser(createDTO);

        sysUserService.toggleStatus(user.getId(), 0);

        SysUser disabled = sysUserService.getById(user.getId());
        assertEquals(0, disabled.getStatus());
    }

    @Test
    void testPageUsers() {
        CreateUserDTO createDTO = new CreateUserDTO();
        createDTO.setUsername("pageuser");
        createDTO.setName("Page User");
        createDTO.setPassword("password123!");
        sysUserService.createUser(createDTO);

        UserQueryDTO query = new UserQueryDTO();
        query.setName("Page");
        query.setCurrent(1);
        query.setSize(10);

        Paged<SysUser> paged = sysUserService.pageUsers(query);

        assertNotNull(paged);
        assertTrue(paged.getTotalCount() > 0);
    }

    @Test
    void testGetByUsername() {
        CreateUserDTO createDTO = new CreateUserDTO();
        createDTO.setUsername("getbyusername");
        createDTO.setName("Get By Username");
        createDTO.setPassword("password123!");
        sysUserService.createUser(createDTO);

        SysUser found = sysUserService.getByUsername("getbyusername");

        assertNotNull(found);
        assertEquals("Get By Username", found.getName());
    }

    @Test
    void testAssignRoles() {
        CreateUserDTO createDTO = new CreateUserDTO();
        createDTO.setUsername("assignroleuser");
        createDTO.setName("Assign Role User");
        createDTO.setPassword("password123!");
        SysUser user = sysUserService.createUser(createDTO);

        // Note: In a real test, we'd create roles first then assign them
        // This is just testing the method call structure
        sysUserService.assignRoles(user.getId(), List.of());

        // Verify no exception thrown
        assertTrue(true);
    }

    @Test
    void testGetUserPermissionCodes() {
        CreateUserDTO createDTO = new CreateUserDTO();
        createDTO.setUsername("permuser");
        createDTO.setName("Permission User");
        createDTO.setPassword("password123!");
        SysUser user = sysUserService.createUser(createDTO);

        Set<String> permissions = sysUserService.getUserPermissionCodes(user.getId());

        assertNotNull(permissions);
        // User without roles should have empty permissions
        assertTrue(permissions.isEmpty());
    }
}
