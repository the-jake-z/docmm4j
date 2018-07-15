package com.jzarob.docmm4j.controllers;

import com.jzarob.docmm4j.models.Document;
import com.jzarob.docmm4j.services.DocumentService;
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

        ResponseEntity<?> responseEntity = documentController.loadByFormNumber("12345");

        Assert.assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void documentController_whenCreate_returnsLocationHeader() {
    }
}
