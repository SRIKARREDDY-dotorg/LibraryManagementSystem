package com.srikar.library.dto;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testBookConstructorAndGetters() {
        // Arrange
        String id = "BK123";
        String title = "Test Book";
        String author = "Test Author";
        int stock = 5;
        String url = "http://example.com/book";

        // Act
        Book book = new Book(id, title, author, stock, url);

        // Assert
        assertEquals(id, book.getId());
        assertEquals(title, book.getTitle());
        assertEquals(author, book.getAuthor());
        assertEquals(stock, book.getStock());
        assertEquals(url, book.getUrl());
    }

    @Test
    void testSetAndGetStock() {
        // Arrange
        Book book = new Book("BK123", "Test Book", "Test Author", 5, "http://example.com/book");
        int newStock = 10;

        // Act
        book.setStock(newStock);

        // Assert
        assertEquals(newStock, book.getStock());
    }

    @Test
    void testSetAndGetBorrowerId() {
        // Arrange
        Book book = new Book("BK123", "Test Book", "Test Author", 5, "http://example.com/book");
        String borrowerId = "user123";

        // Act
        book.setBorrowerId(borrowerId);

        // Assert
        assertEquals(borrowerId, book.getBorrowerId());
    }

    @Test
    void testSetAndGetDueDate() {
        // Arrange
        Book book = new Book("BK123", "Test Book", "Test Author", 5, "http://example.com/book");
        Date dueDate = new Date();

        // Act
        book.setDueDate(dueDate);

        // Assert
        assertEquals(dueDate, book.getDueDate());
    }

    @Test
    void testToString() {
        // Arrange
        String id = "BK123";
        String title = "Test Book";
        String author = "Test Author";
        int stock = 5;
        String url = "http://example.com/book";
        Book book = new Book(id, title, author, stock, url);

        // Act
        String result = book.toString();

        // Assert
        assertTrue(result.contains(id));
        assertTrue(result.contains(title));
        assertTrue(result.contains(author));
        assertTrue(result.contains(String.valueOf(stock)));
    }
}
