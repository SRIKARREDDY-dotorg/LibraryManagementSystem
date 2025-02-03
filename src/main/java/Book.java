import java.util.UUID;

/**
 * Book class representing a book entity
 */
public class Book {
    private final String title;
    private final String author;
    private final String id;

    public Book(String title, String author) {
        this.id = IdGeneratorUtil.generateBookId();
        this.title = title;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
