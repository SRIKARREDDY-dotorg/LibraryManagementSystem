import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Library class to manage books.
 * This class provides functionality to add books and view all books in the library.
 */
public class Library {
    private static Library instance;
    private final Map<String, Book> books;

    private Library() {
        this.books = new ConcurrentHashMap<String, Book>();
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
        books.put(book.getId(), book);
    }

    /**
     * Remove a book from the library.
     * @param id
     * @return
     */
    public boolean removeBook(String id) {
        return books.remove(id) != null;
    }

    /**
     * View all books in the library.
     * @return
     */
    public List<Book> viewBooks() {
        return Collections.unmodifiableList(books.values().stream().toList());
    }

    /**
     * Get a book from the library by its id.
     * @param bookId
     * @return
     */
    public Book getBook(String bookId) {
        return books.get(bookId);
    }
}
