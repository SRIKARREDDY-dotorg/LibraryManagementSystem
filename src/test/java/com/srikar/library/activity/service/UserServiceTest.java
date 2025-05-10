package com.srikar.library.activity.service;

import com.srikar.library.dao.book.BookModel;
import com.srikar.library.dao.book.BookRepository;
import com.srikar.library.dao.user.UserModel;
import com.srikar.library.dao.user.UserRepository;
import com.srikar.library.dto.Book;
import com.srikar.library.queue.EmailProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailProducer emailProducer;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testViewBooks() {
        // Arrange
        List<BookModel> bookModels = Arrays.asList(
                BookModel.builder().id("BK123").title("Book 1").author("Author 1").stock(5).url("url1").build(),
                BookModel.builder().id("BK456").title("Book 2").author("Author 2").stock(3).url("url2").build()
        );
        when(bookRepository.findAll()).thenReturn(bookModels);

        // Act
        List<Book> result = userService.viewBooks();

        // Assert
        assertEquals(2, result.size());
        assertEquals("BK123", result.get(0).getId());
        assertEquals("Book 1", result.get(0).getTitle());
        assertEquals("Author 1", result.get(0).getAuthor());
        assertEquals(5, result.get(0).getStock());
        assertEquals("url1", result.get(0).getUrl());
        
        assertEquals("BK456", result.get(1).getId());
        assertEquals("Book 2", result.get(1).getTitle());
        assertEquals("Author 2", result.get(1).getAuthor());
        assertEquals(3, result.get(1).getStock());
        assertEquals("url2", result.get(1).getUrl());
        
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testBorrowBookSuccessfully() {
        // Arrange
        String userId = "user123";
        String bookId = "BK123";
        String password = "password";
        
        UserModel userModel = new UserModel(userId, password, new ArrayList<>());
        BookModel bookModel = BookModel.builder()
                .id(bookId)
                .title("Test Book")
                .author("Test Author")
                .stock(1)
                .url("test-url")
                .build();
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookModel));
        
        // Act
        boolean result = userService.borrowBook(userId, bookId);
        
        // Assert
        assertTrue(result);
        verify(bookRepository, times(1)).save(any(BookModel.class));
        verify(userRepository, times(1)).save(any(UserModel.class));
        verify(emailProducer, times(1)).sendEmailToQueue(eq(userId), anyString(), anyString());
    }

    @Test
    void testBorrowBookWhenStockIsZero() {
        // Arrange
        String userId = "user123";
        String bookId = "BK123";
        String password = "password";
        
        UserModel userModel = new UserModel(userId, password, new ArrayList<>());
        BookModel bookModel = BookModel.builder()
                .id(bookId)
                .title("Test Book")
                .author("Test Author")
                .stock(0)
                .url("test-url")
                .build();
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookModel));
        
        // Act & Assert
        boolean result = userService.borrowBook(userId, bookId);
        assertTrue(result);
        
        verify(bookRepository, times(1)).save(any(BookModel.class));
        verify(userRepository, times(1)).save(any(UserModel.class));
    }

    @Test
    void testBorrowBookWhenUserReachedLimit() {
        // Arrange
        String userId = "user123";
        String bookId = "BK123";
        String password = "password";
        
        List<String> borrowedBooks = Arrays.asList("BK456", "BK789");
        UserModel userModel = new UserModel(userId, password, borrowedBooks);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
        
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.borrowBook(userId, bookId);
        });
        
        assertEquals("You have already borrowed 2 books. Please return one before borrowing another.", exception.getMessage());
        verify(bookRepository, never()).save(any(BookModel.class));
        verify(userRepository, never()).save(any(UserModel.class));
    }

    @Test
    void testBorrowBookWhenAlreadyBorrowed() {
        // Arrange
        String userId = "user123";
        String bookId = "BK123";
        String password = "password";
        
        List<String> borrowedBooks = Arrays.asList(bookId);
        UserModel userModel = new UserModel(userId, password, borrowedBooks);
        BookModel bookModel = BookModel.builder()
                .id(bookId)
                .title("Test Book")
                .author("Test Author")
                .stock(1)
                .url("test-url")
                .build();
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookModel));
        
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.borrowBook(userId, bookId);
        });
        
        assertEquals("You have already borrowed the book: Test Book", exception.getMessage());
        verify(bookRepository, never()).save(any(BookModel.class));
        verify(userRepository, never()).save(any(UserModel.class));
    }

    @Test
    void testBorrowBookWithInvalidUserId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.borrowBook("", "BK123");
        });
        
        assertEquals("User ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void testBorrowBookWithInvalidBookId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.borrowBook("user123", "");
        });
        
        assertEquals("Book ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void testReturnBooksSuccessfully() {
        // Arrange
        String userId = "user123";
        String bookId = "BK123";
        String password = "password";
        
        List<String> borrowedBooks = new ArrayList<>();
        borrowedBooks.add(bookId);
        UserModel userModel = new UserModel(userId, password, borrowedBooks);
        BookModel bookModel = BookModel.builder()
                .id(bookId)
                .title("Test Book")
                .author("Test Author")
                .stock(0)
                .url("test-url")
                .build();
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookModel));
        
        // Act
        boolean result = userService.returnBooks(userId, bookId);
        
        // Assert
        assertTrue(result);
        verify(bookRepository, times(1)).save(any(BookModel.class));
        verify(userRepository, times(1)).save(any(UserModel.class));
        verify(emailProducer, times(1)).sendEmailToQueue(eq(userId), anyString(), anyString());
    }

    @Test
    void testReturnBooksWhenNotBorrowed() {
        // Arrange
        String userId = "user123";
        String bookId = "BK123";
        String password = "password";
        
        List<String> borrowedBooks = new ArrayList<>();
        UserModel userModel = new UserModel(userId, password, borrowedBooks);
        BookModel bookModel = BookModel.builder()
                .id(bookId)
                .title("Test Book")
                .author("Test Author")
                .stock(1)
                .url("test-url")
                .build();
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookModel));
        
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.returnBooks(userId, bookId);
        });
        
        assertEquals("You have not borrowed the book: Test Book", exception.getMessage());
        verify(bookRepository, never()).save(any(BookModel.class));
        verify(userRepository, never()).save(any(UserModel.class));
    }

    @Test
    void testReturnBooksWithInvalidUserId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.returnBooks("", "BK123");
        });
        
        assertEquals("User ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void testReturnBooksWithInvalidBookId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.returnBooks("user123", "");
        });
        
        assertEquals("Book IDs cannot be null or empty", exception.getMessage());
    }
}
