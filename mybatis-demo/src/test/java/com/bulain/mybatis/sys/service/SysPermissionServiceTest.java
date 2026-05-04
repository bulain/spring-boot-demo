package com.bulain.mybatis.sys.service;

import com.bulain.mybatis.core.pojo.Paged;
import com.bulain.mybatis.sys.dto.CreatePermissionDTO;
import com.bulain.mybatis.sys.dto.PermissionQueryDTO;
import com.bulain.mybatis.sys.dto.UpdatePermissionDTO;
import com.bulain.mybatis.sys.entity.SysPermission;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SysPermissionServiceTest {

    @Autowired
    private SysPermissionService sysPermissionService;

    @Test
    void testCreatePermission() {
        CreatePermissionDTO dto = new CreatePermissionDTO();
        dto.setCode("service:create");
        dto.setName("Service Create Permission");
        dto.setResourceType("API");
        dto.setDescription("Created by service test");

        SysPermission permission = sysPermissionService.createPermission(dto);

        assertNotNull(permission);
        assertNotNull(permission.getId());
        assertEquals("service:create", permission.getCode());
    }

    @Test
    void testUpdatePermission() {
        CreatePermissionDTO createDTO = new CreatePermissionDTO();
        createDTO.setCode("service:update");
        createDTO.setName("Update Permission");
        SysPermission permission = sysPermissionService.createPermission(createDTO);

        UpdatePermissionDTO updateDTO = new UpdatePermissionDTO();
        updateDTO.setName("Updated Permission Name");
        updateDTO.setResourceType("MENU");
        updateDTO.setDescription("Updated Description");

        SysPermission updated = sysPermissionService.updatePermission(permission.getId(), updateDTO);

        assertEquals("Updated Permission Name", updated.getName());
        assertEquals("MENU", updated.getResourceType());
        assertEquals("Updated Description", updated.getDescription());
    }

    @Test
    void testGetByCode() {
        CreatePermissionDTO createDTO = new CreatePermissionDTO();
        createDTO.setCode("service:find");
        createDTO.setName("Find By Code Permission");
        sysPermissionService.createPermission(createDTO);

        SysPermission found = sysPermissionService.getByCode("service:find");

        assertNotNull(found);
        assertEquals("Find By Code Permission", found.getName());
    }

    @Test
    void testPagePermissions() {
        CreatePermissionDTO createDTO = new CreatePermissionDTO();
        createDTO.setCode("service:page");
        createDTO.setName("Page Permission");
        sysPermissionService.createPermission(createDTO);

        PermissionQueryDTO query = new PermissionQueryDTO();
        query.setName("Page");
        query.setCurrent(1);
        query.setSize(10);

        Paged<SysPermission> paged = sysPermissionService.pagePermissions(query);

        assertNotNull(paged);
        assertTrue(paged.getTotalCount() > 0);
    }
}
