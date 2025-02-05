package com.srikar.library.user;

import com.srikar.library.Book;
import com.srikar.library.IdGeneratorUtil;
import com.srikar.library.Library;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
    @Getter
    private final String name;
    @Getter
    private final String email;
    @Getter
    private final String id;
    protected final Library library;
    // Using list as limit is 2
    private final List<Book> borrowedBooks;
    private static final int BORROW_LIMIT = 2;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.id = IdGeneratorUtil.generateUserId();
        this.library = Library.getInstance();
        this.borrowedBooks = new ArrayList<>();
    }

    /**
     * If Empty print empty library
     * else print all books
     */
    public void viewBooks() {
        if(library.viewBooks().isEmpty()) {
            System.out.println("Library is empty");
            throw new IllegalStateException("Library is empty");
        } else {
            printBooks();
        }
    }

    private void printBooks() {
        System.out.println("Books in the library:");
        for (Book book : library.viewBooks()) {
            System.out.println(book);
        }
    }

    /**
     * Borrow a book from the library by its id
     * @param bookId
     * @return boolean
     */
    public boolean borrowBook(String bookId) {
        if(borrowedBooks.size() >= BORROW_LIMIT) {
            System.out.println("You have reached the borrowing limit.");
            throw new IllegalStateException("You have reached the borrowing limit.");
        }
        final Book book = library.getBook(bookId);
        return handleBorrowedBook(book);
    }
    private boolean handleBorrowedBook(Book book) {
        // check if the same copy present in borrowed books
        if (borrowedBooks.stream().anyMatch(b -> b.getId().equals(book.getId()))) {
            System.out.println("You have already borrowed a copy of this book.");
            throw new IllegalStateException("You have already borrowed a copy of this book.");
        }
        if (book == null) {
            System.out.println("Book not found.");
            throw new IllegalStateException("Book not found.");
        }
        if (book.getStock() > 1) {
            book.setStock(book.getStock() - 1); // Reduce stock
            library.addUser(this);
        } else {
            book.setStock(0);
            library.removeBook(book.getId()); // Remove from library if last copy
            library.addUser(this);
        }
        borrowedBooks.add(book);
        System.out.println(book.getTitle() + " Book borrowed successfully.");
        if(book.getStock() == 0) {
            System.out.println(book.getTitle() + " Book is out of stock.");
        }
        return true;
    }

    /**
     * Get a list of borrowed books
     * @return
     */
    public List<Book> getBorrowedBooks() {
        return Collections.unmodifiableList(borrowedBooks);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

}
