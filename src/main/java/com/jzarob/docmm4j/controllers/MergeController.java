package com.jzarob.docmm4j.controllers;

import com.jzarob.docmm4j.models.MergeTemplateRequest;
import com.jzarob.docmm4j.models.MultiMergeRequest;
import com.jzarob.docmm4j.services.MergeService;
import io.swagger.annotations.ApiOperation;
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
    public MergeController(final MergeService mergeService) {
        this.mergeService = mergeService;
    }

    @PostMapping("/single")
    @ApiOperation("Returns a single PDF to the client with the one document")
    public ResponseEntity<?> mergeTemplate(@RequestBody MergeTemplateRequest request) {
        Resource mergedDocument = this.mergeService.mergeDocument(request.getDocumentNumber(), request.getMergeData());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", request.getDocumentNumber() + ".pdf");


        return new ResponseEntity(mergedDocument, headers, HttpStatus.OK);
    }

    @PostMapping("/multiple")
    @ApiOperation(value = "Returns a zip file of all the merged documents")
    public ResponseEntity<?> mergeTemplates(@RequestBody MultiMergeRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "batch.zip");

        Resource content = mergeService.mergeMultipleDocuments(request);

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
}
