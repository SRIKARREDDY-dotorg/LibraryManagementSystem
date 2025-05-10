package com.srikar.library.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthRequestTest {

    @Test
    void testAuthRequestSettersAndGetters() {
        // Arrange
        AuthRequest request = new AuthRequest();
        String email = "test@example.com";
        String password = "password123";

        // Act
        request.setEmail(email);
        request.setPassword(password);

        // Assert
        assertEquals(email, request.getEmail());
        assertEquals(password, request.getPassword());
    }

    @Test
    void testEquals() {
        // Arrange
        AuthRequest request1 = new AuthRequest();
        request1.setEmail("test@example.com");
        request1.setPassword("password123");

        AuthRequest request2 = new AuthRequest();
        request2.setEmail("test@example.com");
        request2.setPassword("password123");

        AuthRequest request3 = new AuthRequest();
        request3.setEmail("other@example.com");
        request3.setPassword("password456");

        // Assert
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request2, request3);
    }

    @Test
    void testHashCode() {
        // Arrange
        AuthRequest request1 = new AuthRequest();
        request1.setEmail("test@example.com");
        request1.setPassword("password123");

        AuthRequest request2 = new AuthRequest();
        request2.setEmail("test@example.com");
        request2.setPassword("password123");

        // Assert
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        AuthRequest request = new AuthRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        // Act
        String result = request.toString();

        // Assert
        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("password123"));
    }
}
