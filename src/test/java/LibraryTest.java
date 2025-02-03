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
}