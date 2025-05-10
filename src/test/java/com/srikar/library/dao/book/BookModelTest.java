package com.srikar.library.dao.book;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BookModelTest {

    @Test
    void testBookModelBuilder() {
        // Arrange
        String id = "BK123";
        String title = "Test Book";
        String author = "Test Author";
        int stock = 5;
        String url = "http://example.com/book";
        Date dueDate = new Date();

        // Act
        BookModel bookModel = BookModel.builder()
                .id(id)
                .title(title)
                .author(author)
                .stock(stock)
                .url(url)
                .dueDate(dueDate)
                .build();

        // Assert
        assertEquals(id, bookModel.getId());
        assertEquals(title, bookModel.getTitle());
        assertEquals(author, bookModel.getAuthor());
        assertEquals(stock, bookModel.getStock());
        assertEquals(url, bookModel.getUrl());
        assertEquals(dueDate, bookModel.getDueDate());
    }

    @Test
    void testBookModelNoArgsConstructor() {
        // Act
        BookModel bookModel = new BookModel();

        // Assert
        assertNull(bookModel.getId());
        assertNull(bookModel.getTitle());
        assertNull(bookModel.getAuthor());
        assertEquals(0, bookModel.getStock());
        assertNull(bookModel.getUrl());
        assertNull(bookModel.getDueDate());
    }

    @Test
    void testBookModelAllArgsConstructor() {
        // Arrange
        String id = "BK123";
        String title = "Test Book";
        String author = "Test Author";
        int stock = 5;
        String url = "http://example.com/book";
        Date dueDate = new Date();

        // Act
        BookModel bookModel = new BookModel(id, title, author, stock, url, dueDate);

        // Assert
        assertEquals(id, bookModel.getId());
        assertEquals(title, bookModel.getTitle());
        assertEquals(author, bookModel.getAuthor());
        assertEquals(stock, bookModel.getStock());
        assertEquals(url, bookModel.getUrl());
        assertEquals(dueDate, bookModel.getDueDate());
    }

    @Test
    void testBookModelSettersAndGetters() {
        // Arrange
        BookModel bookModel = new BookModel();
        String id = "BK123";
        String title = "Test Book";
        String author = "Test Author";
        int stock = 5;
        String url = "http://example.com/book";
        Date dueDate = new Date();

        // Act
        bookModel.setId(id);
        bookModel.setTitle(title);
        bookModel.setAuthor(author);
        bookModel.setStock(stock);
        bookModel.setUrl(url);
        bookModel.setDueDate(dueDate);

        // Assert
        assertEquals(id, bookModel.getId());
        assertEquals(title, bookModel.getTitle());
        assertEquals(author, bookModel.getAuthor());
        assertEquals(stock, bookModel.getStock());
        assertEquals(url, bookModel.getUrl());
        assertEquals(dueDate, bookModel.getDueDate());
    }

    @Test
    void testEquals() {
        // Arrange
        BookModel bookModel1 = BookModel.builder()
                .id("BK123")
                .title("Test Book")
                .author("Test Author")
                .stock(5)
                .url("http://example.com/book")
                .build();

        BookModel bookModel2 = BookModel.builder()
                .id("BK123")
                .title("Test Book")
                .author("Test Author")
                .stock(5)
                .url("http://example.com/book")
                .build();

        BookModel bookModel3 = BookModel.builder()
                .id("BK456")
                .title("Another Book")
                .author("Another Author")
                .stock(3)
                .url("http://example.com/another")
                .build();

        // Assert
        assertEquals(bookModel1, bookModel2);
        assertNotEquals(bookModel1, bookModel3);
        assertNotEquals(bookModel2, bookModel3);
    }

    @Test
    void testHashCode() {
        // Arrange
        BookModel bookModel1 = BookModel.builder()
                .id("BK123")
                .title("Test Book")
                .author("Test Author")
                .stock(5)
                .url("http://example.com/book")
                .build();

        BookModel bookModel2 = BookModel.builder()
                .id("BK123")
                .title("Test Book")
                .author("Test Author")
                .stock(5)
                .url("http://example.com/book")
                .build();

        // Assert
        assertEquals(bookModel1.hashCode(), bookModel2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        BookModel bookModel = BookModel.builder()
                .id("BK123")
                .title("Test Book")
                .author("Test Author")
                .stock(5)
                .url("http://example.com/book")
                .build();

        // Act
        String result = bookModel.toString();

        // Assert
        assertTrue(result.contains("BK123"));
        assertTrue(result.contains("Test Book"));
        assertTrue(result.contains("Test Author"));
        assertTrue(result.contains("5"));
        assertTrue(result.contains("http://example.com/book"));
    }
}
