package com.srikar.library.activity.service;

import com.srikar.library.dao.book.BookModel;
import com.srikar.library.dao.book.BookRepository;
import com.srikar.library.dao.user.UserModel;
import com.srikar.library.dao.user.UserRepository;
import com.srikar.library.dto.Book;
import com.srikar.library.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminService = new AdminService(bookRepository, userRepository);
    }

    @Test
    void testAddBook() {
        // Arrange
        String title = "Test Book";
        String author = "Test Author";
        int stock = 5;
        String url = "http://example.com/book";

        // Act
        Book result = adminService.addBook(title, author, stock, url);

        // Assert
        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertEquals(author, result.getAuthor());
        assertEquals(stock, result.getStock());
        assertEquals(url, result.getUrl());
        assertTrue(result.getId().startsWith("BK"));
        
        verify(bookRepository, times(1)).save(any(BookModel.class));
    }

    @Test
    void testUpdateBookSuccessfully() {
        // Arrange
        String bookId = "BK123";
        String title = "Updated Book";
        String author = "Updated Author";
        int stock = 10;
        String url = "http://example.com/updated";
        
        BookModel existingBook = BookModel.builder()
                .id(bookId)
                .title("Original Book")
                .author("Original Author")
                .stock(5)
                .url("http://example.com/original")
                .build();
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        
        // Act
        Book result = adminService.updateBook(bookId, title, author, stock, url);
        
        // Assert
        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals(title, result.getTitle());
        assertEquals(author, result.getAuthor());
        assertEquals(stock, result.getStock());
        assertEquals(url, result.getUrl());
        
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(any(BookModel.class));
    }

    @Test
    void testUpdateBookNotFound() {
        // Arrange
        String bookId = "BK123";
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        
        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            adminService.updateBook(bookId, "Title", "Author", 5, "url");
        });
        
        assertEquals("Book not found with id: " + bookId, exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, never()).save(any(BookModel.class));
    }

    @Test
    void testCheckBorrowedBooksAsAdmin() {
        // Arrange
        boolean isAdmin = true;
        String adminId = "admin123";
        
        String userId1 = "user1";
        String userId2 = "user2";
        String bookId1 = "BK123";
        String bookId2 = "BK456";
        
        List<String> user1Books = Collections.singletonList(bookId1);
        List<String> user2Books = Collections.singletonList(bookId2);
        
        UserModel user1 = new UserModel(userId1, "password1", user1Books);
        UserModel user2 = new UserModel(userId2, "password2", user2Books);
        
        BookModel book1 = BookModel.builder()
                .id(bookId1)
                .title("Book 1")
                .author("Author 1")
                .stock(0)
                .url("url1")
                .dueDate(new Date())
                .build();
        
        BookModel book2 = BookModel.builder()
                .id(bookId2)
                .title("Book 2")
                .author("Author 2")
                .stock(0)
                .url("url2")
                .dueDate(new Date())
                .build();
        
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(bookRepository.findById(bookId1)).thenReturn(Optional.of(book1));
        when(bookRepository.findById(bookId2)).thenReturn(Optional.of(book2));
        
        // Act
        List<Book> result = adminService.checkBorrowedBooks(isAdmin, adminId);
        
        // Assert
        assertEquals(2, result.size());
        
        assertEquals(bookId1, result.get(0).getId());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Author 1", result.get(0).getAuthor());
        assertEquals(userId1, result.get(0).getBorrowerId());
        
        assertEquals(bookId2, result.get(1).getId());
        assertEquals("Book 2", result.get(1).getTitle());
        assertEquals("Author 2", result.get(1).getAuthor());
        assertEquals(userId2, result.get(1).getBorrowerId());
        
        verify(userRepository, times(1)).findAll();
        verify(bookRepository, times(1)).findById(bookId1);
        verify(bookRepository, times(1)).findById(bookId2);
    }

    @Test
    void testCheckBorrowedBooksAsUser() {
        // Arrange
        boolean isAdmin = false;
        String userId = "user123";
        String bookId1 = "BK123";
        String bookId2 = "BK456";
        
        List<String> userBooks = Arrays.asList(bookId1, bookId2);
        UserModel user = new UserModel(userId, "password", userBooks);
        
        BookModel book1 = BookModel.builder()
                .id(bookId1)
                .title("Book 1")
                .author("Author 1")
                .stock(0)
                .url("url1")
                .dueDate(new Date())
                .build();
        
        BookModel book2 = BookModel.builder()
                .id(bookId2)
                .title("Book 2")
                .author("Author 2")
                .stock(0)
                .url("url2")
                .dueDate(new Date())
                .build();
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId1)).thenReturn(Optional.of(book1));
        when(bookRepository.findById(bookId2)).thenReturn(Optional.of(book2));
        
        // Act
        List<Book> result = adminService.checkBorrowedBooks(isAdmin, userId);
        
        // Assert
        assertEquals(2, result.size());
        
        assertEquals(bookId1, result.get(0).getId());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Author 1", result.get(0).getAuthor());
        assertEquals(userId, result.get(0).getBorrowerId());
        
        assertEquals(bookId2, result.get(1).getId());
        assertEquals("Book 2", result.get(1).getTitle());
        assertEquals("Author 2", result.get(1).getAuthor());
        assertEquals(userId, result.get(1).getBorrowerId());
        
        verify(userRepository, times(1)).findById(userId);
        verify(bookRepository, times(1)).findById(bookId1);
        verify(bookRepository, times(1)).findById(bookId2);
    }

    @Test
    void testCheckBorrowedBooksAsUserNotFound() {
        // Arrange
        boolean isAdmin = false;
        String userId = "user123";
        
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        
        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            adminService.checkBorrowedBooks(isAdmin, userId);
        });
        
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(bookRepository, never()).findById(anyString());
    }

    @Test
    void testCheckBorrowedBooksAsUserWithNoBorrowedBooks() {
        // Arrange
        boolean isAdmin = false;
        String userId = "user123";
        
        UserModel user = new UserModel(userId, "password", null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        // Act
        List<Book> result = adminService.checkBorrowedBooks(isAdmin, userId);
        
        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findById(userId);
        verify(bookRepository, never()).findById(anyString());
    }
}
