package com.jzarob.docmm4j.models;

import com.fasterxml.jackson.databind.JsonNode;

public final class MergeTemplateRequest {
    private String documentNumber;
    private String mergeData;

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getMergeData() {
        return mergeData;
    }

    public void setMergeData(JsonNode jsonNode) {
        this.mergeData = jsonNode.toString();
    }
}
