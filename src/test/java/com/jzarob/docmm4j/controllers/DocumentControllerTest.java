package com.jzarob.docmm4j.controllers;

import com.jzarob.docmm4j.models.Document;
import com.jzarob.docmm4j.services.DocumentService;
import org.apache.http.HttpHeaders;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentControllerTest {

    @Mock
    private DocumentService documentService;

    @Mock
    private UriComponentsBuilder componentsBuilder;

    @InjectMocks
    private DocumentController documentController;

    @Test
    public void documentController_whenGetDocumentNumber_returnsSuccessStatus() {
        when(documentService.loadByDocumentNumber(any())).thenReturn(new Document());

        ResponseEntity<?> responseEntity = documentController.loadByDocumentNumber("12345");

        Assert.assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void documentController_whenCreate_returnsLocationHeader() {
        Document d = new Document();
        d.setDocumentNumber("12345");
        when(documentService.createDocument(d)).thenReturn(d);

        ResponseEntity<?> responseEntity = documentController.createDocument(d, UriComponentsBuilder.newInstance());

        Assert.assertEquals("/document/12345", responseEntity.getHeaders().getLocation().toString());
    }
}
