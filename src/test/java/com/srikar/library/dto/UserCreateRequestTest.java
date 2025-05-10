package com.srikar.library.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserCreateRequestTest {

    @Test
    void testUserCreateRequestGettersAndSetters() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest();
        String name = "John Doe";
        String email = "john.doe@example.com";

        // Act
        request.setName(name);
        request.setEmail(email);

        // Assert
        assertEquals(name, request.getName());
        assertEquals(email, request.getEmail());
    }

    @Test
    void testEqualsWithDifferentValues() {
        // Arrange
        UserCreateRequest request1 = new UserCreateRequest();
        request1.setName("John Doe");
        request1.setEmail("john.doe@example.com");

        UserCreateRequest request2 = new UserCreateRequest();
        request2.setName("Jane Doe");
        request2.setEmail("jane.doe@example.com");

        UserCreateRequest request3 = new UserCreateRequest();
        request3.setName("John Doe");
        request3.setEmail("john.doe@example.com");

        // Assert
        assertNotEquals(request1, request2);
        assertEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testEqualsWithSameValues() {
        // Arrange
        UserCreateRequest request1 = new UserCreateRequest();
        request1.setName("John Doe");
        request1.setEmail("john.doe@example.com");

        UserCreateRequest request2 = new UserCreateRequest();
        request2.setName("John Doe");
        request2.setEmail("john.doe@example.com");

        // Assert
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        UserCreateRequest request = new UserCreateRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");

        // Act
        String result = request.toString();

        // Assert
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("john.doe@example.com"));
    }
}
