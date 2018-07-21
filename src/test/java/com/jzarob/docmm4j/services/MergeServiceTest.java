package com.jzarob.docmm4j.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jzarob.docmm4j.models.Document;
import com.jzarob.docmm4j.models.Groupings;
import com.jzarob.docmm4j.models.MergeTemplateRequest;
import com.jzarob.docmm4j.models.MultiMergeRequest;
import com.jzarob.docmm4j.services.impl.MergeServiceImpl;
import com.jzarob.docmm4j.services.impl.ZipServiceImpl;
import org.apache.commons.io.FileUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MergeServiceTest {


    @Mock
    private DocumentService documentService;
    private ZipService zipService = new ZipServiceImpl();
    private MergeService mergeService;
    private String mergeData;
    private Document document;

    @Before
    public void setUp() throws Exception {

        mergeService = new MergeServiceImpl(documentService, zipService);

        mergeData = "{\"SampleName\":\"Jake Zarobsky\"}";

        document = new Document();

        document.setId("12345");
        document.setMappingScheme(new HashMap<String, String>() {{
            put("SampleName", "$.SampleName");
        }});
        document.setDocumentTemplate(new Binary(BsonBinarySubType.BINARY,
                FileUtils.readFileToByteArray(new File("samples/sample.docx"))));

        when(documentService.loadByDocumentNumber("12345")).thenReturn(document);
    }

    @Test
    public void mergeService_whenMergeDocument_ShouldReturnResource() {
        Resource result = mergeService.mergeDocument("12345", mergeData);
        Assert.notNull(result, "ensure that we have a resource");
        Assert.isTrue(result instanceof ByteArrayResource, "ensure that its a byte array one");
    }

    @Test
    public void mergeService_whenMergeMultiple_ShouldReturnResource() throws Exception {

        MultiMergeRequest multiMergeRequest = new MultiMergeRequest();
        MergeTemplateRequest request = new MergeTemplateRequest();

        request.setDocumentNumber("12345");
        ObjectMapper objectMapper = new ObjectMapper();
        request.setMergeData(objectMapper.readTree(mergeData));

        Groupings groupings = new Groupings();
        groupings.setName("sampleTest");
        groupings.setMergeRequests(Collections.singletonList(request));

        multiMergeRequest.setGroupings(Collections.singletonList(groupings));


        Resource result = mergeService.mergeMultipleDocuments(multiMergeRequest);
        Assert.notNull(result, "ensure that we have a resource");
        Assert.isTrue(result instanceof FileSystemResource, "ensure that we have a file");
    }
}
