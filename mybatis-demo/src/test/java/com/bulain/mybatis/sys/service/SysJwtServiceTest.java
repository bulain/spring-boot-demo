package com.bulain.mybatis.sys.service;

import com.bulain.mybatis.config.Profiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(Profiles.TEST)
@SpringBootTest
class SysJwtServiceTest {

    @Autowired
    private SysJwtService sysJwtService;

    @Test
    void testGenerateToken() {
        String token = sysJwtService.generateToken("1", "testuser");

        assertNotNull(token);
        assertFalse(token.isEmpty());
        // JWT tokens have 3 parts separated by dots
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void testGetUserIdFromToken() {
        String token = sysJwtService.generateToken("123", "testuser");

        String userId = sysJwtService.getUserIdFromToken(token);

        assertEquals("123", userId);
    }

    @Test
    void testGetUsernameFromToken() {
        String token = sysJwtService.generateToken("1", "jwtuser");

        String username = sysJwtService.getUsernameFromToken(token);

        assertEquals("jwtuser", username);
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = sysJwtService.generateToken("1", "validuser");

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
        String token = sysJwtService.generateToken("1", "notexpired");

        boolean isExpired = sysJwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }
}
