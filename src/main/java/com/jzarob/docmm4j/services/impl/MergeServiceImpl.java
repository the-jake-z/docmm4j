package com.jzarob.docmm4j.services.impl;

import com.google.common.io.Files;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jzarob.docmm4j.models.*;
import com.jzarob.docmm4j.services.DocumentService;
import com.jzarob.docmm4j.services.MergeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.acl.Group;
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

    private static Map<String, String> getMergeFieldValues(Map<String, String> mappingScheme, Object mergeObjectData) {
        Map<String, String> mergeFieldValues = new HashMap<>();
        mappingScheme.forEach((key, value) -> {
            mergeFieldValues.put(key, JsonPath.read(mergeObjectData, value));
        });

        return mergeFieldValues;
    }

    @Override
    // TODO: major refactor. Don't let uncle bob see this one kids.
    public Resource mergeMultipleDocuments(MultiMergeRequest multiMergeRequest) {

        try {
            File rootDirectory = new File(Paths.get(Files.createTempDir().getPath().toString(),
                    "batch").toString());

            rootDirectory.mkdir();

            for(Groupings group : multiMergeRequest.getGroupings()) {
                File groupDirectory = new File(Paths.get(rootDirectory.getPath(), group.getName()).toString());
                groupDirectory.mkdir();

                for(MergeTemplateRequest request : group.getMergeRequests()) {
                    Document d = documentService.loadByDocumentNumber(request.getDocumentNumber());
                    Object mergeDataObject = Configuration.defaultConfiguration().jsonProvider()
                            .parse(request.getMergeData());

                    Map<String, String> mergeFieldValues = getMergeFieldValues(d.getMappingScheme(), mergeDataObject);

                    File file = new File(Paths.get(groupDirectory.getPath().toString(), d.getFileName()).toString());
                    file.createNewFile();

                    FileOutputStream fileOutputStream = new FileOutputStream(
                            Paths.get(groupDirectory.getPath().toString(),
                                      d.getFileName()).toString()
                    );

                    mergeDocument(d.getFormTemplateInputStream(), mergeFieldValues, fileOutputStream);
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            pack(rootDirectory.getPath(), outputStream);

            return new ByteArrayResource(outputStream.toByteArray());

        } catch (Exception ex) {
            System.out.println("some exception");
        }

        return null;
    }


    // Shamelessly stolen from:
    // https://stackoverflow.com/questions/15968883/how-to-zip-a-folder-itself-using-java
    public static void pack(String sourceDirPath, OutputStream outputStream) throws IOException {
        try (ZipOutputStream zs = new ZipOutputStream(outputStream)) {
            Path pp = Paths.get(sourceDirPath);
            java.nio.file.Files.walk(pp)
                    .filter(path -> !java.nio.file.Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
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

