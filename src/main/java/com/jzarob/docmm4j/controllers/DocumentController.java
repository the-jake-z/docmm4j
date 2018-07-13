package com.jzarob.docmm4j.controllers;

import com.jzarob.docmm4j.models.Document;
import com.jzarob.docmm4j.services.DocumentService;
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
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(final DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/{formNumber}")
    public ResponseEntity<Document> loadByFormNumber(@PathVariable final String formNumber) {
        return ResponseEntity.ok(this.documentService.loadByFormNumber(formNumber));
    }

    @PostMapping()
    public ResponseEntity<?> createDocument(@RequestBody Document document,
                                            final UriComponentsBuilder componentsBuilder) {

        document = this.documentService.createDocument(document);
        UriComponents components = componentsBuilder.path("documents/{formNumber}")
                .buildAndExpand(document.getFormNumber());

        return ResponseEntity.created(components.toUri()).build();
    }

    @PostMapping("/{formNumber}/template")
    public ResponseEntity<?> uploadTemplate(@PathVariable("formNumber") String formNumber,
                                            @RequestParam("file") MultipartFile file,
                                            final UriComponentsBuilder componentsBuilder) throws IOException {

        Document document = this.documentService.loadByFormNumber(formNumber);
        document.setFormTemplate(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        this.documentService.saveDocument(document);

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{formNumber}/template")
    public ResponseEntity<?> getTemplateData(@PathVariable("formNumber") String formNumber) throws IOException {
        Document document = this.documentService.loadByFormNumber(formNumber);
        byte[] formTemplateData = document.getFormTemplate().getData();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
        headers.setContentDispositionFormData("attachment", document.getFormNumber() + ".docx");
        headers.setContentLength(formTemplateData.length);

        return new ResponseEntity<>(formTemplateData, headers, HttpStatus.OK);
    }
}
