package com.bulain.mybatis.sys.aspect;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bulain.mybatis.config.Profiles;
import com.bulain.mybatis.sys.dao.SysRoleMapper;
import com.bulain.mybatis.sys.dao.SysUserMapper;
import com.bulain.mybatis.sys.dto.CreateRoleDTO;
import com.bulain.mybatis.sys.dto.CreateUserDTO;
import com.bulain.mybatis.sys.entity.SysRole;
import com.bulain.mybatis.sys.entity.SysUser;
import com.bulain.mybatis.sys.service.SysJwtService;
import com.bulain.mybatis.sys.service.SysRoleService;
import com.bulain.mybatis.sys.service.SysUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
@AutoConfigureMockMvc
class PermissionCheckAspectTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SysJwtService sysJwtService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    private String validToken;
    private String testUserId;

    @BeforeEach
    void setUp() {
        sysUserMapper.directDelete(new LambdaQueryWrapper<>());
        sysRoleMapper.directDelete(new LambdaQueryWrapper<>());

        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("aspectuser");
        dto.setName("Aspect User");
        dto.setPassword("password123!");
        SysUser user = sysUserService.createUser(dto);
        testUserId = user.getId();
        validToken = sysJwtService.generateToken(testUserId, "aspectuser");
    }

    @Test
    void testJwtTokenValidation() {
        String token = sysJwtService.generateToken("100", "testuser");
        assertTrue(sysJwtService.validateToken(token));
        assertEquals("100", sysJwtService.getUserIdFromToken(token));
        assertEquals("testuser", sysJwtService.getUsernameFromToken(token));
    }

    @Test
    void testInvalidJwtToken() {
        assertFalse(sysJwtService.validateToken("invalid.token.here"));
    }

    @Test
    void testRequestWithoutAuthorizationHeader() throws Exception {
        // Without Authorization header, getCurrentUserId should return null
        // The aspect will throw exception or let it pass depending on annotation
        // This tests a public endpoint or endpoint without permission annotation
        mockMvc.perform(get("/api/sys/users")
                        .param("current", "1")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testRequestWithValidAuthorizationHeader() throws Exception {
        mockMvc.perform(get("/api/sys/users")
                        .header("Authorization", "Bearer " + validToken)
                        .param("current", "1")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testRequestWithInvalidAuthorizationHeader() throws Exception {
        // This will depend on how the aspect handles it
        // For endpoints without permission annotations, the request should proceed
        mockMvc.perform(get("/api/sys/users")
                        .header("Authorization", "Bearer invalid.token.here")
                        .param("current", "1")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testRoleCodeCheck() {
        CreateRoleDTO roleDTO = new CreateRoleDTO();
        roleDTO.setCode("TEST_ROLE");
        roleDTO.setName("Test Role");
        SysRole role = sysRoleService.createRole(roleDTO);

        assertEquals("TEST_ROLE", role.getCode());
    }
}
