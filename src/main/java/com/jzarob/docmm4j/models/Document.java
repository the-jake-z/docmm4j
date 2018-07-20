package com.jzarob.docmm4j.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

public class Document {
    @Id
    private String id;
    @Indexed
    private String documentNumber;
    private Map<String, String> mappingScheme;

    @JsonIgnore
    private Binary documentTemplate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Map<String, String> getMappingScheme() {
        return mappingScheme;
    }

    public void setMappingScheme(Map<String, String> mappingScheme) {
        this.mappingScheme = mappingScheme;
    }

    public Binary getDocumentTemplate() {
        return documentTemplate;
    }

    @JsonIgnore
    public String getFileName() {
        return String.format("%s.docx", documentNumber);
    }

    public void setDocumentTemplate(Binary documentTemplate) {
        this.documentTemplate = documentTemplate;
    }

    @JsonIgnore
    public InputStream getFormTemplateInputStream() {
        return new ByteArrayInputStream(this.documentTemplate.getData());
    }
}
