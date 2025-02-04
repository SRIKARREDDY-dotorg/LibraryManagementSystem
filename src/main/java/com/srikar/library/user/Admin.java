package com.srikar.library.user;

import com.srikar.library.Book;

import java.util.List;
/**
 * Admin class extends User and provides additional functionality for managing the library.
 */
public class Admin extends User {
    public Admin(String name, String email) {
        super(name, email);
    }

    /**
     * Add a book to the library
     * @param book
     */
    public void addBook(Book book) {
        library.addBook(book);
        System.out.println("Book added: " + book.getTitle());
    }

    /**
     * Check the inventory of the library
     */
    public void checkInventory() {
        List<Book> books = library.viewBooks();
        if (books.isEmpty()) {
            System.out.println("Library is empty.");
        } else {
            System.out.println("Library Inventory:");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    /**
     * Check if any user has borrowed books
     */
    public void checkBorrowedBooks() {
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
    }
}
