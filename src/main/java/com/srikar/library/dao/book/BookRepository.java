package com.srikar.library.dao.book;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<BookModel, String> {
}
