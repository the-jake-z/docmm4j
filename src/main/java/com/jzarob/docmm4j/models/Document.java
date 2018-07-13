package com.jzarob.docmm4j.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
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
    private String formNumber;
    private Map<String, String> mappingScheme;

    @JsonIgnore
    private Binary formTemplate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormNumber() {
        return formNumber;
    }

    public void setFormNumber(String formNumber) {
        this.formNumber = formNumber;
    }

    public Map<String, String> getMappingScheme() {
        return mappingScheme;
    }

    public void setMappingScheme(Map<String, String> mappingScheme) {
        this.mappingScheme = mappingScheme;
    }

    public Binary getFormTemplate() {
        return formTemplate;
    }

    public void setFormTemplate(Binary formTemplate) {
        this.formTemplate = formTemplate;
    }

    @JsonIgnore
    public InputStream getFormTemplateInputStream() {
        return new ByteArrayInputStream(this.formTemplate.getData());
    }
}
