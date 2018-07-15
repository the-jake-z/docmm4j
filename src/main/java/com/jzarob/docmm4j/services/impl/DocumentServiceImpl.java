package com.jzarob.docmm4j.services.impl;

import com.jzarob.docmm4j.exceptions.DocumentNotFoundException;
import com.jzarob.docmm4j.exceptions.DuplicateDocumentException;
import com.jzarob.docmm4j.models.Document;
import com.jzarob.docmm4j.repositories.DocumentRepository;
import com.jzarob.docmm4j.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(final DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }


    @Override
    public Document loadByDocumentNumber(String documentNumber) {
        Document document = this.documentRepository.findByDocumentNumber(documentNumber);

        if(document == null) {
            throw new DocumentNotFoundException();
        }

        return document;
    }

    @Override
    public Document createDocument(Document document) {
        if(document.getId() != null && this.documentRepository.existsById(document.getId())) {
            throw new DuplicateDocumentException();
        }

        return this.documentRepository.save(document);
    }

    @Override
    public void saveDocument(Document document) {
        this.documentRepository.save(document);
    }
}
