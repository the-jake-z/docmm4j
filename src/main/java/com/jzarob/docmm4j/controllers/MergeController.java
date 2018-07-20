package com.jzarob.docmm4j.controllers;

import com.jzarob.docmm4j.models.Document;
import com.jzarob.docmm4j.models.MediaTypes;
import com.jzarob.docmm4j.models.MultiMergeRequest;
import com.jzarob.docmm4j.services.DocumentService;
import com.jzarob.docmm4j.services.MergeService;
import com.jzarob.docmm4j.models.MergeTemplateRequest;
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

    private final MergeService mergeService;

    @Autowired
    public MergeController(
                           final MergeService mergeService) {
        this.mergeService = mergeService;
    }

    @PostMapping("/single")
    public ResponseEntity<?> mergeTemplate(@RequestBody MergeTemplateRequest request) {
        Resource mergedDocument = this.mergeService.mergeDocument(request.getDocumentNumber(), request.getMergeData());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", request.getDocumentNumber() + ".pdf");


        return new ResponseEntity(mergedDocument, headers, HttpStatus.OK);
    }

    @PostMapping("/multiple")
    public ResponseEntity<?> mergeTemplates(@RequestBody MultiMergeRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "test.zip");

        // TODO: finish this;
        return new ResponseEntity<>(null, headers, HttpStatus.OK);
    }
}
