package com.srikar.library.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserNotFoundExceptionTest {

    @Test
    void testUserNotFoundExceptionWithMessage() {
        // Arrange
        String message = "User not found";
        
        // Act
        UserNotFoundException exception = new UserNotFoundException(message);
        
        // Assert
        assertEquals(message, exception.getMessage());
    }
}
