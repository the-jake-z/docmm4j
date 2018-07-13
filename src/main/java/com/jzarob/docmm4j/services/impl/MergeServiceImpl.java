package com.jzarob.docmm4j.services.impl;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jzarob.docmm4j.models.Document;
import com.jzarob.docmm4j.models.WordDocument;
import com.jzarob.docmm4j.services.MergeService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class MergeServiceImpl implements MergeService {

    @Override
    public Resource mergeDocument(Document document, String mergeData) {
        WordDocument document1 = new WordDocument(document.getFormTemplateInputStream());
        Object mergeDataObject = Configuration.defaultConfiguration().jsonProvider().parse(mergeData);

        Map<String, String> mergeFieldValues = new HashMap<>();

        document.getMappingScheme().forEach((key, value) -> {
            mergeFieldValues.put(key, JsonPath.read(mergeDataObject, value));
        });

        document1.setMergeData(mergeFieldValues);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        document1.performMerge( outputStream,true);

        return new ByteArrayResource(outputStream.toByteArray());
    }
}
