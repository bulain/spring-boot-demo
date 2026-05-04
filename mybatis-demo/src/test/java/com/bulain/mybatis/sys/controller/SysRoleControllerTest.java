package com.bulain.mybatis.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.config.Profiles;
import com.bulain.mybatis.sys.dao.SysRoleMapper;
import com.bulain.mybatis.sys.dto.CreateRoleDTO;
import com.bulain.mybatis.sys.dto.RolePermissionAssignDTO;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.service.SysRoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
@AutoConfigureMockMvc
class SysRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    private String testRoleId;

    @BeforeEach
    void setUp() {
        sysRoleMapper.directDelete(new LambdaQueryWrapper<>());

        CreateRoleDTO dto = new CreateRoleDTO();
        dto.setCode("ROLE_CTRL_TEST");
        dto.setName("Controller Test Role");
        SysRole role = sysRoleService.createRole(dto);
        testRoleId = role.getId();
    }

    @Test
    void testCreateRole() throws Exception {
        CreateRoleDTO dto = new CreateRoleDTO();
        dto.setCode("ROLE_NEW");
        dto.setName("New Role");
        dto.setDescription("New Role Description");

        mockMvc.perform(post("/api/sys/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.code").value("ROLE_NEW"));
    }

    @Test
    void testGetRoleById() throws Exception {
        mockMvc.perform(get("/api/sys/roles/{id}", testRoleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(testRoleId));
    }

    @Test
    void testListRoles() throws Exception {
        mockMvc.perform(get("/api/sys/roles")
                        .param("name", "Controller")
                        .param("current", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testAssignPermissions() throws Exception {
        RolePermissionAssignDTO dto = new RolePermissionAssignDTO();
        dto.setPermissionIds(List.of());

        mockMvc.perform(put("/api/sys/roles/{id}/permissions", testRoleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testGetRolePermissions() throws Exception {
        mockMvc.perform(get("/api/sys/roles/{id}/permissions", testRoleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
}
