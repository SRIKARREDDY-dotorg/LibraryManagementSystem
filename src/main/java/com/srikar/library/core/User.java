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
                libraryBook = new Book(bookToReturn.getId(), bookToReturn.getTitle(), bookToReturn.getAuthor(), 1, bookToReturn.getUrl());
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
