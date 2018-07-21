package com.jzarob.docmm4j.controllers;

import com.jzarob.docmm4j.models.Document;
import com.jzarob.docmm4j.models.MediaTypes;
import com.jzarob.docmm4j.services.DocumentService;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

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

    MockMultipartFile file;

    @Before
    public void setUp() {
        file = new MockMultipartFile("test", new byte[] {0x15});
    }

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

    @Test
    public void documentController_whenUploadTemplate_returnsAccepted() throws Exception {
        Document d = new Document();
        d.setDocumentNumber("12345");

        when(documentService.loadByDocumentNumber("12345")).thenReturn(d);
        Mockito.doNothing().when(documentService).saveDocument(any());

        ResponseEntity<?> responseEntity = documentController.uploadTemplate(
                "12345",
                file,
                UriComponentsBuilder.newInstance()
        );

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.ACCEPTED);
        Assert.assertEquals("/document/12345", responseEntity.getHeaders().getLocation().toString());
    }

    @Test
    public void documentController_whenGetDocumentTemplate_returnsTemplate() {
        byte[] content = new byte[] { 0x15 };
        Document d = new Document();
        d.setDocumentTemplate(new Binary(BsonBinarySubType.BINARY, content));

        when(documentService.loadByDocumentNumber("12345")).thenReturn(d);

        ResponseEntity<?> responseEntity = documentController.getTemplateData("12345");

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals(MediaTypes.WORD_MEDIA_TYPE, responseEntity.getHeaders().getContentType());
        Assert.assertTrue(Arrays.equals((byte[])responseEntity.getBody(), content));
    }
}
