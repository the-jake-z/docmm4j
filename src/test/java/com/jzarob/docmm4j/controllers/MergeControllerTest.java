package com.jzarob.docmm4j.controllers;

import com.jzarob.docmm4j.models.Document;
import com.jzarob.docmm4j.models.MediaTypes;
import com.jzarob.docmm4j.services.DocumentService;
import com.jzarob.docmm4j.services.MergeService;
import com.jzarob.docmm4j.transfer.MergeTemplateRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MergeControllerTest {

    @Mock
    DocumentService documentService;

    @Mock
    MergeService mergeService;

    @InjectMocks
    MergeController mergeController;

    @Before
    public void setUp() {

    }

    @Test
    public void mergeDocumentRequest_returnsMergedDocument() {

        ByteArrayResource resource = new ByteArrayResource(new byte[] { 0x15 });

        when(documentService.loadByDocumentNumber("12345")).thenReturn(new Document());
        when(mergeService.mergeDocument(any(), any())).thenReturn(resource);

        MergeTemplateRequest templateRequest = new MergeTemplateRequest();
        templateRequest.setDocumentNumber("12345");
        templateRequest.setMergeData("");

        ResponseEntity<?> responseEntity = mergeController.mergeTemplate(templateRequest);

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(MediaTypes.WORD_MEDIA_TYPE, responseEntity.getHeaders().getContentType());
    }
}
