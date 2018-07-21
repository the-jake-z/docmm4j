package com.jzarob.docmm4j.services.impl;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jzarob.docmm4j.exceptions.MultiMergeException;
import com.jzarob.docmm4j.models.*;
import com.jzarob.docmm4j.services.DocumentService;
import com.jzarob.docmm4j.services.MergeService;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class MergeServiceImpl implements MergeService {

    @Autowired
    private DocumentService documentService;

    private ByteArrayOutputStream mergeDocument(InputStream documentTemplate, Map<String, String> mergeFieldValues) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        this.mergeDocument(documentTemplate, mergeFieldValues, outputStream);

        return outputStream;
    }

    private void mergeDocument(InputStream documentTemplate,
                               Map<String, String> mergeFieldValues,
                               OutputStream outputStream) {
        DocumentMerger documentMerger = new DocumentMerger(documentTemplate, mergeFieldValues);
        documentMerger.performMerge(outputStream);
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

    private static Map<String, String> getMergeFieldValues(Map<String, String> mappingScheme, Object jsonObjectData) {
        Map<String, String> mergeFieldValues = new HashMap<>();
        mappingScheme.forEach((key, value) -> {
            mergeFieldValues.put(key, JsonPath.read(jsonObjectData, value));
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

        Map<String, String> mergeFieldValues = getMergeFieldValues(document.getMappingScheme(), jsonObjectData);

        FileOutputStream fileOutputStream = new FileOutputStream(
                createFileInDirectory(document.getFileName(), groupDirectory));

        mergeDocument(document.getFormTemplateInputStream(), mergeFieldValues, fileOutputStream);
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


            zipDirectory(rootDirectory, zipFileOutputStream);

            return new FileSystemResource(temporaryZipFile.toFile());

        } catch (IOException ex) {
            throw new MultiMergeException(ex);
        }
    }


    // Shamelessly stolen from:
    // https://stackoverflow.com/questions/15968883/how-to-zip-a-folder-itself-using-java
    private static void zipDirectory(Path sourceDirPath, OutputStream outputStream) throws IOException {
        try (ZipOutputStream zs = new ZipOutputStream(outputStream)) {
            Files.walk(sourceDirPath)
                .filter(path -> !java.nio.file.Files.isDirectory(path))
                .forEach(path -> {
                    ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
                    try {
                        zs.putNextEntry(zipEntry);
                        java.nio.file.Files.copy(path, zs);
                        zs.closeEntry();
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                });
        }
    }
}

