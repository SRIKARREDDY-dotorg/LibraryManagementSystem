package com.srikar.library.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void testAuthResponseConstructorAndGetters() {
        // Arrange
        String token = "jwt-token";
        String role = "USER";

        // Act
        AuthResponse response = new AuthResponse(token, role);

        // Assert
        assertEquals(token, response.getToken());
        assertEquals(role, response.getRole());
    }
}
