package com.srikar.library.activity.controller;

import com.srikar.library.activity.service.UserService;
import com.srikar.library.dto.Book;
import com.srikar.library.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;
    
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController = new UserController() {};

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testViewBooks() {
        // Arrange
        List<Book> books = Arrays.asList(
                new Book("BK123", "Book 1", "Author 1", 5, "url1"),
                new Book("BK456", "Book 2", "Author 2", 3, "url2")
        );
        when(userService.viewBooks()).thenReturn(books);

        // Act
        ResponseEntity<?> response = userController.viewBooks();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
        verify(userService, times(1)).viewBooks();
    }

    @Test
    void testBorrowBookSuccessfully() {
        // Arrange
        String userId = "user123";
        String bookId = "BK123";
        when(jwtUtil.getAuthenticatedEmail()).thenReturn(userId);
        when(userService.borrowBook(userId, bookId)).thenReturn(true);

        // Act
        UserController.BorrowRequest request = new UserController.BorrowRequest();
        request.bookId = bookId;
        ResponseEntity<?> response = userController.borrowBook(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book borrowed successfully", response.getBody());
        verify(userService, times(1)).borrowBook(userId, bookId);
    }

    @Test
    void testBorrowBookWithException() {
        // Arrange
        String userId = "user123";
        String bookId = "BK123";
        String errorMessage = "Book not available";
        when(jwtUtil.getAuthenticatedEmail()).thenReturn(userId);
        when(userService.borrowBook(userId, bookId)).thenThrow(new IllegalStateException(errorMessage));

        // Act
        UserController.BorrowRequest request = new UserController.BorrowRequest();
        request.bookId = bookId;
        ResponseEntity<?> response = userController.borrowBook(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, times(1)).borrowBook(userId, bookId);
    }

    @Test
    void testReturnBookSuccessfully() {
        // Arrange
        String userId = "user123";
        String bookId = "BK123";
        when(jwtUtil.getAuthenticatedEmail()).thenReturn(userId);
        when(userService.returnBooks(userId, bookId)).thenReturn(true);

        // Act
        UserController.BorrowRequest request = new UserController.BorrowRequest();
        request.bookId = bookId;
        ResponseEntity<?> response = userController.returnBooks(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Books returned successfully", response.getBody());
        verify(userService, times(1)).returnBooks(userId, bookId);
    }

    @Test
    void testReturnBookWithException() {
        // Arrange
        String userId = "user123";
        String bookId = "BK123";
        String errorMessage = "Book not borrowed by this user";
        when(jwtUtil.getAuthenticatedEmail()).thenReturn(userId);
        when(userService.returnBooks(userId, bookId)).thenThrow(new IllegalStateException(errorMessage));

        // Act
        UserController.BorrowRequest request = new UserController.BorrowRequest();
        request.bookId = bookId;
        ResponseEntity<?> response = userController.returnBooks(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, times(1)).returnBooks(userId, bookId);
    }
}
