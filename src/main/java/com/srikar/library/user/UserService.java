package com.srikar.library.user;

import com.srikar.library.Book;
import com.srikar.library.Library;
import com.srikar.library.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for User operations
 */
@Service
public class UserService {
    protected final List<User> users;
    protected final Library library;

    public UserService() {
        this.users = new ArrayList<>();
        this.library = Library.getInstance();
    }

    /**
     * Create a new user
     * @param name
     * @param email
     * @return
     */
    public User createUser(String name, String email) {
        User user = new User(name, email);
        users.add(user);
        return user;
    }

    /**
     * View all books in the library
     * @param userId
     * @return
     */
    public List<Book> viewBooks(String userId) {
        User user = findUserById(userId);
        if (user != null) {
            user.viewBooks(); // This will print to console as per your implementation
            return library.viewBooks();
        }
        System.out.println("User not found with id: " + userId);
        throw new UserNotFoundException("User not found with id: " + userId);
    }

    /**
     * Borrow a book from the library
     * @param userId
     * @param bookId
     * @return
     */
    public boolean borrowBook(String userId, String bookId) {
        User user = findUserById(userId);
        if (user != null) {
            return user.borrowBook(bookId);
        }
        throw new UserNotFoundException("User not found with id: " + userId);
    }
    /**
     * Return one or more books to the library
     * @param userId
     * @param bookIds
     * @return
     */
    public boolean returnBooks(String userId, List<String> bookIds) {
        User user = findUserById(userId);
        if (user != null) {
            return user.returnBooks(bookIds);
        }
        throw new UserNotFoundException("User not found with id: " + userId);
    }
    protected User findUserById(String userId) {
        return users.stream().filter(user -> user.getId().equals(userId)).findFirst().orElse(null);
    }
}
