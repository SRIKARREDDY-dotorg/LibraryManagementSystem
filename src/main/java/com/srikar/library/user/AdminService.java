package com.srikar.library.user;

import com.srikar.library.Book;
import com.srikar.library.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService extends UserService {
    private final Admin admin;
    public AdminService() {
        super();
        admin = new Admin("admin", "admin@example.com");
        System.out.println(admin);
        users.add(admin);
    }

    /**
     * Add a new book to the library
     * @param userId
     * @param title
     * @param author
     * @param stock
     */
    public Book addBook(String userId, String title, String author, int stock) {
        Admin admin = (Admin) findUserById(userId);
        if (admin != null) {
            if (stock < 1) {
                throw new IllegalArgumentException("Stock must be greater than 0");
            }
            Book book = new Book(title, author, stock);
            admin.addBook(book);
            return book;
        } else {
            throw new UserNotFoundException("Admin not found with id: " + userId);
        }
    }
    /**
     * Check the list of borrowed books
     *
     * @return
     */
    public List<Book> checkBorrowedBooks(String userId) {
        Admin admin = (Admin) findUserById(userId);
        if (admin == null) {
            throw new UserNotFoundException("Admin not found with id: " + userId);
        }
        return admin.checkBorrowedBooks();
    }
    /**
     * Check inventory
     *
     * @return
     */
    public List<Book> checkInventory(String userId) {
        Admin admin = (Admin) findUserById(userId);
        if (admin == null) {
            throw new UserNotFoundException("Admin not found");
        }
        return admin.checkInventory();
    }
}
