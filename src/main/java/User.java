import java.util.UUID;

public class User {
    private final String name;
    private final String email;
    private final String id;
    private final Library library;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.id = UUID.randomUUID().toString();
        this.library = Library.getInstance();
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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

}
