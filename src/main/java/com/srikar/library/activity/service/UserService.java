package com.srikar.library.activity.service;

import com.srikar.library.core.Book;
import com.srikar.library.core.User;
import com.srikar.library.dao.book.BookModel;
import com.srikar.library.dao.book.BookRepository;
import com.srikar.library.dao.user.UserModel;
import com.srikar.library.dao.user.UserRepository;
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
    @Autowired
    private UserRepository userRepository;
    private static final int BORROW_LIMIT = 2;


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
        List<String> bookIds = userRepository.findById(userId).get().getBorrowedBooks();
        if (bookIds == null) {
            bookIds = new ArrayList<>();
        }
        if(bookIds.size() >= BORROW_LIMIT) {
            throw new IllegalStateException("You have already borrowed " + BORROW_LIMIT + " books. Please return one before borrowing another.");
        }
        if (bookIds.contains(bookId)) {
            throw new IllegalStateException("You have already borrowed the book with ID: " + bookId);
        }
        BookModel bookModel = bookRepository.findById(bookId).orElse(null);
        if (bookModel == null) {
            throw new IllegalStateException("Book not found.");
        }
        if(bookModel.getStock() >= 1) {
            bookModel.setStock(bookModel.getStock() - 1);
            bookRepository.save(bookModel);
        } else {
            bookModel.setStock(0);
            bookRepository.save(bookModel);
            throw new IllegalStateException("Book is out of stock.");
        }
        bookIds.add(bookId);
        userRepository.save(new UserModel(userId, userRepository.findById(userId).get().getPassword(), bookIds));
        UserModel userModel = userRepository.findById(userId).orElse(null);
        System.out.println("User had these books");
        userModel.getBorrowedBooks().stream().forEach(System.out::println);
        return true;
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
     * @param bookId
     * @return
     */
    public boolean returnBooks(String userId, String bookId) {
        validateReturnBooks(userId, bookId);
        List<String> bookIds = userRepository.findById(userId).get().getBorrowedBooks();
        if (bookIds == null) {
            bookIds = new ArrayList<>();
        }
        if(!bookIds.contains(bookId)) {
            throw new IllegalStateException("You have not borrowed the book with ID: " + bookId);
        }
        bookIds.remove(bookId);
        userRepository.save(new UserModel(userId, userRepository.findById(userId).get().getPassword(), bookIds));
        BookModel bookModel = bookRepository.findById(bookId).orElse(null);
        if (bookModel == null) {
            throw new IllegalStateException("Book not found.");
        }
        bookModel.setStock(bookModel.getStock() + 1);
        bookRepository.save(bookModel);
        return true;
    }

    private void validateReturnBooks(String userId, String bookId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (bookId == null || bookId.isEmpty()) {
            throw new IllegalArgumentException("Book IDs cannot be null or empty");
        }
    }

    protected User findUserById(String userId) {
        return users.stream().filter(user -> user.getId().equals(userId)).findFirst().orElse(null);
    }
}
