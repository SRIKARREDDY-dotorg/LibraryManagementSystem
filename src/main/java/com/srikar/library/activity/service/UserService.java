package com.srikar.library.activity.service;

import com.srikar.library.core.Book;
import com.srikar.library.core.User;
import com.srikar.library.dao.book.BookRepository;
import com.srikar.library.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for User operations
 */
@Service
public class UserService {
    protected final List<User> users;
    @Autowired
    private BookRepository bookRepository;

    public UserService() {
        this.users = new ArrayList<>();
    }

    /**
     * View all books in the library
     * @return
     */
    public List<Book> viewBooks() {
        // later add pagination
        // for now return all books
        // simplify the code
        return bookRepository.findAll().stream()
                .map(bookModel -> new Book(
                        bookModel.getId(),
                        bookModel.getTitle(),
                        bookModel.getAuthor(),
                        bookModel.getStock(),
                        bookModel.getUrl()))
                .toList();
    }

    /**
     * Borrow a book from the library
     * @param userId
     * @param bookId
     * @return
     */
    public boolean borrowBook(String userId, String bookId) {
        validateBorrowBook(userId, bookId);
        User user = findUserById(userId);
        if (user != null) {
            return user.borrowBook(bookId);
        }
        throw new UserNotFoundException("User not found with id: " + userId);
    }

    private void validateBorrowBook(String userId, String bookId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (bookId == null || bookId.isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be null or empty");
        }
    }

    /**
     * Return one or more books to the library
     * @param userId
     * @param bookIds
     * @return
     */
    public boolean returnBooks(String userId, List<String> bookIds) {
        validateReturnBooks(userId, bookIds);
        User user = findUserById(userId);
        if (user != null) {
            return user.returnBooks(bookIds);
        }
        throw new UserNotFoundException("User not found with id: " + userId);
    }

    private void validateReturnBooks(String userId, List<String> bookIds) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (bookIds == null || bookIds.isEmpty()) {
            throw new IllegalArgumentException("Book IDs cannot be null or empty");
        }
    }

    protected User findUserById(String userId) {
        return users.stream().filter(user -> user.getId().equals(userId)).findFirst().orElse(null);
    }
}
