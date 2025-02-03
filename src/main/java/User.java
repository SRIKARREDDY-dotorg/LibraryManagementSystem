import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class User {
    private final String name;
    private final String email;
    private final String id;
    private final Library library;
    private final List<Book> borrowedBooks;
    private static final int BORROW_LIMIT = 2;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.id = IdGeneratorUtil.generateUserId();
        this.library = Library.getInstance();
        this.borrowedBooks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getId() {
        return id;
    }

    /**
     * If Empty print empty library
     * else print all books
     */
    public void viewBooks() {
        if(library.viewBooks().isEmpty()) {
            System.out.println("Library is empty");
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
            return false;
        }
        final Book book = library.getBook(bookId);
        return handleBorrowedBook(book);
    }
    private boolean handleBorrowedBook(Book book) {
        if (book == null) {
            System.out.println("Book not found.");
            return false;
        }
        borrowedBooks.add(book);
        library.removeBook(book.getId());
        System.out.println(book.getTitle() + " Book borrowed successfully.");
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
