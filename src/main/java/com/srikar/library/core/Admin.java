package com.srikar.library.core;

import java.util.List;
/**
 * Admin class extends User and provides additional functionality for managing the library.
 */
public class Admin extends User {
    public Admin(String name, String email) {
        super(name, email);
    }

    /**
     * Check if any user has borrowed books
     */
    public List<Book> checkBorrowedBooks() {
        List<User> users = library.getUsers();
        if (users.isEmpty()) {
            System.out.println("No users have borrowed books.");
        } else {
            System.out.println("Users who have borrowed books:");
            for (User user : users) {
                List<Book> borrowedBooks = user.getBorrowedBooks();
                if (!borrowedBooks.isEmpty()) {
                    System.out.println(user.getName() + " has borrowed:");
                    for (Book book : borrowedBooks) {
                        System.out.println(book);
                    }
                }
            }
        }
        return users.stream()
                .flatMap(user -> user.getBorrowedBooks().stream())
                .toList();
    }
}
