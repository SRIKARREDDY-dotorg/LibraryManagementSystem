package com.srikar.library.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateAndValidateToken() {
        // Arrange
        String userId = "test@example.com";
        String role = "USER";

        // Act
        String token = jwtUtil.generateToken(userId, role);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertEquals(userId, jwtUtil.extractUserId(token));
        assertEquals(role, jwtUtil.extractRole(token));
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void testInvalidToken() {
        // Act & Assert
        assertFalse(jwtUtil.validateToken("invalid.token.string"));
    }

    @Test
    void testExtractRole() {
        // Arrange
        String userId = "test@example.com";
        String role = "ADMIN";
        String token = jwtUtil.generateToken(userId, role);

        // Act
        String extractedRole = jwtUtil.extractRole(token);

        // Assert
        assertEquals(role, extractedRole);
    }
}
