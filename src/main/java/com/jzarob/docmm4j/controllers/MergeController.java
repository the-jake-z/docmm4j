package com.jzarob.docmm4j.controllers;

import com.jzarob.docmm4j.models.Document;
import com.jzarob.docmm4j.services.DocumentService;
import com.jzarob.docmm4j.services.MergeService;
import com.jzarob.docmm4j.transfer.MergeTemplateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/merge")
public class MergeController {

    private final DocumentService documentService;
    private final MergeService mergeService;

    @Autowired
    public MergeController(final DocumentService documentService,
                           final MergeService mergeService) {
        this.documentService = documentService;
        this.mergeService = mergeService;
    }

    @PostMapping()
    public ResponseEntity<?> mergeTempalte(@RequestBody MergeTemplateRequest request) {
        Document document = this.documentService.loadByFormNumber(request.getFormNumber());
        Resource mergedDocument = this.mergeService.mergeDocument(document, request.getMergeData());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
        headers.setContentDispositionFormData("attachment", document.getFormNumber() + ".docx");


        return new ResponseEntity(mergedDocument, headers, HttpStatus.OK);
    }
}
