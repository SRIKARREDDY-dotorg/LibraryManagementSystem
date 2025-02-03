public class LibraryDemo {
    public static void main(String[] args) {
        User  user = new User("John Doe", "john.doe@example.com");
        // Create a new Library instance
        Library library = Library.getInstance();
        user.viewBooks();
        // Add some books to the library
        library.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald"));
        library.addBook(new Book("To Kill a Mockingbird", "Harper Lee"));
        library.addBook(new Book("1984", "George Orwell"));

        user.viewBooks();
    }
}
