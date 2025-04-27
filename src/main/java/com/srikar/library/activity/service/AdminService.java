package com.srikar.library.activity.service;

import com.srikar.library.dao.user.UserModel;
import com.srikar.library.dao.user.UserRepository;
import com.srikar.library.dto.Book;
import com.srikar.library.dao.book.BookModel;
import com.srikar.library.dao.book.BookRepository;
import com.srikar.library.exception.UserNotFoundException;
import com.srikar.library.util.IdGeneratorUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService extends UserService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public AdminService(BookRepository bookRepository, UserRepository userRepository) {
        super();
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    /**
     * Add a new book to the library
     * @param title
     * @param author
     * @param stock
     */
    public Book addBook(String title, String author, int stock, String url) {
        final String bookId = IdGeneratorUtil.generateBookId();
        Book book = new Book(bookId, title, author, stock, url);
        BookModel bookModel = BookModel.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .stock(book.getStock())
                .url(book.getUrl())
                .build();
        bookRepository.save(bookModel);
        return book;
    }
    public Book updateBook(String bookId, String title, String author, int stock, String url) {
        final BookModel bookModel = bookRepository.findById(bookId).orElseThrow(() -> new UserNotFoundException("Book not found with id: " + bookId));
        // set if the fields are present
        bookModel.setTitle(title);
        bookModel.setAuthor(author);
        bookModel.setStock(stock);
        bookModel.setUrl(url);

        bookRepository.save(bookModel);
        return new Book(bookId, title, author, stock, url);
    }

    /**
     * Check the list of borrowed books
     *
     * @return
     */
    public List<Book> checkBorrowedBooks(final boolean isAdmin, final String userId) {
        if (!isAdmin) {
            final UserModel user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }
            if (user.getBorrowedBooks() == null) {
                return new ArrayList<>();
            }
            return user.getBorrowedBooks().stream()
                    .map(bookId -> bookRepository.findById(bookId).orElse(null))
                    .map(bookModel -> new Book(bookModel.getId(), bookModel.getTitle(), bookModel.getAuthor(), bookModel.getStock(), bookModel.getUrl()))
                    .toList();
        }
        final List<UserModel> users = userRepository.findAll();
        final List<Book> books = new ArrayList<>();
        if (users != null) {
            for (UserModel user : users) {
                if (user.getBorrowedBooks() != null) {
                    List<Book> borrowedBooks = user.getBorrowedBooks().stream()
                            .map(bookId -> bookRepository.findById(bookId).orElse(null))
                            .map(bookModel -> {
                                Book book = new Book(bookModel.getId(), bookModel.getTitle(), bookModel.getAuthor(), bookModel.getStock(), bookModel.getUrl());
                                book.setBorrowerId(user.getEmail());
                                book.setDueDate(bookModel.getDueDate());
                                return book;
                            })
                            .toList();
                    books.addAll(borrowedBooks);
                }
            }
        }
        return books;
    }
}
