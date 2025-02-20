package com.srikar.library.core;

import com.srikar.library.util.IdGeneratorUtil;
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
     * Return one or more books to the library
     * @param bookIds List of book IDs to be returned
     * @return boolean indicating success or failure
     */
    public boolean returnBooks(List<String> bookIds) {
        if (bookIds.isEmpty()) {
            throw new IllegalArgumentException("No books specified for return.");
        }
        handleReturnedBook(bookIds);
        return true;
    }

    private void handleReturnedBook(List<String> bookIds) {
        for (String bookId : bookIds) {
            Book bookToReturn = borrowedBooks.stream()
                    .filter(book -> book.getId().equals(bookId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("You haven't borrowed the book with ID: " + bookId));

            borrowedBooks.remove(bookToReturn);

            // Check if the book exists in the library
            Book libraryBook = library.getBook(bookId);
            if (libraryBook == null) {
                // If the book was removed earlier, add it back
                libraryBook = new Book(bookToReturn.getTitle(), bookToReturn.getAuthor(), 1);
                library.addBook(libraryBook);
            } else {
                // If the book still exists, increase the stock
                libraryBook.setStock(libraryBook.getStock() + 1);
            }

            System.out.println(bookToReturn.getTitle() + " returned successfully.");
        }
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
