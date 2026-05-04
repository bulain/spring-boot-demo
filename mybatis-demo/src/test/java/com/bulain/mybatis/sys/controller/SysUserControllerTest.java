package com.bulain.mybatis.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.config.Profiles;
import com.bulain.mybatis.sys.dao.SysUserMapper;
import com.bulain.mybatis.sys.dto.CreateUserDTO;
import com.bulain.mybatis.sys.dto.UserRoleAssignDTO;
import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.service.SysUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
@AutoConfigureMockMvc
class SysUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserMapper sysUserMapper;

    private String testUserId;

    @BeforeEach
    void setUp() {
        sysUserMapper.directDelete(new LambdaQueryWrapper<>());

        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("controlleruser");
        dto.setName("Controller User");
        dto.setEmail("controller@example.com");
        dto.setPassword("password123");
        SysUser user = sysUserService.createUser(dto);
        testUserId = user.getId();
    }

    @Test
    void testCreateUser() throws Exception {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("newuser");
        dto.setName("New User");
        dto.setEmail("new@example.com");
        dto.setPassword("password123");

        mockMvc.perform(post("/api/sys/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("newuser"));
    }

    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/sys/users/{id}", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(testUserId));
    }

    @Test
    void testListUsers() throws Exception {
        mockMvc.perform(get("/api/sys/users")
                        .param("name", "Controller")
                        .param("current", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testToggleStatus() throws Exception {
        mockMvc.perform(put("/api/sys/users/{id}/status", testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testAssignRoles() throws Exception {
        UserRoleAssignDTO dto = new UserRoleAssignDTO();
        dto.setRoleIds(List.of());

        mockMvc.perform(put("/api/sys/users/{id}/roles", testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testGetUserPermissions() throws Exception {
        mockMvc.perform(get("/api/sys/users/{id}/permissions", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
}
