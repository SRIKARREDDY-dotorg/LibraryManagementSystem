package com.srikar.library.dao.user;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    @Test
    void testUserModelNoArgsConstructor() {
        // Act
        UserModel userModel = new UserModel();

        // Assert
        assertNull(userModel.getEmail());
        assertNull(userModel.getPassword());
        assertNull(userModel.getBorrowedBooks());
    }

    @Test
    void testUserModelAllArgsConstructor() {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        List<String> borrowedBooks = Arrays.asList("BK123", "BK456");

        // Act
        UserModel userModel = new UserModel(email, password, borrowedBooks);

        // Assert
        assertEquals(email, userModel.getEmail());
        assertEquals(password, userModel.getPassword());
        assertEquals(borrowedBooks, userModel.getBorrowedBooks());
    }

    @Test
    void testUserModelSettersAndGetters() {
        // Arrange
        UserModel userModel = new UserModel();
        String email = "test@example.com";
        String password = "password123";
        List<String> borrowedBooks = Arrays.asList("BK123", "BK456");

        // Act
        userModel.setEmail(email);
        userModel.setPassword(password);
        userModel.setBorrowedBooks(borrowedBooks);

        // Assert
        assertEquals(email, userModel.getEmail());
        assertEquals(password, userModel.getPassword());
        assertEquals(borrowedBooks, userModel.getBorrowedBooks());
    }

    @Test
    void testEquals() {
        // Arrange
        UserModel userModel1 = new UserModel("test@example.com", "password123", Arrays.asList("BK123", "BK456"));
        UserModel userModel2 = new UserModel("test@example.com", "password123", Arrays.asList("BK123", "BK456"));
        UserModel userModel3 = new UserModel("other@example.com", "password456", Arrays.asList("BK789"));

        // Assert
        assertEquals(userModel1, userModel2);
        assertNotEquals(userModel1, userModel3);
        assertNotEquals(userModel2, userModel3);
    }

    @Test
    void testHashCode() {
        // Arrange
        UserModel userModel1 = new UserModel("test@example.com", "password123", Arrays.asList("BK123", "BK456"));
        UserModel userModel2 = new UserModel("test@example.com", "password123", Arrays.asList("BK123", "BK456"));

        // Assert
        assertEquals(userModel1.hashCode(), userModel2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        UserModel userModel = new UserModel("test@example.com", "password123", Arrays.asList("BK123", "BK456"));

        // Act
        String result = userModel.toString();

        // Assert
        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("password123"));
        assertTrue(result.contains("BK123"));
        assertTrue(result.contains("BK456"));
    }
}
