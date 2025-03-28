package com.srikar.library.dto;

public class BookCreateRequest {
    private String title;
    private String author;
    private int stock;
    private String url;
    private String id;
    public String getId() {
        return id;
    }
    public String getUrl() {
        return url;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
}
