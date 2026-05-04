package com.bulain.mybatis.sys.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SysJwtServiceTest {

    @Autowired
    private SysJwtService sysJwtService;

    @Test
    void testGenerateToken() {
        String token = sysJwtService.generateToken(1L, "testuser");

        assertNotNull(token);
        assertFalse(token.isEmpty());
        // JWT tokens have 3 parts separated by dots
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void testGetUserIdFromToken() {
        String token = sysJwtService.generateToken(123L, "testuser");

        Long userId = sysJwtService.getUserIdFromToken(token);

        assertEquals(123L, userId);
    }

    @Test
    void testGetUsernameFromToken() {
        String token = sysJwtService.generateToken(1L, "jwtuser");

        String username = sysJwtService.getUsernameFromToken(token);

        assertEquals("jwtuser", username);
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = sysJwtService.generateToken(1L, "validuser");

        boolean isValid = sysJwtService.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidToken() {
        boolean isValid = sysJwtService.validateToken("invalid.token.here");

        assertFalse(isValid);
    }

    @Test
    void testIsTokenExpired_NotExpired() {
        String token = sysJwtService.generateToken(1L, "notexpired");

        boolean isExpired = sysJwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }
}
