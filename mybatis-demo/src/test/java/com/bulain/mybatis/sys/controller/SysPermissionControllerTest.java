package com.bulain.mybatis.sys.controller;

import com.bulain.mybatis.sys.dto.CreatePermissionDTO;
import com.bulain.mybatis.sys.entity.SysPermission;
import com.bulain.mybatis.sys.service.SysPermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SysPermissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SysPermissionService sysPermissionService;

    private Long testPermissionId;

    @BeforeEach
    void setUp() {
        CreatePermissionDTO dto = new CreatePermissionDTO();
        dto.setCode("controller:test");
        dto.setName("Controller Test Permission");
        dto.setResourceType("API");
        SysPermission permission = sysPermissionService.createPermission(dto);
        testPermissionId = permission.getId();
    }

    @Test
    void testCreatePermission() throws Exception {
        CreatePermissionDTO dto = new CreatePermissionDTO();
        dto.setCode("controller:new");
        dto.setName("New Permission");
        dto.setResourceType("API");

        mockMvc.perform(post("/api/sys/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.code").value("controller:new"));
    }

    @Test
    void testGetPermissionById() throws Exception {
        mockMvc.perform(get("/api/sys/permissions/{id}", testPermissionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(testPermissionId));
    }

    @Test
    void testListPermissions() throws Exception {
        mockMvc.perform(get("/api/sys/permissions")
                        .param("name", "Controller")
                        .param("current", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists());
    }
}
