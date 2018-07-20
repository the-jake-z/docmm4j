package com.jzarob.docmm4j.services.impl;

import com.google.common.io.Files;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jzarob.docmm4j.models.Document;
import com.jzarob.docmm4j.models.DocumentMerger;
import com.jzarob.docmm4j.models.Groupings;
import com.jzarob.docmm4j.models.MultiMergeRequest;
import com.jzarob.docmm4j.services.DocumentService;
import com.jzarob.docmm4j.services.MergeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class MergeServiceImpl implements MergeService {

    @Autowired
    private DocumentService documentService;

    private ByteArrayOutputStream mergeDocument(InputStream documentTemplate, Map<String, String> mergeFieldValues) {
        DocumentMerger documentMerger = new DocumentMerger(documentTemplate, mergeFieldValues);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        documentMerger.performMerge(outputStream);

        return outputStream;
    }

    @Override
    public Resource mergeDocument(String documentNumber, String mergeData) {

        Document document = documentService.loadByDocumentNumber(documentNumber);

        Object mergeDataObject = Configuration.defaultConfiguration().jsonProvider().parse(mergeData);

        Map<String, String> mergeFieldValues = getMergeFieldValues(document.getMappingScheme(), mergeDataObject);

        return new ByteArrayResource(
                mergeDocument(document.getFormTemplateInputStream(), mergeFieldValues).toByteArray()
        );
    }

    private static Map<String, String> getMergeFieldValues(Map<String, String> mappingScheme, Object mergeObjectData) {
        Map<String, String> mergeFieldValues = new HashMap<>();
        mappingScheme.forEach((key, value) -> {
            mergeFieldValues.put(key, JsonPath.read(mergeObjectData, value));
        });

        return mergeFieldValues;
    }

    @Override
    public Resource mergeMultipleDocuments(MultiMergeRequest multiMergeRequest) {
        return null;
    }
}

