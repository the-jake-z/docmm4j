package com.jzarob.docmm4j.repositories;

import com.jzarob.docmm4j.models.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<Document, String> {
    Document findByFormNumber(String formNumber);
}
