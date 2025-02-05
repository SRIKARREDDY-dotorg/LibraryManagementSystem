import com.srikar.library.Book;
import com.srikar.library.Library;
import com.srikar.library.user.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Library class
 */
public class LibraryTest {
    @Test
    public void testUserBorrowBookSuccess() {
        Library library = Library.getInstance();
        Book book = new Book("The Hobbit", "J.R.R. Tolkien", 1);
        library.addBook(book);
        User user = new User("John Doe", "john@example.com");

        assertTrue("User should be able to borrow the book", user.borrowBook(book.getId()));
        assertEquals(1, user.getBorrowedBooks().size(), "User should have 1 borrowed book");
    }
    @Test
    public void testUserBorrowLimit() throws Exception {
        Library library = Library.getInstance();
        User user = new User("Jane Doe", "jane@example.com");

        Book book1 = new Book("Book 1", "Author 1", 1);
        Book book2 = new Book("Book 2", "Author 2", 1);
        Book book3 = new Book("Book 3", "Author 3", 1);

        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        assertTrue(user.borrowBook(book1.getId()));
        assertTrue(user.borrowBook(book2.getId()));
        assertThrows(IllegalStateException.class, () -> user.borrowBook(book3.getId()));
    }
    @Test
    public void testUserBorrowBookWithMultipleCopies() {
        Library library = Library.getInstance();
        Book book = new Book("The Hobbit", "J.R.R. Tolkien", 2);
        library.addBook(book);
        User user = new User("John Doe", "john@example.com");

        assertTrue("User should be able to borrow the book.", user.borrowBook(book.getId()));
        assertEquals(1, user.getBorrowedBooks().size(), "User should have 1 borrowed book.");
        assertEquals(1, library.getBook(book.getId()).getStock(), "Library stock should reduce by 1.");
    }
    @Test
    public void testUserBorrowBookWithSingleCopy() {
        Library library = Library.getInstance();
        Book book = new Book("The Catcher in the Rye", "J.D. Salinger", 1);
        library.addBook(book);
        User user = new User("Alice", "alice@example.com");

        assertTrue("User should be able to borrow the last copy.", user.borrowBook(book.getId()));
        assertEquals(1, user.getBorrowedBooks().size(), "User should have 1 borrowed book.");
        assertNull(library.getBook(book.getId()), "Book should be removed from library.");
    }
    @Test
    public void testUserCannotBorrowSameBookTwice() {
        Library library = Library.getInstance();
        Book book = new Book("Brave New World", "Aldous Huxley", 3);
        library.addBook(book);
        User user = new User("Eve", "eve@example.com");

        assertTrue("User should be able to borrow a book.", user.borrowBook(book.getId()));
        assertThrows(IllegalStateException.class, () -> user.borrowBook(book.getId()), "User should not be able to borrow the same book twice.");
        assertEquals(1, user.getBorrowedBooks().size(), "User should have only 1 copy of the book.");
    }
    @Test
    public void testUserReturnBookSuccess() {
        Library library = Library.getInstance();
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald", 1);
        library.addBook(book);
        User user = new User("Bob", "bob@example.com");

        user.borrowBook(book.getId());
        assertTrue("User should be able to return the book.", user.returnBooks(List.of(book.getId())));
        assertEquals(0, user.getBorrowedBooks().size(), "User should have no borrowed books.");
    }
}