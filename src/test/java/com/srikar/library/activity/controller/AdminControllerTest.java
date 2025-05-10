package com.srikar.library.activity.controller;

import com.srikar.library.activity.service.AdminService;
import com.srikar.library.dto.Book;
import com.srikar.library.dto.BookCreateRequest;
import com.srikar.library.dto.ErrorResponse;
import com.srikar.library.activity.service.UserService;
import com.srikar.library.exception.UserNotFoundException;
import com.srikar.library.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @Mock
    private UserService userService;
    
    @Mock
    private JwtUtil jwtUtil;
    
    @Mock(lenient = true)
    private SecurityContext securityContext;
    
    @Mock(lenient = true)
    private Authentication authentication;

    @InjectMocks
    private AdminController adminController;

    private BookCreateRequest validRequest;
    private Book sampleBook;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        validRequest = new BookCreateRequest();
//        validRequest.setId("BK123");
        validRequest.setTitle("The Great Gatsby");
        validRequest.setAuthor("F. Scott Fitzgerald");
        validRequest.setStock(5);

        sampleBook = new Book("BK123", "The Great Gatsby", "F. Scott Fitzgerald", 5, "http://example.com/book.jpg");
    }

    private void mockAdminRole() {
        Collection<SimpleGrantedAuthority> authorities = Arrays.asList(
            new SimpleGrantedAuthority("ROLE_ADMIN"),
            new SimpleGrantedAuthority("ROLE_USER")
        );

        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
    }

    private void mockUserRole() {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_USER")
        );

        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
    }

    @Test
    void addBook_WhenAdmin_ReturnsCreatedBook() {
        // Arrange
        mockAdminRole();
        when(adminService.addBook(
            validRequest.getTitle(),
            validRequest.getAuthor(),
            validRequest.getStock(),
            validRequest.getUrl()
        )).thenReturn(sampleBook);

        // Act
        ResponseEntity<?> response = adminController.addBook(validRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleBook, response.getBody());
    }

    @Test
    void addBook_WhenNotAdmin_ReturnsForbidden() {
        // Arrange
        mockUserRole();

        // Act
        ResponseEntity<?> response = adminController.addBook(validRequest);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Access denied", error.getMessage());
        verify(adminService, never()).addBook(any(), any(), anyInt(), any());
    }

    @Test
    void addBook_WhenUserNotFound_ReturnsNotFound() {
        // Arrange
        mockAdminRole();
        when(adminService.addBook(any(), any(), anyInt(), any()))
            .thenThrow(new UserNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = adminController.addBook(validRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("User not found", error.getMessage());
    }

    @Test
    void addBook_WhenInvalidInput_ReturnsBadRequest() {
        // Arrange
        mockAdminRole();
        when(adminService.addBook(any(), any(), anyInt(), any()))
            .thenThrow(new IllegalArgumentException("Invalid input"));

        // Act
        ResponseEntity<?> response = adminController.addBook(validRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Invalid input", error.getMessage());
    }

    @Test
    void updateBook_WhenAdmin_ReturnsUpdatedBook() {
        // Arrange
        mockAdminRole();
        when(adminService.updateBook(
            validRequest.getId(),
            validRequest.getTitle(),
            validRequest.getAuthor(),
            validRequest.getStock(),
            validRequest.getUrl()
        )).thenReturn(sampleBook);

        // Act
        ResponseEntity<?> response = adminController.updateBook(validRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleBook, response.getBody());
    }

    @Test
    void updateBook_WhenNotAdmin_ReturnsForbidden() {
        // Arrange
        mockUserRole();

        // Act
        ResponseEntity<?> response = adminController.updateBook(validRequest);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Access denied", error.getMessage());
        verify(adminService, never()).updateBook(any(), any(), any(), anyInt(), any());
    }

    @Test
    void updateBook_WhenUserNotFound_ReturnsNotFound() {
        // Arrange
        mockAdminRole();
        when(adminService.updateBook(any(), any(), any(), anyInt(), any()))
            .thenThrow(new UserNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = adminController.updateBook(validRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("User not found", error.getMessage());
    }

    @Test
    void checkBorrowed_WhenAdmin_ReturnsAllBooks() {
        // Arrange
        mockAdminRole();
        String adminEmail = "admin@example.com";
        List<Book> borrowedBooks = Arrays.asList(sampleBook,
            new Book("BK456", "1984", "George Orwell", 3, "http://example.com/1984.jpg"));

        when(jwtUtil.getAuthenticatedEmail()).thenReturn(adminEmail);
        when(adminService.checkBorrowedBooks(true, adminEmail)).thenReturn(borrowedBooks);

        // Act
        ResponseEntity<?> response = adminController.checkBorrowed();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(borrowedBooks, response.getBody());
        verify(adminService).checkBorrowedBooks(true, adminEmail);
    }

    @Test
    void checkBorrowed_WhenUser_ReturnsUserBooks() {
        // Arrange
        mockUserRole();
        String userEmail = "user@example.com";
        List<Book> borrowedBooks = Collections.singletonList(sampleBook);

        when(jwtUtil.getAuthenticatedEmail()).thenReturn(userEmail);
        when(adminService.checkBorrowedBooks(false, userEmail)).thenReturn(borrowedBooks);

        // Act
        ResponseEntity<?> response = adminController.checkBorrowed();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(borrowedBooks, response.getBody());
        verify(adminService).checkBorrowedBooks(false, userEmail);
    }

    @Test
    void checkBorrowed_WhenUserNotFound_ReturnsNotFound() {
        // Arrange
        mockAdminRole();
        when(jwtUtil.getAuthenticatedEmail()).thenReturn("admin@example.com");
        when(adminService.checkBorrowedBooks(anyBoolean(), anyString()))
            .thenThrow(new UserNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = adminController.checkBorrowed();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("User not found", error.getMessage());
    }

    @Test
    void checkBorrowed_WhenUnexpectedError_ReturnsInternalError() {
        // Arrange
        String adminEmail = "admin@example.com";
        mockAdminRole();
        when(jwtUtil.getAuthenticatedEmail()).thenReturn(adminEmail);
        when(adminService.checkBorrowedBooks(eq(true), eq(adminEmail)))
            .thenThrow(new RuntimeException("Database connection failed"));

        // Act
        ResponseEntity<?> response = adminController.checkBorrowed();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("An unexpected error occurred", error.getMessage());
    }

    @Test
    void viewBooks_WhenAdmin_ReturnsBooks() {
        // Arrange
        mockAdminRole();
        List<Book> books = Arrays.asList(sampleBook);
        when(userService.viewBooks()).thenReturn(books);

        // Act
        ResponseEntity<?> response = adminController.viewBooks();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
    }

    @Test
    void viewBooks_WhenUserNotFound_ReturnsNotFound() {
        // Arrange
        mockAdminRole();
        when(userService.viewBooks()).thenThrow(new UserNotFoundException("User not found"));

        // Act
        ResponseEntity<?> response = adminController.viewBooks();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("User not found", error.getMessage());
    }

    @Test
    void borrowBook_WhenAdmin_ReturnsSuccess() {
        // Arrange
        mockAdminRole();
        String adminEmail = "admin@example.com";
        UserController.BorrowRequest request = new UserController.BorrowRequest();
        request.bookId = "BK123";
        when(jwtUtil.getAuthenticatedEmail()).thenReturn(adminEmail);
        when(userService.borrowBook(adminEmail, "BK123")).thenReturn(true);

        // Act
        ResponseEntity<?> response = adminController.borrowBook(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book borrowed successfully", response.getBody());
    }

    @Test
    void borrowBook_WhenBorrowFails_ReturnsNotFound() {
        // Arrange
        mockAdminRole();
        String adminEmail = "admin@example.com";
        UserController.BorrowRequest request = new UserController.BorrowRequest();
        request.bookId = "BK123";
        when(jwtUtil.getAuthenticatedEmail()).thenReturn(adminEmail);
        when(userService.borrowBook(adminEmail, "BK123")).thenReturn(false);

        // Act
        ResponseEntity<?> response = adminController.borrowBook(request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse error = (ErrorResponse) response.getBody();
        assertNotNull(error);
        assertEquals("Unable to borrow book. due to unknown reason", error.getMessage());
    }

    @Test
    void returnBooks_WhenAdmin_ReturnsSuccess() {
        // Arrange
        mockAdminRole();
        String adminEmail = "admin@example.com";
        when(jwtUtil.getAuthenticatedEmail()).thenReturn(adminEmail);
        UserController.BorrowRequest request = new UserController.BorrowRequest();
        request.bookId = "BK123";

        when(userService.returnBooks(adminEmail, "BK123")).thenReturn(true);

        // Act
        ResponseEntity<?> response = adminController.returnBooks(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Books returned successfully", response.getBody());
    }

}
