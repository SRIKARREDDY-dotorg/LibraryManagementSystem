package com.srikar.library.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a request for creating or updating a book.
 * It contains the book's details including title, author, stock count, URL, and ID.
 */
@Data
@NoArgsConstructor
public class BookCreateRequest {
    private String id;
    private String title;
    private String author;
    private int stock;
    private String url;
}
