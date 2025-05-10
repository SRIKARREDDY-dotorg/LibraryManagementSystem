package com.srikar.library.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookCreateRequestTest {

    private BookCreateRequest request;
    private String id;
    private String title;
    private String author;
    private int stock;
    private String url;

    @BeforeEach
    void setUp() {
        request = new BookCreateRequest();
        id = "BK123";
        title = "The Great Gatsby";
        author = "F. Scott Fitzgerald";
        stock = 5;
        url = "http://example.com/book.jpg";
    }

    @Test
    void testBookCreateRequestGettersAndSetters() {
        // Act
        request.setId(id);
        request.setTitle(title);
        request.setAuthor(author);
        request.setStock(stock);
        request.setUrl(url);

        // Assert
        assertEquals(id, request.getId());
        assertEquals(title, request.getTitle());
        assertEquals(author, request.getAuthor());
        assertEquals(stock, request.getStock());
        assertEquals(url, request.getUrl());
    }

    @Test
    void testEqualsWithDifferentValues() {
        // Arrange
        BookCreateRequest request1 = new BookCreateRequest();
        request1.setId("BK123");
        request1.setTitle("The Great Gatsby");
        request1.setAuthor("F. Scott Fitzgerald");
        request1.setStock(5);
        request1.setUrl("http://example.com/gatsby.jpg");

        BookCreateRequest request2 = new BookCreateRequest();
        request2.setId("BK456");
        request2.setTitle("To Kill a Mockingbird");
        request2.setAuthor("Harper Lee");
        request2.setStock(3);
        request2.setUrl("http://example.com/mockingbird.jpg");

        BookCreateRequest request3 = new BookCreateRequest();
        request3.setId("BK123");
        request3.setTitle("The Great Gatsby");
        request3.setAuthor("F. Scott Fitzgerald");
        request3.setStock(5);
        request3.setUrl("http://example.com/gatsby.jpg");

        // Assert
        assertNotEquals(request1, request2);
        assertEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request2.hashCode());
        assertEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testEqualsWithSameValues() {
        // Arrange
        BookCreateRequest request1 = new BookCreateRequest();
        request1.setId(id);
        request1.setTitle(title);
        request1.setAuthor(author);
        request1.setStock(stock);
        request1.setUrl(url);

        BookCreateRequest request2 = new BookCreateRequest();
        request2.setId(id);
        request2.setTitle(title);
        request2.setAuthor(author);
        request2.setStock(stock);
        request2.setUrl(url);

        // Assert
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        request.setId(id);
        request.setTitle(title);
        request.setAuthor(author);
        request.setStock(stock);
        request.setUrl(url);

        // Act
        String result = request.toString();

        // Assert
        assertTrue(result.contains(id));
        assertTrue(result.contains(title));
        assertTrue(result.contains(author));
        assertTrue(result.contains(String.valueOf(stock)));
        assertTrue(result.contains(url));
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        BookCreateRequest newRequest = new BookCreateRequest();

        // Assert
        assertNotNull(newRequest);
        assertEquals(0, newRequest.getStock()); // Default value for int
        assertNull(newRequest.getId());
        assertNull(newRequest.getTitle());
        assertNull(newRequest.getAuthor());
        assertNull(newRequest.getUrl());
    }
}
