import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Library class
 */
public class LibraryTest {
    @Test
    public void testViewBooksWhenLibraryIsEmpty() {
        Library library = Library.getInstance();
        assertTrue("Library should be empty", library.viewBooks().isEmpty());
    }
    @Test
    public void testViewBooksWhenLibraryHasBooks() {
        Library library = Library.getInstance();
        library.addBook(new Book("1984", "George Orwell"));
        library.addBook(new Book("To Kill a Mockingbird", "Harper Lee"));

        assertEquals(2, library.viewBooks().size(), "Library should contain 2 books");
    }
    @Test
    public void testUserBorrowBookSuccess() {
        Library library = Library.getInstance();
        Book book = new Book("The Hobbit", "J.R.R. Tolkien");
        library.addBook(book);
        User user = new User("John Doe", "john@example.com");

        assertTrue("User should be able to borrow the book", user.borrowBook(book.getId()));
        assertEquals(1, user.getBorrowedBooks().size(), "User should have 1 borrowed book");
        assertTrue("Library should be empty after borrowing the book", library.viewBooks().isEmpty());
    }
    @Test
    public void testUserBorrowLimit() {
        Library library = Library.getInstance();
        User user = new User("Jane Doe", "jane@example.com");

        Book book1 = new Book("Book 1", "Author 1");
        Book book2 = new Book("Book 2", "Author 2");
        Book book3 = new Book("Book 3", "Author 3");

        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        assertTrue(user.borrowBook(book1.getId()));
        assertTrue(user.borrowBook(book2.getId()));
        assertFalse(user.borrowBook(book3.getId()), "User should not be able to borrow more than 2 books");
    }
}