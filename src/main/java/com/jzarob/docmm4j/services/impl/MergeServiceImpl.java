package com.jzarob.docmm4j.services.impl;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jzarob.docmm4j.exceptions.MultiMergeException;
import com.jzarob.docmm4j.exceptions.WordDocumentMergeException;
import com.jzarob.docmm4j.models.*;
import com.jzarob.docmm4j.services.DocumentService;
import com.jzarob.docmm4j.services.MergeService;
import com.jzarob.docmm4j.services.ZipService;
import org.docx4j.Docx4J;
import org.docx4j.model.fields.merge.DataFieldName;
import org.docx4j.model.fields.merge.MailMerger;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class MergeServiceImpl implements MergeService {

    static {
        MailMerger.setMERGEFIELDInOutput(MailMerger.OutputField.KEEP_MERGEFIELD);
    }

    private final DocumentService documentService;
    private final ZipService zipService;

    @Autowired
    public MergeServiceImpl(final DocumentService documentService,
                            final ZipService zipService) {
        this.documentService = documentService;
        this.zipService = zipService;
    }


    @Override
    public Resource mergeDocument(String documentNumber, String mergeData) {

        Document document = documentService.loadByDocumentNumber(documentNumber);

        Object mergeDataObject = Configuration.defaultConfiguration().jsonProvider().parse(mergeData);

        Map<DataFieldName, String> mergeFieldValues = getMergeFieldValues(document.getMappingScheme(), mergeDataObject);

        return new ByteArrayResource(
                mergeDocument(document.getFormTemplateInputStream(), mergeFieldValues).toByteArray()
        );
    }

    @Override
    public Resource mergeMultipleDocuments(MultiMergeRequest multiMergeRequest) {

        try {
            Path rootDirectory = Files.createTempDirectory("batch");

            for(Groupings group : multiMergeRequest.getGroupings()) {
                mergeGroupInDirectory(group, rootDirectory);
            }

            Path temporaryZipFile = Files.createTempFile(null, ".zip");
            FileOutputStream zipFileOutputStream =
                    new FileOutputStream(temporaryZipFile.toString());


            zipService.zipDirectory(rootDirectory, zipFileOutputStream);

            return new FileSystemResource(temporaryZipFile.toFile());

        } catch (IOException ex) {
            throw new MultiMergeException(ex);
        }
    }

    private static Map<DataFieldName, String> getMergeFieldValues(Map<String, String> mappingScheme,
                                                                  Object jsonObjectData) {
        Map<DataFieldName, String> mergeFieldValues = new HashMap<>();
        mappingScheme.forEach((key, value) -> {
            mergeFieldValues.put(new DataFieldName(key), JsonPath.read(jsonObjectData, value));
        });

        return mergeFieldValues;
    }

    private void mergeGroupInDirectory(Groupings groupings, Path rootDirectory) throws IOException {
        Path groupDirectory = rootDirectory.resolve(groupings.getName());
        Files.createDirectories(groupDirectory);

        for(MergeTemplateRequest request : groupings.getMergeRequests()) {
            mergeDocumentInDirectory(request, groupDirectory);
        }
    }

    private File createFileInDirectory(String fileName, Path directory) throws IOException {
        Path filePath = directory.resolve(fileName);
        Files.createFile(filePath);
        return filePath.toFile();
    }

    private void mergeDocumentInDirectory(MergeTemplateRequest request, Path groupDirectory) throws IOException {
        Document document = documentService.loadByDocumentNumber(request.getDocumentNumber());
        Object jsonObjectData = Configuration
                .defaultConfiguration()
                .jsonProvider()
                .parse(request.getMergeData());

        Map<DataFieldName, String> mergeFieldValues = getMergeFieldValues(document.getMappingScheme(), jsonObjectData);

        FileOutputStream fileOutputStream = new FileOutputStream(
                createFileInDirectory(document.getFileName(), groupDirectory));

        mergeDocument(document.getFormTemplateInputStream(), mergeFieldValues, fileOutputStream);
    }


    private ByteArrayOutputStream mergeDocument(InputStream documentTemplate,
                                                Map<DataFieldName, String> mergeFieldValues) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        this.mergeDocument(documentTemplate, mergeFieldValues, outputStream);

        return outputStream;
    }

    private void mergeDocument(final InputStream documentTemplate,
                               final Map<DataFieldName, String> mergeFieldValues,
                               final OutputStream outputStream) {
        try {
            final WordprocessingMLPackage wordDocument = WordprocessingMLPackage.load(documentTemplate);
            MailMerger.performMerge(wordDocument, mergeFieldValues, true);
            Docx4J.toPDF(wordDocument, outputStream);
        } catch (Exception ex) {
            throw new WordDocumentMergeException(ex);
        }
    }
}

