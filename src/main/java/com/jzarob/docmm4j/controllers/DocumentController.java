package com.jzarob.docmm4j.controllers;

import com.jzarob.docmm4j.models.Document;
import com.jzarob.docmm4j.models.MediaTypes;
import com.jzarob.docmm4j.services.DocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/document")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(final DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/{documentNumber}")
    @ApiOperation("Returns a document object")
    public ResponseEntity<Document> loadByDocumentNumber(@PathVariable final String documentNumber) {
        return ResponseEntity.ok(this.documentService.loadByDocumentNumber(documentNumber));
    }

    @PostMapping()
    @ApiOperation("Creates a document to use for merging")
    public ResponseEntity<?> createDocument(@RequestBody Document document,
                                            final UriComponentsBuilder componentsBuilder) {

        document = this.documentService.createDocument(document);
        UriComponents components = componentsBuilder.path("/document/{documentNumber}")
                .buildAndExpand(document.getDocumentNumber());

        return ResponseEntity.created(components.toUri()).build();
    }

    @PostMapping("/{documentNumber}/template")
    @ApiOperation("Uploads a template to a document for merging")
    public ResponseEntity<?> uploadTemplate(@PathVariable("documentNumber") String documentNumber,
                                            @RequestParam("file") MultipartFile file,
                                            final UriComponentsBuilder componentsBuilder) throws IOException {

        Document document = this.documentService.loadByDocumentNumber(documentNumber);
        document.setDocumentTemplate(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        this.documentService.saveDocument(document);

        UriComponents components = componentsBuilder.path("/document/{documentNumber}")
                .buildAndExpand(document.getDocumentNumber());

        return ResponseEntity.accepted().location(components.toUri()).build();
    }

    @GetMapping("/{documentNumber}/template")
    @ApiOperation("Returns the template of a document")
    public ResponseEntity<?> getTemplateData(@PathVariable("documentNumber") String documentNumber) {
        Document document = this.documentService.loadByDocumentNumber(documentNumber);
        byte[] formTemplateData = document.getDocumentTemplate().getData();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaTypes.WORD_MEDIA_TYPE);
        headers.setContentDispositionFormData("attachment", document.getFileName());
        headers.setContentLength(formTemplateData.length);

        return new ResponseEntity<>(formTemplateData, headers, HttpStatus.OK);
    }
}
