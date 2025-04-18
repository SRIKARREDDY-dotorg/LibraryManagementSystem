package com.srikar.library.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
    @Getter
    @Setter
    private String borrowerId;
    @Getter
    @Setter
    private Date dueDate;
    private final String id;

    public Book(String id, String title, String author, int stock, String url) {
        this.url = url;
        this.id = id;
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
