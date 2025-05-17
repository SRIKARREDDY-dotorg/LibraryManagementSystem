package com.srikar.library.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;

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
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String status;

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

    public void calculateStatus() {
        if (dueDate == null) {
            this.status = "ON_TIME";
            return;
        }

        long currentTime = System.currentTimeMillis();
        long dueDateMillis = dueDate.getTime();
        long threeDaysInMillis = 3 * 24 * 60 * 60 * 1000L;

        if (currentTime > dueDateMillis) {
            this.status = "OVERDUE";
        } else if (dueDateMillis - currentTime <= threeDaysInMillis) {
            this.status = "DUE_SOON";
        } else {
            this.status = "ON_TIME";
        }
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
