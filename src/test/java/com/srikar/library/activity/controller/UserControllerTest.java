package com.srikar.library.activity.controller;

import com.srikar.library.activity.service.UserService;
import com.srikar.library.dto.Book;
import com.srikar.library.dto.ErrorResponse;
import com.srikar.library.dto.PageResponse;
import com.srikar.library.exception.UserNotFoundException;
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
    void testViewBooksSuccess() {
        // Arrange
        List<Book> books = Arrays.asList(
                new Book("BK123", "Book 1", "Author 1", 5, "url1"),
                new Book("BK456", "Book 2", "Author 2", 3, "url2")
        );
        PageResponse<Book> pageResponse = new PageResponse<>(books, 0, 1, 2);
        when(userService.viewBooks(0, 12)).thenReturn(pageResponse);

        // Act
        ResponseEntity<?> response = userController.viewBooks(0, 12);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageResponse, response.getBody());
        verify(userService, times(1)).viewBooks(0, 12);
    }

    @Test
    void testViewBooksUserNotFound() {
        // Arrange
        when(userService.viewBooks(0, 12)).thenThrow(new UserNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = userController.viewBooks(0, 12);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("User not found", error.getMessage());
    }

    @Test
    void testViewBooksInvalidInput() {
        // Arrange
        when(userService.viewBooks(0, 12)).thenThrow(new IllegalArgumentException("Invalid page size"));

        // Act
        ResponseEntity<?> response = userController.viewBooks(0, 12);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Invalid page size", error.getMessage());
    }

    @Test
    void testViewBooksUnexpectedError() {
        // Arrange
        when(userService.viewBooks(0, 12)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = userController.viewBooks(0, 12);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("An unexpected error occurred", error.getMessage());
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
    void testBorrowBookWhenServiceReturnsFalse() {
        // Arrange
        String userId = "user123";
        String bookId = "BK123";
        when(jwtUtil.getAuthenticatedEmail()).thenReturn(userId);
        when(userService.borrowBook(userId, bookId)).thenReturn(false);

        // Act
        UserController.BorrowRequest request = new UserController.BorrowRequest();
        request.bookId = bookId;
        ResponseEntity<?> response = userController.borrowBook(request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Unable to borrow book. due to unknown reason", error.getMessage());
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
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals(errorMessage, error.getMessage());
        verify(userService, times(1)).borrowBook(userId, bookId);
    }

    @Test
    void testBorrowBookWithUserNotFoundException() {
        // Arrange
        String userId = "user123";
        String bookId = "BK123";
        when(jwtUtil.getAuthenticatedEmail()).thenReturn(userId);
        when(userService.borrowBook(userId, bookId)).thenThrow(new UserNotFoundException("User not found"));

        // Act
        UserController.BorrowRequest request = new UserController.BorrowRequest();
        request.bookId = bookId;
        ResponseEntity<?> response = userController.borrowBook(request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("User not found", error.getMessage());
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
    void testReturnBookWhenServiceReturnsFalse() {
        // Arrange
        String userId = "user123";
        String bookId = "BK123";
        when(jwtUtil.getAuthenticatedEmail()).thenReturn(userId);
        when(userService.returnBooks(userId, bookId)).thenReturn(false);

        // Act
        UserController.BorrowRequest request = new UserController.BorrowRequest();
        request.bookId = bookId;
        ResponseEntity<?> response = userController.returnBooks(request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Unable to return books. due to unknown reason", error.getMessage());
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
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals(errorMessage, error.getMessage());
        verify(userService, times(1)).returnBooks(userId, bookId);
    }

    @Test
    void testReturnBookWithUserNotFoundException() {
        // Arrange
        String userId = "user123";
        String bookId = "BK123";
        when(jwtUtil.getAuthenticatedEmail()).thenReturn(userId);
        when(userService.returnBooks(userId, bookId)).thenThrow(new UserNotFoundException("User not found"));

        // Act
        UserController.BorrowRequest request = new UserController.BorrowRequest();
        request.bookId = bookId;
        ResponseEntity<?> response = userController.returnBooks(request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("User not found", error.getMessage());
        verify(userService, times(1)).returnBooks(userId, bookId);
    }

    @Test
    void testReturnBookWithUnexpectedError() {
        // Arrange
        String userId = "user123";
        String bookId = "BK123";
        when(jwtUtil.getAuthenticatedEmail()).thenReturn(userId);
        when(userService.returnBooks(userId, bookId)).thenThrow(new RuntimeException("Database error"));

        // Act
        UserController.BorrowRequest request = new UserController.BorrowRequest();
        request.bookId = bookId;
        ResponseEntity<?> response = userController.returnBooks(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("An unexpected error occurred", error.getMessage());
        verify(userService, times(1)).returnBooks(userId, bookId);
    }
}
