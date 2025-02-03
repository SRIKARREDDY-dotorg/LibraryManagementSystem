import java.util.List;

public class LibraryDemo {
    public static void main(String[] args) {
        User user = new User("John Doe", "john.doe@example.com");
        System.out.println(user+"\n");
        // Create a new Library instance
        Library library = Library.getInstance();
        user.viewBooks();
        System.out.println("");
        // Add some books to the library
        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald");
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee");
        Book book3 = new Book("1984", "George Orwell");
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        user.viewBooks();
        System.out.println("");
        // Borrow a book
        user.borrowBook(book1.getId());
        user.borrowBook(book2.getId());
        System.out.println("");
        // show the limit breach
        user.borrowBook(book3.getId());
        System.out.println("\nBorrowed books");
        // View the borrowed books
        List<Book> borrowedBooks = user.getBorrowedBooks();
        borrowedBooks.stream().forEach(System.out::println);
    }
}
