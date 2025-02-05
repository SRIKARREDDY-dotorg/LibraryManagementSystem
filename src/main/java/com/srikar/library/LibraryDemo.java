package com.srikar.library;

import com.srikar.library.user.Admin;
import com.srikar.library.user.User;

import java.util.List;

public class LibraryDemo {
    public static void main(String[] args) {
        User user = new User("John Doe", "john.doe@example.com");
        Admin admin = new Admin("Admin", "admin@example.com");
        System.out.println(user+"\n");
        // Create a new com.srikar.library.Library instance
        Library library = Library.getInstance();
        try {
            user.viewBooks();
        } catch (Exception e) {
        }
        System.out.println("");
        // Add some books to the library
        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", 2);
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee", 1);
        Book book3 = new Book("1984", "George Orwell", 1);
        admin.addBook(book1);
        admin.addBook(book2);
        admin.addBook(book3);

        user.viewBooks();
        System.out.println("");
        // Borrow a book
        user.borrowBook(book1.getId());
        user.borrowBook(book2.getId());
        System.out.println("");
        // show the limit breach
        try {
            user.borrowBook(book3.getId());
        } catch (Exception e) {
        }
        System.out.println("\nBorrowed books");
        // View the borrowed books
        List<Book> borrowedBooks = user.getBorrowedBooks();
        borrowedBooks.stream().forEach(System.out::println);

        System.out.println("");
        // View inventory
        admin.checkInventory();

        System.out.println("");
        // View borrowed books
        admin.checkBorrowedBooks();
    }
}
