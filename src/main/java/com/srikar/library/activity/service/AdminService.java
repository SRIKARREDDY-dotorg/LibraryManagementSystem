package com.srikar.library.activity.service;

import com.srikar.library.core.Admin;
import com.srikar.library.core.Book;
import com.srikar.library.dao.book.BookModel;
import com.srikar.library.dao.book.BookRepository;
import com.srikar.library.exception.UserNotFoundException;
import com.srikar.library.util.IdGeneratorUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService extends UserService {
    private final Admin admin;
    private final BookRepository bookRepository;
    public AdminService(BookRepository bookRepository) {
        super();
        this.bookRepository = bookRepository;
        admin = new Admin("admin", "admin@example.com");
        System.out.println(admin);
        users.add(admin);
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
    public List<Book> checkBorrowedBooks(String userId) {
        validateCheckBorrowedBooks(userId);
        Admin admin = (Admin) findUserById(userId);
        if (admin == null) {
            throw new UserNotFoundException("Admin not found with id: " + userId);
        }
        return admin.checkBorrowedBooks();
    }

    private void validateCheckBorrowedBooks(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
    }
}
