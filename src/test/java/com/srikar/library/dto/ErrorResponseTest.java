package com.srikar.library.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testErrorResponseConstructorAndGetters() {
        // Arrange
        String message = "Error message";

        // Act
        ErrorResponse response = new ErrorResponse(message);

        // Assert
        assertEquals(message, response.getMessage());
        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp() instanceof LocalDateTime);
    }

    @Test
    void testErrorResponseTimestamp() {
        // Arrange
        String message = "Error message";
        LocalDateTime before = LocalDateTime.now();

        // Act
        ErrorResponse response = new ErrorResponse(message);
        LocalDateTime after = LocalDateTime.now();

        // Assert
        assertNotNull(response.getTimestamp());
        // The timestamp should be between before and after
        assertTrue(!response.getTimestamp().isBefore(before) && !response.getTimestamp().isAfter(after));
    }
}
