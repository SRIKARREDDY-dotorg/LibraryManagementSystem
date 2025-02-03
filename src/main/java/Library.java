import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Library class to manage books.
 * This class provides functionality to add books and view all books in the library.
 */
public class Library {
    private static Library instance;
    private final List<Book> books;

    private Library() {
        this.books = new ArrayList<>();
    }

    /**
     * Get the singleton instance of the Library class.
     * This method is thread-safe and ensures that only one instance of the Library class is created.
     * @return
     */
    public static synchronized Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    /**
     * Add a book to the library.
     * @param book
     */
    public void addBook(Book book) {
        books.add(book);
    }

    /**
     * View all books in the library.
     * @return
     */
    public List<Book> viewBooks() {
        return Collections.unmodifiableList(books);
    }
}
