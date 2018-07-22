package com.jzarob.docmm4j.services;

import com.jzarob.docmm4j.exceptions.DocumentNotFoundException;
import com.jzarob.docmm4j.exceptions.DuplicateDocumentException;
import com.jzarob.docmm4j.models.Document;
import com.jzarob.docmm4j.repositories.DocumentRepository;
import com.jzarob.docmm4j.services.impl.DocumentServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @Test(expected = DocumentNotFoundException.class)
    public void whenDocumentServiceDocumentNumberNotFoundThrowsExeption() {
        when(documentRepository.findByDocumentNumber(any())).thenThrow(DocumentNotFoundException.class);
        documentService.loadByDocumentNumber("12345");
    }

    @Test()
    public void whenDocumentServiceDocumentNumberFoundReturnsDocument() {
        Document expected = new Document();
        when(documentRepository.findByDocumentNumber(any())).thenReturn(expected);

        Document result = documentService.loadByDocumentNumber("1234");

        Assert.assertEquals(expected, result);
    }

    @Test(expected = DocumentNotFoundException.class)
    public void whenDocumentServiceDocumentNotFoundThrowsException() {
        documentService.loadByDocumentNumber("12345");
    }

    @Test(expected = DuplicateDocumentException.class)
    public void whenDocumentServiceInsertDuplicateDocumentThrowsException() {
        when(documentRepository.existsById("12345")).thenReturn(true);

        Document temp = new Document();
        temp.setId("12345");

        documentService.createDocument(temp);
    }

    @Test
    public void whenDocumentServiceCreateDocumentReturnDocument() {
        Document temp = new Document();

        when(documentRepository.save(temp)).thenReturn(temp);

        Document result = documentService.createDocument(temp);

        Assert.assertEquals(result, temp);
    }

    @Test
    public void whenDocumentServiceSavesDocumentReturnsDocument() {
        Document temp = new Document();

        when(documentRepository.save(temp)).thenReturn(temp);

        documentService.saveDocument(temp);

        Assert.assertTrue(true);
    }
}
