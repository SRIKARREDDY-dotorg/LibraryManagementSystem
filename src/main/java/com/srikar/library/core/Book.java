package com.srikar.library.core;

import com.srikar.library.util.IdGeneratorUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * Book class representing a book entity
 */
public class Book {
    private final String title;
    private final String author;
    private final String url;
    @Getter
    @Setter
    private int stock;
    private final String id;

    public Book(String title, String author, int stock, String url) {
        this.url = url;
        this.id = IdGeneratorUtil.generateBookId();
        this.title = title;
        this.author = author;
        this.stock = stock;
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
    public String getUrl() {
        return url;
    }
    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", stock=" + stock +
                '}';
    }
}
