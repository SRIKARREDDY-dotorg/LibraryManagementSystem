package com.srikar.library.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorUtilTest {

    @Test
    void testGenerateBookId() {
        // Act
        String bookId = IdGeneratorUtil.generateBookId();

        // Assert
        assertNotNull(bookId);
        assertTrue(bookId.startsWith("BK"));
        assertEquals(10, bookId.length());
    }

    @Test
    void testGenerateUserIdFormat() {
        // Act
        String userId = IdGeneratorUtil.generateUserId();

        // Assert
        assertNotNull(userId);
        assertTrue(userId.startsWith("USR"));
        assertEquals(11, userId.length());
    }

    @Test
    void testGenerateMultipleBookIdsAreUnique() {
        // Act
        String bookId1 = IdGeneratorUtil.generateBookId();
        String bookId2 = IdGeneratorUtil.generateBookId();
        String bookId3 = IdGeneratorUtil.generateBookId();

        // Assert
        assertNotEquals(bookId1, bookId2);
        assertNotEquals(bookId1, bookId3);
        assertNotEquals(bookId2, bookId3);
    }

    @Test
    void testGenerateMultipleUserIdsAreUnique() {
        // Act
        String userId1 = IdGeneratorUtil.generateUserId();
        String userId2 = IdGeneratorUtil.generateUserId();
        String userId3 = IdGeneratorUtil.generateUserId();

        // Assert
        assertNotEquals(userId1, userId2);
        assertNotEquals(userId1, userId3);
        assertNotEquals(userId2, userId3);
    }
}
