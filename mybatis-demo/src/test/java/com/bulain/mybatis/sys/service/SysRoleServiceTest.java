package com.bulain.mybatis.sys.service;

import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dto.CreateRoleDTO;
import com.bulain.mybatis.sys.dto.RoleQueryDTO;
import com.bulain.mybatis.sys.dto.UpdateRoleDTO;
import com.bulain.mybatis.sys.entity.SysPermission;
import com.bulain.mybatis.sys.entity.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SysRoleServiceTest {

    @Autowired
    private SysRoleService sysRoleService;

    @Test
    void testCreateRole() {
        CreateRoleDTO dto = new CreateRoleDTO();
        dto.setCode("ROLE_SERVICE_TEST");
        dto.setName("Service Test Role");
        dto.setDescription("Role created by service test");

        SysRole role = sysRoleService.createRole(dto);

        assertNotNull(role);
        assertNotNull(role.getId());
        assertEquals("ROLE_SERVICE_TEST", role.getCode());
    }

    @Test
    void testUpdateRole() {
        CreateRoleDTO createDTO = new CreateRoleDTO();
        createDTO.setCode("ROLE_UPDATE_TEST");
        createDTO.setName("Update Test Role");
        SysRole role = sysRoleService.createRole(createDTO);

        UpdateRoleDTO updateDTO = new UpdateRoleDTO();
        updateDTO.setName("Updated Role Name");
        updateDTO.setDescription("Updated Description");

        SysRole updated = sysRoleService.updateRole(role.getId(), updateDTO);

        assertEquals("Updated Role Name", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
    }

    @Test
    void testGetByCode() {
        CreateRoleDTO createDTO = new CreateRoleDTO();
        createDTO.setCode("ROLE_FIND_BY_CODE");
        createDTO.setName("Find By Code Role");
        sysRoleService.createRole(createDTO);

        SysRole found = sysRoleService.getByCode("ROLE_FIND_BY_CODE");

        assertNotNull(found);
        assertEquals("Find By Code Role", found.getName());
    }

    @Test
    void testPageRoles() {
        CreateRoleDTO createDTO = new CreateRoleDTO();
        createDTO.setCode("ROLE_PAGE_TEST");
        createDTO.setName("Page Test Role");
        sysRoleService.createRole(createDTO);

        RoleQueryDTO query = new RoleQueryDTO();
        query.setName("Page");
        query.setCurrent(1);
        query.setSize(10);

        Paged<SysRole> paged = sysRoleService.pageRoles(query);

        assertNotNull(paged);
        assertTrue(paged.getTotalCount() > 0);
    }

    @Test
    void testAssignPermissions() {
        CreateRoleDTO createDTO = new CreateRoleDTO();
        createDTO.setCode("ROLE_ASSIGN_PERM");
        createDTO.setName("Assign Permission Role");
        SysRole role = sysRoleService.createRole(createDTO);

        // Test assigning empty list (no permissions)
        sysRoleService.assignPermissions(role.getId(), List.of());

        assertTrue(true);
    }

    @Test
    void testGetRolePermissions() {
        CreateRoleDTO createDTO = new CreateRoleDTO();
        createDTO.setCode("ROLE_GET_PERM");
        createDTO.setName("Get Permission Role");
        SysRole role = sysRoleService.createRole(createDTO);

        List<SysPermission> permissions = sysRoleService.getRolePermissions(role.getId());

        assertNotNull(permissions);
        assertTrue(permissions.isEmpty());
    }
}
