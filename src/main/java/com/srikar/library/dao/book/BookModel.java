package com.srikar.library.dao.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "books")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookModel {
    @Id
    private String id;
    private String title;
    private String author;
    private int stock;
    private String url;
}
