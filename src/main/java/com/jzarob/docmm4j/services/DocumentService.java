package com.jzarob.docmm4j.services;

import com.jzarob.docmm4j.models.Document;

public interface DocumentService {
    Document loadByFormNumber(String formNumber);
    Document createDocument(Document document);
    void saveDocument(Document document);
}
